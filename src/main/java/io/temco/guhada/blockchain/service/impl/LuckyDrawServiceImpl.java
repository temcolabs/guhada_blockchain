package io.temco.guhada.blockchain.service.impl;

import com.klaytn.caver.Caver;
import com.klaytn.caver.crypto.KlayCredentials;
import com.klaytn.caver.fee.FeePayerManager;
import com.klaytn.caver.methods.response.KlayTransactionReceipt;
import com.klaytn.caver.tx.manager.TransactionManager;
import com.klaytn.caver.tx.model.SmartContractExecutionTransaction;
import com.klaytn.caver.utils.ChainId;
import io.temco.guhada.blockchain.mapper.LuckyDrawMapper;
import io.temco.guhada.blockchain.model.LuckyDrawModel;
import io.temco.guhada.blockchain.model.request.LuckyDrawRequest;
import io.temco.guhada.blockchain.service.LuckyDrawService;
import io.temco.guhada.blockchain.smartcontract.LuckyDraw;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.utils.Numeric;

import javax.annotation.PostConstruct;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;

/**
 * Created by Shin Han
 * Since 2019-11-13
 */
@Service
@Slf4j
public class LuckyDrawServiceImpl implements LuckyDrawService {

    @Autowired
    private LuckyDrawMapper luckyDrawMapper;

    private final String data ="608060405234801561001057600080fd5b50610d1a806100206000396000f3fe608060405234801561001057600080fd5b50600436106100625760003560e01c80633b304147146100675780633c0202f01461010e5780639dfc066114610274578063ac3a5e93146102a2578063cf1ab1af146103e4578063fe85f40a1461048b575b600080fd5b6100936004803603602081101561007d57600080fd5b810190808035906020019092919050505061053c565b6040518080602001828103825283818151815260200191508051906020019080838360005b838110156100d35780820151818401526020810190506100b8565b50505050905090810190601f1680156101005780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b6102726004803603608081101561012457600080fd5b810190808035906020019064010000000081111561014157600080fd5b82018360208201111561015357600080fd5b8035906020019184600183028401116401000000008311171561017557600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f8201169050808301925050505050505091929192908035906020019092919080359060200190929190803590602001906401000000008111156101ec57600080fd5b8201836020820111156101fe57600080fd5b8035906020019184600183028401116401000000008311171561022057600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f8201169050808301925050505050505091929192905050506105c8565b005b6102a06004803603602081101561028a57600080fd5b81019080803590602001909291905050506106ff565b005b61035b600480360360208110156102b857600080fd5b81019080803590602001906401000000008111156102d557600080fd5b8201836020820111156102e757600080fd5b8035906020019184600183028401116401000000008311171561030957600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f820116905080830192505050505050509192919290505050610721565b6040518084815260200183815260200180602001828103825283818151815260200191508051906020019080838360005b838110156103a757808201518184015260208101905061038c565b50505050905090810190601f1680156103d45780820380516001836020036101000a031916815260200191505b5094505050505060405180910390f35b610410600480360360208110156103fa57600080fd5b81019080803590602001909291905050506107f9565b6040518080602001828103825283818151815260200191508051906020019080838360005b83811015610450578082015181840152602081019050610435565b50505050905090810190601f16801561047d5780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b6104c1600480360360408110156104a157600080fd5b8101908080359060200190929190803590602001909291905050506108a9565b6040518080602001828103825283818151815260200191508051906020019080838360005b838110156105015780820151818401526020810190506104e6565b50505050905090810190601f16801561052e5780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b606060006001600084815260200190815260200160002080549050905060006105648261096f565b905060016000858152602001908152602001600020818154811061058457fe5b906000526020600020016002600086815260200190815260200160002090805460018160011615610100020316600290046105c09291906109b6565b505050919050565b6040518060600160405280848152602001838152602001828152506000856040518082805190602001908083835b6020831061061957805182526020820191506020810190506020830392506105f6565b6001836020036101000a03801982511681845116808217855250505050505090500191505090815260200160405180910390206000820151816000015560208201518160010155604082015181600201908051906020019061067c929190610a3d565b509050506000600160008581526020019081526020016000209050808590806001815401808255809150509060018203906000526020600020016000909192909190915090805190602001906106d3929190610abd565b505080600160008681526020019081526020016000209080546106f7929190610b3d565b505050505050565b60016000828152602001908152602001600020600061071e9190610bad565b50565b600081805160208101820180518482526020830160208501208183528095505050505050600091509050806000015490806001015490806002018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156107ef5780601f106107c4576101008083540402835291602001916107ef565b820191906000526020600020905b8154815290600101906020018083116107d257829003601f168201915b5050505050905083565b60026020528060005260406000206000915090508054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156108a15780601f10610876576101008083540402835291602001916108a1565b820191906000526020600020905b81548152906001019060200180831161088457829003601f168201915b505050505081565b600160205281600052604060002081815481106108c257fe5b90600052602060002001600091509150508054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156109675780601f1061093c57610100808354040283529160200191610967565b820191906000526020600020905b81548152906001019060200180831161094a57829003601f168201915b505050505081565b600081424460405160200180838152602001828152602001925050506040516020818303038152906040528051906020012060001c816109ab57fe5b0660ff169050919050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106109ef5780548555610a2c565b82800160010185558215610a2c57600052602060002091601f016020900482015b82811115610a2b578254825591600101919060010190610a10565b5b509050610a399190610bce565b5090565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f10610a7e57805160ff1916838001178555610aac565b82800160010185558215610aac579182015b82811115610aab578251825591602001919060010190610a90565b5b509050610ab99190610bce565b5090565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f10610afe57805160ff1916838001178555610b2c565b82800160010185558215610b2c579182015b82811115610b2b578251825591602001919060010190610b10565b5b509050610b399190610bce565b5090565b828054828255906000526020600020908101928215610b9c5760005260206000209182015b82811115610b9b5782829080546001816001161561010002031660029004610b8b929190610bf3565b5091600101919060010190610b62565b5b509050610ba99190610c7a565b5090565b5080546000825590600052602060002090810190610bcb9190610c7a565b50565b610bf091905b80821115610bec576000816000905550600101610bd4565b5090565b90565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f10610c2c5780548555610c69565b82800160010185558215610c6957600052602060002091601f016020900482015b82811115610c68578254825591600101919060010190610c4d565b5b509050610c769190610bce565b5090565b610ca391905b80821115610c9f5760008181610c969190610ca6565b50600101610c80565b5090565b90565b50805460018160011615610100020316600290046000825580601f10610ccc5750610ceb565b601f016020900490600052602060002090810190610cea9190610bce565b5b5056fea165627a7a72305820420b4a9a5c63707026ece97185901a9414b3b1b0a7ad060e7da9e03a735fb41d0029";
    private final String payerPrivateKey = "";
    private final String payerPublicKey = "";
    private final String senderPrivateKey = "";
    private final String senderPublicKey = "";
    private final String delegationContract = "";

    private Caver caver;
    private LuckyDraw luckyDraw;
    private KlayCredentials feePayer;
    KlayCredentials sender;
    KlayCredentials credentials = KlayCredentials.create(senderPrivateKey);

    @PostConstruct
    private void initCaverJava(){

        caver = Caver.build(Caver.MAINNET_URL);
        feePayer = KlayCredentials.create(payerPrivateKey, payerPublicKey);
        sender = KlayCredentials.create(senderPrivateKey);

        luckyDraw = luckyDraw.load(delegationContract, caver, credentials, ChainId.BAOBAB_TESTNET, new DefaultGasProvider());

    }


    @Override
    public String entry(LuckyDrawRequest luckyDrawRequest) {
        LuckyDrawModel luckyDrawModel = luckyDrawMapper.getUser(luckyDrawRequest.getDealId(), luckyDrawRequest.getUserId());
        String eventId = luckyDrawModel.getDealId() + "_" + luckyDrawModel.getUserId();

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
            }catch (Exception ex){
                log.error(ex.getMessage());
            }

            if(!transactionHash.isEmpty()){
                break;
            }
        }

        return transactionHash;

    }

    private String entryLuckyDraw(LuckyDrawRequest luckyDrawRequest, String eventId, String emailString) {
        final Function function = new Function(
                LuckyDraw.FUNC_ENTRY,
                Arrays.<Type>asList(new Utf8String(eventId),
                        new Uint256(BigInteger.valueOf(luckyDrawRequest.getDealId())),
                        new Uint256(BigInteger.valueOf(luckyDrawRequest.getUserId())),
                        new Utf8String(emailString)),
                Collections.<TypeReference<?>>emptyList());
        String encodeData = FunctionEncoder.encode(function);
        SmartContractExecutionTransaction smartContractExecutionTransaction = SmartContractExecutionTransaction.create(
                senderPublicKey,
                delegationContract,
                BigInteger.ZERO,
                Numeric.hexStringToByteArray(encodeData),
                new DefaultGasProvider().getGasLimit(LuckyDraw.FUNC_ENTRY));

        TransactionManager transactionManager = new TransactionManager.Builder(caver,sender).setChaindId(ChainId.MAINNET).build();

        String senderRawTransaction = transactionManager.sign(smartContractExecutionTransaction,true).getValueAsString();

        FeePayerManager feePayerManager = new FeePayerManager.Builder(caver,feePayer).setChainId(ChainId.MAINNET).build();

        KlayTransactionReceipt.TransactionReceipt transactionReceipt = feePayerManager.executeTransaction(senderRawTransaction);
        return transactionReceipt.getTransactionHash();
    }

    @Override
    public Long draw(long dealId) throws Exception {
        String transactionHash = "";
        while(true) {
        final Function function = new Function(
                    LuckyDraw.FUNC_DRAW,
                    Arrays.<Type>asList(new Uint256(dealId)),
                    Collections.<TypeReference<?>>emptyList());
        String encodeData = FunctionEncoder.encode(function);
        SmartContractExecutionTransaction smartContractExecutionTransaction = SmartContractExecutionTransaction.create(
                senderPublicKey,
                delegationContract,
                BigInteger.ZERO,
                Numeric.hexStringToByteArray(encodeData),
                new DefaultGasProvider().getGasLimit(LuckyDraw.FUNC_ENTRY));

        TransactionManager transactionManager = new TransactionManager.Builder(caver,sender).setChaindId(ChainId.MAINNET).build();

        String senderRawTransaction = transactionManager.sign(smartContractExecutionTransaction,true).getValueAsString();

        FeePayerManager feePayerManager = new FeePayerManager.Builder(caver,feePayer).setChainId(ChainId.MAINNET).build();

        KlayTransactionReceipt.TransactionReceipt transactionReceipt = feePayerManager.executeTransaction(senderRawTransaction);
        transactionHash = transactionReceipt.getTransactionHash();

            if(!ObjectUtils.isEmpty(transactionHash)){
                break;
            }
        }
        return getLuckyDrawWinner(dealId);
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
//        Tuple3<BigInteger, BigInteger, String> data = luckyDraw.entryUsers(response).send();
//        LuckyDrawModel luckyDrawModel = new LuckyDrawModel();
//        luckyDrawModel.setDealId(Long.valueOf(response.split("_")[0]));
//        luckyDrawModel.setUserId(Long.valueOf(response.split("_")[1]));
//        luckyDrawModel.setUserEmail(response.split()));
        return Long.valueOf(response.split("_")[1]);
    }
}
