package io.temco.guhada.blockchain.scenario;

import org.caverj.methods.response.Callback;
import org.caverj.methods.response.KlayTransactionReceipt;
import org.caverj.tx.ValueTransfer;
import org.caverj.tx.manager.FastGetNonceProcessor;
import org.caverj.tx.manager.QueuingTransactionReceiptProcessor;
import org.caverj.tx.manager.TransactionManager;
import org.caverj.utils.Convert;
import org.junit.Test;
import org.web3j.protocol.core.RemoteCall;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

public class FastTransactionManagerIT extends Scenario {

    private static final int COUNT = 20;
    private static final int POLLING_ATTEMPTS = 30;
    private static final int POLLING_FREQUENCY = 1000;

    @Test
    public void testTransactionQueuing() throws Exception {

        Map<String, Object> pendingTransactions = new ConcurrentHashMap<>();
        ConcurrentLinkedQueue<KlayTransactionReceipt.TransactionReceipt> transactionReceipts =
                new ConcurrentLinkedQueue<>();


        Callback callback = new Callback<KlayTransactionReceipt.TransactionReceipt>() {
            @Override
            public void accept(KlayTransactionReceipt.TransactionReceipt transactionReceipt) {
                transactionReceipts.add(transactionReceipt);
            }

            @Override
            public void exception(Exception exception) {
            }
        };

        QueuingTransactionReceiptProcessor queuingTransactionReceiptProcessor =
                new QueuingTransactionReceiptProcessor(caverj, callback, POLLING_ATTEMPTS, POLLING_FREQUENCY);

        TransactionManager transactionManager = new TransactionManager.Builder(caverj, BRANDON)
                .setGetNonceProcessor(new FastGetNonceProcessor(caverj))
                .setTransactionReceiptProcessor(queuingTransactionReceiptProcessor)
                .build();

        ValueTransfer valueTransfer = new ValueTransfer(caverj, transactionManager);

        for (int i = 0; i < COUNT; i++) {
            KlayTransactionReceipt.TransactionReceipt transactionReceipt = createTransaction(valueTransfer).send();
            pendingTransactions.put(transactionReceipt.getTransactionHash(), new Object());
        }

        for (int i = 0; i < POLLING_ATTEMPTS && !pendingTransactions.isEmpty(); i++) {
            for (KlayTransactionReceipt.TransactionReceipt transactionReceipt : transactionReceipts) {
                assertFalse(transactionReceipt.getBlockHash().isEmpty());
                pendingTransactions.remove(transactionReceipt.getTransactionHash());
                transactionReceipts.remove(transactionReceipt);
            }

            Thread.sleep(POLLING_FREQUENCY);
        }

        assertTrue(transactionReceipts.isEmpty());
    }

    private RemoteCall<KlayTransactionReceipt.TransactionReceipt> createTransaction(
            ValueTransfer valueTransfer) {
        return valueTransfer.sendFunds(BRANDON.getAddress(), LUMAN.getAddress(), BigDecimal.ONE, Convert.Unit.PEB, GAS_LIMIT);
    }

}
