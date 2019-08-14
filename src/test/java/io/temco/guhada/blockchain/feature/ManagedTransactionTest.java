//package io.temco.guhada.blockchain.feature;
//
//import com.klaytn.caver.Caver;
//import com.klaytn.caver.crpyto.KlayCredentials;
//import com.klaytn.caver.tx.account.AccountKeyPublic;
//import com.klaytn.caver.tx.manager.PollingTransactionReceiptProcessor;
//import com.klaytn.caver.tx.manager.TransactionManager;
//import com.klaytn.caver.tx.model.SmartContractDeployTransaction;
//import com.klaytn.caver.tx.model.SmartContractExecutionTransaction;
//import com.klaytn.caver.tx.model.ValueTransferTransaction;
//import com.klaytn.caver.utils.CodeFormat;
//import com.klaytn.caver.utils.Convert;
//import com.klaytn.caver.wallet.WalletManager;
//import org.junit.Before;
//import org.junit.Test;
//import org.web3j.crypto.Hash;
//import org.web3j.crypto.Keys;
//import org.web3j.utils.Numeric;
//
//import java.math.BigDecimal;
//import java.math.BigInteger;
//import java.util.concurrent.ExecutionException;
//
//public class ManagedTransactionTest {
//
//    static final KlayCredentials LUMAN = KlayCredentials.create(
//            "0x0921f3e0610ce361bd11876a59be65b91cfc959fb8eb9a4f8b37995960395fc5"
//    );
//
//    static final KlayCredentials WAYNE = KlayCredentials.create(
//            "0x88acad5201d752ed8873aea194848b51ef6555a7a664877cfeefd436c64a82b8"
//    );
//
//    private Caver caver;
//    private final BigInteger GAS_LIMIT = BigInteger.valueOf(4_300_000);
//    private TransactionManager transactionManager;
//
//    private ValueTransferTransaction valueTransferTransaction;
//    private SmartContractDeployTransaction smartContractDeployTransaction;
//    private SmartContractExecutionTransaction smartContractExecutionTransaction;
//
//    @Before
//    public void setUp() throws Exception {
//        caver = Caver.build(Caver.BAOBAB_URL);
//
//        this.transactionManager = getTransactionManager(LUMAN);
//
//        this.valueTransferTransaction = ValueTransferTransaction
//                .create(LUMAN.getAddress(), WAYNE.getAddress(), BigInteger.ZERO, BigInteger.valueOf(1000000));
//
//        KlayCredentials deployContract = KlayCredentials.create(Keys.createEcKeyPair());
//        byte[] payload = Numeric.hexStringToByteArray("0x60806040526000805534801561001457600080fd5b50610116806100246000396000f3006080604052600436106053576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806306661abd14605857806342cbb15c146080578063d14e62b81460a8575b600080fd5b348015606357600080fd5b50606a60d2565b6040518082815260200191505060405180910390f35b348015608b57600080fd5b50609260d8565b6040518082815260200191505060405180910390f35b34801560b357600080fd5b5060d06004803603810190808035906020019092919050505060e0565b005b60005481565b600043905090565b80600081905550505600a165627a7a7230582064856de85a2706463526593b08dd790054536042ef66d3204018e6790a2208d10029");
//        this.smartContractDeployTransaction = SmartContractDeployTransaction
//                .create(LUMAN.getAddress(), deployContract.getAddress(), BigInteger.ZERO, payload, GAS_LIMIT, CodeFormat.EVM);
//
//        String deployedContract = "0xffadaa1345eaf8b7e5798ba7dfb7e9a6afa55eb1";
//        this.smartContractExecutionTransaction = SmartContractExecutionTransaction
//                .create(LUMAN.getAddress(), deployedContract, BigInteger.ZERO, getChangePayload(), GAS_LIMIT);
//    }
//
//    public TransactionManager getTransactionManager(KlayCredentials credentials) {
//        WalletManager walletManager = new WalletManager();
//        walletManager.add(credentials);
//        return transactionManager = new TransactionManager.Builder(caver, walletManager)
//                .setTransactionReceiptProcessor(new PollingTransactionReceiptProcessor(caver, 1000, 15))
//                .build();
//    }
//
//    public AccountCreationTransaction getAccountCreationTransaction(KlayCredentials credentials) {
//        return AccountCreationTransaction.create(
//                LUMAN.getAddress(),
//                credentials.getAddress(),
//                Convert.toPeb("0.2", Convert.Unit.KLAY).toBigInteger(),
//                GAS_LIMIT,
//                AccountKeyPublic.create(credentials.getEcKeyPair().getPublicKey())
//        );
//    }
//
//    public AccountUpdateTransaction getAccountUpdateTransaction(KlayCredentials from, KlayCredentials to) {
//        return AccountUpdateTransaction.create(
//                from.getAddress(),
//                AccountKeyPublic.create(to.getEcKeyPair().getPublicKey()),
//                GAS_LIMIT
//        );
//    }
//
//    private byte[] getChangePayload() {
//        String setCommand = "setCount(uint256)";
//        BigInteger replaceValue = BigInteger.valueOf(27);
//        String payLoadNoCommand = Numeric.toHexStringNoPrefix(Numeric.toBytesPadded(replaceValue, 32));
//        String payLoad = new StringBuilder(Hash.sha3String(setCommand)
//                .substring(2, 10))
//                .append(payLoadNoCommand)
//                .toString();
//        return Numeric.hexStringToByteArray(payLoad);
//    }
//
//    @Test
//    public void testValueTransfer() throws Exception {
//        ValueTransfer valueTransfer = new ValueTransfer(caver, transactionManager);
//        KlayTransactionReceipt.TransactionReceipt transactionReceipt = valueTransfer.sendFunds(LUMAN.getAddress(), WAYNE.getAddress(), BigDecimal.ONE, Convert.Unit.PEB, GAS_LIMIT).send();
//        assertEquals("0x1", transactionReceipt.getStatus());
//    }
//
//    @Test
//    public void testValueTransferFlow() {
//        ValueTransfer valueTransfer = new ValueTransfer(caver, transactionManager);
//        valueTransfer.sendFunds(LUMAN.getAddress(), WAYNE.getAddress(), BigDecimal.ONE, Convert.Unit.PEB, GAS_LIMIT).flowable()
//                .test()
//                .assertSubscribed()
//                .assertValue(receipt -> receipt.getStatus().equals("0x1"));
//    }
//
//    @Test
//    public void testValueTransferFuture() throws ExecutionException, InterruptedException {
//        ValueTransfer valueTransfer = new ValueTransfer(caver, transactionManager);
//        KlayTransactionReceipt.TransactionReceipt receipt = valueTransfer.sendFunds(LUMAN.getAddress(), WAYNE.getAddress(), BigDecimal.ONE, Convert.Unit.PEB, GAS_LIMIT).sendAsync().get();
//        assertEquals("0x1", receipt.getStatus());
//    }
//
//    @Test
//    public void testValueTransferStatic() throws Exception {
//        KlayTransactionReceipt.TransactionReceipt transactionReceipt = ValueTransfer.sendFunds(caver, LUMAN, valueTransferTransaction).send();
//        assertEquals("0x1", transactionReceipt.getStatus());
//    }
//
//    @Test
//    public void testValueTransferStaticFlow() {
//        ValueTransfer.sendFunds(caver, LUMAN, valueTransferTransaction).flowable()
//                .test()
//                .assertSubscribed()
//                .assertValue(receipt -> receipt.getStatus().equals("0x1"));
//    }
//
//    @Test
//    public void testValueTransferStaticFuture() throws ExecutionException, InterruptedException {
//        KlayTransactionReceipt.TransactionReceipt transactionReceipt = ValueTransfer.sendFunds(caver, LUMAN, valueTransferTransaction).sendAsync().get();
//        assertEquals("0x1", transactionReceipt.getStatus());
//    }
//
//    @Test
//    public void testAccountCreationAndUpdate() throws Exception {
//        KlayCredentials credentials = KlayCredentials.create(Keys.createEcKeyPair());
//        AccountCreationTransaction accountCreationTransaction = getAccountCreationTransaction(credentials);
//        Account account = new Account(caver, transactionManager);
//
//        KlayTransactionReceipt.TransactionReceipt transactionReceipt = account.sendCreationTransaction(accountCreationTransaction).send();
//        assertEquals("0x1", transactionReceipt.getStatus());
//
//        KlayCredentials updateCredential = KlayCredentials.create(Keys.createEcKeyPair());
//        Account updateAccount = new Account(caver, getTransactionManager(credentials));
//        transactionReceipt = updateAccount.sendUpdateTransaction(getAccountUpdateTransaction(credentials, updateCredential)).send();
//        assertEquals("0x1", transactionReceipt.getStatus());
//    }
//
//    @Test
//    public void testAccountCreationAndUpdateFlow() throws Exception {
//        KlayCredentials credentials = KlayCredentials.create(Keys.createEcKeyPair());
//        AccountCreationTransaction accountCreationTransaction = getAccountCreationTransaction(credentials);
//        Account account = new Account(caver, transactionManager);
//
//        account.sendCreationTransaction(accountCreationTransaction).flowable()
//                .test()
//                .assertSubscribed()
//                .assertValue(receipt -> receipt.getStatus().equals("0x1"));
//
//        KlayCredentials updateCredential = KlayCredentials.create(Keys.createEcKeyPair());
//        Account updateAccount = new Account(caver, getTransactionManager(credentials));
//        updateAccount.sendUpdateTransaction(getAccountUpdateTransaction(credentials, updateCredential)).flowable()
//                .test()
//                .assertSubscribed()
//                .assertValue(receipt -> receipt.getStatus().equals("0x1"));
//    }
//
//    @Test
//    public void testAccountCreationAndUpdateFuture() throws Exception {
//        KlayCredentials credentials = KlayCredentials.create(Keys.createEcKeyPair());
//        AccountCreationTransaction accountCreationTransaction = getAccountCreationTransaction(credentials);
//        Account account = new Account(caver, transactionManager);
//
//        KlayTransactionReceipt.TransactionReceipt transactionReceipt = account.sendCreationTransaction(accountCreationTransaction).sendAsync().get();
//        assertEquals("0x1", transactionReceipt.getStatus());
//
//        KlayCredentials updateCredential = KlayCredentials.create(Keys.createEcKeyPair());
//        Account updateAccount = new Account(caver, getTransactionManager(credentials));
//        transactionReceipt = updateAccount.sendUpdateTransaction(getAccountUpdateTransaction(credentials, updateCredential)).sendAsync().get();
//        assertEquals("0x1", transactionReceipt.getStatus());
//    }
//
//    @Test
//    public void testAccountCreationAndUpdateStatic() throws Exception {
//        KlayCredentials credentials = KlayCredentials.create(Keys.createEcKeyPair());
//        AccountCreationTransaction accountCreationTransaction = getAccountCreationTransaction(credentials);
//        KlayTransactionReceipt.TransactionReceipt transactionReceipt = Account.sendCreationTransaction(caver, LUMAN, accountCreationTransaction).send();
//        assertEquals("0x1", transactionReceipt.getStatus());
//
//        KlayCredentials updateCredential = KlayCredentials.create(Keys.createEcKeyPair());
//        transactionReceipt = Account.sendUpdateTransaction(caver, credentials, getAccountUpdateTransaction(credentials, updateCredential)).send();
//        assertEquals("0x1", transactionReceipt.getStatus());
//    }
//
//    @Test
//    public void testAccountCreationAndUpdateStaticFlow() throws Exception {
//        KlayCredentials credentials = KlayCredentials.create(Keys.createEcKeyPair());
//        AccountCreationTransaction accountCreationTransaction = getAccountCreationTransaction(credentials);
//        Account.sendCreationTransaction(caver, LUMAN, accountCreationTransaction).flowable()
//                .test()
//                .assertSubscribed()
//                .assertValue(receipt -> receipt.getStatus().equals("0x1"));
//
//        KlayCredentials updateCredential = KlayCredentials.create(Keys.createEcKeyPair());
//        Account.sendUpdateTransaction(caver, credentials, getAccountUpdateTransaction(credentials, updateCredential)).flowable()
//                .test()
//                .assertSubscribed()
//                .assertValue(receipt -> receipt.getStatus().equals("0x1"));
//    }
//
//    @Test
//    public void testAccountCreationAndUpdateStaticFuture() throws Exception {
//        KlayCredentials credentials = KlayCredentials.create(Keys.createEcKeyPair());
//        AccountCreationTransaction accountCreationTransaction = getAccountCreationTransaction(credentials);
//        KlayTransactionReceipt.TransactionReceipt transactionReceipt = Account.sendCreationTransaction(caver, LUMAN, accountCreationTransaction).sendAsync().get();
//        assertEquals("0x1", transactionReceipt.getStatus());
//
//        KlayCredentials updateCredential = KlayCredentials.create(Keys.createEcKeyPair());
//        transactionReceipt = Account.sendUpdateTransaction(caver, credentials, getAccountUpdateTransaction(credentials, updateCredential)).sendAsync().get();
//        assertEquals("0x1", transactionReceipt.getStatus());
//    }
//
//    @Test
//    public void testSmartContractDeploy() throws Exception {
//        SmartContract smartContractDeploy = new SmartContract(caver, transactionManager);
//        KlayTransactionReceipt.TransactionReceipt receipt = smartContractDeploy.sendDeployTransaction(smartContractDeployTransaction).send();
//        assertEquals("0x1", receipt.getStatus());
//    }
//
//    @Test
//    public void testSmartContractDeployFlow() {
//        SmartContract smartContractDeploy = new SmartContract(caver, transactionManager);
//        smartContractDeploy.sendDeployTransaction(smartContractDeployTransaction).flowable()
//                .test()
//                .assertSubscribed()
//                .assertValue(receipt -> receipt.getStatus().equals("0x1"));
//    }
//
//    @Test
//    public void testSmartContractDeployFuture() throws ExecutionException, InterruptedException {
//        SmartContract smartContractDeploy = new SmartContract(caver, transactionManager);
//        KlayTransactionReceipt.TransactionReceipt receipt = smartContractDeploy.sendDeployTransaction(smartContractDeployTransaction).sendAsync().get();
//        assertEquals("0x1", receipt.getStatus());
//    }
//
//    @Test
//    public void testSmartContractDeployStatic() throws Exception {
//        assertEquals("0x1", SmartContract.sendDeployTransaction(caver, LUMAN, smartContractDeployTransaction).send().getStatus());
//    }
//
//    @Test
//    public void testSmartContractDeployStaticFlow() {
//        SmartContract.sendDeployTransaction(caver, LUMAN, smartContractDeployTransaction).flowable()
//                .test()
//                .assertSubscribed()
//                .assertValue(receipt -> receipt.getStatus().equals("0x1"));
//    }
//
//    @Test
//    public void testSmartContractDeployStaticFuture() throws ExecutionException, InterruptedException {
//        KlayTransactionReceipt.TransactionReceipt receipt = SmartContract.sendDeployTransaction(caver, LUMAN, smartContractDeployTransaction).sendAsync().get();
//        assertEquals("0x1", receipt.getStatus());
//    }
//
//    @Test
//    public void testSmartContractExecution() throws Exception {
//        SmartContract smartContractExecution = new SmartContract(caver, transactionManager);
//        KlayTransactionReceipt.TransactionReceipt receipt = smartContractExecution.sendExecutionTransaction(smartContractExecutionTransaction).send();
//        assertEquals("0x1", receipt.getStatus());
//        System.out.println(receipt.getErrorMessage());
//    }
//
//    @Test
//    public void testSmartContractExecutionFlow() {
//        SmartContract smartContractExecution = new SmartContract(caver, transactionManager);
//        smartContractExecution.sendExecutionTransaction(smartContractExecutionTransaction).flowable()
//                .test()
//                .assertSubscribed()
//                .assertValue(receipt -> receipt.getStatus().equals("0x1"));
//    }
//
//    @Test
//    public void testSmartContractExecutionFuture() throws ExecutionException, InterruptedException {
//        SmartContract smartContractExecution = new SmartContract(caver, transactionManager);
//        KlayTransactionReceipt.TransactionReceipt receipt = smartContractExecution.sendExecutionTransaction(smartContractExecutionTransaction).sendAsync().get();
//        assertEquals("0x1", receipt.getStatus());
//    }
//
//    @Test
//    public void testSmartContractExecutionStatic() throws Exception {
//        assertEquals("0x1", SmartContract.sendExecutionTransaction(caver, LUMAN, smartContractExecutionTransaction).send().getStatus());
//    }
//
//    @Test
//    public void testSmartContractExecutionStaticFlow() {
//        SmartContract.sendExecutionTransaction(caver, LUMAN, smartContractExecutionTransaction).flowable()
//                .test()
//                .assertSubscribed()
//                .assertValue(receipt -> receipt.getStatus().equals("0x1"));
//    }
//
//    @Test
//    public void testSmartContractExecutionStaticFuture() throws ExecutionException, InterruptedException {
//        KlayTransactionReceipt.TransactionReceipt receipt = SmartContract.sendExecutionTransaction(caver, LUMAN, smartContractExecutionTransaction).sendAsync().get();
//        assertEquals("0x1", receipt.getStatus());
//    }
//}
