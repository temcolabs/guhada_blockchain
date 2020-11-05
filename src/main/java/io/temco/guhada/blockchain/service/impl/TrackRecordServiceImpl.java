package io.temco.guhada.blockchain.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.web3j.abi.datatypes.Type;
import org.web3j.protocol.exceptions.TransactionException;
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
import io.temco.guhada.blockchain.smartcontract.LuckyDraw;
import io.temco.guhada.blockchain.util.HashUtils;
import lombok.extern.slf4j.Slf4j;

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
        
    private Caver caver;
           
    private SingleKeyring feePayer;
    private SingleKeyring sender;
    
    private Contract contract;
	
    @PostConstruct
    private void initCaverJava(){
    	try {
	        caver = new Caver(Caver.MAINNET_URL);
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
	        
	        //this.testUpload();
    	} catch (Exception e) {
    		log.error("lucky draw init : {}", e.getMessage());
	    	e.printStackTrace();
	    }   
    }
        
    public void testUpload() {
    	TrackRecord trackRecord = new TrackRecord();
    	trackRecord.setOrderId(0L);
    	trackRecord.setDealId(3456525L);
    	trackRecord.setSerialId("525721 W0650 1006");
    	trackRecord.setProductName("20FW 발렌시아가 스니커즈 525721 W0650 1006");
    	trackRecord.setBrandName("BALENCIAGA");
    	trackRecord.setPrice(new BigDecimal(722000));
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
					BigInteger.valueOf(trackRecordRequest.getOrderId().intValue()),
	                BigInteger.valueOf(trackRecordRequest.getDealId().intValue()),
	                trackRecordRequest.getSerialId(),
	                trackRecordRequest.getProductName(),
	                trackRecordRequest.getBrandName(),
	                BigInteger.valueOf(trackRecordRequest.getPrice().intValue()),
	                trackRecordRequest.getColor(),
	                trackRecordRequest.getOwner(),                        
	                trackRecordRequest.getHash()));
			
					
	//        final Function function = new Function(
	//                TrackRecordSC.FUNC_ADDTRANSACTION,
	//                Arrays.<Type>asList(
	//                		new Uint256(BigInteger.valueOf(trackRecordRequest.getOrderId())),
	//                        new Uint256(BigInteger.valueOf(trackRecordRequest.getDealId())),
	//                        new Utf8String(trackRecordRequest.getSerialId()),
	//                        new Utf8String(trackRecordRequest.getProductName()),
	//                        new Utf8String(trackRecordRequest.getBrandName()),
	//                        new Uint256(BigInteger.valueOf(trackRecordRequest.getPrice())),
	//                        new Utf8String(trackRecordRequest.getColor()),
	//                        new Utf8String(trackRecordRequest.getOwner()),                        
	//                        new Utf8String(trackRecordRequest.getHash())),
	//                Collections.<TypeReference<?>>emptyList());
	//        String encodeData = FunctionEncoder.encode(function);
			
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
	public List<TrackRecord> getProductTransactions(int dealId) {
		List<TrackRecord> productTransactions = new ArrayList<TrackRecord>();		
		try {
			
			List<Type> retrunResult = contract.getMethod("getDealSize").call(Arrays.asList(BigInteger.valueOf(dealId)), CallObject.createCallObject());			
//			((ArrayList) retrunResult.get(0).getValue()).forEach(element -> packIdList.add(Long.valueOf(((Int256)element).getValue().longValue())));
//			
//			BigInteger response = contract.getDealSize(BigInteger.valueOf(dealId)).send();
//			
//			List<Type> retrunResult = contract.getMethod("getDeal").call(Arrays.asList(BigInteger.valueOf(packId.intValue())), CallObject.createCallObject());	        
//	        result.setBundleId(Long.valueOf(((BigInteger)retrunResult.get(0).getValue()).toString()));
//			result.setPackId(Long.valueOf(((BigInteger)retrunResult.get(1).getValue()).toString()));			
//			result.setSortDate(retrunResult.get(2).getValue().toString());
//			result.setUserCode(retrunResult.get(3).getValue().toString());
//			result.setRank(retrunResult.get(4).getValue().toString());
//			result.setUnitSize(retrunResult.get(5).getValue().toString());						
//			result.setWeight(new BigDecimal((BigInteger)retrunResult.get(6).getValue()));
//			result.setGoodsType(retrunResult.get(7).getValue().toString());
//			
//			
//			BigInteger response = trackRecordSc.getDealSize(BigInteger.valueOf(dealId)).send();
//			for(int i = 0; i < response.intValue(); i++) {
//				Tuple9<BigInteger, BigInteger, String, String, String, BigInteger, String, String, String> result 
//					= trackRecordSc.getDeal(BigInteger.valueOf(dealId), BigInteger.valueOf(i)).send();
//				TrackRecord transaction = new TrackRecord();
//				transaction.setOrderId(result.getValue1().longValue());
//				transaction.setDealId(result.getValue2().longValue());
//				transaction.setSerialId(result.getValue3());
//				transaction.setProductName(result.getValue4());
//				transaction.setBrandName(result.getValue5());
//				transaction.setPrice(result.getValue6().longValue());
//				transaction.setColor(result.getValue7());
//				transaction.setOwner(result.getValue8());
//				transaction.setHash(result.getValue9());
//				productTransactions.add(transaction);
//			}									
		} catch (Exception e) {
			e.printStackTrace();
		}        
		return productTransactions;
	}


}
