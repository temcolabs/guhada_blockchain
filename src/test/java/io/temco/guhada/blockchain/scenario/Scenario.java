package io.temco.guhada.blockchain.scenario;

import org.caverj.Caverj;
import org.caverj.crpyto.KlayCredentials;
import org.caverj.fee.FeePayerManager;
import org.caverj.methods.response.Bytes32;
import org.caverj.methods.response.KlayTransactionReceipt;
import org.caverj.tx.type.TxType;
import org.caverj.utils.ChainId;
import org.caverj.utils.Convert;
import org.caverj.utils.HumanReadableAddressUtils;
import org.junit.Before;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.Response;
import org.web3j.tx.gas.StaticGasProvider;

import java.math.BigInteger;
import java.util.Optional;

import static junit.framework.TestCase.fail;

/**
 * Common methods & settings used across scenarios
 */
public class Scenario {

    static final BigInteger GAS_PRICE = Convert.toPeb("25", Convert.Unit.STON).toBigInteger();
    static final BigInteger GAS_LIMIT = BigInteger.valueOf(4_300_000);
    static final StaticGasProvider STATIC_GAS_PROVIDER = new StaticGasProvider(GAS_PRICE, GAS_LIMIT);
    public static final int BAOBAB_CHAIN_ID = ChainId.BAOBAB;

    private static final String WALLET_PASSWORD = "";

    static final KlayCredentials LUMAN = KlayCredentials.create(
            "0x2359d1ae7317c01532a58b01452476b796a3ac713336e97d8d3c9651cc0aecc3"
    );

    static final KlayCredentials WAYNE = KlayCredentials.create(
            "0x92c0815f28b20cc22fff5fcf41adc80efe9d7ebe00439628b468f2f88a0aadc4"
    );

    static final KlayCredentials BRANDON = KlayCredentials.create(
            "0x734aa75ef35fd4420eea2965900e90040b8b9f9f7484219b1a06d06394330f4e"
    );

    static final KlayCredentials FEE_PAYER = KlayCredentials.create(
            "0x1e558ea00698990d875cb69d3c8f9a234fe8eab5c6bd898488d851669289e178"
    );

    private static final BigInteger ACCOUNT_UNLOCK_DURATION = BigInteger.valueOf(30);

    private static final int SLEEP_DURATION = 2000;
    private static final int ATTEMPTS = 15;

    Caverj caverj;

    public Scenario() {
    }

    @Before
    public void setUp() {
        this.caverj = Caverj.build(Caverj.BAOBAB_URL);
    }

    BigInteger getNonce(String address) throws Exception {
        BigInteger nonce = caverj.klay().getTransactionCount(
                HumanReadableAddressUtils.toRawAddress(address),
                DefaultBlockParameterName.PENDING).sendAsync().get().getValue();

        return nonce;
    }

    String signTransaction(TxType tx, KlayCredentials credentials) {
        return tx.sign(credentials, BAOBAB_CHAIN_ID).getValueAsString();
    }

    String signTransactionFromFeePayer(String senderRawTx, KlayCredentials feePayer) {
        FeePayerManager feePayerManager = new FeePayerManager.Builder(this.caverj, feePayer).build();
        return feePayerManager.sign(senderRawTx).getValueAsString();
    }

    KlayTransactionReceipt.TransactionReceipt sendTxAndGetReceipt(String rawTx) throws Exception {
        Bytes32 response = caverj.klay().sendSignedTransaction(rawTx).send();
        return waitForTransactionReceipt(response.getResult());
    }

    KlayTransactionReceipt.TransactionReceipt waitForTransactionReceipt(
            String transactionHash) throws Exception {

        Optional<KlayTransactionReceipt.TransactionReceipt> transactionReceiptOptional =
                getTransactionReceipt(transactionHash, SLEEP_DURATION, ATTEMPTS);

        if (!transactionReceiptOptional.isPresent()) {
            fail("Transaction receipt not generated after " + ATTEMPTS + " attempts");
        }

        return transactionReceiptOptional.get();
    }

    private Optional<KlayTransactionReceipt.TransactionReceipt> getTransactionReceipt(
            String transactionHash, int sleepDuration, int attempts) throws Exception {

        Optional<KlayTransactionReceipt.TransactionReceipt> receiptOptional =
                sendTransactionReceiptRequest(transactionHash);
        for (int i = 0; i < attempts; i++) {
            if (!receiptOptional.isPresent()) {
                Thread.sleep(sleepDuration);
                receiptOptional = sendTransactionReceiptRequest(transactionHash);
            } else {
                break;
            }
        }

        return receiptOptional;
    }

    private Optional<KlayTransactionReceipt.TransactionReceipt> sendTransactionReceiptRequest(
            String transactionHash) throws Exception {
        Response<KlayTransactionReceipt.TransactionReceipt> transactionReceipt =
                caverj.klay().getTransactionReceipt(transactionHash).sendAsync().get();

        return Optional.ofNullable(transactionReceipt.getResult());
    }
}
