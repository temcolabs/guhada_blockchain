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
        log.error("transaction hash : {}" , transactionHash);
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
        if(transactionReceipt.getStatus().equals("1")){
            return transactionReceipt.getTransactionHash();
        }else{
            return null;
        }
    }

    @Override
    public Long draw(long dealId) throws Exception {
        String transactionHash = "";
        KlayTransactionReceipt.TransactionReceipt transactionReceipt;
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
                new DefaultGasProvider().getGasLimit(LuckyDraw.FUNC_DRAW));

        TransactionManager transactionManager = new TransactionManager.Builder(caver,sender).setChaindId(ChainId.MAINNET).build();

        String senderRawTransaction = transactionManager.sign(smartContractExecutionTransaction,true).getValueAsString();

        FeePayerManager feePayerManager = new FeePayerManager.Builder(caver,feePayer).setChainId(ChainId.MAINNET).build();

        transactionReceipt = feePayerManager.executeTransaction(senderRawTransaction);
        transactionHash = transactionReceipt.getTransactionHash();

            if(!ObjectUtils.isEmpty(transactionHash)){
                break;
            }
        }
        Long luckyDrawWinner = 0l;
        if(transactionReceipt.getStatus().equals("1")){
            luckyDrawWinner = getLuckyDrawWinner(dealId);
            luckyDrawMapper.insertLuckyDrawWinner(dealId,luckyDrawWinner);
            return luckyDrawWinner;
        }else{
            return null;
        }

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
        final Function function = new Function(
                LuckyDraw.FUNC_DESTORYDRAWITEM,
                Arrays.<Type>asList(new Uint256(dealId)),
                Collections.<TypeReference<?>>emptyList());
        String encodeData = FunctionEncoder.encode(function);
        SmartContractExecutionTransaction smartContractExecutionTransaction = SmartContractExecutionTransaction.create(
                senderPublicKey,
                delegationContract,
                BigInteger.ZERO,
                Numeric.hexStringToByteArray(encodeData),
                new DefaultGasProvider().getGasLimit(LuckyDraw.FUNC_DESTORYDRAWITEM));

        TransactionManager transactionManager = new TransactionManager.Builder(caver,sender).setChaindId(ChainId.MAINNET).build();

        String senderRawTransaction = transactionManager.sign(smartContractExecutionTransaction,true).getValueAsString();

        FeePayerManager feePayerManager = new FeePayerManager.Builder(caver,feePayer).setChainId(ChainId.MAINNET).build();

        KlayTransactionReceipt.TransactionReceipt transactionReceipt = feePayerManager.executeTransaction(senderRawTransaction);
        log.info(transactionReceipt.getTransactionHash());
    }
}
