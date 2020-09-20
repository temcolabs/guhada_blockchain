package io.temco.guhada.blockchain.service.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.tuples.generated.Tuple9;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.utils.Numeric;

import com.klaytn.caver.Caver;
import com.klaytn.caver.crypto.KlayCredentials;
import com.klaytn.caver.fee.FeePayerManager;
import com.klaytn.caver.methods.response.KlayTransactionReceipt;
import com.klaytn.caver.tx.manager.TransactionManager;
import com.klaytn.caver.tx.model.SmartContractExecutionTransaction;
import com.klaytn.caver.utils.ChainId;

import io.temco.guhada.blockchain.model.request.TrackRecord;
import io.temco.guhada.blockchain.repository.TrackRecordRepository;
import io.temco.guhada.blockchain.service.TrackRecordService;
import io.temco.guhada.blockchain.smartcontract.TrackRecordSC;
import io.temco.guhada.blockchain.util.HashUtils;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TrackRecordServiceImpl implements TrackRecordService {
	
	@Autowired
    private TrackRecordRepository trackRecordRepository;
	
	@Value("${smart-contract.sender-private-key}")
    private String senderPrivateKey;
	
	@Value("${smart-contract.sender-public-key}")
    private String senderPublicKey;
	
	@Value("${smart-contract.payer-private-key}")
    private String payerPrivateKey;
	
	@Value("${smart-contract.payer-public-key}")
    private String payerPublicKey;
	
	@Value("${smart-contract.track-record-contract}")
    private String trackRecordContract;
	
	private Caver caver;
	
	private KlayCredentials feePayer;
	
	private KlayCredentials sender;
	
	private KlayCredentials credentials;
	
	private TrackRecordSC trackRecordSc;
	
	@PostConstruct
    private void initCaverJava(){
//        caver = Caver.build(Caver.MAINNET_URL);
//        feePayer = KlayCredentials.create(payerPrivateKey, payerPublicKey);
//        sender = KlayCredentials.create(senderPrivateKey);
//        credentials = KlayCredentials.create(senderPrivateKey);
//        trackRecordSc = trackRecordSc.load(trackRecordContract, caver, credentials, ChainId.MAINNET, new DefaultGasProvider());
    }
	
	@Override
	public void uploadProductInfo(TrackRecord trackRecordRequest) {
//		log.info("uploadProductInfo called. deal id : " + trackRecordRequest.getDealId());
//		if(trackRecordRequest.getProductName() == null | trackRecordRequest.getBrandName() == null | 
//				trackRecordRequest.getOwner() == null) {
//			log.info("track record upload escape. " + trackRecordRequest.getDealId());
//			return;
//		}
//		trackRecordRequest.setHash(HashUtils.getSha(trackRecordRequest.toString()));
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
//
//        SmartContractExecutionTransaction smartContractExecutionTransaction = SmartContractExecutionTransaction.create(
//                senderPublicKey,
//                trackRecordContract,
//                BigInteger.ZERO,
//                Numeric.hexStringToByteArray(encodeData),
//                new DefaultGasProvider().getGasLimit(TrackRecordSC.FUNC_ADDTRANSACTION));
//
//        TransactionManager transactionManager = new TransactionManager.Builder(caver,sender).setChaindId(ChainId.MAINNET).build();
//
//        String senderRawTransaction = transactionManager.sign(smartContractExecutionTransaction,true).getValueAsString();
//
//
//        FeePayerManager feePayerManager = new FeePayerManager.Builder(caver,feePayer).setChainId(ChainId.MAINNET).build();
//
//        KlayTransactionReceipt.TransactionReceipt transactionReceipt = feePayerManager.executeTransaction(senderRawTransaction);
//        
//        if(transactionReceipt.getStatus().equals("0x1")) {
//        	log.info("uploadProductInfo success. dealId : " + trackRecordRequest.getDealId());
//        	trackRecordRequest.setTxUrl(transactionReceipt.getTransactionHash());            
//    		trackRecordRepository.save(trackRecordRequest);
//        }else {        	
//        	log.info("uploadProductInfo fail. dealId : " + trackRecordRequest.getDealId());
//            log.error("blockchain deal upload error detail : " + transactionReceipt.getErrorMessage());
//        }        		      
	}

	
	@Override
	public List<TrackRecord> getProductTransactions(int dealId) {
		List<TrackRecord> productTransactions = new ArrayList<TrackRecord>();		
		try {
			BigInteger response = trackRecordSc.getDealSize(BigInteger.valueOf(dealId)).send();
			for(int i = 0; i < response.intValue(); i++) {
				Tuple9<BigInteger, BigInteger, String, String, String, BigInteger, String, String, String> result 
					= trackRecordSc.getDeal(BigInteger.valueOf(dealId), BigInteger.valueOf(i)).send();
				TrackRecord transaction = new TrackRecord();
				transaction.setOrderId(result.getValue1().longValue());
				transaction.setDealId(result.getValue2().longValue());
				transaction.setSerialId(result.getValue3());
				transaction.setProductName(result.getValue4());
				transaction.setBrandName(result.getValue5());
				transaction.setPrice(result.getValue6().longValue());
				transaction.setColor(result.getValue7());
				transaction.setOwner(result.getValue8());
				transaction.setHash(result.getValue9());
				productTransactions.add(transaction);
			}									
		} catch (Exception e) {
			e.printStackTrace();
		}        
		return productTransactions;
	}


}
