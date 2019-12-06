package io.temco.guhada.blockchain.schedule;

import io.temco.guhada.blockchain.mapper.BlockChainMapper;
import io.temco.guhada.blockchain.model.request.GuhadaTransactRequest;
import io.temco.guhada.blockchain.model.response.UnregisteredDeal;
import io.temco.guhada.blockchain.service.BlockchainWalletService;
import io.temco.guhada.blockchain.service.GuhadaContractMainnetService;
import io.temco.guhada.blockchain.service.GuhadaContractService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Shin Han
 * Since 2019-10-17
 */
@Slf4j
@Component
public class GuhadaTransactSchedule {
    @Value("${spring.profiles.active:dev}")
    private String profilesName;

    @Autowired
    private BlockchainWalletService blockchainWalletService;

    private final GuhadaContractMainnetService guhadaContractMainnetService;
    private final BlockChainMapper blockChainMapper;
    private final GuhadaContractService guhadaContractService;
    public GuhadaTransactSchedule(GuhadaContractMainnetService guhadaContractMainnetService,
                                  BlockChainMapper blockChainMapper,
                                  GuhadaContractService guhadaContractService){
        this.guhadaContractMainnetService = guhadaContractMainnetService;
        this.blockChainMapper = blockChainMapper;
        this.guhadaContractService = guhadaContractService;
    }



    @Scheduled(cron = "${cron.exp.blockchain.upload-deal}")
    public void blockChainUploadExecuter() throws Exception {
        if("local".equals(profilesName) || StringUtils.isEmpty(profilesName)) {
            return;
        }
        List<UnregisteredDeal> unregisteredDealList = blockChainMapper.getUnregisteredDeal();
        for (UnregisteredDeal unregisteredDeal:unregisteredDealList) {

            GuhadaTransactRequest guhadaTransactRequest = new GuhadaTransactRequest();
            guhadaTransactRequest.setProductId(unregisteredDeal.getProductId());
            guhadaTransactRequest.setDealId(unregisteredDeal.getDealId());
            guhadaTransactRequest.setSerialId(unregisteredDeal.getDealId() + "");
            guhadaTransactRequest.setBrandName(unregisteredDeal.getBrandName());
            guhadaTransactRequest.setSeller(unregisteredDeal.getSeller());
            guhadaTransactRequest.setProductName(unregisteredDeal.getProductName());
            guhadaTransactRequest.setPrice(unregisteredDeal.getPrice());
            guhadaContractMainnetService.uploadToBlockchainFeeDelegationMainNet(guhadaTransactRequest);
        }
    }

    @Scheduled(cron = "${cron.exp.blockchain.guhada-airdrop-update}")
    public void guhadaAirdropUpdate() throws Exception {
        blockchainWalletService.updateToken();
    }

    @Profile("prod")
    @Scheduled(cron = "${cron.exp.blockchain.coinone-guhada-airdrop-update}")
    public void coinoneGuhadaAirdropUpdate() throws Exception {
        blockchainWalletService.updateToken();
    }


}
