package io.temco.guhada.blockchain.service.impl;

import io.temco.guhada.blockchain.model.Goods;
import io.temco.guhada.blockchain.service.SmartContractService;
import io.temco.guhada.blockchain.smartcontract.TransactSC;
import io.temco.guhada.blockchain.util.AES256Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Contract;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.math.BigInteger;
import java.security.GeneralSecurityException;

@Service("smartContractService")
public class SmartContractServiceImpl implements SmartContractService {

    private static final Logger log = LoggerFactory.getLogger(SmartContractServiceImpl.class);

    @Value("${spring.private-key}")
    private String privateKey;

    @Value("${spring.contract-address}")
    private String contractAddress;

    @Value("${spring.chain-url}")
    private String chianUrl;

    private Web3j web3j;

    private TransactSC transactSC;


    @PostConstruct
    private void initWeb3j() throws java.io.IOException, GeneralSecurityException {
        web3j = Web3j.build(new HttpService(chianUrl));
        log.info("Connected to RSK client version: {}", web3j.web3ClientVersion().send().getWeb3ClientVersion());

        AES256Util aes256Util = new AES256Util();
        Credentials credentials = Credentials.create(aes256Util.decrypt(privateKey));
        ECKeyPair ecKeyPair = ECKeyPair.create(new BigInteger(credentials.getEcKeyPair().getPrivateKey().toString()) );
        credentials = Credentials.create(ecKeyPair);
        log.info("My address: {}", credentials.getAddress());

        BigInteger gasPrice = new BigInteger("3");
        System.out.println("Using gas price of: " + gasPrice );
        transactSC = transactSC.load(contractAddress, web3j, credentials, gasPrice, Contract.GAS_LIMIT);
    }

    @PreDestroy
    public void stop(){
        web3j.shutdown();
        log.info("Connection to RSK closed.");
    }

    @Override
    public String insert(Goods goods) throws Exception {
        log.info("inserting transaction {}", goods);
        // enable this constructor to test without smart contract
        // return HashUtil.sha256(HashUtil.sha256(Instant.now().toString()));
        // disable this constructor to test without smart contract
        return transactSC.insert(BigInteger.valueOf(goods.getId()),
                BigInteger.valueOf(goods.generateProductId()), goods.getHash()).send().getTransactionHash();
    }
}
