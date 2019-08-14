//package io.temco.guhada.blockchain.feature;
//
//import org.caverj.tx.account.AccountKeyPublic;
//import org.caverj.crpyto.KlayCredentials;
//import org.caverj.tx.model.*;
//import org.caverj.tx.type.TxType;
//import org.caverj.utils.CodeFormat;
//import org.junit.Test;
//
//import java.math.BigInteger;
//
//import static junit.framework.TestCase.assertEquals;
//
//public class TransactionTransformTest {
//
//    static final KlayCredentials CREDENTIALS = KlayCredentials.create("0xf8cc7c3813ad23817466b1802ee805ee417001fcce9376ab8728c92dd8ea0a6b");
//    static final AccountKeyPublic ACCOUNT_KEY_PUBLIC = AccountKeyPublic.create(CREDENTIALS.getEcKeyPair().getPublicKey());
//
//    @Test
//    public void testValueTransferTransform() {
//
//        assertEquals(
//                TxType.Type.VALUE_TRANFSER,
//                ValueTransferTransaction.create("", "", BigInteger.ZERO, BigInteger.ONE)
//                        .build()
//                        .getType()
//        );
//        assertEquals(
//                TxType.Type.VALUE_TRANSFER_MEMO,
//                ValueTransferTransaction.create("", "", BigInteger.ZERO, BigInteger.ONE)
//                        .memo("")
//                        .build()
//                        .getType()
//        );
//        assertEquals(
//                TxType.Type.FEE_DELEGATED_VALUE_TRANSFER,
//                ValueTransferTransaction.create("", "", BigInteger.ZERO, BigInteger.ONE)
//                        .nonce(BigInteger.ZERO)
//                        .buildFeeDelegated()
//                        .getType()
//        );
//        assertEquals(
//                TxType.Type.FEE_DELEGATED_VALUE_TRANSFER_MEMO,
//                ValueTransferTransaction.create("", "", BigInteger.ZERO, BigInteger.ONE)
//                        .memo("")
//                        .buildFeeDelegated()
//                        .getType()
//        );
//        assertEquals(
//                TxType.Type.FEE_DELEGATED_VALUE_TRANSFER_WITH_RATIO,
//                ValueTransferTransaction.create("", "", BigInteger.ZERO, BigInteger.ONE)
//                        .feeRatio(BigInteger.valueOf(10))
//                        .buildFeeDelegated()
//                        .getType()
//        );
//        assertEquals(
//                TxType.Type.FEE_DELEGATED_VALUE_TRANSFER_MEMO_WITH_RATIO,
//                ValueTransferTransaction.create("", "", BigInteger.ZERO, BigInteger.ONE)
//                        .memo("")
//                        .feeRatio(BigInteger.valueOf(10))
//                        .buildFeeDelegated()
//                        .getType()
//        );
//    }
//
//    @Test
//    public void testAccountCreation() {
//        assertEquals(
//                TxType.Type.ACCOUNT_CREATION,
//                AccountCreationTransaction.create("", "", BigInteger.ZERO, BigInteger.ZERO, ACCOUNT_KEY_PUBLIC)
//                        .build()
//                        .getType()
//        );
//    }
//
//    @Test
//    public void testAccountUpdate() {
//        assertEquals(
//                TxType.Type.ACCOUNT_UPDATE,
//                AccountUpdateTransaction.create("", ACCOUNT_KEY_PUBLIC, BigInteger.ZERO)
//                        .build()
//                        .getType()
//        );
//
//        assertEquals(
//                TxType.Type.FEE_DELEGATED_ACCOUNT_UPDATE,
//                AccountUpdateTransaction.create("", ACCOUNT_KEY_PUBLIC, BigInteger.ZERO)
//                        .buildFeeDelegated()
//                        .getType()
//        );
//
//        assertEquals(
//                TxType.Type.FEE_DELEGATED_ACCOUNT_UPDATE_WITH_RATIO,
//                AccountUpdateTransaction.create("", ACCOUNT_KEY_PUBLIC, BigInteger.ZERO)
//                        .feeRatio(BigInteger.valueOf(10))
//                        .buildFeeDelegated()
//                        .getType()
//        );
//    }
//
//    @Test
//    public void testSmartContractDeploy() {
//        assertEquals(
//                TxType.Type.SMART_CONTRACT_DEPLOY,
//                SmartContractDeployTransaction.create("", "", BigInteger.ZERO, new byte[]{}, BigInteger.ZERO, CodeFormat.EVM)
//                        .build()
//                        .getType()
//        );
//
//        assertEquals(
//                TxType.Type.FEE_DELEGATED_SMART_CONTRACT_DEPLOY,
//                SmartContractDeployTransaction.create("", "", BigInteger.ZERO, new byte[]{}, BigInteger.ZERO, CodeFormat.EVM)
//                        .buildFeeDelegated()
//                        .getType()
//        );
//
//        assertEquals(
//                TxType.Type.FEE_DELEGATED_SMART_CONTRACT_DEPLOY_WITH_RATIO,
//                SmartContractDeployTransaction.create("", "", BigInteger.ZERO, new byte[]{}, BigInteger.ZERO, CodeFormat.EVM)
//                        .feeRatio(BigInteger.valueOf(10))
//                        .buildFeeDelegated()
//                        .getType()
//        );
//    }
//
//    @Test
//    public void testSmartContractExecution() {
//        assertEquals(
//                TxType.Type.SMART_CONTRACT_EXECUTION,
//                SmartContractExecutionTransaction.create("", "", BigInteger.ZERO, new byte[]{}, BigInteger.ZERO)
//                        .build()
//                        .getType()
//        );
//
//        assertEquals(
//                TxType.Type.FEE_DELEGATED_SMART_CONTRACT_EXECUTION,
//                SmartContractExecutionTransaction.create("", "", BigInteger.ZERO, new byte[]{}, BigInteger.ZERO)
//                        .buildFeeDelegated()
//                        .getType()
//        );
//
//        assertEquals(
//                TxType.Type.FEE_DELEGATED_SMART_CONTRACT_EXECUTION_WITH_RATIO,
//                SmartContractExecutionTransaction.create("", "", BigInteger.ZERO, new byte[]{}, BigInteger.ZERO)
//                        .feeRatio(BigInteger.valueOf(10))
//                        .buildFeeDelegated()
//                        .getType()
//        );
//
//    }
//
//    @Test
//    public void testCancel() {
//        assertEquals(
//                TxType.Type.CANCEL,
//                CancelTransaction.create("", BigInteger.ZERO)
//                        .nonce(BigInteger.ZERO)
//                        .build()
//                        .getType()
//        );
//
//        assertEquals(
//                TxType.Type.FEE_DELEGATED_CANCEL,
//                CancelTransaction.create("", BigInteger.ZERO)
//                        .nonce(BigInteger.ZERO)
//                        .buildFeeDelegated()
//                        .getType()
//        );
//
//        assertEquals(
//                TxType.Type.FEE_DELEGATED_CANCEL_WITH_RATIO,
//                CancelTransaction.create("", BigInteger.ZERO)
//                        .nonce(BigInteger.ZERO)
//                        .feeRatio(BigInteger.valueOf(10))
//                        .buildFeeDelegated()
//                        .getType()
//        );
//    }
//
//}
