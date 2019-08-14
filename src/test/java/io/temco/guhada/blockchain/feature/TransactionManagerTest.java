//package io.temco.guhada.blockchain.feature;
//
//import com.klaytn.caver.Caver;
//import com.klaytn.caver.crpyto.KlayCredentials;
//import com.klaytn.caver.tx.manager.TransactionManager;
//import org.caverj.Caverj;
//import org.caverj.methods.response.KlayTransactionReceipt;
//import org.caverj.tx.account.AccountKeyPublic;
//import org.caverj.tx.manager.PollingTransactionReceiptProcessor;
//import org.caverj.tx.model.*;
//import org.caverj.utils.CodeFormat;
//import org.caverj.utils.Convert;
//import org.caverj.wallet.WalletManager;
//import org.junit.Before;
//import org.junit.Test;
//import org.web3j.crypto.Credentials;
//import org.web3j.crypto.Hash;
//import org.web3j.crypto.Keys;
//import org.web3j.utils.Numeric;
//
//import java.math.BigInteger;
//
//public class TransactionManagerTest {
//
//    static final KlayCredentials LUMAN = KlayCredentials.create(
//            "0x68e649c49c2493fbe3429d98cbfa7ba2e8acd5f03102f8826799606ddbcd1376"
//    );
//
//    static final KlayCredentials WAYNE = KlayCredentials.create(
//            "0x88acad5201d752ed8873aea194848b51ef6555a7a664877cfeefd436c64a82b8"
//    );
//
//    private final BigInteger GAS_LIMIT = BigInteger.valueOf(4_300_000);
//    private Caver caver;
//    private TransactionManager transactionManager;
//
//    @Before
//    public void setUp() {
//        caver = Caver.build(Caverj.BAOBAB_URL);
//        WalletManager walletManager = new WalletManager();
//        walletManager.add(LUMAN);
//        transactionManager = new TransactionManager.Builder(caver, walletManager)
//                .setTransactionReceiptProcessor(new PollingTransactionReceiptProcessor(caver, 1000, 15))
//                .build();
//    }
//
//    @Test
//    public void testValueTransfer() {
//        ValueTransferTransaction valueTransferTransaction = ValueTransferTransaction
//                .create(LUMAN.getAddress(), WAYNE.getAddress(), BigInteger.ZERO, BigInteger.valueOf(1000000));
//
//        KlayTransactionReceipt.TransactionReceipt transactionReceipt = transactionManager.executeTransaction(valueTransferTransaction);
//        assertNull(transactionReceipt.getErrorMessage());
//    }
//
//    @Test
//    public void testAccountCreation() throws Exception {
//        Credentials credentials = Credentials.create(Keys.createEcKeyPair());
//        AccountCreationTransaction accountCreation = AccountCreationTransaction.create(
//                LUMAN.getAddress(),
//                credentials.getAddress(),
//                Convert.toPeb("0.1", Convert.Unit.KLAY).toBigInteger(),
//                GAS_LIMIT,
//                AccountKeyPublic.create(credentials.getEcKeyPair().getPublicKey())
//        );
//
//        KlayTransactionReceipt.TransactionReceipt transactionReceipt = transactionManager.executeTransaction(accountCreation);
//        assertEquals("0x1", transactionReceipt.getStatus());
//    }
//
//    @Test
//    public void testAccountUpdate() throws Exception {
//        KlayCredentials credentials = KlayCredentials.create(Keys.createEcKeyPair());
//
//        // new account created
//        AccountCreationTransaction accountCreation = AccountCreationTransaction.create(
//                LUMAN.getAddress(),
//                credentials.getAddress(),
//                Convert.toPeb("0.2", Convert.Unit.KLAY).toBigInteger(),
//                GAS_LIMIT,
//                AccountKeyPublic.create(credentials.getEcKeyPair().getPublicKey())
//        );
//        transactionManager.executeTransaction(accountCreation);
//
//        TransactionManager updateTransactionManager = new TransactionManager.Builder(caver, credentials)
//                .setTransactionReceiptProcessor(new PollingTransactionReceiptProcessor(caver, 1000, 15))
//                .build();
//        // update account public key
//        KlayCredentials newCredentials = KlayCredentials.create(Keys.createEcKeyPair());
//        AccountUpdateTransaction accountUpdate = AccountUpdateTransaction.create(
//                credentials.getAddress(),
//                AccountKeyPublic.create(newCredentials.getEcKeyPair().getPublicKey()),
//                GAS_LIMIT
//        );
//        KlayTransactionReceipt.TransactionReceipt accountUpdateReceipt = updateTransactionManager.executeTransaction(accountUpdate);
//        assertEquals("0x1", accountUpdateReceipt.getStatus());
//    }
//
//    @Test
//    public void testSmartContractDeploy() throws Exception {
//        String contractInput = "0x60806040526000805534801561001457600080fd5b50610116806100246000396000f3006080604052600436106053576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806306661abd14605857806342cbb15c146080578063d14e62b81460a8575b600080fd5b348015606357600080fd5b50606a60d2565b6040518082815260200191505060405180910390f35b348015608b57600080fd5b50609260d8565b6040518082815260200191505060405180910390f35b34801560b357600080fd5b5060d06004803603810190808035906020019092919050505060e0565b005b60005481565b600043905090565b80600081905550505600a165627a7a7230582064856de85a2706463526593b08dd790054536042ef66d3204018e6790a2208d10029";
//        Credentials credentials = Credentials.create(Keys.createEcKeyPair());
//
//        SmartContractDeployTransaction smartContractDeployTransaction = SmartContractDeployTransaction.create(
//                LUMAN.getAddress(),
//                credentials.getAddress(),
//                BigInteger.ZERO,
//                Numeric.hexStringToByteArray(contractInput),
//                GAS_LIMIT,
//                CodeFormat.EVM
//        );
//
//        KlayTransactionReceipt.TransactionReceipt contractDeployReceipt = transactionManager.executeTransaction(smartContractDeployTransaction);
//        assertEquals("0x1", contractDeployReceipt.getStatus());
//    }
//
//    @Test
//    public void testSmartContractExecution() {
//        String deployedContractAddress = "0xdc7eb926958efa9e7991d16346446f5ab7bb0499";
//
//        SmartContractExecutionTransaction smartContractExecution = SmartContractExecutionTransaction.create(
//                LUMAN.getAddress(),
//                deployedContractAddress,
//                BigInteger.ZERO,
//                getChangePayload(),
//                GAS_LIMIT
//        );
//
//        KlayTransactionReceipt.TransactionReceipt contractExecutionReceipt = transactionManager.executeTransaction(smartContractExecution);
//        assertEquals("0x1", contractExecutionReceipt.getStatus());
//    }
//
//    @Test
//    public void testCancel() {
//        String deployedContractAddress = "0xdc7eb926958efa9e7991d16346446f5ab7bb0499";
//
//        SmartContractExecutionTransaction smartContractExecution = SmartContractExecutionTransaction.create(
//                LUMAN.getAddress(),
//                deployedContractAddress,
//                BigInteger.ZERO,
//                getChangePayload(),
//                GAS_LIMIT
//        );
//
//        String rawContractDeploy = transactionManager.sign(smartContractExecution).getValueAsString();
//        caver.klay().sendSignedTransaction(rawContractDeploy).sendAsync();
//
//        CancelTransaction cancelTransaction = CancelTransaction.create(
//                LUMAN.getAddress(),
//                GAS_LIMIT
//        );
//
//        KlayTransactionReceipt.TransactionReceipt cancelReceipt = transactionManager.executeTransaction(cancelTransaction);
//        assertEquals("0x1", cancelReceipt.getStatus());
//    }
//
//    private byte[] getChangePayload() {
//        String setCommand = "setCount(uint256)";
//        int changeValue = 27;
//
//        BigInteger replaceValue = BigInteger.valueOf(changeValue);
//        String payLoadNoCommand = Numeric.toHexStringNoPrefix(Numeric.toBytesPadded(replaceValue, 32));
//        String payLoad = new StringBuilder(Hash.sha3String(setCommand)
//                .substring(2, 10))
//                .append(payLoadNoCommand)
//                .toString();
//        return Numeric.hexStringToByteArray(payLoad);
//    }
//
//}
