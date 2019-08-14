//package io.temco.guhada.blockchain.scenario;
//
//import org.caverj.fee.FeePayer;
//import org.caverj.methods.request.CallObject;
//import org.caverj.methods.response.Bytes;
//import org.caverj.methods.response.KlayTransactionReceipt;
//import org.caverj.tx.type.TxTypeFeeDelegatedSmartContractExecution;
//import org.caverj.tx.model.KlayRawTransaction;
//import org.caverj.utils.HumanReadableAddressUtils;
//import org.junit.Test;
//import org.web3j.crypto.Hash;
//import org.web3j.protocol.core.DefaultBlockParameterName;
//import org.web3j.utils.Numeric;
//
//import java.math.BigInteger;
//
//import static junit.framework.TestCase.assertEquals;
//
//public class FeeDelegatedSmartContractExecutionIT extends Scenario {
//
//    public static final String DEPLOYED_CONTRACT_ADDRESS = "0x1b24096B5a84D0F422Faaa69F4dE65D24329bd87";
//    public static final String SET_COMMAND = "setCount(uint256)";
//    public static final String COUNT_COMMAND = "count()";
//    public static final int CHANGE_VALUE = 27;
//
//    @Test
//    public void testFeeDelegatedSmartContractExecution() throws Exception {
//        BigInteger nonce =  getNonce(BRANDON.getAddress());
//        TxTypeFeeDelegatedSmartContractExecution senderTransaction
//                = TxTypeFeeDelegatedSmartContractExecution.createTransaction(
//                        nonce,
//                        GAS_PRICE,
//                        GAS_LIMIT,
//                        DEPLOYED_CONTRACT_ADDRESS,
//                        BigInteger.ZERO,
//                        BRANDON.getAddress(),
//                        getPayLoad()
//        );
//
//        KlayRawTransaction senderSignedTransaction = senderTransaction.sign(BRANDON, BAOBAB_CHAIN_ID);
//
//        TxTypeFeeDelegatedSmartContractExecution payerTransaction =
//                TxTypeFeeDelegatedSmartContractExecution.decodeFromRawTransaction(senderSignedTransaction.getValueAsString());
//        assertEquals(HumanReadableAddressUtils.toRawAddress(BRANDON.getAddress()), payerTransaction.getFrom());
//
//        KlayRawTransaction payerSignedTransaction = new FeePayer(FEE_PAYER, BAOBAB_CHAIN_ID).sign(payerTransaction);
//
//        KlayTransactionReceipt.TransactionReceipt receipt = sendTxAndGetReceipt(payerSignedTransaction.getValueAsString());
//        assertEquals(true, DEPLOYED_CONTRACT_ADDRESS.equalsIgnoreCase(receipt.getTo()));
//
//        CallObject callObject = new CallObject(
//                BRANDON.getAddress(),
//                DEPLOYED_CONTRACT_ADDRESS,
//                GAS_LIMIT,
//                GAS_PRICE,
//                BigInteger.ZERO,
//                Numeric.prependHexPrefix(Hash.sha3String(COUNT_COMMAND).substring(2, 10)));
//        Bytes changedValue = caverj.klay().call(callObject, DefaultBlockParameterName.LATEST).send();
//        assertEquals(BigInteger.valueOf(CHANGE_VALUE), Numeric.toBigInt(changedValue.getResult()));
//    }
//
//    private byte[] getPayLoad() {
//        BigInteger replaceValue = BigInteger.valueOf(CHANGE_VALUE);
//        String payLoadNoCommand = Numeric.toHexString(Numeric.toBytesPadded(replaceValue, 32)).substring(2);
//        String payLoad = new StringBuilder(Hash.sha3String(SET_COMMAND)
//                        .substring(2, 10))
//                        .append(payLoadNoCommand)
//                        .toString();
//        return Numeric.hexStringToByteArray(payLoad);
//    }
//}
