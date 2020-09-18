package io.temco.guhada.blockchain.service.impl;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.util.Arrays;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.protocol.exceptions.TransactionException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.klaytn.caver.Caver;
import com.klaytn.caver.contract.Contract;
import com.klaytn.caver.methods.request.CallObject;
import com.klaytn.caver.methods.response.Bytes32;
import com.klaytn.caver.methods.response.TransactionReceipt;
import com.klaytn.caver.transaction.response.PollingTransactionReceiptProcessor;
import com.klaytn.caver.transaction.response.TransactionReceiptProcessor;
import com.klaytn.caver.transaction.type.FeeDelegatedSmartContractExecution;
import com.klaytn.caver.tx.gas.DefaultGasProvider;
import com.klaytn.caver.wallet.keyring.KeyStore;
import com.klaytn.caver.wallet.keyring.KeyringFactory;
import com.klaytn.caver.wallet.keyring.SingleKeyring;

import io.temco.guhada.blockchain.model.GuhadaTransact;
import io.temco.guhada.blockchain.model.request.GuhadaTransactRequest;
import io.temco.guhada.blockchain.repository.GuhadaTransactRepository;
import io.temco.guhada.blockchain.service.GuhadaContractMainnetService;
import io.temco.guhada.blockchain.util.HashUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by Shin Han
 * Since 2019-10-15
 */
@Service
@Slf4j
public class GuhadaContractMainnetServiceImpl implements GuhadaContractMainnetService {

	private final String ABI = "[{\"constant\":false,\"inputs\":[{\"name\":\"transactId\",\"type\":\"int256\"},{\"name\":\"productId\",\"type\":\"int256\"},{\"name\":\"hash\",\"type\":\"string\"}],\"name\":\"insert\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"name\":\"productId\",\"type\":\"int256\"}],\"name\":\"getByProduct\",\"outputs\":[{\"name\":\"\",\"type\":\"int256[]\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"name\":\"transactId\",\"type\":\"int256\"}],\"name\":\"getHash\",\"outputs\":[{\"name\":\"\",\"type\":\"string\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"}]";
	
    @Autowired
    private GuhadaTransactRepository guhadaTransactRepository;

    //region klaytn
    private Caver caver;
    @Value("${smart-contract.payer-private-key}")
    private String payerPrivateKey;
    @Value("${smart-contract.payer-public-key}")
    private String payerPublicKey;
    @Value("${smart-contract.sender-private-key}")
    private String senderPrivateKey;
    @Value("${smart-contract.sender-public-key}")
    private String senderPublicKey;
    @Value("${smart-contract.delegation-contract}")
    private String delegationContract;
    
    @Value("${smart-contract.kestore-decryption}")
	private String keyStoreDecrypt;

    private final String data ="608060405234801561001057600080fd5b50610538806100206000396000f300608060405260043610610057576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff1680631a1a2beb1461005c578063595cb289146100d95780636795ee1d1461017f575b600080fd5b34801561006857600080fd5b506100d76004803603810190808035906020019092919080359060200190929190803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290505050610201565b005b3480156100e557600080fd5b50610104600480360381019080803590602001909291905050506102ce565b6040518080602001828103825283818151815260200191508051906020019080838360005b83811015610144578082015181840152602081019050610129565b50505050905090810190601f1680156101715780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b34801561018b57600080fd5b506101aa60048036038101908080359060200190929190505050610385565b6040518080602001828103825283818151815260200191508051906020019060200280838360005b838110156101ed5780820151818401526020810190506101d2565b505050509050019250505060405180910390f35b600060606040519081016040528085815260200184815260200183815250600080868152602001908152602001600020600082015181600001556020820151816001015560408201518160020190805190602001906102619291906103f0565b509050506001600084815260200190815260200160002090508084908060018154018082558091505090600182039060005260206000200160009091929091909150555080600160008581526020019081526020016000209080546102c7929190610470565b5050505050565b60606000808381526020019081526020016000206002018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156103795780601f1061034e57610100808354040283529160200191610379565b820191906000526020600020905b81548152906001019060200180831161035c57829003601f168201915b50505050509050919050565b6060600160008381526020019081526020016000208054806020026020016040519081016040528092919081815260200182805480156103e457602002820191906000526020600020905b8154815260200190600101908083116103d0575b50505050509050919050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061043157805160ff191683800117855561045f565b8280016001018555821561045f579182015b8281111561045e578251825591602001919060010190610443565b5b50905061046c91906104c2565b5090565b8280548282559060005260206000209081019282156104b15760005260206000209182015b828111156104b0578254825591600101919060010190610495565b5b5090506104be91906104e7565b5090565b6104e491905b808211156104e05760008160009055506001016104c8565b5090565b90565b61050991905b808211156105055760008160009055506001016104ed565b5090565b905600a165627a7a72305820ea792c0a7dc13c7bdffd8cf0b60c729a2f21b3fad8fbbf0831735b4d35a113300029";
    private SingleKeyring feePayer;
    private SingleKeyring sender;
    
    private Contract contract;
    
    @PostConstruct
    private void initCaverJava(){
    	try {
	        caver = new Caver(Caver.MAINNET_URL);
	        contract = new Contract(caver, ABI, delegationContract);
	        
	        log.info("Contract Address : {}", contract.getContractAddress());
	        
	        ObjectMapper mapper = new ObjectMapper();
		    FileReader file = new FileReader("src/main/resources/keystore.json");
			KeyStore store = mapper.readValue(file, KeyStore.class);
					    		
		    feePayer = (SingleKeyring)KeyringFactory.decrypt(store, keyStoreDecrypt);
		    sender = KeyringFactory.createFromPrivateKey(senderPrivateKey);
		    
		    caver.wallet.add(sender);
	        caver.wallet.add(feePayer);	        	       
	        	        
    	} catch (Exception e) {
	    	e.printStackTrace();
	    }
    }   

    @Override
    public GuhadaTransact uploadToBlockchainFeeDelegationMainNet(GuhadaTransactRequest guhadaTransactRequest) {
    	String transactionHash = null;
    	GuhadaTransact guhadaTransact = GuhadaTransact.of(guhadaTransactRequest);
    	try {        
    		        
	        guhadaTransact.setHash(HashUtils.getSha(guhadaTransact.toString()));
	        guhadaTransact = guhadaTransactRepository.save(guhadaTransact);       
	        
	        String encodeData = contract.getMethod("insert").encodeABI(Arrays.asList(
	        		BigInteger.valueOf(guhadaTransact.getGuhadaTransactId()),
	        		BigInteger.valueOf(guhadaTransact.getProductId()),
	        		guhadaTransact.getHash()));
	        
	        //TODO : caver 1.5버전에서 gas limit 받는거와 deprecated 된  DefaultGasProvider 로 비교 필요      
	        CallObject callObject = CallObject.createCallObject(delegationContract);
	        String gas = contract.getMethod("insert").estimateGas(Arrays.asList(
	        		BigInteger.valueOf(guhadaTransact.getGuhadaTransactId()),
	        		BigInteger.valueOf(guhadaTransact.getProductId()),
	        		guhadaTransact.getHash()), callObject);
		    
	        BigInteger gasLimit = new DefaultGasProvider().getGasLimit("insert");
	
	        FeeDelegatedSmartContractExecution feeDelegatedSmartConstract = new FeeDelegatedSmartContractExecution.Builder()
	        		.setKlaytnCall(caver.rpc.klay)
	        		.setFrom(sender.getAddress())
	        		.setTo(delegationContract)
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
        return guhadaTransact;
    }
}
