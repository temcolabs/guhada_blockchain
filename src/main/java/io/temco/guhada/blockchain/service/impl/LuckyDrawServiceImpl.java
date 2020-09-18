package io.temco.guhada.blockchain.service.impl;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Objects;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.protocol.exceptions.TransactionException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.klaytn.caver.Caver;
import com.klaytn.caver.contract.Contract;
import com.klaytn.caver.methods.response.Bytes32;
import com.klaytn.caver.methods.response.TransactionReceipt;
import com.klaytn.caver.transaction.response.PollingTransactionReceiptProcessor;
import com.klaytn.caver.transaction.response.TransactionReceiptProcessor;
import com.klaytn.caver.transaction.type.FeeDelegatedSmartContractExecution;
import com.klaytn.caver.tx.gas.DefaultGasProvider;
import com.klaytn.caver.wallet.keyring.KeyStore;
import com.klaytn.caver.wallet.keyring.KeyringFactory;
import com.klaytn.caver.wallet.keyring.SingleKeyring;

import io.temco.guhada.blockchain.mapper.LuckyDrawMapper;
import io.temco.guhada.blockchain.model.LuckyDrawModel;
import io.temco.guhada.blockchain.model.request.LuckyDrawRequest;
import io.temco.guhada.blockchain.service.LuckyDrawService;
import io.temco.guhada.blockchain.service.retrofit.ProductApiService;
import io.temco.guhada.blockchain.smartcontract.LuckyDraw;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by Shin Han
 * Since 2019-11-13
 */
@Service
@Slf4j
public class LuckyDrawServiceImpl implements LuckyDrawService {
	
	private final String ABI = "[{\"constant\":false,\"inputs\":[{\"name\":\"dealId\",\"type\":\"uint256\"}],\"name\":\"destoryDrawItem\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"dealId\",\"type\":\"uint256\"}],\"name\":\"draw\",\"outputs\":[{\"name\":\"\",\"type\":\"string\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"eventId\",\"type\":\"string\"},{\"name\":\"dealId\",\"type\":\"uint256\"},{\"name\":\"userId\",\"type\":\"uint256\"},{\"name\":\"userEmail\",\"type\":\"string\"}],\"name\":\"entry\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"name\":\"\",\"type\":\"string\"}],\"name\":\"entryUsers\",\"outputs\":[{\"name\":\"dealId\",\"type\":\"uint256\"},{\"name\":\"userId\",\"type\":\"uint256\"},{\"name\":\"userEmail\",\"type\":\"string\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"name\":\"\",\"type\":\"uint256\"},{\"name\":\"\",\"type\":\"uint256\"}],\"name\":\"luckyDrawEntrys\",\"outputs\":[{\"name\":\"\",\"type\":\"string\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"name\":\"\",\"type\":\"uint256\"}],\"name\":\"luckyDrawWinner\",\"outputs\":[{\"name\":\"\",\"type\":\"string\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"}]";

    private final LuckyDrawMapper luckyDrawMapper;
    private final ProductApiService productApiService;

    @Value("${smart-contract.payer-private-key}")
    private String payerPrivateKey;
    @Value("${smart-contract.payer-public-key}")
    private String payerPublicKey;
    @Value("${smart-contract.sender-private-key}")
    private String senderPrivateKey;
    @Value("${smart-contract.sender-public-key}")
    private String senderPublicKey;
    @Value("${smart-contract.lucky-draw-contract}")
    private String luckyDrawContractAddress;
    
    @Value("${smart-contract.kestore-decryption}")
	private String keyStoreDecrypt;
    
    private final BigInteger gasLimit = BigInteger.valueOf(43000000l);
    private Caver caver;
    private LuckyDraw luckyDraw;
        
    private SingleKeyring feePayer;
    private SingleKeyring sender;
    
    private Contract contract;

    public LuckyDrawServiceImpl(LuckyDrawMapper luckyDrawMapper, ProductApiService productApiService) {
        this.luckyDrawMapper = luckyDrawMapper;
        this.productApiService = productApiService;
    }        

    @PostConstruct
    private void initCaverJava(){
    	try {
	        caver = new Caver(Caver.MAINNET_URL);
	        contract = new Contract(caver, ABI, luckyDrawContractAddress);
	        
	        log.info("Contract Address : {}", contract.getContractAddress());
	        
	        ObjectMapper mapper = new ObjectMapper();
		    FileReader file = new FileReader("src/main/resources/keystore.json");
			KeyStore store = mapper.readValue(file, KeyStore.class);
					    		
		    feePayer = (SingleKeyring)KeyringFactory.decrypt(store, keyStoreDecrypt);
		    sender = KeyringFactory.createFromPrivateKey(senderPrivateKey);
		    
		    caver.wallet.add(sender);
	        caver.wallet.add(feePayer);
	        
	        LuckyDrawRequest luckyDrawRequest = new LuckyDrawRequest();	        
	        entry(luckyDrawRequest);
	        	        
    	} catch (Exception e) {
	    	e.printStackTrace();
	    }   
    }


    @Override
    public String entry(LuckyDrawRequest luckyDrawRequest) {
        LuckyDrawModel luckyDrawModel = luckyDrawMapper.getUser(luckyDrawRequest.getDealId(), luckyDrawRequest.getUserId());
        String eventId = luckyDrawModel.getDealId() + "_" + luckyDrawModel.getUserId();
        log.error("eventId : {}",eventId);
        log.error("User Email : {}",luckyDrawModel.getUserEmail());
        String userEmail = luckyDrawModel.getUserEmail().split("@")[0];
        String star = "";
        for(int i= 0; i < userEmail.length() - 2 ; i++) {
            star += "*";
        }
        String userEmailDomain = luckyDrawModel.getUserEmail().split("@")[1];
        String emailString = userEmail.substring(0,2) + star+ "@" + userEmailDomain;
        String transactionHash = "";
        for(int i=0 ; i < 3 ; i++) {
            try {
                transactionHash = entryLuckyDraw(luckyDrawRequest, eventId, emailString);
                log.error("transaction hash : {}" , transactionHash);
            }catch (Exception ex){
                log.error(ex.getMessage());
            }

            if(!transactionHash.isEmpty()){
                break;
            }
        }
        log.info("transaction hash : {}" , transactionHash);
        return transactionHash;

    }

    private String entryLuckyDraw(LuckyDrawRequest luckyDrawRequest, String eventId, String emailString) {
    	String transactionHash = null;
    	try {        
    		                	        
			String encodeData = contract.getMethod("entry").encodeABI(Arrays.asList(
						eventId,
			            BigInteger.valueOf(luckyDrawRequest.getDealId()),
			            BigInteger.valueOf(luckyDrawRequest.getUserId()),
			            emailString));			

	        //caver 1.5버전에서 gas limit 받는거  deprecated 되었지만 사용 가낭하다고 답변
			BigInteger gasLimit = new DefaultGasProvider().getGasLimit("entry");
							      
	        FeeDelegatedSmartContractExecution feeDelegatedSmartConstract = new FeeDelegatedSmartContractExecution.Builder()
	        		.setKlaytnCall(caver.rpc.klay)
	        		.setFrom(sender.getAddress())
	        		.setTo(luckyDrawContractAddress)
	        		.setInput(encodeData)
	        		.setGas(gasLimit)
		            .setFeePayer(feePayer.getAddress())
	        		.build();
	
	        caver.wallet.sign(sender.getAddress(), feeDelegatedSmartConstract);
	        caver.wallet.signAsFeePayer(feePayer.getAddress(), feeDelegatedSmartConstract);
	        
	        Bytes32 response = caver.rpc.klay.sendRawTransaction(feeDelegatedSmartConstract).send();
	        TransactionReceiptProcessor processor = new PollingTransactionReceiptProcessor(caver, 1000, 30);
	        TransactionReceipt.TransactionReceiptData receipt;			
			receipt = processor.waitForTransactionReceipt(response.getResult());
			transactionHash = receipt.getStatus().equals("0x1") ? receipt.getTransactionHash() : null;
				        	        	        	             
		} catch (IOException | ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException | TransactionException e) {
			e.printStackTrace();
		} 
		return transactionHash;
    }

    @Override
    public Long draw(long dealId) throws Exception {
        String transactionHash = "";        
        while(true) {
        	try {            
        		String encodeData = contract.getMethod("draw").encodeABI(Arrays.asList(dealId));
            
                //BigInteger gasLimit = new DefaultGasProvider().getGasLimit(LuckyDraw.FUNC_DRAW);
        		FeeDelegatedSmartContractExecution feeDelegatedSmartConstract = new FeeDelegatedSmartContractExecution.Builder()
    	        		.setKlaytnCall(caver.rpc.klay)
    	        		.setFrom(sender.getAddress())
    	        		.setTo(luckyDrawContractAddress)
    	        		.setInput(encodeData)
    	        		.setGas(gasLimit)
    		            .setFeePayer(feePayer.getAddress())
    	        		.build();
    	
    	        caver.wallet.sign(sender.getAddress(), feeDelegatedSmartConstract);
    	        caver.wallet.signAsFeePayer(feePayer.getAddress(), feeDelegatedSmartConstract);
    	        
    	        Bytes32 response = caver.rpc.klay.sendRawTransaction(feeDelegatedSmartConstract).send();
    	        TransactionReceiptProcessor processor = new PollingTransactionReceiptProcessor(caver, 1000, 30);
    	        TransactionReceipt.TransactionReceiptData receipt;			
    			receipt = processor.waitForTransactionReceipt(response.getResult());
    			transactionHash = receipt.getStatus().equals("0x1") ? receipt.getTransactionHash() : null;
    			
            if(Objects.nonNull(transactionHash)){
                break;
            }
            }catch (Exception ex){
                log.error("executeTransaction error : {}", ex.getMessage());
            }
        }

        Long luckyDrawWinner = getLuckyDrawWinner(dealId);
        productApiService.luckyDraw(new LuckyDrawRequest(dealId,luckyDrawWinner)).execute();
        return luckyDrawWinner;    	
    }

    @Override
    public LuckyDrawModel getEntryUser(long dealId, long userId) throws Exception {
        String response = luckyDraw.luckyDrawEntrys(BigInteger.valueOf(dealId), BigInteger.valueOf(userId)).send();
        LuckyDrawModel luckyDrawModel = new LuckyDrawModel();
        luckyDrawModel.setDealId(Long.valueOf(response.split("_")[0]));
        luckyDrawModel.setUserId(Long.valueOf(response.split("_")[1]));
        return luckyDrawModel;
    }

    @Override
    public Long getLuckyDrawWinner(long dealId) throws Exception {
        String response = luckyDraw.luckyDrawWinner(BigInteger.valueOf(dealId)).send();
        return Long.valueOf(response.split("_")[1]);
    }

    @Override
    public void delete(long dealId) {
    	String transactionHash = "";      
        String encodeData;
		try {
			encodeData = contract.getMethod("destoryDrawItem").encodeABI(Arrays.asList(dealId));
			FeeDelegatedSmartContractExecution feeDelegatedSmartConstract = new FeeDelegatedSmartContractExecution.Builder()
	        		.setKlaytnCall(caver.rpc.klay)
	        		.setFrom(sender.getAddress())
	        		.setTo(luckyDrawContractAddress)
	        		.setInput(encodeData)
	        		.setGas(gasLimit)
		            .setFeePayer(feePayer.getAddress())
	        		.build();

	        caver.wallet.sign(sender.getAddress(), feeDelegatedSmartConstract);
	        caver.wallet.signAsFeePayer(feePayer.getAddress(), feeDelegatedSmartConstract);
	        
	        Bytes32 response = caver.rpc.klay.sendRawTransaction(feeDelegatedSmartConstract).send();
	        TransactionReceiptProcessor processor = new PollingTransactionReceiptProcessor(caver, 1000, 30);
	        TransactionReceipt.TransactionReceiptData receipt;			
			receipt = processor.waitForTransactionReceipt(response.getResult());
			transactionHash = receipt.getStatus().equals("0x1") ? receipt.getTransactionHash() : null;
		} catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException
				| InvocationTargetException | IOException | TransactionException e) {
			e.printStackTrace();
		}
        
        log.info("lucky draw {} event destoryed. transaction hash : {} ", dealId, transactionHash);
    }
}
