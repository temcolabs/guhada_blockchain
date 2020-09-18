package io.temco.guhada.blockchain.service.impl;


import com.klaytn.caver.Caver;
import com.klaytn.caver.crypto.KlayCredentials;
import com.klaytn.caver.fee.FeePayerManager;
import com.klaytn.caver.methods.response.KlayTransactionReceipt;
import com.klaytn.caver.tx.manager.TransactionManager;
import com.klaytn.caver.tx.model.SmartContractDeployTransaction;
import com.klaytn.caver.tx.model.SmartContractExecutionTransaction;
import com.klaytn.caver.utils.ChainId;
import io.temco.guhada.blockchain.model.GuhadaTransact;
import io.temco.guhada.blockchain.model.request.GuhadaTransactRequest;
import io.temco.guhada.blockchain.model.response.GuhadaTransactResponse;
import io.temco.guhada.blockchain.repository.GuhadaTransactRepository;
import io.temco.guhada.blockchain.service.GuhadaContractService;
import io.temco.guhada.blockchain.smartcontract.GuhadaTransactSC;
import io.temco.guhada.blockchain.util.HashUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Int256;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.utils.Numeric;

import javax.annotation.PostConstruct;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Created by Shin Han
 * Since 2019-06-10
 */
@Service
@Slf4j
public class GuhadaContractServiceImpl implements GuhadaContractService {

    @Autowired
    private GuhadaTransactRepository guhadaTransactRepository;

    // region web3j(ether)

//    private Web3j web3j;
//
//    private GuhadaTransactSC guhadaTransactSC;
//
//    private Credentials credentials;
//
//
//    @PostConstruct
//    private void initCaverJava() throws java.io.IOException, GeneralSecurityException {
//        String privateKey = "11E8E50CB37DF62B66B6D43E5140495E9E1C41017FA5096EA70F9F361661D16E";
//
//        String contractAddress = "0x3dc22c969a7af9fc97f8f9feed1ab8d829be574f";
//        String chianUrl = "https://ropsten.infura.io/v3/67b3ebcb7a574122967fb41c7a968b90";
//        web3j = Web3j.build(new HttpService(chianUrl));
//        log.info(web3j.ethGasPrice().send().getGasPrice().toString());
//
//        credentials = Credentials.create(privateKey);
//        ECKeyPair ecKeyPair = ECKeyPair.create(new BigInteger(credentials.getEcKeyPair().getPrivateKey().toString()) );
//        credentials = Credentials.create(ecKeyPair);
//        log.info("My address: {}", credentials.getAddress());
//
//        guhadaTransactSC = guhadaTransactSC.load(contractAddress, web3j, credentials, new DefaultGasProvider());
//
//    }
//
//    @Override
//    public GuhadaTransact uploadToBlockchain(GuhadaTransactRequest guhadaTransactRequest) throws Exception {
//        GuhadaTransact guhadaTransact = GuhadaTransact.of(guhadaTransactRequest);
//        guhadaTransact.setHash(HashUtils.getSha(guhadaTransact.toString()));
//        guhadaTransact = guhadaTransactRepository.save(guhadaTransact);
//        String contractAddress = guhadaTransactSC.insert(BigInteger.valueOf(guhadaTransact.getGuhadaTransactId()),
//                BigInteger.valueOf(guhadaTransact.getProductId()), guhadaTransact.getHash()).send().getTransactionHash();
//        guhadaTransact.setContractAddress(contractAddress);
//        guhadaTransactRepository.save(guhadaTransact);
//        return guhadaTransact;
//    }
//
//    @Override
//    public List<GuhadaTransactResponse> getTransactData(long productId) {
//
//        List<GuhadaTransact> guhadaTransactList = guhadaTransactRepository.findAllByProductId(productId);
//        List<GuhadaTransactResponse> guhadaTransactResponseList = guhadaTransactList
//                .stream()
//                .map(guhadaTransact -> GuhadaTransactResponse.of(guhadaTransact))
//                .collect(Collectors.toList());
//        return guhadaTransactResponseList;
//    }

    //endregion


    //region klaytn
    private Caver caver;
    private GuhadaTransactSC guhadaTransactSC;
    private String senderPrivateKey = "0x46da273c96d4625032009566a3ed82837f9d22bf0d87acff82e14e6fa442b46f";
    private String senderPublicKey = "0xf868cc138d7d1b29014d2a3ff78a427aba34396d";
    private String delegationContract = "0x813baeda761546c9a91393d04902b2e1c0cd7784";

    private String data ="608060405234801561001057600080fd5b50610538806100206000396000f300608060405260043610610057576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff1680631a1a2beb1461005c578063595cb289146100d95780636795ee1d1461017f575b600080fd5b34801561006857600080fd5b506100d76004803603810190808035906020019092919080359060200190929190803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290505050610201565b005b3480156100e557600080fd5b50610104600480360381019080803590602001909291905050506102ce565b6040518080602001828103825283818151815260200191508051906020019080838360005b83811015610144578082015181840152602081019050610129565b50505050905090810190601f1680156101715780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b34801561018b57600080fd5b506101aa60048036038101908080359060200190929190505050610385565b6040518080602001828103825283818151815260200191508051906020019060200280838360005b838110156101ed5780820151818401526020810190506101d2565b505050509050019250505060405180910390f35b600060606040519081016040528085815260200184815260200183815250600080868152602001908152602001600020600082015181600001556020820151816001015560408201518160020190805190602001906102619291906103f0565b509050506001600084815260200190815260200160002090508084908060018154018082558091505090600182039060005260206000200160009091929091909150555080600160008581526020019081526020016000209080546102c7929190610470565b5050505050565b60606000808381526020019081526020016000206002018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156103795780601f1061034e57610100808354040283529160200191610379565b820191906000526020600020905b81548152906001019060200180831161035c57829003601f168201915b50505050509050919050565b6060600160008381526020019081526020016000208054806020026020016040519081016040528092919081815260200182805480156103e457602002820191906000526020600020905b8154815260200190600101908083116103d0575b50505050509050919050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061043157805160ff191683800117855561045f565b8280016001018555821561045f579182015b8281111561045e578251825591602001919060010190610443565b5b50905061046c91906104c2565b5090565b8280548282559060005260206000209081019282156104b15760005260206000209182015b828111156104b0578254825591600101919060010190610495565b5b5090506104be91906104e7565b5090565b6104e491905b808211156104e05760008160009055506001016104c8565b5090565b90565b61050991905b808211156105055760008160009055506001016104ed565b5090565b905600a165627a7a72305820ea792c0a7dc13c7bdffd8cf0b60c729a2f21b3fad8fbbf0831735b4d35a113300029";

    @PostConstruct
    private void initCaverJava() throws java.io.IOException, GeneralSecurityException {
        String payer = "0xb52673131d1f9c35b641a037682b1d0ebd6ad9f176aa41b45f60545a2f8b1a9a";

        String contractAddress = "0x003BDeC726E2986604CA74EFc1D91bE1Fa3de519";

        KlayCredentials credentials = KlayCredentials.create(payer);


        caver = Caver.build(Caver.BAOBAB_URL);
        log.info(caver.klay().getGasPrice().send().toString());
        log.info("My address: {}", credentials.getAddress());

        guhadaTransactSC = guhadaTransactSC.load(contractAddress, caver, credentials, ChainId.BAOBAB_TESTNET, new DefaultGasProvider());

    }

    @Override
    public GuhadaTransact uploadToBlockchain(GuhadaTransactRequest guhadaTransactRequest) throws Exception {
        GuhadaTransact guhadaTransact = GuhadaTransact.of(guhadaTransactRequest);
        guhadaTransact.setHash(HashUtils.getSha(guhadaTransact.toString()));
        guhadaTransact = guhadaTransactRepository.save(guhadaTransact);
        String contractAddress = guhadaTransactSC.insert(BigInteger.valueOf(guhadaTransact.getGuhadaTransactId()),
                BigInteger.valueOf(guhadaTransact.getProductId()), guhadaTransact.getHash())
                .send().getTransactionHash();
        guhadaTransact.setContractAddress(contractAddress);
        guhadaTransactRepository.save(guhadaTransact);
        return guhadaTransact;
    }

    @Override
    public List<GuhadaTransactResponse> getTransactData(long productId) {

        List<GuhadaTransact> guhadaTransactList = guhadaTransactRepository.findAllByProductId(productId);
        List<GuhadaTransactResponse> guhadaTransactResponseList = guhadaTransactList
                .stream()
                .map(guhadaTransact -> GuhadaTransactResponse.of(guhadaTransact))
                .collect(Collectors.toList());
        return guhadaTransactResponseList;
    }

    @Override
    public void smartContractDeployFeeDelegation() {
        KlayCredentials sender = KlayCredentials.create(senderPrivateKey);
        SmartContractDeployTransaction smartContractDeployTransaction
                = SmartContractDeployTransaction.create(
                senderPublicKey,
                BigInteger.ZERO,
          Numeric.hexStringToByteArray(data),
          new DefaultGasProvider().getGasLimit(),
                BigInteger.ZERO);

        TransactionManager transactionManager
                = new TransactionManager.Builder(caver, sender)
                .setChaindId(ChainId.BAOBAB_TESTNET)
                .build();

//        String senderRawTransaction
//                = transactionManager
//                .sign(smartContractDeployTransaction, true)
//                .getValueAsString();
//
//        String payer = "0xb52673131d1f9c35b641a037682b1d0ebd6ad9f176aa41b45f60545a2f8b1a9a";
//
//        KlayCredentials feePayer = KlayCredentials.create(payer);
//        FeePayerManager feePayerManager = new FeePayerManager.Builder(caver, feePayer).setChainId(ChainId.BAOBAB_TESTNET).build();
//        KlayTransactionReceipt.TransactionReceipt transactionReceipt = feePayerManager.executeTransaction(senderRawTransaction);
//
//        log.error(transactionReceipt.toString());

    }

    @Override
    public GuhadaTransact uploadToBlockchainFeeDelegation(GuhadaTransactRequest guhadaTransactRequest) {
        GuhadaTransact guhadaTransact = GuhadaTransact.of(guhadaTransactRequest);
        guhadaTransact.setHash(HashUtils.getSha(guhadaTransact.toString()));
        guhadaTransact = guhadaTransactRepository.save(guhadaTransact);
        KlayCredentials sender = KlayCredentials.create(senderPrivateKey);

        final Function function = new Function(
                GuhadaTransactSC.FUNC_INSERT,
                Arrays.<Type>asList(new Int256(BigInteger.valueOf(guhadaTransact.getGuhadaTransactId())),
                        new Int256(BigInteger.valueOf(guhadaTransact.getProductId())),
                        new Utf8String(guhadaTransact.getHash())),
                Collections.<TypeReference<?>>emptyList());
        String encodeData = FunctionEncoder.encode(function);

        SmartContractExecutionTransaction smartContractExecutionTransaction = SmartContractExecutionTransaction.create(
                senderPublicKey,
                delegationContract,
                BigInteger.ZERO,
                Numeric.hexStringToByteArray(encodeData),
                new DefaultGasProvider().getGasLimit(GuhadaTransactSC.FUNC_INSERT));

        TransactionManager transactionManager = new TransactionManager.Builder(caver,sender).setChaindId(ChainId.BAOBAB_TESTNET).build();

//        String senderRawTransaction = transactionManager.sign(smartContractExecutionTransaction,true).getValueAsString();
//
//        String payer = "0xb52673131d1f9c35b641a037682b1d0ebd6ad9f176aa41b45f60545a2f8b1a9a";
//
//        KlayCredentials feePayer = KlayCredentials.create(payer);
//
//        FeePayerManager feePayerManager = new FeePayerManager.Builder(caver,feePayer).setChainId(ChainId.BAOBAB_TESTNET).build();
//
//        KlayTransactionReceipt.TransactionReceipt transactionReceipt = feePayerManager.executeTransaction(senderRawTransaction);
//
//        log.debug(transactionReceipt.getTransactionHash());
//        log.debug(transactionReceipt.getErrorMessage());

        return guhadaTransact;
    }

    //endregion


}
