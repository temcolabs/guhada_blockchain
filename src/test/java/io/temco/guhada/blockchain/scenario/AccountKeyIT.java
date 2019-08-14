//package io.temco.guhada.blockchain.scenario;
//
//import org.caverj.crpyto.*;
//import org.caverj.tx.model.AccountCreationTransaction;
//import org.caverj.methods.response.KlayTransactionReceipt;
//import org.caverj.tx.account.AccountKey;
//import org.caverj.tx.account.AccountKeyPublic;
//import org.caverj.tx.account.AccountKeyRoleBased;
//import org.caverj.tx.account.AccountKeyWeightedMultiSig;
//import org.caverj.tx.manager.PollingTransactionReceiptProcessor;
//import org.caverj.tx.manager.TransactionManager;
//import org.caverj.utils.Convert;
//import org.junit.Test;
//import org.web3j.crypto.ECKeyPair;
//import org.web3j.crypto.Keys;
//
//import java.math.BigInteger;
//import java.util.Arrays;
//
//import static org.junit.Assert.assertEquals;
//
//public class AccountKeyIT extends Scenario {
//
//    KlayCredentials credentials1, credentials2, credentials3;
//
//    @Test
//    public void AccountKeyRolebased() throws Exception {
//        KlayCredentials rolebased = KlayCredentials.create(Keys.createEcKeyPair());
//        TransactionManager transactionManager = new TransactionManager.Builder(caverj, BRANDON)
//                .setTransactionReceiptProcessor(new PollingTransactionReceiptProcessor(caverj, 1000, 15))
//                .build();
//        setUpAccount(transactionManager);
//
//        AccountCreationTransaction accountCreationTransaction = AccountCreationTransaction.create(
//                BRANDON.getAddress(),
//                rolebased.getAddress(),
//                Convert.toPeb("0.2", Convert.Unit.KLAY).toBigInteger(),
//                GAS_LIMIT,
//                createRolebased()
//        );
//        KlayTransactionReceipt.TransactionReceipt transactionReceipt = transactionManager.executeTransaction(accountCreationTransaction);
//        assertEquals("0x1", transactionReceipt.getStatus());
//    }
//
//    private AccountKey createRolebased() {
//        return AccountKeyRoleBased.create(
//                AccountKeyPublic.create(credentials1.getEcKeyPair().getPublicKey()),
//                AccountKeyPublic.create(credentials2.getEcKeyPair().getPublicKey()),
//                getRoleFeePayer()
//        );
//    }
//
//    private AccountKeyWeightedMultiSig getRoleFeePayer() {
//        return AccountKeyWeightedMultiSig.create(
//                BigInteger.valueOf(5),
//                Arrays.asList(
//                        AccountKeyWeightedMultiSig.WeightedPublicKey.create(
//                                BigInteger.valueOf(2),
//                                AccountKeyPublic.create(credentials1.getEcKeyPair().getPublicKey())
//                        ),
//                        AccountKeyWeightedMultiSig.WeightedPublicKey.create(
//                                BigInteger.valueOf(2),
//                                AccountKeyPublic.create(credentials2.getEcKeyPair().getPublicKey())
//                        ),
//                        AccountKeyWeightedMultiSig.WeightedPublicKey.create(
//                                BigInteger.valueOf(1),
//                                AccountKeyPublic.create(credentials3.getEcKeyPair().getPublicKey())
//                        )
//                )
//        );
//    }
//
//    private void setUpAccount(TransactionManager transactionManager) throws Exception {
//        ECKeyPair key1 = Keys.createEcKeyPair();
//        setUpCreateAccount(transactionManager, key1);
//        credentials1 = KlayCredentials.create(key1);
//
//        ECKeyPair key2 = Keys.createEcKeyPair();
//        setUpCreateAccount(transactionManager, key2);
//        credentials2 = KlayCredentials.create(key2);
//
//        ECKeyPair key3 = Keys.createEcKeyPair();
//        setUpCreateAccount(transactionManager, key3);
//        credentials3 = KlayCredentials.create(key3);
//    }
//
//    private void setUpCreateAccount(TransactionManager transactionManager, ECKeyPair key) throws Exception {
//        AccountCreationTransaction accountCreationTransaction = AccountCreationTransaction.create(
//                BRANDON.getAddress(),
//                Keys.getAddress(key),
//                Convert.toPeb("0.2", Convert.Unit.KLAY).toBigInteger(),
//                GAS_LIMIT,
//                AccountKeyPublic.create(key.getPublicKey())
//        );
//        transactionManager.executeTransaction(accountCreationTransaction);
//    }
//
//}
