//package io.temco.guhada.blockchain.feature;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.caverj.methods.response.KlayAccount;
//import org.caverj.methods.response.KlayAccountKey;
//import org.caverj.tx.account.AccountKey;
//import org.caverj.tx.account.AccountKeyPublic;
//import org.caverj.tx.account.AccountKeyRoleBased;
//import org.caverj.tx.account.AccountKeyWeightedMultiSig;
//import org.junit.Test;
//import org.web3j.protocol.ObjectMapperFactory;
//
//import java.io.IOException;
//import java.math.BigInteger;
//
//import static junit.framework.TestCase.assertTrue;
//import static org.junit.Assert.assertEquals;
//
//public class ResponseDeserializer {
//
//    protected final ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper(false);
//
//    @Test
//    public void testAccountKeyRolebased() throws IOException {
//        String response = "{\"jsonrpc\":\"2.0\",\"id\":1,\"result\":{\"Key\":[{\"Key\":{\"X\":\"0x819659d4f08e08d4bd97c6ce5ed2c2eb914201a5b3731eb9d208128df24b97dd\",\"Y\":\"0x1824267ab9e55f5a3fb1030f0299fa73fc0037305d5b1d90100e2131af41c010\"},\"KeyType\":2},{\"Key\":{\"X\":\"0x73363604ca8776a2883b02046361b7eb6bd11f4fc10700ee51c525bcded134c1\",\"Y\":\"0xfc3e3cb3f4f5b709df5a2075107bc73c8618440c08456bafc44ee6f27f9e6326\"},\"KeyType\":2},{\"Key\":{\"X\":\"0x95c920eb2571dff37baecdbbee32897e6e448c6725c5ab73569cc6f659684307\",\"Y\":\"0xef7839023c48acf710ad322356c12b7c5b7f475515ba7d5834f41a993f42b8f9\"},\"KeyType\":2}],\"KeyType\":5}}";
//        KlayAccountKey parsed = objectMapper.readValue(response, KlayAccountKey.class);
//        AccountKeyRoleBased accountKeyRoleBased = (AccountKeyRoleBased) parsed.getResult().getKey();
//        assertTrue(accountKeyRoleBased.getRoleFeePayer().getType().getValue() == 2);
//        assertTrue(accountKeyRoleBased.getRoleTransaction().getType().getValue() == 2);
//        assertTrue(accountKeyRoleBased.getRoleUpdate().getType().getValue() == 2);
//        assertTrue(parsed.getResult().getKey().getType() == AccountKey.Type.ROLEBASED);
//    }
//
//    @Test
//    public void testAccountKeyPublic() throws IOException {
//        String response = "{\"jsonrpc\":\"2.0\",\"id\":1,\"result\":{\"Key\":{\"X\":\"0x230037a99462acd829f317d0ce5c8e2321ac2951de1c1b1a18f9af5cff66f0d7\",\"Y\":\"0x18a7fb1b9012d2ac87bc291cbf1b3b2339356f1ce7669ae68405389be7f8b3b6\"},\"KeyType\":2}}";
//        KlayAccountKey parsed = objectMapper.readValue(response, KlayAccountKey.class);
//        assertTrue(parsed.getResult().getKeyType() == 2);
//
//        AccountKeyPublic accountKeyPublic = (AccountKeyPublic)parsed.getResult().getKey();
//        assertTrue(accountKeyPublic.getType() == AccountKey.Type.PUBLIC);
//        assertEquals(accountKeyPublic.getPublicKeyX(), "0x230037a99462acd829f317d0ce5c8e2321ac2951de1c1b1a18f9af5cff66f0d7");
//        assertEquals(accountKeyPublic.getPublicKeyY(), "0x18a7fb1b9012d2ac87bc291cbf1b3b2339356f1ce7669ae68405389be7f8b3b6");
//    }
//
//    @Test
//    public void testAccountKeyLegacy() throws IOException {
//        String response = "{\"jsonrpc\":\"2.0\",\"id\":1,\"result\":{\"Key\":{},\"KeyType\":1}}";
//        KlayAccountKey parsed = objectMapper.readValue(response, KlayAccountKey.class);
//        assertTrue(parsed.getResult().getKeyType() == 1);
//        assertTrue(parsed.getResult().getKey().getType() == AccountKey.Type.LEGACY);
//    }
//
//    @Test
//    public void testAccountKeyFail() throws IOException {
//        String response = "{\"jsonrpc\":\"2.0\",\"id\":1,\"result\":{\"Key\":{},\"KeyType\":3}}";
//        KlayAccountKey parsed = objectMapper.readValue(response, KlayAccountKey.class);
//        assertTrue(parsed.getResult().getKeyType() == 3);
//        assertTrue(parsed.getResult().getKey().getType() == AccountKey.Type.FAIL);
//    }
//
//    @Test
//    public void testAccountKeyMultiSig() throws IOException {
//        String response = "{\"jsonrpc\":\"2.0\",\"id\":1,\"result\":{\"Key\":{\"Threshold\":2,\"Keys\":[{\"Weight\":1,\"Key\":{\"X\":\"0xae6b72d7ce2c11520ac00cbd1c4da216171a96eae1ae3a0a1f979a554c9063ae\",\"Y\":\"0x79ddf38c8717030512f3ca6f304408a3beb51519b918b8d62a55ff4a8c165fea\"}},{\"Weight\":1,\"Key\":{\"X\":\"0xd4256fc43f42b3313b7204e42a82893a8d9b562f6c9b39456ee989339949c67c\",\"Y\":\"0xfc5e78e71b26f5a93b5bec454e4d63947576ffd23b4df624579ff4eb67a2a29b\"}},{\"Weight\":1,\"Key\":{\"X\":\"0xd653eae5f0e9cd6bfe4c3929f4c4f28c94f3bd183eafafee2d73db38a020d9d8\",\"Y\":\"0xe974e859b5be80755dedaebe937ac49800cbac483ca304179050a177e9ca0270\"}}]},\"KeyType\":4}}";
//        KlayAccountKey parsed = objectMapper.readValue(response, KlayAccountKey.class);
//        assertTrue(parsed.getResult().getKeyType() == 4);
//
//        AccountKeyWeightedMultiSig accountKeyPublic = (AccountKeyWeightedMultiSig)parsed.getResult().getKey();
//        assertTrue(accountKeyPublic.getType() == AccountKey.Type.MULTISIG);
//        assertEquals(accountKeyPublic.getWeightedPublicKeys().get(0).getWeight(), BigInteger.valueOf(1));
//        assertEquals(accountKeyPublic.getWeightedPublicKeys().get(0).getAccountKeyPublic().getPublicKeyX(),
//                "0xae6b72d7ce2c11520ac00cbd1c4da216171a96eae1ae3a0a1f979a554c9063ae");
//        assertEquals(accountKeyPublic.getWeightedPublicKeys().get(1).getWeight(), BigInteger.valueOf(1));
//        assertEquals(accountKeyPublic.getWeightedPublicKeys().get(1).getAccountKeyPublic().getPublicKeyX(),
//                "0xd4256fc43f42b3313b7204e42a82893a8d9b562f6c9b39456ee989339949c67c");
//        assertEquals(accountKeyPublic.getWeightedPublicKeys().get(2).getWeight(), BigInteger.valueOf(1));
//        assertEquals(accountKeyPublic.getWeightedPublicKeys().get(2).getAccountKeyPublic().getPublicKeyX(),
//                "0xd653eae5f0e9cd6bfe4c3929f4c4f28c94f3bd183eafafee2d73db38a020d9d8");
//    }
//
//
//    @Test
//    public void testAccountEOA() throws IOException {
//        String response = "{\"id\":1,\"jsonrpc\":\"2.0\",\"result\":{\"AccType\":1,\"Account\":{\"Balance\":4985316100000000000,\"HumanReadable\":false,\"Key\":{\"Key\":{\"X\":\"0x230037a99462acd829f317d0ce5c8e2321ac2951de1c1b1a18f9af5cff66f0d7\",\"Y\":\"0x18a7fb1b9012d2ac87bc291cbf1b3b2339356f1ce7669ae68405389be7f8b3b6\"},\"KeyType\":2},\"Nonce\":11}}}";
//        KlayAccount parsed = objectMapper.readValue(response, KlayAccount.class);
//        assertTrue(parsed.getResult().getAccType() == 1);
//    }
//
//    @Test
//    public void testAccountLegacy() throws IOException {
//        String response = "{\"id\":1,\"jsonrpc\":\"2.0\",\"result\":{\"Balance\":3.34599847501718e+21,\"CodeHash\":\"xdJGAYb3IzySfn2y3McDwOUAtlPKgic7e/rYBF2FpHA=\",\"Nonce\":20,\"Root\":\"0x56e81f171bcc55a6ff8345e692c0f86e5b48e01b996cadc001622fb5e363b421\"}}";
//        KlayAccount parsed = objectMapper.readValue(response, KlayAccount.class);
//        assertTrue(parsed.getResult().getAccType() == 0);
//    }
//
//    @Test
//    public void testAccountSmartContract() throws IOException {
//        String response = "{\"id\":1,\"jsonrpc\":\"2.0\",\"result\":{\"AccType\":2,\"Account\":{\"CodeHash\":\"80NXvdOay02rYC/JgQ7RfF7yoxY1N7W8P7BiPvkIeF8=\",\"CommonSerializable\":{\"Balance\":0,\"HumanReadable\":true,\"Key\":{\"Key\":{},\"KeyType\":3},\"Nonce\":0},\"StorageRoot\":\"0x56e81f171bcc55a6ff8345e692c0f86e5b48e01b996cadc001622fb5e363b421\"}}}";
//        KlayAccount parsed = objectMapper.readValue(response, KlayAccount.class);
//        assertTrue(parsed.getResult().getAccType() == 2);
//    }
//
//}
//
