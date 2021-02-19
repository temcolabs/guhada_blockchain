package io.temco.guhada.blockchain.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.DefaultGasProvider;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.klaytn.caver.Caver;
import com.klaytn.caver.contract.Contract;
import com.klaytn.caver.methods.request.CallObject;
import com.klaytn.caver.methods.response.Bytes32;
import com.klaytn.caver.methods.response.TransactionReceipt;
import com.klaytn.caver.transaction.response.PollingTransactionReceiptProcessor;
import com.klaytn.caver.transaction.response.TransactionReceiptProcessor;
import com.klaytn.caver.transaction.type.FeeDelegatedSmartContractExecution;
import com.klaytn.caver.wallet.keyring.KeyStore;
import com.klaytn.caver.wallet.keyring.KeyringFactory;
import com.klaytn.caver.wallet.keyring.SingleKeyring;

import io.temco.guhada.blockchain.model.request.TrackRecord;
import io.temco.guhada.blockchain.repository.TrackRecordRepository;
import io.temco.guhada.blockchain.service.TrackRecordService;
import io.temco.guhada.blockchain.util.HashUtils;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Credentials;

@Service
@Slf4j
public class TrackRecordServiceImpl implements TrackRecordService {
	
	private final String ABI =  "[{\"constant\":false,\"inputs\":[{\"name\":\"orderId\",\"type\":\"uint256\"},{\"name\":\"dealId\",\"type\":\"uint256\"},{\"name\":\"serialNumber\",\"type\":\"string\"},{\"name\":\"productName\",\"type\":\"string\"},{\"name\":\"brand\",\"type\":\"string\"},{\"name\":\"price\",\"type\":\"uint256\"},{\"name\":\"color\",\"type\":\"string\"},{\"name\":\"owner\",\"type\":\"string\"},{\"name\":\"hash\",\"type\":\"string\"}],\"name\":\"addTransaction\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"name\":\"dealId\",\"type\":\"uint256\"},{\"name\":\"index\",\"type\":\"uint256\"}],\"name\":\"getDeal\",\"outputs\":[{\"name\":\"\",\"type\":\"uint256\"},{\"name\":\"\",\"type\":\"uint256\"},{\"name\":\"\",\"type\":\"string\"},{\"name\":\"\",\"type\":\"string\"},{\"name\":\"\",\"type\":\"string\"},{\"name\":\"\",\"type\":\"uint256\"},{\"name\":\"\",\"type\":\"string\"},{\"name\":\"\",\"type\":\"string\"},{\"name\":\"\",\"type\":\"string\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"name\":\"dealId\",\"type\":\"uint256\"}],\"name\":\"getDealSize\",\"outputs\":[{\"name\":\"\",\"type\":\"uint256\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"}]";
	
	@Autowired
    private TrackRecordRepository trackRecordRepository;
	
	@Value("${smart-contract.payer-private-key}")
    private String payerPrivateKey;
    @Value("${smart-contract.payer-public-key}")
    private String payerPublicKey;
    @Value("${smart-contract.sender-private-key}")
    private String senderPrivateKey;
    @Value("${smart-contract.sender-public-key}")
    private String senderPublicKey;	
	@Value("${smart-contract.track-record-contract}")
    private String trackRecordContractAddress;
	
	@Value("${smart-contract.kestore-decryption}")
	private String keyStoreDecrypt;
	
	@Value("${kas-access-key}")
	private String kasAccessKey;
    
	@Value("${kas-secrect-access-key}")
    private String secretAccessKey;
        
    private Caver caver;
           
    private SingleKeyring feePayer;
    private SingleKeyring sender;
    
    private Contract contract;
	
    @PostConstruct
    private void initCaverJava(){
    	try {
    		HttpService httpService = new HttpService("https://node-api.klaytnapi.com/v1/klaytn");
    		String auth = Credentials.basic(kasAccessKey, secretAccessKey, StandardCharsets.UTF_8);
    		httpService.addHeader("Authorization", auth);
    		httpService.addHeader("x-chain-id", "8217");
    		
	        caver = new Caver(httpService);
	        contract = new Contract(caver, ABI, trackRecordContractAddress);
	        
	        log.info("Contract Address: {}", contract.getContractAddress());
	        
	        ObjectMapper mapper = new ObjectMapper();	    
	        
	        ClassPathResource resource = new ClassPathResource("keystore.json");	    
	 
	        InputStream inputStream = resource.getInputStream();
	        File file = File.createTempFile("keystore", ".json");
	        FileUtils.copyInputStreamToFile(inputStream, file);
	        inputStream.close();
	         	        
	        log.info("File Found for keystore.json : " + file.exists());	 
	        
			KeyStore store = mapper.readValue(file, KeyStore.class);
					    		
		    feePayer = (SingleKeyring)KeyringFactory.decrypt(store, keyStoreDecrypt);
		    sender = KeyringFactory.createFromPrivateKey(senderPrivateKey);
		    
		    log.info("feePayer Address : {}", feePayer.getAddress());
		    log.info("sender Address : {}", sender.getAddress());
		    caver.wallet.add(sender);
	        caver.wallet.add(feePayer);	        
    	} catch (Exception e) {
    		log.error("track record init exception : {}", e.getMessage());
	    	e.printStackTrace();
	    }   
    }
        
    public void testUpload() {
    	TrackRecord trackRecord = new TrackRecord();
    	trackRecord.setOrderId(0L);
    	trackRecord.setDealId(3866755L);
    	trackRecord.setSerialId("RL5004 205 A100");
    	trackRecord.setProductName("21SS 멀버리 핸드백 RL5004 205 A100");
    	trackRecord.setBrandName("Mulberry");
    	trackRecord.setPrice(new BigDecimal(708000));
    	trackRecord.setColor("");
    	trackRecord.setOwner("TheBase");
    	trackRecord.setHash(HashUtils.getSha(trackRecord.toString()));
    	
    	this.uploadProductInfo(trackRecord);
    }
	
	@Override
	public void uploadProductInfo(TrackRecord trackRecordRequest) {
		log.info("uploadProductInfo called. deal id : " + trackRecordRequest.getDealId());
		if(trackRecordRequest.getProductName() == null | trackRecordRequest.getBrandName() == null | trackRecordRequest.getOwner() == null) {
			log.info("track record upload escape. product name or brand name or owner is null " + trackRecordRequest.getDealId());
			return;
		}
		
		trackRecordRequest.setHash(HashUtils.getSha(trackRecordRequest.toString()));
		
		try {       
			
			String encodeData = contract.getMethod("addTransaction").encodeABI(Arrays.asList(
					trackRecordRequest.getOrderId() == null ? BigInteger.valueOf(0) : BigInteger.valueOf(trackRecordRequest.getOrderId().intValue()),					
	                BigInteger.valueOf(trackRecordRequest.getDealId().intValue()),
	                Optional.ofNullable(trackRecordRequest.getSerialId()).orElse(""),
	                trackRecordRequest.getProductName(),
	                trackRecordRequest.getBrandName(),
	                BigInteger.valueOf(trackRecordRequest.getPrice().intValue()),
	                Optional.ofNullable(trackRecordRequest.getColor()).orElse(""),
	                trackRecordRequest.getOwner(),                        
	                trackRecordRequest.getHash()));
										
			BigInteger gasLimit = new DefaultGasProvider().getGasLimit("addTransaction");
			log.info("track record gas limit : {}" , gasLimit.toString());				      
	        FeeDelegatedSmartContractExecution feeDelegatedSmartConstract = new FeeDelegatedSmartContractExecution.Builder()
	        		.setKlaytnCall(caver.rpc.klay)
	        		.setFrom(sender.getAddress())
	        		.setTo(trackRecordContractAddress)
	        		.setInput(encodeData)
	        		.setGas(gasLimit)
		            .setFeePayer(feePayer.getAddress())
	        		.build();
	        log.info("track record prepare sign transaction sender adderess : {}, fee payer address : {}", sender.getAddress(), feePayer.getAddress() );
        
        
	        caver.wallet.sign(sender.getAddress(), feeDelegatedSmartConstract);
	        log.info("track record prepare sign transaction sign sender complete.");
	        caver.wallet.signAsFeePayer(feePayer.getAddress(), feeDelegatedSmartConstract);
	        log.info("track record complete prepare sign transaction");
	        Bytes32 response = caver.rpc.klay.sendRawTransaction(feeDelegatedSmartConstract).send();
	        log.info("track record response : ", response.getResult());
	        TransactionReceiptProcessor processor = new PollingTransactionReceiptProcessor(caver, 1000, 30);
	        TransactionReceipt.TransactionReceiptData receipt = processor.waitForTransactionReceipt(response.getResult());
	        log.info("track record final transaction receipt : ", receipt.getStatus());
			String transactionHash = receipt.getStatus().equals("0x1") ? receipt.getTransactionHash() : null;
	       
	        if(transactionHash != null) {
	        	log.info("uploadProductInfo success. dealId : " + trackRecordRequest.getDealId());
	        	trackRecordRequest.setTxUrl(transactionHash);            
	    		trackRecordRepository.save(trackRecordRequest);
	        }else {        	
	        	log.info("uploadProductInfo fail. dealId : " + trackRecordRequest.getDealId());            
	        }
        } catch (IOException | TransactionException | ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}   
	        
	}

	
	@Override
	public List<TrackRecord> getProductTransactions(Long dealId) {
		List<TrackRecord> productTransactions = new ArrayList<TrackRecord>();		
		try {			
			List<Type> dealSizeResult = contract.getMethod("getDealSize").call(Arrays.asList(BigInteger.valueOf(dealId)), CallObject.createCallObject());			
			int transactionSize = ((Uint256) dealSizeResult.get(0)).getValue().intValue();
			TrackRecord trackRecord = trackRecordRepository.findTopByDealIdOrderByCreatedAtDesc(dealId).orElse(null);
			if(trackRecord == null) {
				log.info("transaction record on db is null, deal id : " + dealId.toString());
				return productTransactions;
			}
			for(int i = 0; i < transactionSize; i++) {
				List<Type> dealResult = contract.getMethod("getDeal").call(Arrays.asList(BigInteger.valueOf(dealId), i), CallObject.createCallObject());
				TrackRecord transaction = new TrackRecord();
				transaction.setOrderId(((BigInteger)dealResult.get(0).getValue()).longValue());
				transaction.setDealId(((BigInteger)dealResult.get(1).getValue()).longValue());
				transaction.setSerialId(dealResult.get(2).getValue().toString());
				transaction.setProductName(dealResult.get(3).getValue().toString());
				transaction.setBrandName(dealResult.get(4).getValue().toString());
				transaction.setPrice(new BigDecimal(((BigInteger)dealResult.get(5).getValue()).doubleValue()));
				transaction.setColor(dealResult.get(6).getValue().toString());
				transaction.setOwner(dealResult.get(7).getValue().toString());
				transaction.setHash(dealResult.get(8).getValue().toString());
				transaction.setTxUrl("https://scope.klaytn.com/tx/" + trackRecord.getTxUrl());
				productTransactions.add(transaction);
			}										
		} catch (Exception e) {
			e.printStackTrace();
		}        
		return productTransactions;
	}


}
