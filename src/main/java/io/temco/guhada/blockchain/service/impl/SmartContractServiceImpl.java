package io.temco.guhada.blockchain.service.impl;

import com.google.zxing.WriterException;
import io.temco.guhada.blockchain.model.ApiUsage;
import io.temco.guhada.blockchain.model.Company;
import io.temco.guhada.blockchain.model.Product;
import io.temco.guhada.blockchain.model.Transact;
import io.temco.guhada.blockchain.model.request.CompanyRequest;
import io.temco.guhada.blockchain.model.request.GenerateQrCodeRequest;
import io.temco.guhada.blockchain.model.request.TransactRequest;
import io.temco.guhada.blockchain.model.response.*;
import io.temco.guhada.blockchain.repository.ApiUsageRepository;
import io.temco.guhada.blockchain.repository.CompanyRepository;
import io.temco.guhada.blockchain.repository.ProductRepository;
import io.temco.guhada.blockchain.repository.TransactRepository;
import io.temco.guhada.blockchain.service.SmartContractService;
import io.temco.guhada.blockchain.smartcontract.TransactSC;
import io.temco.guhada.blockchain.util.AES256Util;
import io.temco.guhada.blockchain.util.BarcodeUtil;
import io.temco.guhada.blockchain.util.Constrants;
import io.temco.guhada.blockchain.util.HashUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthTransaction;
import org.web3j.protocol.http.HttpService;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.transaction.Transactional;
import java.io.IOException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
@Service("smartContractService")
public class SmartContractServiceImpl implements SmartContractService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TransactRepository transactRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private ApiUsageRepository apiUsageRepository;

    @Autowired
    private BarcodeUtil barcodeUtil;

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
        log.info("Connected to baobab.klaytn client version: {}", web3j.web3ClientVersion().send().getWeb3ClientVersion());

        AES256Util aes256Util = new AES256Util();
        credentials = Credentials.create(aes256Util.decrypt(privateKey));
        ECKeyPair ecKeyPair = ECKeyPair.create(new BigInteger(credentials.getEcKeyPair().getPrivateKey().toString()) );
        credentials = Credentials.create(ecKeyPair);
        log.info("My address: {}", credentials.getAddress());

        BigInteger gasPrice = new BigInteger("30000");
        System.out.println("Using gas price of: " + gasPrice );
        transactSC = transactSC.load(contractAddress, web3j, credentials, gasPrice, new BigInteger("400000"));

    }

    @PreDestroy
    public void stop(){
        web3j.shutdown();
        log.info("Connection to RSK closed.");
    }

    @Override
    public GenerateQrCodeResponse generateQrCode(String apiToken, GenerateQrCodeRequest generateQrCodeRequest) throws Exception {
        Company company = companyRepository.findByApiToken(apiToken);
        if(company == null){
            throw new Exception("apitoken error");
        }
        apiLimitCheck(company);
        // Product 생성
        Product product = getProduct(generateQrCodeRequest, company);
        TransactRequest transactRequest = TransactRequest.of(product.getProductId(), generateQrCodeRequest);
        Transact transact = transactInsert(transactRequest, company);
        GenerateQrCodeResponse generateQrCodeResponse = new GenerateQrCodeResponse();
        generateQrCodeResponse.setContractAddress(blockChainTransact(transact).getContractAddress());
        generateQrCodeResponse.setProductId(product.getProductId());
        generateQrCodeResponse.setQrCode(product.getQrCodeUrl());
        return generateQrCodeResponse;

    }

    @Override
    public UploadToBlockchainResponse uploadToBlockchain(String apiToken,TransactRequest transactRequest) throws Exception {
        Company company = companyRepository.findByApiToken(apiToken);
        if(company == null){
            throw new Exception("apitoken error");
        }

        if(!productRepository.findById(transactRequest.getProductId()).isPresent()){
            throw new Exception("product not exist");
        }

        apiLimitCheck(company);

        Transact transact = transactInsert(transactRequest, company);
        return UploadToBlockchainResponse.of(blockChainTransact(transact));
    }



    @Override
    public BlockChainInfo getBlockChainInfo(String apiToken,String hashId) throws Exception {
        Company company = companyRepository.findByApiToken(apiToken);
        if(company == null){
            throw new Exception("apitoken error");
        }
        EthTransaction ethTransaction = web3j.ethGetTransactionByHash(hashId).send();
        log.debug("input : " + ethTransaction.getResult().getInput());
        Transact transact = transactRepository.findByContractAddress(hashId);
        Product product = productRepository.getOne(transact.getProductId());
        return new BlockChainInfo(ProductResponse.of(product), TransactResponse.of(transact));
    }

    @Override
    @Transactional
    public CompanyResponse registerCompany(CompanyRequest companyRequest) {

        Company company = new Company();
        company.setCompanyName(companyRequest.getCompanyName());
        company.setCategory(companyRequest.getCategory());
        company.setMonthLimit(Constrants.DEFAULT_API_MONTH_LIMIT);
        company.setDayLimit(Constrants.DEFAULT_API_DAY_LIMIT);
        company = companyRepository.save(company);

        company.setApiToken(HashUtils.getSha(company.getCompanyId() + "-" + company.getCompanyName() + "-" + company.getCategory()));
        return CompanyResponse.of(companyRepository.save(company));
    }

    private Product getProduct(GenerateQrCodeRequest generateQrCodeRequest, Company company) throws WriterException, IOException {
        Product product = new Product();
        product.setProductName(generateQrCodeRequest.getProductName());
        product.setCompanyId(company.getCompanyId());
        product.setProductCode(generateQrCodeRequest.getProductCode());
        product.setCategory(generateQrCodeRequest.getCategory());
        product.setCertificate(generateQrCodeRequest.getCertificate());
        product.setRegDate(generateQrCodeRequest.getRegDate());
        product.setSalesDate(generateQrCodeRequest.getSalesDate());
        product.setOrderNumber(generateQrCodeRequest.getOrderNumber());
        product.setBoxSize(generateQrCodeRequest.getBoxSize());
        String productId =  productRepository.save(product).getProductId() + "";
        String barcodeUrl = barcodeUtil.generateQRCodeImageToS3Url(productId, product.getCompanyId()+ "_" + productId, 128, 128);
        product.setQrCodeUrl(barcodeUrl);
        return product;
    }

    private void apiLimitCheck(Company company) throws Exception {
        LocalDate nowDate = LocalDate.now();
        // 월 사용량 초과
        Integer monthUsageSum = apiUsageRepository.sumMonthUsage(company.getCompanyId(), nowDate.getMonthValue());
        if (monthUsageSum == null) {
            monthUsageSum = 0;
        }
        if (monthUsageSum >= company.getMonthLimit()) {
            throw new Exception("usage limit exceed ");
        }
        // 오늘 사용량
        ApiUsage apiUsage = apiUsageRepository.findByCompanyIdAndMonthAndDay(company.getCompanyId(), nowDate.getMonthValue(), nowDate.getDayOfMonth());
        // 오늘 처음 사용인경우
        if (apiUsage == null) {
            apiUsage = new ApiUsage();
            apiUsage.setCompanyId(company.getCompanyId());
            apiUsage.setMonth(nowDate.getMonthValue());
            apiUsage.setDay(nowDate.getDayOfMonth());
            apiUsage.setUsageCount(0);
        }

        // 사용횟수 +1회
        apiUsage.setUsageCount(apiUsage.getUsageCount() + 1);
        apiUsageRepository.save(apiUsage);
    }

    private Transact transactInsert(TransactRequest transactRequest, Company company) {
        Transact transact = new Transact();
        transact.setProductId(transactRequest.getProductId());
        transact.setTransactTime(LocalDateTime.now());
        transact.setInvoiceNumber(transactRequest.getInvoiceNumber());
        transact.setDeliveryCode(transactRequest.getDeliveryCode());
        transact.setTemperature(transactRequest.getTemperature());
        transact.setLatitude(transactRequest.getLatitude());
        transact.setLongitude(transactRequest.getLongitude());
        transact.setCompanyId(company.getCompanyId());

        transact.setHash(HashUtils.getSha(transact.toString()));
        transactRepository.save(transact);
        return transact;
    }

    private Transact blockChainTransact(Transact transact) throws Exception {
        String contractAddress = transactSC.insert(BigInteger.valueOf(transact.getTransactId()),
                BigInteger.valueOf(transact.getProductId()), transact.getHash()).send().getTransactionHash();
        transact.setContractAddress(contractAddress);
        return transactRepository.save(transact);
    }
}
