package io.temco.guhada.blockchain.service.impl;

import io.temco.guhada.blockchain.model.ApiLimit;
import io.temco.guhada.blockchain.model.ApiUsage;
import io.temco.guhada.blockchain.model.Product;
import io.temco.guhada.blockchain.model.Transact;
import io.temco.guhada.blockchain.model.request.ProductRequest;
import io.temco.guhada.blockchain.model.request.TransactRequest;
import io.temco.guhada.blockchain.model.response.BlockChainInfo;
import io.temco.guhada.blockchain.repository.ApiLimitRepository;
import io.temco.guhada.blockchain.repository.ApiUsageRepository;
import io.temco.guhada.blockchain.repository.ProductRepository;
import io.temco.guhada.blockchain.repository.TransactRepository;
import io.temco.guhada.blockchain.service.SmartContractService;
import io.temco.guhada.blockchain.smartcontract.TransactSC;
import io.temco.guhada.blockchain.util.AES256Util;
import io.temco.guhada.blockchain.util.HashUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Hash;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthTransaction;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Contract;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.time.LocalDate;

@Slf4j
@Service("smartContractService")
public class SmartContractServiceImpl implements SmartContractService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TransactRepository transactRepository;

    @Autowired
    private ApiLimitRepository apiLimitRepository;

    @Autowired
    private ApiUsageRepository apiUsageRepository;

    @Value("${spring.private-key}")
    private String privateKey;

    @Value("${spring.contract-address}")
    private String contractAddress;

    @Value("${spring.chain-url}")
    private String chianUrl;

    private Web3j web3j;

    private TransactSC transactSC;

    private Credentials credentials;



    @PostConstruct
    private void initWeb3j() throws java.io.IOException, GeneralSecurityException {
        web3j = Web3j.build(new HttpService(chianUrl));
        log.info("Connected to RSK client version: {}", web3j.web3ClientVersion().send().getWeb3ClientVersion());

        AES256Util aes256Util = new AES256Util();
        credentials = Credentials.create(aes256Util.decrypt(privateKey));
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
    public long generateQrCode(String apiToken, ProductRequest productRequest) throws Exception {
        ApiLimit apiLimit = apiLimitRepository.findByApiToken(apiToken);
        if(apiLimit == null){
            throw new Exception("apitoken error");
        }
        // Product 생성
        Product product = Product.builder()
                .productName(productRequest.getProductName())
                .companyId(productRequest.getCompanyId())
                .productCode(productRequest.getProductCode())
                .category(productRequest.getCategory())
                .certificate(productRequest.getCertificate())
                .regDate(productRequest.getRegDate())
                .salesDate(productRequest.getSalesDate())
                .orderNumber(productRequest.getOrderNumber())
                .boxSize(productRequest.getBoxSize())
                .build();
        return productRepository.save(product).getProductId();
    }

    @Override
    public String uploadToBlockchain(String apiToken,TransactRequest transactRequest) throws Exception {
        ApiLimit apiLimit = apiLimitRepository.findByApiToken(apiToken);
        if(apiLimit == null){
            throw new Exception("apitoken error");
        }
        LocalDate nowDate = LocalDate.now();
        // 월 사용량 초과
        int monthUsageSum = apiUsageRepository.sumMonthUsage(apiLimit.getApiLimitId(), nowDate.getMonthValue());
        if(monthUsageSum >= apiLimit.getMonthLimit()){
            throw new Exception("usage limit exceed ");
        }
        // 오늘 사용량
        ApiUsage apiUsage = apiUsageRepository.findByApiLimitIdAndMonthAndDay(apiLimit.getApiLimitId(),nowDate.getMonthValue(), nowDate.getDayOfMonth());
        // 오늘 처음 사용인경우
        if(apiUsage == null){
            apiUsage = ApiUsage.builder()
                    .apiLimitId(apiLimit.getApiLimitId())
                    .month(nowDate.getMonthValue())
                    .day(nowDate.getDayOfMonth())
                    .usageCount(0)
                    .build();
        }
        // 사용횟수 +1회
        apiUsage.setUsageCount(apiUsage.getUsageCount() + 1);
        apiUsageRepository.save(apiUsage);

        Transact transact = Transact.builder()
                .productId(transactRequest.getProductId())
                .transactTime(transactRequest.getTransactTime())
                .invoiceNumber(transactRequest.getInvoiceNumber())
                .deliveryCode(transactRequest.getDeliveryCode())
                .temperature(transactRequest.getTemperature())
                .build();

        transact.setHash(HashUtils.getSha(transact.toString()));
        transact = transactRepository.save(transact);
        return transactInsert(transact);
    }

    private String transactInsert(Transact transact) throws Exception {
        String contractAddress = transactSC.insert(BigInteger.valueOf(transact.getTransactId()),
                BigInteger.valueOf(transact.getProductId()), HashUtils.getSha(transact.toString())).send().getTransactionHash();
        transact.setContractAddress(contractAddress);
        transactRepository.save(transact);
        return contractAddress;
    }

    @Override
    public BlockChainInfo getBlockChainInfo(String apiToken,String hashId) throws Exception {
        ApiLimit apiLimit = apiLimitRepository.findByApiToken(apiToken);
        if(apiLimit == null){
            throw new Exception("apitoken error");
        }
        EthTransaction ethTransaction = web3j.ethGetTransactionByHash(hashId).send();
        log.debug("input : " + ethTransaction.getResult().getInput());
        Transact transact = transactRepository.findByTxHash(hashId);
        Product product = productRepository.getOne(transact.getProductId());
        return new BlockChainInfo(product,transact);
    }
}
