package io.temco.guhada.blockchain.smartcontract;

import com.klaytn.caver.Caver;
import com.klaytn.caver.crypto.KlayCredentials;
import com.klaytn.caver.methods.response.KlayTransactionReceipt;
import com.klaytn.caver.tx.SmartContract;
import com.klaytn.caver.tx.manager.TransactionManager;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated smart contract code.
 * <p><strong>Do not modify!</strong>
 */
public class TrackRecordSCNew extends SmartContract {
    private static final String BINARY = "608060405234801561001057600080fd5b50610185806100206000396000f3fe608060405234801561001057600080fd5b50600436106100415760003560e01c8063176f964c14610046578063d184226114610065578063d9dea8241461009a575b600080fd5b6100636004803603602081101561005c57600080fd5b50356100b7565b005b6100886004803603604081101561007b57600080fd5b50803590602001356100e5565b60408051918252519081900360200190f35b610088600480360360208110156100b057600080fd5b503561012b565b6000818152602081815260408083208151808401909252938152835460018101855593835291209051910155565b60006100ef61013d565b600084815260208190526040902080548490811061010957fe5b6000918252602091829020604080519384019052015490819052949350505050565b60009081526020819052604090205490565b604051806020016040528060008152509056fea265627a7a72305820ec3c0f80c8f84b8048f78c9a1ec1f3ad64b2bc4d2031db5ecb2bbad00646377764736f6c63430005090032";

    public static final String FUNC_ADDTRANSACTION = "addTransaction";

    public static final String FUNC_GETDEAL = "getDeal";

    public static final String FUNC_GETDEALSIZE = "getDealSize";

    protected TrackRecordSCNew(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    protected TrackRecordSCNew(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, caver, transactionManager, contractGasProvider);
    }

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> addTransaction(BigInteger dealId) {
        final Function function = new Function(
                FUNC_ADDTRANSACTION, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(dealId)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> getDeal(BigInteger dealId, BigInteger index) {
        final Function function = new Function(FUNC_GETDEAL, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(dealId), 
                new org.web3j.abi.datatypes.generated.Uint256(index)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> getDealSize(BigInteger dealId) {
        final Function function = new Function(FUNC_GETDEALSIZE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(dealId)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public static TrackRecordSCNew load(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        return new TrackRecordSCNew(contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    public static TrackRecordSCNew load(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new TrackRecordSCNew(contractAddress, caver, transactionManager, contractGasProvider);
    }

    public static RemoteCall<TrackRecordSCNew> deploy(Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(TrackRecordSCNew.class, caver, credentials, chainId, contractGasProvider, BINARY, "");
    }

    public static RemoteCall<TrackRecordSCNew> deploy(Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(TrackRecordSCNew.class, caver, transactionManager, contractGasProvider, BINARY, "");
    }
}
