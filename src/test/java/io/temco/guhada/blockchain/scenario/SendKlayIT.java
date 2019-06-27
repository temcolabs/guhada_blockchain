package io.temco.guhada.blockchain.scenario;

import org.caverj.methods.request.KlayTransaction;
import org.caverj.methods.response.Bytes32;
import org.caverj.methods.response.KlaySignTransaction;
import org.caverj.methods.response.KlayTransactionReceipt;
import org.caverj.tx.type.TxTypeLegacyTransaction;
import org.caverj.utils.Convert;
import org.junit.Ignore;
import org.junit.Test;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.utils.Numeric;

import java.math.BigInteger;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class SendKlayIT extends Scenario {

    @Test
    @Ignore
    public void testTransferKlay() throws Exception {
        BigInteger nonce = getNonce(LUMAN.getAddress());
        BigInteger value = Convert.toPeb("0.5", Convert.Unit.KLAY).toBigInteger();

        Boolean isUnlock = caverj.klay().unlockAccount(LUMAN.getAddress(), "password", BigInteger.valueOf(10))
                .send().getResult();
        assertTrue(isUnlock);

        KlayTransaction transaction = KlayTransaction.createKlayTransaction(
                LUMAN.getAddress(), nonce, GAS_PRICE, GAS_LIMIT, WAYNE.getAddress(), value);

        Bytes32 response = caverj.klay().sendTransaction(transaction).send();
        String txHash = response.getResult();
        assertFalse(txHash.isEmpty());

        KlayTransactionReceipt.TransactionReceipt transactionReceipt = waitForTransactionReceipt(txHash);
        assertThat(transactionReceipt.getTransactionHash(), is(txHash));
    }

    @Test
    @Ignore
    public void testNodeSignedTransferKlay() throws Exception {
        BigInteger nonce = getNonce(LUMAN.getAddress());
        BigInteger value = Convert.toPeb("0.5", Convert.Unit.KLAY).toBigInteger();

        Boolean isUnlock = caverj.klay().unlockAccount(LUMAN.getAddress(), "password", BigInteger.valueOf(0))
                .send().getResult();
        assertTrue(isUnlock);

        KlayTransaction tx = KlayTransaction.createKlayTransaction(
                LUMAN.getAddress(), nonce, GAS_PRICE, GAS_LIMIT, WAYNE.getAddress(), value);
        KlaySignTransaction.SignTransaction response = caverj.klay().signTransaction(tx).send().getResult();

        Bytes32 klaySendTransaction = caverj.klay().sendSignedTransaction(response.getRaw()).sendAsync().get();
        String transactionHash = klaySendTransaction.getResult();
        assertFalse(transactionHash.isEmpty());

        KlayTransactionReceipt.TransactionReceipt transactionReceipt = waitForTransactionReceipt(transactionHash);
        assertThat(transactionReceipt.getTransactionHash(), is(transactionHash));
    }

    @Test
    public void testSignedTransferKlay() throws Exception {
        BigInteger nonce = getNonce(LUMAN.getAddress());
        TxTypeLegacyTransaction tx = createKlayTransaction(nonce, LUMAN.getAddress(), WAYNE.getAddress());

        BigInteger balance = caverj.klay().getBalance(
                LUMAN.getAddress(),
                DefaultBlockParameterName.LATEST).sendAsync().get().getValue();
        assertTrue(balance.compareTo(Convert.toPeb("0.01", Convert.Unit.KLAY).toBigInteger()) > 0);

        byte[] signedTransaction = tx.sign(LUMAN, BAOBAB_CHAIN_ID).getValue();
        Bytes32 klaySendTransaction = caverj.klay().sendSignedTransaction(Numeric.toHexString(signedTransaction)).sendAsync().get();
        String transactionHash = klaySendTransaction.getResult();
        assertFalse(transactionHash.isEmpty());

        KlayTransactionReceipt.TransactionReceipt transactionReceipt = waitForTransactionReceipt(transactionHash);

        assertThat(transactionReceipt.getTransactionHash(), is(transactionHash));
    }

    private static TxTypeLegacyTransaction createKlayTransaction(BigInteger nonce, String fromAddress, String toAddress) {
        BigInteger value = Convert.toPeb("0.01", Convert.Unit.KLAY).toBigInteger();
        return TxTypeLegacyTransaction.createTransaction(
                nonce, GAS_PRICE, GAS_LIMIT, fromAddress, toAddress, value, "");
    }

}
