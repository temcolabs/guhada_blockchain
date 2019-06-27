package io.temco.guhada.blockchain.scenario;

import org.caverj.tx.account.AccountKeyPublic;
import org.caverj.crpyto.KlayCredentials;
import org.caverj.methods.response.KlayTransactionReceipt;
import org.caverj.tx.model.AccountCreationTransaction;
import org.caverj.tx.model.ValueTransferTransaction;
import org.caverj.tx.manager.TransactionManager;
import org.caverj.utils.Convert;
import org.junit.Test;
import org.web3j.crypto.Keys;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import static junit.framework.TestCase.assertEquals;

public class TransactionManagerIT extends Scenario {

    static final String MEMO_VALUE = "this is TransactionManagerIT";

    @Test
    public void testTransactionManager() throws Exception {
        TransactionManager transactionManager
                = new TransactionManager.Builder(caverj, BRANDON)
                /*
                .setChaindId(0)
                .setErrorHandler(new ErrorHandler() {
                    @Override
                    public void exception(Exception exception) {

                    }
                })
                .setGetNonceProcessor(new FastGetNonceProcessor(caverj))
                .setTransactionReceiptProcessor(new QueuingTransactionReceiptProcessor(
                        caverj,
                        new Callback<KlayTransactionReceipt.TransactionReceipt>() {
                            @Override
                            public void accept(KlayTransactionReceipt.TransactionReceipt result) {
                                System.out.println(result);
                            }

                            @Override
                            public void exception(Exception exception) {
                                System.out.println(exception);
                            }
                        }
                ))
                */
                .build();



        KlayCredentials credentials = KlayCredentials.create(Keys.createEcKeyPair());

        createNewAccount(transactionManager, credentials);

        ValueTransferTransaction valueTransferTransaction = ValueTransferTransaction.create(
                BRANDON.getAddress(),
                WAYNE.getAddress(),
                BigInteger.ONE,
                GAS_LIMIT
        ).memo(MEMO_VALUE);

        KlayTransactionReceipt.TransactionReceipt valueTransferReceipt = transactionManager.executeTransaction(valueTransferTransaction);

        assertEquals(Numeric.toHexString(MEMO_VALUE.getBytes()), valueTransferReceipt.getInput());
    }

    private void createNewAccount(TransactionManager transactionManager, KlayCredentials credentials) throws Exception {
        AccountCreationTransaction accountCreationTransaction = AccountCreationTransaction.create(
                BRANDON.getAddress(),
                credentials.getAddress(),
                Convert.toPeb("0.2", Convert.Unit.KLAY).toBigInteger(),
                GAS_LIMIT,
                AccountKeyPublic.create(credentials.getEcKeyPair().getPublicKey())
        );
        transactionManager.executeTransaction(accountCreationTransaction);
    }
}
