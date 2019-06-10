package io.temco.guhada.blockchain.service.impl;

import io.temco.guhada.blockchain.model.GuhadaTransact;
import io.temco.guhada.blockchain.model.request.GuhadaTransactRequest;
import io.temco.guhada.blockchain.model.response.GuhadaTransactResponse;
import io.temco.guhada.blockchain.repository.GuhadaTransactRepository;
import io.temco.guhada.blockchain.service.GuhadaContractService;
import io.temco.guhada.blockchain.smartcontract.GuhadaTransactSC;
import io.temco.guhada.blockchain.util.HashUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.DefaultGasProvider;

import javax.annotation.PostConstruct;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Shin Han
 * Since 2019-06-10
 */
@Service
@Slf4j
public class GuhadaContractServiceImpl implements GuhadaContractService {

    @Autowired
    private GuhadaTransactRepository guhadaTransactRepository;

    private Web3j web3j;

    private GuhadaTransactSC guhadaTransactSC;

    private Credentials credentials;


    @PostConstruct
    private void initWeb3j() throws java.io.IOException, GeneralSecurityException {
        String privateKey = "11E8E50CB37DF62B66B6D43E5140495E9E1C41017FA5096EA70F9F361661D16E";

        String contractAddress = "0x3dc22c969a7af9fc97f8f9feed1ab8d829be574f";
        String chianUrl = "https://ropsten.infura.io/v3/67b3ebcb7a574122967fb41c7a968b90";
        web3j = Web3j.build(new HttpService(chianUrl));
        log.info(web3j.ethGasPrice().send().getGasPrice().toString());

        credentials = Credentials.create(privateKey);
        ECKeyPair ecKeyPair = ECKeyPair.create(new BigInteger(credentials.getEcKeyPair().getPrivateKey().toString()) );
        credentials = Credentials.create(ecKeyPair);
        log.info("My address: {}", credentials.getAddress());

        guhadaTransactSC = guhadaTransactSC.load(contractAddress, web3j, credentials, new DefaultGasProvider());

    }

    @Override
    public GuhadaTransact uploadToBlockchain(GuhadaTransactRequest guhadaTransactRequest) throws Exception {
        GuhadaTransact guhadaTransact = GuhadaTransact.of(guhadaTransactRequest);
        guhadaTransact.setHash(HashUtils.getSha(guhadaTransact.toString()));
        guhadaTransact = guhadaTransactRepository.save(guhadaTransact);
        String contractAddress = guhadaTransactSC.insert(BigInteger.valueOf(guhadaTransact.getGuhadaTransactId()),
                BigInteger.valueOf(guhadaTransact.getProductId()), guhadaTransact.getHash()).send().getTransactionHash();
        guhadaTransact.setContractAddress(contractAddress);
        guhadaTransactRepository.save(guhadaTransact);
        return guhadaTransact;
    }

    @Override
    public List<GuhadaTransactResponse> getTransactData(long productId) {

        List<GuhadaTransact> guhadaTransactList = guhadaTransactRepository.findAllByProductId(productId);
        List<GuhadaTransactResponse> guhadaTransactResponseList = guhadaTransactList
                .stream()
                .map(guhadaTransact -> GuhadaTransactResponse.of(guhadaTransact))
                .collect(Collectors.toList());
        return guhadaTransactResponseList;
    }
}
