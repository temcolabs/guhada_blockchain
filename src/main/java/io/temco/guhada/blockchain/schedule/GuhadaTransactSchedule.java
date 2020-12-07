package io.temco.guhada.blockchain.schedule;

import io.swagger.annotations.Info;
import io.temco.guhada.blockchain.mapper.BlockChainMapper;
import io.temco.guhada.blockchain.model.request.GuhadaTransactRequest;
import io.temco.guhada.blockchain.model.request.TrackRecord;
import io.temco.guhada.blockchain.model.response.UnregisteredDeal;
import io.temco.guhada.blockchain.service.BlockchainWalletService;
import io.temco.guhada.blockchain.service.GuhadaContractMainnetService;
import io.temco.guhada.blockchain.service.GuhadaContractService;
import io.temco.guhada.blockchain.service.TrackRecordService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

import javax.annotation.PostConstruct;

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
    private final TrackRecordService trackRecordService;
    
    public GuhadaTransactSchedule(GuhadaContractMainnetService guhadaContractMainnetService,
                                  BlockChainMapper blockChainMapper,
                                  GuhadaContractService guhadaContractService,
                                  TrackRecordService trackRecordService){
        this.guhadaContractMainnetService = guhadaContractMainnetService;
        this.blockChainMapper = blockChainMapper;
        this.guhadaContractService = guhadaContractService;
        this.trackRecordService = trackRecordService;
    }

    @PostConstruct
    private void init() {
    	try {
			trackRecordUploadExecuter();
		} catch (Exception e) {
			e.printStackTrace();
		}
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
    
    @Scheduled(cron = "${cron.exp.blockchain.track-record-upload-blockchain}")
    public void trackRecordUploadExecuter() throws Exception {
        if("local".equals(profilesName) || StringUtils.isEmpty(profilesName)) {
            return;
        }
        log.info("block chain register deal btach started.");
        List<TrackRecord> unregisterDeals = blockChainMapper.getUnregisteredBlockchainDeal();
        log.info("block chain register deal btach size : " + unregisterDeals);
        for(int i = 0; i < unregisterDeals.size(); i++) {
        	log.info("[{}/{}] block chain register deal upload.", i, unregisterDeals.size() - 1);
        	trackRecordService.uploadProductInfo(unregisterDeals.get(i));
        }        
        log.info("block chain register deal btach started.");
    }

    @Scheduled(cron = "${cron.exp.blockchain.guhada-airdrop-update}")
    public void guhadaAirdropUpdate() throws Exception {
        blockchainWalletService.updateToken();
    }

    @Profile("prod")
    @Scheduled(cron = "${cron.exp.blockchain.coinone-guhada-airdrop-update}")
    public void coinoneGuhadaAirdropUpdate() throws Exception {
        blockchainWalletService.tokenTransfer();
    }


}
