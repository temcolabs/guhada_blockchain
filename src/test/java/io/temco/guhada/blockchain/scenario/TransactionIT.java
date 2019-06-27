package io.temco.guhada.blockchain.scenario;

import org.caverj.crpyto.KlayCredentials;
import org.caverj.utils.CodeFormat;
import org.caverj.utils.KlayTransactionUtils;
import org.caverj.methods.response.KlayTransactionReceipt;
import org.caverj.tx.type.*;
import org.junit.Ignore;
import org.junit.Test;
import org.web3j.crypto.Hash;
import org.web3j.crypto.Keys;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.Random;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNull;

public class TransactionIT extends Scenario {

    @Test
    public void testTxTypeLegacyTransaction() throws Exception {
        TxTypeLegacyTransaction tx = TxTypeLegacyTransaction.createTransaction(
                getNonce(LUMAN.getAddress()),
                GAS_PRICE,
                GAS_LIMIT,
                LUMAN.getAddress(),
                BRANDON.getAddress(),
                new BigInteger("174876e800", 16),
                "");

        String rawTx = signTransaction(tx, LUMAN);
        KlayTransactionReceipt.TransactionReceipt transactionReceipt = sendTxAndGetReceipt(rawTx);

        assertEquals(KlayTransactionUtils.getType(rawTx), TxType.Type.LEGACY);
        assertEquals("0x1", transactionReceipt.getStatus());
    }

    @Test
    public void testTxTypeValueTransfer() throws Exception {
        TxTypeValueTransfer tx = TxTypeValueTransfer.createTransaction(
                getNonce(BRANDON.getAddress()),
                GAS_PRICE,
                GAS_LIMIT,
                BRANDON.getAddress(),
                LUMAN.getAddress(),
                BigInteger.valueOf(0x989680));

        String rawTx = signTransaction(tx, BRANDON);
        KlayTransactionReceipt.TransactionReceipt transactionReceipt = sendTxAndGetReceipt(rawTx);

        assertEquals(KlayTransactionUtils.getType(rawTx), TxType.Type.VALUE_TRANFSER);
        assertEquals("0x1", transactionReceipt.getStatus());
    }

    @Test
    public void testTxTypeFeeDelegatedValueTransfer() throws Exception {
        /**
         * Client Side
         */
        TxTypeFeeDelegatedValueTransfer tx = TxTypeFeeDelegatedValueTransfer.createTransaction(
                getNonce(BRANDON.getAddress()),
                GAS_PRICE,
                GAS_LIMIT,
                BRANDON.getAddress(),
                LUMAN.getAddress(),
                BigInteger.valueOf(0x989680));

        String rawTx = signTransaction(tx, BRANDON);

        /**
         * Payer Side
         */
        String payerTx = signTransactionFromFeePayer(rawTx, FEE_PAYER);
        KlayTransactionReceipt.TransactionReceipt transactionReceipt = sendTxAndGetReceipt(payerTx);

        assertEquals(KlayTransactionUtils.getType(rawTx), TxType.Type.FEE_DELEGATED_VALUE_TRANSFER);
        assertEquals("0x1", transactionReceipt.getStatus());
    }

    @Test
    public void testTxTypeFeeDelegatedValueTransferWithRatio() throws Exception {
        /**
         * Client Side
         */
        TxTypeFeeDelegatedValueTransferWithRatio tx = TxTypeFeeDelegatedValueTransferWithRatio.createTransaction(
                getNonce(BRANDON.getAddress()),
                GAS_PRICE,
                GAS_LIMIT,
                LUMAN.getAddress(),
                BigInteger.valueOf(0x989680),
                BRANDON.getAddress(),
                BigInteger.valueOf(20));

        String rawTx = signTransaction(tx, BRANDON);

        /**
         * Payer Side
         */
        String payerTx = signTransactionFromFeePayer(rawTx, FEE_PAYER);
        KlayTransactionReceipt.TransactionReceipt transactionReceipt = sendTxAndGetReceipt(payerTx);

        assertEquals(KlayTransactionUtils.getType(rawTx), TxType.Type.FEE_DELEGATED_VALUE_TRANSFER_WITH_RATIO);
        assertEquals("0x1", transactionReceipt.getStatus());
    }

    @Test
    public void testTxTypeValueTransferMemo() throws Exception {
        TxTypeValueTransferMemo tx = TxTypeValueTransferMemo.createTransaction(
                getNonce(BRANDON.getAddress()),
                GAS_PRICE,
                GAS_LIMIT,
                LUMAN.getAddress(),
                BigInteger.valueOf(0x989680),
                BRANDON.getAddress(),
                Numeric.hexStringToByteArray("0x68656c6c6f"));

        String rawTx = signTransaction(tx, BRANDON);
        KlayTransactionReceipt.TransactionReceipt transactionReceipt = sendTxAndGetReceipt(rawTx);

        assertEquals(KlayTransactionUtils.getType(rawTx), TxType.Type.VALUE_TRANSFER_MEMO);
        assertEquals("0x1", transactionReceipt.getStatus());
    }

    @Test
    public void testTxTypeFeeDelegatedValueTransferMemo() throws Exception {
        /**
         * Client Side
         */
        TxTypeFeeDelegatedValueTransferMemo tx = TxTypeFeeDelegatedValueTransferMemo.createTransaction(
                getNonce(BRANDON.getAddress()),
                GAS_PRICE,
                GAS_LIMIT,
                LUMAN.getAddress(),
                BigInteger.valueOf(0x989680),
                BRANDON.getAddress(),
                Numeric.hexStringToByteArray("0x68656c6c6f")
        );

        String rawTx = signTransaction(tx, BRANDON);

        /**
         * Payer Side
         */
        String payerTx = signTransactionFromFeePayer(rawTx, FEE_PAYER);
        KlayTransactionReceipt.TransactionReceipt transactionReceipt = sendTxAndGetReceipt(payerTx);

        assertEquals(KlayTransactionUtils.getType(rawTx), TxType.Type.FEE_DELEGATED_VALUE_TRANSFER_MEMO);
        assertEquals("0x1", transactionReceipt.getStatus());
    }

    @Test
    public void testTxTypeFeeDelegatedValueTransferMemoWithRatio() throws Exception {
        /**
         * Client Side
         */
        TxTypeFeeDelegatedValueTransferMemoWithRatio tx = TxTypeFeeDelegatedValueTransferMemoWithRatio.createTransaction(
                getNonce(BRANDON.getAddress()),
                GAS_PRICE,
                GAS_LIMIT,
                LUMAN.getAddress(),
                BigInteger.valueOf(0x989680),
                BRANDON.getAddress(),
                Numeric.hexStringToByteArray("0x68656c6c6f"),
                BigInteger.valueOf(30)
        );

        String rawTx = signTransaction(tx, BRANDON);

        /**
         * Payer Side
         */
        String payerTx = signTransactionFromFeePayer(rawTx, FEE_PAYER);
        KlayTransactionReceipt.TransactionReceipt transactionReceipt = sendTxAndGetReceipt(payerTx);

        assertEquals(KlayTransactionUtils.getType(rawTx), TxType.Type.FEE_DELEGATED_VALUE_TRANSFER_MEMO_WITH_RATIO);
        assertEquals("0x1", transactionReceipt.getStatus());
    }

    @Test
    public void testTxTypeSmartContractDeploy() throws Exception {
        String contractName = KlayCredentials.create(Keys.createEcKeyPair()).getAddress();

        TxTypeSmartContractDeploy tx = TxTypeSmartContractDeploy.createTransaction(
                getNonce(BRANDON.getAddress()),
                GAS_PRICE,
                GAS_LIMIT,
                contractName,
                BigInteger.valueOf(0x0),
                BRANDON.getAddress(),
                Numeric.hexStringToByteArray("0x60806040526000805534801561001457600080fd5b50610116806100246000396000f3006080604052600436106053576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806306661abd14605857806342cbb15c146080578063d14e62b81460a8575b600080fd5b348015606357600080fd5b50606a60d2565b6040518082815260200191505060405180910390f35b348015608b57600080fd5b50609260d8565b6040518082815260200191505060405180910390f35b34801560b357600080fd5b5060d06004803603810190808035906020019092919050505060e0565b005b60005481565b600043905090565b80600081905550505600a165627a7a7230582064856de85a2706463526593b08dd790054536042ef66d3204018e6790a2208d10029"),
                CodeFormat.EVM
        );

        String rawTx = signTransaction(tx, BRANDON);
        KlayTransactionReceipt.TransactionReceipt transactionReceipt = sendTxAndGetReceipt(rawTx);

        assertEquals(KlayTransactionUtils.getType(rawTx), TxType.Type.SMART_CONTRACT_DEPLOY);
        assertEquals("0x1", transactionReceipt.getStatus());
    }


    @Test
    public void testTxTypeFeeDelegatedSmartContractDeploy() throws Exception {
        String contractName = KlayCredentials.create(Keys.createEcKeyPair()).getAddress();

        /**
         * Client Side
         */
        TxTypeFeeDelegatedSmartContractDeploy tx = TxTypeFeeDelegatedSmartContractDeploy.createTransaction(
                getNonce(BRANDON.getAddress()),
                GAS_PRICE,
                GAS_LIMIT,
                contractName,
                BigInteger.ZERO,
                BRANDON.getAddress(),
                Numeric.hexStringToByteArray("0x608060405234801561001057600080fd5b506101de806100206000396000f3006080604052600436106100615763ffffffff7c01000000000000000000000000000000000000000000000000000000006000350416631a39d8ef81146100805780636353586b146100a757806370a08231146100ca578063fd6b7ef8146100f8575b3360009081526001602052604081208054349081019091558154019055005b34801561008c57600080fd5b5061009561010d565b60408051918252519081900360200190f35b6100c873ffffffffffffffffffffffffffffffffffffffff60043516610113565b005b3480156100d657600080fd5b5061009573ffffffffffffffffffffffffffffffffffffffff60043516610147565b34801561010457600080fd5b506100c8610159565b60005481565b73ffffffffffffffffffffffffffffffffffffffff1660009081526001602052604081208054349081019091558154019055565b60016020526000908152604090205481565b336000908152600160205260408120805490829055908111156101af57604051339082156108fc029083906000818181858888f193505050501561019c576101af565b3360009081526001602052604090208190555b505600a165627a7a72305820627ca46bb09478a015762806cc00c431230501118c7c26c30ac58c4e09e51c4f0029"),
                CodeFormat.EVM
        );

        String rawTx = signTransaction(tx, BRANDON);

        /**
         * Payer Side
         */
        String payerTx = signTransactionFromFeePayer(rawTx, FEE_PAYER);
        KlayTransactionReceipt.TransactionReceipt transactionReceipt = sendTxAndGetReceipt(payerTx);

        assertEquals(KlayTransactionUtils.getType(rawTx), TxType.Type.FEE_DELEGATED_SMART_CONTRACT_DEPLOY);
        assertEquals("0x1", transactionReceipt.getStatus());
    }


    @Test
    public void testTxTypeFeeDelegatedSmartContractDeployWithRatio() throws Exception {
        String contractName = KlayCredentials.create(Keys.createEcKeyPair()).getAddress();

        /**
         * Client Side
         */
        TxTypeFeeDelegatedSmartContractDeployWithRatio tx = TxTypeFeeDelegatedSmartContractDeployWithRatio.createTransaction(
                getNonce(BRANDON.getAddress()),
                GAS_PRICE,
                GAS_LIMIT,
                contractName,
                BigInteger.ZERO,
                BRANDON.getAddress(),
                Numeric.hexStringToByteArray("0x608060405234801561001057600080fd5b506101de806100206000396000f3006080604052600436106100615763ffffffff7c01000000000000000000000000000000000000000000000000000000006000350416631a39d8ef81146100805780636353586b146100a757806370a08231146100ca578063fd6b7ef8146100f8575b3360009081526001602052604081208054349081019091558154019055005b34801561008c57600080fd5b5061009561010d565b60408051918252519081900360200190f35b6100c873ffffffffffffffffffffffffffffffffffffffff60043516610113565b005b3480156100d657600080fd5b5061009573ffffffffffffffffffffffffffffffffffffffff60043516610147565b34801561010457600080fd5b506100c8610159565b60005481565b73ffffffffffffffffffffffffffffffffffffffff1660009081526001602052604081208054349081019091558154019055565b60016020526000908152604090205481565b336000908152600160205260408120805490829055908111156101af57604051339082156108fc029083906000818181858888f193505050501561019c576101af565b3360009081526001602052604090208190555b505600a165627a7a72305820627ca46bb09478a015762806cc00c431230501118c7c26c30ac58c4e09e51c4f0029"),
                BigInteger.valueOf(33),
                CodeFormat.EVM
        );

        String rawTx = signTransaction(tx, BRANDON);

        /**
         * Payer Side
         */
        String payerTx = signTransactionFromFeePayer(rawTx, FEE_PAYER);
        KlayTransactionReceipt.TransactionReceipt transactionReceipt = sendTxAndGetReceipt(payerTx);

        assertEquals(KlayTransactionUtils.getType(rawTx), TxType.Type.FEE_DELEGATED_SMART_CONTRACT_DEPLOY_WITH_RATIO);
        assertEquals("0x1", transactionReceipt.getStatus());
    }

    @Test
    public void testTxTypeSmartContractExecution() throws Exception {
        String DEPLOYED_CONTRACT = "0x1b24096b5a84d0f422faaa69f4de65d24329bd87";

        TxTypeSmartContractExecution tx = TxTypeSmartContractExecution.createTransaction(
                getNonce(BRANDON.getAddress()),
                GAS_PRICE,
                GAS_LIMIT,
                DEPLOYED_CONTRACT,
                BigInteger.ZERO,
                BRANDON.getAddress(),
                getPayLoad());

        String rawTx = signTransaction(tx, BRANDON);
        KlayTransactionReceipt.TransactionReceipt transactionReceipt = sendTxAndGetReceipt(rawTx);

        assertEquals(KlayTransactionUtils.getType(rawTx), TxType.Type.SMART_CONTRACT_EXECUTION);
        assertEquals("0x1", transactionReceipt.getStatus());
    }

    @Test
    public void testTxTypeFeeDelegatedSmartContractExecution() throws Exception {
        String DEPLOYED_CONTRACT = "0x1b24096b5a84d0f422faaa69f4de65d24329bd87";

        /**
         * Client Side
         */
        TxTypeFeeDelegatedSmartContractExecution tx = TxTypeFeeDelegatedSmartContractExecution.createTransaction(
                getNonce(BRANDON.getAddress()),
                GAS_PRICE,
                GAS_LIMIT,
                DEPLOYED_CONTRACT,
                BigInteger.ZERO,
                BRANDON.getAddress(),
                getPayLoad());

        String rawTx = signTransaction(tx, BRANDON);

        /**
         * Payer Side
         */
        String payerTx = signTransactionFromFeePayer(rawTx, FEE_PAYER);
        KlayTransactionReceipt.TransactionReceipt transactionReceipt = sendTxAndGetReceipt(payerTx);

        assertEquals(KlayTransactionUtils.getType(rawTx), TxType.Type.FEE_DELEGATED_SMART_CONTRACT_EXECUTION);
        assertEquals("0x1", transactionReceipt.getStatus());
    }

    private byte[] getPayLoad() {
        String SET_COMMAND = "setCount(uint256)";
        long RANDOM_VALUE = new Random().nextLong();
        BigInteger replaceValue = BigInteger.valueOf(RANDOM_VALUE);
        String payLoadNoCommand = Numeric.toHexString(Numeric.toBytesPadded(replaceValue, 32)).substring(2);
        String payLoad = new StringBuilder(Hash.sha3String(SET_COMMAND)
                .substring(2, 10))
                .append(payLoadNoCommand)
                .toString();
        return Numeric.hexStringToByteArray(payLoad);
    }

    @Test
    public void testTxTypeFeeDelegatedSmartContractExecutionWithRatio() throws Exception {
        String DEPLOYED_CONTRACT = "0x1b24096B5a84D0F422Faaa69F4dE65D24329bd87";

        /**
         * Client Side
         */
        TxTypeFeeDelegatedSmartContractExecutionWithRatio tx = TxTypeFeeDelegatedSmartContractExecutionWithRatio.createTransaction(
                getNonce(LUMAN.getAddress()),
                GAS_PRICE,
                GAS_LIMIT,
                DEPLOYED_CONTRACT,
                BigInteger.ZERO,
                LUMAN.getAddress(),
                getPayLoad(),
                BigInteger.valueOf(66));

        String rawTx = tx.sign(LUMAN, BAOBAB_CHAIN_ID).getValueAsString();

        /**
         * Payer Side
         */
        String payerTx = signTransactionFromFeePayer(rawTx, FEE_PAYER);
        KlayTransactionReceipt.TransactionReceipt transactionReceipt = sendTxAndGetReceipt(payerTx);

        assertEquals(KlayTransactionUtils.getType(rawTx), TxType.Type.FEE_DELEGATED_SMART_CONTRACT_EXECUTION_WITH_RATIO);
        assertEquals("0x1", transactionReceipt.getStatus());
    }

    @Test
    public void testTxTypeCancel() throws Exception {
        BigInteger nonce = getNonce(WAYNE.getAddress());
        sendBasicTransaction(nonce, WAYNE, LUMAN);

        TxTypeCancel tx = TxTypeCancel.createTransaction(
                nonce,
                GAS_PRICE,
                GAS_LIMIT,
                WAYNE.getAddress());

        String rawTx = signTransaction(tx, WAYNE);
        KlayTransactionReceipt.TransactionReceipt transactionReceipt = sendTxAndGetReceipt(rawTx);

        assertEquals(KlayTransactionUtils.getType(rawTx), TxType.Type.CANCEL);
        assertEquals("0x1", transactionReceipt.getStatus());
    }

    @Test
    public void testTxTypeFeeDelegatedCancel() throws Exception {
        BigInteger nonce = getNonce(WAYNE.getAddress());
        sendBasicTransaction(nonce, WAYNE, LUMAN);

        /**
         * Client Side
         */
        TxTypeFeeDelegatedCancel tx = TxTypeFeeDelegatedCancel.createTransaction(
                nonce,
                GAS_PRICE,
                GAS_LIMIT,
                WAYNE.getAddress()
        );

        String rawTx = signTransaction(tx, WAYNE);

        /**
         * Payer Side
         */
        String payerTx = signTransactionFromFeePayer(rawTx, FEE_PAYER);
        KlayTransactionReceipt.TransactionReceipt transactionReceipt = sendTxAndGetReceipt(payerTx);

        assertEquals(KlayTransactionUtils.getType(rawTx), TxType.Type.FEE_DELEGATED_CANCEL);
        assertEquals("0x1", transactionReceipt.getStatus());
    }

    @Test
    public void testTxTypeFeeDelegatedCancelWithRatio() throws Exception {
        BigInteger nonce = getNonce(WAYNE.getAddress());
        sendBasicTransaction(nonce, WAYNE, LUMAN);

        /**
         * Client Side
         */
        TxTypeFeeDelegatedCancelWithRatio tx = TxTypeFeeDelegatedCancelWithRatio.createTransaction(
                nonce,
                GAS_PRICE,
                GAS_LIMIT,
                WAYNE.getAddress(),
                BigInteger.valueOf(88));

        String rawTx = signTransaction(tx, WAYNE);

        /**
         * Payer Side
         */
        String payerTx = signTransactionFromFeePayer(rawTx, FEE_PAYER);
        KlayTransactionReceipt.TransactionReceipt transactionReceipt = sendTxAndGetReceipt(payerTx);

        assertEquals(KlayTransactionUtils.getType(rawTx), TxType.Type.FEE_DELEGATED_CANCEL_WITH_RATIO);
        assertEquals("0x1", transactionReceipt.getStatus());
    }

    private void sendBasicTransaction(BigInteger nonce, KlayCredentials from, KlayCredentials to) throws Exception {
        TxTypeLegacyTransaction tx = TxTypeLegacyTransaction.createTransaction(
                nonce,
                GAS_PRICE,
                GAS_LIMIT,
                from.getAddress(),
                to.getAddress(),
                new BigInteger("174876e800", 16),
                "");

        String rawTx = tx.sign(from, BAOBAB_CHAIN_ID).getValueAsString();
        caverj.klay().sendSignedTransaction(rawTx).sendAsync().get();
    }


    @Ignore
    @Test
    public void testTxTypeChainDataAnchoringTransaction() throws Exception {
        TxTypeChainDataAnchoringTransaction tx = TxTypeChainDataAnchoringTransaction.createTransaction(
                getNonce(BRANDON.getAddress()),
                GAS_PRICE,
                GAS_LIMIT,
                BRANDON.getAddress(),
                Numeric.hexStringToByteArray("0xf8a6a00000000000000000000000000000000000000000000000000000000000000000a00000000000000000000000000000000000000000000000000000000000000001a00000000000000000000000000000000000000000000000000000000000000002a00000000000000000000000000000000000000000000000000000000000000003a0000000000000000000000000000000000000000000000000000000000000000405"));

        String rawTx = signTransaction(tx, BRANDON);
        KlayTransactionReceipt.TransactionReceipt transactionReceipt = sendTxAndGetReceipt(rawTx);  // TxTypeChainDataAnchoring cannot be submitted via RPC!

        assertEquals(KlayTransactionUtils.getType(rawTx), TxType.Type.CHAIN_DATA_ANCHROING);
        assertEquals("0x1", transactionReceipt.getStatus());
    }

}
