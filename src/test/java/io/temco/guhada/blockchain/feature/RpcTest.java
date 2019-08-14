//package io.temco.guhada.blockchain.feature;
//
//import org.caverj.Caverj;
//import org.caverj.crpyto.KlayCredentials;
//import org.caverj.methods.request.CallObject;
//import org.caverj.methods.request.KlayFilter;
//import org.caverj.methods.request.KlayLogFilter;
//import org.caverj.methods.response.Boolean;
//import org.caverj.methods.response.*;
//import io.temco.guhada.blockchain.scenario.Scenario;
//import org.caverj.utils.ChainId;
//import org.junit.Before;
//import org.junit.Ignore;
//import org.junit.Test;
//import org.web3j.protocol.core.DefaultBlockParameterName;
//import org.web3j.protocol.core.DefaultBlockParameterNumber;
//import org.web3j.protocol.core.Response;
//import org.web3j.protocol.core.methods.response.NetListening;
//import org.web3j.protocol.core.methods.response.NetPeerCount;
//import org.web3j.utils.Numeric;
//
//import java.io.IOException;
//import java.math.BigInteger;
//import java.util.List;
//import java.util.Optional;
//
//import static junit.framework.TestCase.*;
//
//public class RpcTest {
//
//    private Caverj caverj;
//    private TestBlock testBlock;
//    private String testTransactionHash;
//
//    static final KlayCredentials LUMAN = KlayCredentials.create(
//            "0x91cb0473a01e4b5c4fbee668351f30711d96f2ba00f7dc7a7d04886622af1d31"
//    );
//
//    static final KlayCredentials BRANDON = KlayCredentials.create(
//            "0xc17750543778efee85bcceae09b3dad39f9dd59293c1863c13b7807cc0db1416"
//    );
//
//    public static class TestBlock {
//
//        int blockNumber;
//        String blockHash;
//        int transactionCount;
//
//        public TestBlock(int blockNumber, String blockHash, int transactionCount) {
//            this.blockNumber = blockNumber;
//            this.blockHash = blockHash;
//            this.transactionCount = transactionCount;
//        }
//    }
//
//    @Before
//    public void setUp() {
//        this.caverj = Caverj.build(Caverj.BAOBAB_URL);
//        this.testBlock = new TestBlock(
//                36523,
//                "0x307b87383974dcf419436f0f30e1dd71945673282cda9b63e7de30361567a46e",
//                1);
//        this.testTransactionHash = "0x26ce172b055cc5adc26cc1350868c9fe4840729d4cc03b3f9f0a934eebdd0cec";
//    }
//
//    @Test
//    public void testGetProtocolVersion() throws Exception {
//        String result = caverj.klay().getProtocolVersion().send().getResult();
//        assertEquals("0x40", result);
//    }
//
//    @Test
//    public void testIsSyncing() throws Exception {
//        KlaySyncing response = caverj.klay().isSyncing().send();
//        KlaySyncing.Result result = response.getResult();
//        assertFalse(result.isSyncing());
//    }
//
//    @Test
//    public void testIsMining() throws Exception {
//        Boolean result = caverj.klay().isMining().send();
//        boolean isMining = result.getResult();
//        assertTrue(isMining);
//    }
//
//    @Test
//    public void testGetGasPrice() throws Exception {
//        Quantity response = caverj.klay().getGasPrice().send();
//        BigInteger result = response.getValue();
//        assertEquals(new BigInteger("5d21dba00", 16), result); // 25,000,000,000 peb = 25 Gpeb
//    }
//
//    @Test
//    public void testGetAccounts() throws Exception {
//        Addresses response = caverj.klay().getAccounts().send();
//        assertNull(response.getError());
//    }
//
//    @Test
//    public void testIsAccountCreated() throws Exception {
//        Boolean response = caverj.klay().isAccountCreated(
//                LUMAN.getAddress(),
//                DefaultBlockParameterName.LATEST
//        ).send();
//        assertTrue(response.getResult());
//    }
//
//    @Test
//    public void testGetBlockNumber() throws Exception {
//        Quantity response = caverj.klay().getBlockNumber().send();
//        BigInteger result = response.getValue();
//        assertNotNull(result);
//    }
//
//    @Test
//    public void testGetChainId() throws Exception {
//        Quantity response = caverj.klay().getChainId().send();
//        BigInteger result = response.getValue();
//        assertEquals(BigInteger.valueOf(ChainId.BAOBAB), result);
//    }
//
//    @Ignore
//    @Test
//    public void testGetRewardbase() throws Exception {
//        Bytes20 response = caverj.klay().getRewardbase().send();
//        // Result - If requested from non-CN nodes
//        assertEquals("rewardbase must be explicitly specified", response.getError().getMessage());
//    }
//
//    @Test
//    public void testIsContractAccount() throws Exception {
//        Boolean response = caverj.klay().isContractAccount(
//                "0xd176364a6e4c0737efbee4c598835633de50f3f0", DefaultBlockParameterName.LATEST).send();
//        java.lang.Boolean result = response.getResult();
//        assertTrue(result);
//    }
//
//    @Test
//    public void testIsParallelDbWrite() throws Exception {
//        Boolean response = caverj.klay().isParallelDbWrite().send();
//        java.lang.Boolean result = response.getResult();
//        assertTrue(result);  // It is enabled by default
//    }
//
//    @Test
//    public void testIsSenderTxHashIndexingEnabled() throws Exception {
//        Boolean response = caverj.klay().isSenderTxHashIndexingEnabled().send();
//        java.lang.Boolean result = response.getResult();
//        assertFalse(result);  // It is disabled by default
//    }
//
//    @Test
//    public void testIsHumanReadable() throws Exception {
//        Boolean response = caverj.klay().isHumanReadable(
//                "0x71776572742e6b6c6179746e0000000000000000", DefaultBlockParameterName.LATEST
//        ).send();
//        java.lang.Boolean result = response.getResult();
//        assertTrue(result);
//    }
//
//    @Test
//    public void testIsWriteThroughCaching() throws Exception {
//        Boolean response = caverj.klay().isWriteThroughCaching().send();
//        java.lang.Boolean result = response.getResult();
//        assertFalse(result);  // It is false by default.
//    }
//
//    @Test
//    public void testGetBalance() throws Exception {
//        Quantity response = caverj.klay().getBalance(LUMAN.getAddress(), DefaultBlockParameterName.LATEST).send();
//        String result = response.getResult();
//        assertNotNull(result);
//    }
//
//    @Test
//    public void testGetStorageAt() throws Exception {
//        Response<String> response = caverj.klay().getStorageAt(
//                LUMAN.getAddress(),
//                new DefaultBlockParameterNumber(0),
//                DefaultBlockParameterName.LATEST).send();
//        String result = response.getResult();
//        assertNotNull(result);
//    }
//
//    @Test
//    public void testGetTransactionCount() throws Exception {
//        Quantity response = caverj.klay().getTransactionCount(
//                LUMAN.getAddress(),
//                DefaultBlockParameterName.LATEST).send();
//        BigInteger result = response.getValue();
//        assertNotNull(result);
//    }
//
//    @Test
//    public void testGetTransactionCountByHash() throws Exception {
//        Quantity response = caverj.klay().getTransactionCountByHash(testBlock.blockHash).send();
//        BigInteger result = response.getValue();
//        assertEquals(testBlock.transactionCount, result.intValue());
//    }
//
//    @Test
//    public void testGetTransactionCountByNumber() throws Exception {
//        Quantity response = caverj.klay().getTransactionCountByNumber(
//                new DefaultBlockParameterNumber(testBlock.blockNumber)).send();
//        BigInteger result = response.getValue();
//        assertEquals(testBlock.transactionCount, result.intValue());
//    }
//
//    @Test
//    public void testGetCode() throws Exception {
//        Response<String> response = caverj.klay().getCode("0x9bf734fcacf0151d81f5d895898bf3afeb1b6e0b", DefaultBlockParameterName.LATEST).send();
//        String result = response.getResult();
//        assertEquals("0x6080604052600436106053576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806306661abd14605857806342cbb15c146080578063d14e62b81460a8575b600080fd5b348015606357600080fd5b50606a60d2565b6040518082815260200191505060405180910390f35b348015608b57600080fd5b50609260d8565b6040518082815260200191505060405180910390f35b34801560b357600080fd5b5060d06004803603810190808035906020019092919050505060e0565b005b60005481565b600043905090565b80600081905550505600a165627a7a7230582064856de85a2706463526593b08dd790054536042ef66d3204018e6790a2208d10029",
//                result);
//    }
//
//    @Test
//    public void getAccountKey() throws IOException {
//        KlayAccountKey response = caverj.klay().getAccountKey("0x9300da845f40c25336e3fbaec88a1842f3029a95", DefaultBlockParameterName.LATEST).send();
//        KlayAccountKey.AccountKeyValue accountKey = response.getResult();
//        assertEquals(0x05, accountKey.getKeyType());
//    }
//
//    @Test
//    public void getAccount() throws IOException {
//        KlayAccount response = caverj.klay().getAccount("0x1b24096b5a84d0f422faaa69f4de65d24329bd87", DefaultBlockParameterName.LATEST).send();
//        KlayAccount.Account account = response.getResult();
//        assertEquals(0x2, account.getAccType());
//    }
//
//    @Test
//    @Ignore
//    public void testSign() throws Exception {
//        // need a account and unlock
//        caverj.klay().unlockAccount(LUMAN.getAddress(), "password", BigInteger.valueOf(10)).send();
//
//        Response<String> response = caverj.klay().sign(LUMAN.getAddress(), "0xdeadbeaf").send();
//        String result = response.getResult();
//        assertEquals("0x8bb279f497d55a9068b9941f056d7b5e70bead57167d0e12aa3576a33766567f23135209a7a08992288cff75a4c60fb1f5c131e906dd0e714722b89a6964a1b51b",
//                result);
//    }
//
//    @Ignore
//    @Test
//    public void testSignTransaction() {
//        // SendKlayIT.java
//
//    }
//
//    @Ignore
//    @Test
//    public void testSendTransaction() {
//        // SendKlayIT.java
//    }
//
//    @Ignore
//    @Test
//    public void testSendSignedTransaction() {
//        // SendKlayIT.java
//    }
//
//    @Test
//    public void testCall() throws Exception {
//        CallObject callObject = CallObject.createCallObject(
//                LUMAN.getAddress(),
//                "0xB18f35236a5296ccf2cc24EC67487E07C224b98E",
//                new BigInteger("2dc6c0", 16),
//                new BigInteger("5d21dba00", 16),
//                new BigInteger("0", 16),
//                "0x06661abd"  // count()
//        );
//        Response<String> response = caverj.klay().call(callObject, DefaultBlockParameterName.LATEST).send();
//        String result = response.getResult();
//        assertEquals(27, Numeric.toBigInt(result).intValue());
//    }
//
//    @Test
//    public void testEstimateGas() throws Exception {
//        CallObject callObject = CallObject.createCallObject(
//                "0x3f71029af4e252b25b9ab999f77182f0cd3bc085",
//                "0x87ac99835e67168d4f9a40580f8f5c33550ba88b",
//                new BigInteger("100000", 16),
//                new BigInteger("5d21dba00", 16),
//                new BigInteger("0", 16),
//                "0x8ada066e"
//        );
//        Quantity response = caverj.klay().estimateGas(callObject).send();
//        String result = response.getResult();
//        assertEquals("0x5318", result);
//    }
//
//    @Test
//    public void testGetBlockByHash() throws Exception {
//        KlayBlock response = caverj.klay().getBlockByHash(testBlock.blockHash, true).send();
//        KlayBlock.Block result = response.getResult();
//        assertEquals(testBlock.blockHash, result.getTransactions().get(0).getBlockHash());
//    }
//
//    @Test
//    public void testGetBlockByNumber() throws Exception {
//        KlayBlock response = caverj.klay().getBlockByNumber(
//                new DefaultBlockParameterNumber(testBlock.blockNumber),
//                true).send();
//        KlayBlock.Block result = response.getResult();
//        assertEquals(testBlock.blockHash, result.getTransactions().get(0).getBlockHash());
//    }
//
//    @Test
//    public void testGetBlockReceipts() throws Exception {
//        BlockReceipts response = caverj.klay().getBlockReceipts(testBlock.blockHash).send();
//        List<KlayTransactionReceipt.TransactionReceipt> result = response.getResult();
//        assertEquals(testBlock.blockHash, result.get(0).getBlockHash());
//    }
//
//    @Test
//    public void testGetTransactionByHash() throws Exception {
//        KlayTransaction response = caverj.klay().getTransactionByHash(testTransactionHash).send();
//        Optional<KlayTransaction.Transaction> result = response.getTransaction();
//        assertEquals(testTransactionHash, result.get().getHash());
//    }
//
//    @Test
//    public void testGetTransactionBySenderTxHash() throws Exception {
//        KlayTransaction response = caverj.klay().getTransactionBySenderTxHash(testTransactionHash).send();
//        Optional<KlayTransaction.Transaction> result = response.getTransaction();
//        assertEquals(testTransactionHash, result.get().getHash());
//    }
//
//    @Test
//    public void testGetTransactionByBlockHashAndIndex() throws Exception {
//        KlayTransaction response = caverj.klay().getTransactionByBlockHashAndIndex(
//                testBlock.blockHash,
//                new DefaultBlockParameterNumber(0)
//        ).send();
//        Optional<KlayTransaction.Transaction> result = response.getTransaction();
//        assertEquals(testBlock.blockHash, result.get().getBlockHash());
//    }
//
//    @Test
//    public void testGetTransactionByBlockNumberAndIndex() throws Exception {
//        KlayTransaction response = caverj.klay().getTransactionByBlockNumberAndIndex(
//                new DefaultBlockParameterNumber(testBlock.blockNumber),
//                new DefaultBlockParameterNumber(0)
//        ).send();
//        Optional<KlayTransaction.Transaction> result = response.getTransaction();
//        assertEquals(testBlock.blockHash, result.get().getBlockHash());
//    }
//
//    @Test
//    public void testGetTransactionReceipt() throws Exception {
//        KlayTransactionReceipt response = caverj.klay().getTransactionReceipt(testTransactionHash).send();
//        Optional<KlayTransactionReceipt.TransactionReceipt> result = response.getTransactionReceipt();
//        assertEquals(testTransactionHash, result.get().getTransactionHash());
//    }
//
//    @Test
//    public void testGetTransactionReceiptBySenderTxHash() throws Exception {
//        KlayTransactionReceipt response = caverj.klay().getTransactionReceiptBySenderTxHash(testTransactionHash).send();
//        Optional<KlayTransactionReceipt.TransactionReceipt> result = response.getTransactionReceipt();
//        assertEquals(testTransactionHash, result.get().getTransactionHash());
//    }
//
//    @Test
//    public void testNewFilter() throws Exception {
//        KlayFilter filter = new KlayFilter(
//                DefaultBlockParameterName.EARLIEST,
//                DefaultBlockParameterName.LATEST,
//                LUMAN.getAddress());
//        filter.addSingleTopic("0xd596fdad182d29130ce218f4c1590c4b5ede105bee36690727baa6592bd2bfc8");
//        Quantity response = caverj.klay().newFilter(filter).send();
//        String result = response.getResult();
//        assertTrue(result.matches("0x\\w{32}$"));
//    }
//
//    @Test
//    public void testNewBlockFilter() throws Exception {
//        Response<String> response = caverj.klay().newBlockFilter().send();
//        String result = response.getResult();
//        assertTrue(result.matches("0x\\w{32}$"));
//    }
//
//    @Test
//    public void testNewPendingTransactionFilter() throws Exception {
//        Response<String> response = caverj.klay().newPendingTransactionFilter().send();
//        String result = response.getResult();
//        assertTrue(result.matches("0x\\w{32}$"));
//    }
//
//    @Test
//    @Ignore
//    public void testUninstallFilter() throws Exception {
//        Boolean response = caverj.klay().uninstallFilter(BigInteger.ZERO).send();
//        java.lang.Boolean result = response.getResult();
//        assertTrue("need test data", false);
//    }
//
//    @Test
//    @Ignore
//    public void testGetFilterChanges() throws Exception {
//        KlayLogs response = caverj.klay().getFilterChanges(
//                new BigInteger("d5b93cf592b2050aee314767a02976c5", 16)).send();
//        List<KlayLogs.LogResult> result = response.getResult();
//        assertTrue("need test data", false);
//    }
//
//    @Test
//    @Ignore
//    public void testGetFilterLogs() throws Exception {
//        KlayLogs response = caverj.klay().getFilterLogs(
//                new BigInteger("d5b93cf592b2050aee314767a02976c5", 16)).send();
//        List<KlayLogs.LogResult> result = response.getResult();
//        assertTrue("need test data", false);
//    }
//
//    @Test
//    @Ignore
//    public void testGetLogs() throws Exception {
//        KlayLogFilter filter = new KlayLogFilter(
//                DefaultBlockParameterName.EARLIEST,
//                DefaultBlockParameterName.LATEST,
//                LUMAN.getAddress(),
//                "0xe2649fe9fbaa75601408fc54200e3f9b2128e8fec7cea96c9a65b9caf905c9e3");
//        KlayLogs response = caverj.klay().getLogs(filter).send();
//        List<KlayLogs.LogResult> result = response.getResult();
//        assertTrue("need test data", false);
//    }
//
//    @Test
//    public void testGetWork() throws Exception {
//        Response<List<String>> response = caverj.klay().getWork().send();
//        List<String> result = response.getResult();
//        assertEquals(3, result.size());
//        assertTrue(result.get(0).matches("0x\\w{64}$"));
//        assertTrue(result.get(1).matches("0x\\w{64}$"));
//        assertTrue(result.get(2).matches("0x\\w{64}$"));
//    }
//
//    @Test
//    public void testValidators() throws Exception {
//        Response<List<String>> response = caverj.klay().getValidators(
//                DefaultBlockParameterName.LATEST
//        ).send();
//        List<String> result = response.getResult();
//        for (int i = 0; i < result.size(); i++) {
//            assertTrue(result.get(i).matches("0x\\w{40}$"));
//        }
//    }
//
//    @Test
//    public void testGetBlockWithConsensusInfoByHash() throws Exception {
//        KlayBlockWithConsensusInfo response = caverj.klay().getBlockWithConsensusInfoByHash(testBlock.blockHash).send();
//        KlayBlockWithConsensusInfo.Block result = response.getResult();
//        assertEquals(testBlock.blockHash, result.getHash());
//    }
//
//    @Test
//    public void testGetBlockWithConsensusInfoByNumber() throws Exception {
//        KlayBlockWithConsensusInfo response = caverj.klay().getBlockWithConsensusInfoByNumber(
//                new DefaultBlockParameterNumber(testBlock.blockNumber)).send();
//        KlayBlockWithConsensusInfo.Block result = response.getResult();
//        assertEquals(testBlock.blockNumber, Numeric.toBigInt(result.getNumber()).intValue());
//    }
//
//    @Test
//    public void testGetId() throws Exception {
//        Bytes netVersion = caverj.net().getNetworkId().send();
//        assertEquals(netVersion.getResult(), String.valueOf(Scenario.BAOBAB_CHAIN_ID));
//    }
//
//    @Test
//    public void testIsListeninng() throws Exception {
//        NetListening netListening = caverj.net().isListening().send();
//        assertTrue(netListening.isListening());
//    }
//
//    @Test
//    public void getPeerCount() throws Exception {
//        NetPeerCount peerCount = caverj.net().getPeerCount().send();
//        assertTrue(peerCount.getQuantity().intValue() >= 0);
//    }
//}
