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
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.tuples.generated.Tuple3;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated smart contract code.
 * <p><strong>Do not modify!</strong>
 */
public class LuckyDraw extends SmartContract {
    private static final String BINARY = "608060405234801561001057600080fd5b5061090f806100206000396000f3fe608060405234801561001057600080fd5b50600436106100625760003560e01c80633b304147146100675780633c0202f0146100f95780639dfc066114610234578063ac3a5e9314610251578063cf1ab1af1461037d578063fe85f40a1461039a575b600080fd5b6100846004803603602081101561007d57600080fd5b50356103bd565b6040805160208082528351818301528351919283929083019185019080838360005b838110156100be5781810151838201526020016100a6565b50505050905090810190601f1680156100eb5780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b6102326004803603608081101561010f57600080fd5b81019060208101813564010000000081111561012a57600080fd5b82018360208201111561013c57600080fd5b8035906020019184600183028401116401000000008311171561015e57600080fd5b91908080601f01602080910402602001604051908101604052809392919081815260200183838082843760009201919091525092958435956020860135959194509250606081019150604001356401000000008111156101bd57600080fd5b8201836020820111156101cf57600080fd5b803590602001918460018302840111640100000000831117156101f157600080fd5b91908080601f01602080910402602001604051908101604052809392919081815260200183838082843760009201919091525092955061043a945050505050565b005b6102326004803603602081101561024a57600080fd5b503561051a565b6102f76004803603602081101561026757600080fd5b81019060208101813564010000000081111561028257600080fd5b82018360208201111561029457600080fd5b803590602001918460018302840111640100000000831117156102b657600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600092019190915250929550610534945050505050565b6040518084815260200183815260200180602001828103825283818151815260200191508051906020019080838360005b83811015610340578181015183820152602001610328565b50505050905090810190601f16801561036d5780820380516001836020036101000a031916815260200191505b5094505050505060405180910390f35b6100846004803603602081101561039357600080fd5b50356105eb565b610084600480360360408110156103b057600080fd5b5080359060200135610684565b6000818152600160205260408120546060916103d882610705565b6000858152600160205260409020805491925090829081106103f657fe5b90600052602060002001600260008681526020019081526020016000209080546001816001161561010002031660029004610432929190610745565b505050919050565b6040518060600160405280848152602001838152602001828152506000856040518082805190602001908083835b602083106104875780518252601f199092019160209182019101610468565b51815160209384036101000a600019018019909216911617905292019485525060408051948590038201909420855181558582015160018201559385015180516104da94506002860193509101906107ca565b50505060008381526001602081815260408320805492830180825581855293829020885191949361051193910191908901906107ca565b50505050505050565b600081815260016020526040812061053191610838565b50565b80516020818301810180516000825292820193820193909320919092528054600180830154600280850180546040805195821615610100026000190190911692909204601f81018890048802850188019092528184529395919493918301828280156105e15780601f106105b6576101008083540402835291602001916105e1565b820191906000526020600020905b8154815290600101906020018083116105c457829003601f168201915b5050505050905083565b600260208181526000928352604092839020805484516001821615610100026000190190911693909304601f810183900483028401830190945283835291929083018282801561067c5780601f106106515761010080835404028352916020019161067c565b820191906000526020600020905b81548152906001019060200180831161065f57829003601f168201915b505050505081565b6001602052816000526040600020818154811061069d57fe5b600091825260209182902001805460408051601f6002600019610100600187161502019094169390930492830185900485028101850190915281815294509092509083018282801561067c5780601f106106515761010080835404028352916020019161067c565b604080514260208083019190915244828401528251808303840181526060909201909252805191012060009082908161073a57fe5b0660ff169050919050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061077e57805485556107ba565b828001600101855582156107ba57600052602060002091601f016020900482015b828111156107ba57825482559160010191906001019061079f565b506107c6929150610856565b5090565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061080b57805160ff19168380011785556107ba565b828001600101855582156107ba579182015b828111156107ba57825182559160200191906001019061081d565b50805460008255906000526020600020908101906105319190610873565b61087091905b808211156107c6576000815560010161085c565b90565b61087091905b808211156107c657600061088d8282610896565b50600101610879565b50805460018160011615610100020316600290046000825580601f106108bc5750610531565b601f016020900490600052602060002090810190610531919061085656fea265627a7a723058201301385eebf672fcf88fa2ba40d446be5f902665ed521d72abec35224d58bbee64736f6c63430005090032";

    public static final String FUNC_DRAW = "draw";

    public static final String FUNC_ENTRY = "entry";

    public static final String FUNC_DESTORYDRAWITEM = "destoryDrawItem";

    public static final String FUNC_ENTRYUSERS = "entryUsers";

    public static final String FUNC_LUCKYDRAWWINNER = "luckyDrawWinner";

    public static final String FUNC_LUCKYDRAWENTRYS = "luckyDrawEntrys";

    protected LuckyDraw(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    protected LuckyDraw(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, caver, transactionManager, contractGasProvider);
    }

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> draw(BigInteger dealId) {
        final Function function = new Function(
                FUNC_DRAW, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(dealId)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> entry(String eventId, BigInteger dealId, BigInteger userId, String userEmail) {
        final Function function = new Function(
                FUNC_ENTRY, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(eventId), 
                new org.web3j.abi.datatypes.generated.Uint256(dealId), 
                new org.web3j.abi.datatypes.generated.Uint256(userId), 
                new org.web3j.abi.datatypes.Utf8String(userEmail)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<KlayTransactionReceipt.TransactionReceipt> destoryDrawItem(BigInteger dealId) {
        final Function function = new Function(
                FUNC_DESTORYDRAWITEM, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(dealId)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<Tuple3<BigInteger, BigInteger, String>> entryUsers(String param0) {
        final Function function = new Function(FUNC_ENTRYUSERS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}));
        return new RemoteCall<Tuple3<BigInteger, BigInteger, String>>(
                new Callable<Tuple3<BigInteger, BigInteger, String>>() {
                    @Override
                    public Tuple3<BigInteger, BigInteger, String> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple3<BigInteger, BigInteger, String>(
                                (BigInteger) results.get(0).getValue(), 
                                (BigInteger) results.get(1).getValue(), 
                                (String) results.get(2).getValue());
                    }
                });
    }

    public RemoteCall<String> luckyDrawWinner(BigInteger param0) {
        final Function function = new Function(FUNC_LUCKYDRAWWINNER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<String> luckyDrawEntrys(BigInteger param0, BigInteger param1) {
        final Function function = new Function(FUNC_LUCKYDRAWENTRYS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(param0), 
                new org.web3j.abi.datatypes.generated.Uint256(param1)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public static LuckyDraw load(String contractAddress, Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        return new LuckyDraw(contractAddress, caver, credentials, chainId, contractGasProvider);
    }

    public static LuckyDraw load(String contractAddress, Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new LuckyDraw(contractAddress, caver, transactionManager, contractGasProvider);
    }

    public static RemoteCall<LuckyDraw> deploy(Caver caver, KlayCredentials credentials, int chainId, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(LuckyDraw.class, caver, credentials, chainId, contractGasProvider, BINARY, "");
    }

    public static RemoteCall<LuckyDraw> deploy(Caver caver, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(LuckyDraw.class, caver, transactionManager, contractGasProvider, BINARY, "");
    }
}
