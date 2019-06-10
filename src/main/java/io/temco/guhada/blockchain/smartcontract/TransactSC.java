package io.temco.guhada.blockchain.smartcontract;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Int256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 4.1.1.
 */
public class TransactSC extends Contract {
    private static final String BINARY = "0x608060405234801561001057600080fd5b50610538806100206000396000f300608060405260043610610057576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff1680631a1a2beb1461005c578063595cb289146100d95780636795ee1d1461017f575b600080fd5b34801561006857600080fd5b506100d76004803603810190808035906020019092919080359060200190929190803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290505050610201565b005b3480156100e557600080fd5b50610104600480360381019080803590602001909291905050506102ce565b6040518080602001828103825283818151815260200191508051906020019080838360005b83811015610144578082015181840152602081019050610129565b50505050905090810190601f1680156101715780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b34801561018b57600080fd5b506101aa60048036038101908080359060200190929190505050610385565b6040518080602001828103825283818151815260200191508051906020019060200280838360005b838110156101ed5780820151818401526020810190506101d2565b505050509050019250505060405180910390f35b600060606040519081016040528085815260200184815260200183815250600080868152602001908152602001600020600082015181600001556020820151816001015560408201518160020190805190602001906102619291906103f0565b509050506001600084815260200190815260200160002090508084908060018154018082558091505090600182039060005260206000200160009091929091909150555080600160008581526020019081526020016000209080546102c7929190610470565b5050505050565b60606000808381526020019081526020016000206002018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156103795780601f1061034e57610100808354040283529160200191610379565b820191906000526020600020905b81548152906001019060200180831161035c57829003601f168201915b50505050509050919050565b6060600160008381526020019081526020016000208054806020026020016040519081016040528092919081815260200182805480156103e457602002820191906000526020600020905b8154815260200190600101908083116103d0575b50505050509050919050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061043157805160ff191683800117855561045f565b8280016001018555821561045f579182015b8281111561045e578251825591602001919060010190610443565b5b50905061046c91906104c2565b5090565b8280548282559060005260206000209081019282156104b15760005260206000209182015b828111156104b0578254825591600101919060010190610495565b5b5090506104be91906104e7565b5090565b6104e491905b808211156104e05760008160009055506001016104c8565b5090565b90565b61050991905b808211156105055760008160009055506001016104ed565b5090565b905600a165627a7a723058205170a2198ae536fea408347bed19bfc7ea1eb06dc19ab26e0036b34006b165dd0029";

    public static final String FUNC_INSERT = "insert";

    public static final String FUNC_GETHASH = "getHash";

    public static final String FUNC_GETBYPRODUCT = "getByProduct";

    @Deprecated
    protected TransactSC(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected TransactSC(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected TransactSC(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected TransactSC(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteCall<TransactionReceipt> insert(BigInteger transactId, BigInteger productId, String hash) {
        final Function function = new Function(
                FUNC_INSERT, 
                Arrays.<Type>asList(new Int256(transactId),
                new Int256(productId),
                new Utf8String(hash)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<String> getHash(BigInteger transactId) {
        final Function function = new Function(FUNC_GETHASH, 
                Arrays.<Type>asList(new Int256(transactId)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<List> getByProduct(BigInteger productId) {
        final Function function = new Function(FUNC_GETBYPRODUCT, 
                Arrays.<Type>asList(new Int256(productId)),
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Int256>>() {}));
        return new RemoteCall<List>(
                new Callable<List>() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public List call() throws Exception {
                        List<Type> result = (List<Type>) executeCallSingleValueReturn(function, List.class);
                        return convertToNative(result);
                    }
                });
    }

    @Deprecated
    public static TransactSC load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new TransactSC(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static TransactSC load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new TransactSC(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static TransactSC load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new TransactSC(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static TransactSC load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new TransactSC(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<TransactSC> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(TransactSC.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<TransactSC> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(TransactSC.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<TransactSC> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(TransactSC.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<TransactSC> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(TransactSC.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }
}
