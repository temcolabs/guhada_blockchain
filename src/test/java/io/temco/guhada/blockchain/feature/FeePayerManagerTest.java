package io.temco.guhada.blockchain.feature;

import org.caverj.Caverj;
import org.caverj.crpyto.KlayCredentials;
import org.caverj.fee.FeePayerManager;
import org.caverj.methods.response.KlayTransactionReceipt;
import org.caverj.tx.model.ValueTransferTransaction;
import org.caverj.tx.manager.PollingTransactionReceiptProcessor;
import org.caverj.tx.type.TxTypeFeeDelegatedValueTransfer;
import org.caverj.utils.ChainId;
import org.caverj.utils.Convert;
import org.junit.Before;
import org.junit.Test;
import org.web3j.protocol.core.DefaultBlockParameterName;

import java.math.BigInteger;

import static org.junit.Assert.assertEquals;

public class FeePayerManagerTest {

    static final BigInteger GAS_PRICE = Convert.toPeb("25", Convert.Unit.STON).toBigInteger();
    static final BigInteger GAS_LIMIT = BigInteger.valueOf(4_300_000);

    static final KlayCredentials LUMAN = KlayCredentials.create(
            "0x352e060481ecc3c9ccf1b0e372b3765d53e0ee5a1b37820a56f293882c1bb07d"
    );

    static final KlayCredentials BRANDON = KlayCredentials.create(
            "0xd970d80ced2251f3dd57806b1b8a697313befedcdb6fb40dfd2db2417a678e91"
    );
    static final KlayCredentials FEE_PAYER = KlayCredentials.create("0xdbf27cba60b0ea2b6b7869f45556542865fd804abe28eb8fb231e79735def7d8");

    Caverj caverj;

    @Before
    public void setUp() {
        caverj = Caverj.build(Caverj.BAOBAB_URL);
    }

    @Test
    public void testFeePayerManagerValueTransfer() throws Exception {
        String rawTx = getSenderRawTx();
        FeePayerManager feePayerManager =
                new FeePayerManager.Builder(caverj, FEE_PAYER)
                        .setTransactionReceiptProcessor(new PollingTransactionReceiptProcessor(caverj, 1000, 10))
                        .build();
        KlayTransactionReceipt.TransactionReceipt transactionReceipt = feePayerManager.executeTransaction(rawTx);
        assertEquals("0x1", transactionReceipt.getStatus());
    }

    private String getSenderRawTx() throws Exception {
        TxTypeFeeDelegatedValueTransfer tx = (TxTypeFeeDelegatedValueTransfer) ValueTransferTransaction.create(LUMAN.getAddress(), BRANDON.getAddress(), BigInteger.ONE, GAS_LIMIT)
                .gasPrice(GAS_PRICE)
                .nonce(getNonce(LUMAN.getAddress()))
                .buildFeeDelegated();

        return tx.sign(LUMAN, ChainId.BAOBAB).getValueAsString();
    }

    BigInteger getNonce(String address) throws Exception {
        BigInteger nonce = caverj.klay().getTransactionCount(
                address,
                DefaultBlockParameterName.PENDING).sendAsync().get().getValue();

        return nonce;
    }
}
