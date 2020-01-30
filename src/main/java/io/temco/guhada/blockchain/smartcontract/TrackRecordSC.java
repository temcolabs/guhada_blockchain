package io.temco.guhada.blockchain.smartcontract;

import com.klaytn.caver.Caver;
import com.klaytn.caver.crypto.KlayCredentials;
import com.klaytn.caver.methods.response.KlayTransactionReceipt;
import com.klaytn.caver.tx.SmartContract;
import com.klaytn.caver.tx.manager.TransactionManager;
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
import org.web3j.protocol.core.RemoteCall;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated smart contract code.
 * <p><strong>Do not modify!</strong>
 */
public class TrackRecordSC extends SmartContract {
    private static final String BINARY = "608060405234801561001057600080fd5b50610753806100206000396000f3fe608060405234801561001057600080fd5b50600436106100415760003560e01c8063267cd8b014610046578063595cb289146103155780636795ee1d146103a7575b600080fd5b610313600480360361010081101561005d57600080fd5b813591602081013591810190606081016040820135600160201b81111561008357600080fd5b82018360208201111561009557600080fd5b803590602001918460018302840111600160201b831117156100b657600080fd5b91908080601f0160208091040260200160405190810160405280939291908181526020018383808284376000920191909152509295949360208101935035915050600160201b81111561010857600080fd5b82018360208201111561011a57600080fd5b803590602001918460018302840111600160201b8311171561013b57600080fd5b91908080601f01602080910402602001604051908101604052809392919081815260200183838082843760009201919091525092958435959094909350604081019250602001359050600160201b81111561019557600080fd5b8201836020820111156101a757600080fd5b803590602001918460018302840111600160201b831117156101c857600080fd5b91908080601f0160208091040260200160405190810160405280939291908181526020018383808284376000920191909152509295949360208101935035915050600160201b81111561021a57600080fd5b82018360208201111561022c57600080fd5b803590602001918460018302840111600160201b8311171561024d57600080fd5b91908080601f0160208091040260200160405190810160405280939291908181526020018383808284376000920191909152509295949360208101935035915050600160201b81111561029f57600080fd5b8201836020820111156102b157600080fd5b803590602001918460018302840111600160201b831117156102d257600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600092019190915250929550610414945050505050565b005b6103326004803603602081101561032b57600080fd5b5035610540565b6040805160208082528351818301528351919283929083019185019080838360005b8381101561036c578181015183820152602001610354565b50505050905090810190601f1680156103995780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b6103c4600480360360208110156103bd57600080fd5b50356105e2565b60408051602080825283518183015283519192839290830191858101910280838360005b838110156104005781810151838201526020016103e8565b505050509050019250505060405180910390f35b604080516101008101825289815260208082018a81528284018a8152606084018a90526080840189905260a0840188905260c0840187905260e0840186905260008d8152808452949094208351815590516001820155925180519293926104819260028501920190610643565b506060820151805161049d916003840191602090910190610643565b506080820151600482015560a082015180516104c3916005840191602090910190610643565b5060c082015180516104df916006840191602090910190610643565b5060e082015180516104fb916007840191602090910190610643565b50505060008781526001602081815260408320805492830181558084529083209091018a905590889052805461053490829081906106c1565b50505050505050505050565b6000818152602081815260409182902060070180548351601f60026000196101006001861615020190931692909204918201849004840281018401909452808452606093928301828280156105d65780601f106105ab576101008083540402835291602001916105d6565b820191906000526020600020905b8154815290600101906020018083116105b957829003601f168201915b50505050509050919050565b6000818152600160209081526040918290208054835181840281018401909452808452606093928301828280156105d657602002820191906000526020600020905b8154815260200190600101908083116106245750505050509050919050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061068457805160ff19168380011785556106b1565b828001600101855582156106b1579182015b828111156106b1578251825591602001919060010190610696565b506106bd929150610701565b5090565b8280548282559060005260206000209081019282156106b15760005260206000209182015b828111156106b15782548255916001019190600101906106e6565b61071b91905b808211156106bd5760008155600101610707565b9056fea265627a7a7230582029c89458fbcf49478f8594cee1bbab1c92e4c55d561df5d3107b987e774a927a64736f6c63430005090032";

    public static final String FUNC_INSERT = "insert";

    public static final String FUNC_GETHASH = "getHash";

    public static final String FUNC_GETBYPRODUCT = "getByProduct";

    protected TrackRecordSC(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    protected TrackRecordSC(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, caver, transactionManager, contractGasProvider);
    }

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> insert(BigInteger transactId, BigInteger productId, String productName, String brand, BigInteger price, String color, String owner, String hash) {
        final Function function = new Function(
                FUNC_INSERT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Int256(transactId), 
                new org.web3j.abi.datatypes.generated.Int256(productId), 
                new org.web3j.abi.datatypes.Utf8String(productName), 
                new org.web3j.abi.datatypes.Utf8String(brand), 
                new org.web3j.abi.datatypes.generated.Int256(price), 
                new org.web3j.abi.datatypes.Utf8String(color), 
                new org.web3j.abi.datatypes.Utf8String(owner), 
                new org.web3j.abi.datatypes.Utf8String(hash)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<String> getHash(BigInteger transactId) {
        final Function function = new Function(FUNC_GETHASH, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Int256(transactId)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<List> getByProduct(BigInteger productId) {
        final Function function = new Function(FUNC_GETBYPRODUCT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Int256(productId)), 
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

    public static TrackRecordSC load(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        return new TrackRecordSC(contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    public static TrackRecordSC load(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new TrackRecordSC(contractAddress, caver, transactionManager, contractGasProvider);
    }

    public static RemoteCall<TrackRecordSC> deploy(Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(TrackRecordSC.class, caver, credentials, chainId, contractGasProvider, BINARY, "");
    }

    public static RemoteCall<TrackRecordSC> deploy(Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(TrackRecordSC.class, caver, transactionManager, contractGasProvider, BINARY, "");
    }
}
