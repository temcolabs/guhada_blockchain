package io.temco.guhada.blockchain.feature;

import org.caverj.crpyto.KlayCredentials;
import org.caverj.fee.FeePayer;
import org.caverj.tx.account.AccountKey;
import org.caverj.tx.account.AccountKeyPublic;
import org.caverj.tx.model.KlayRawTransaction;
import org.caverj.tx.type.*;
import org.caverj.utils.CodeFormat;
import org.caverj.utils.KlayTransactionUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.web3j.utils.Numeric;

import java.math.BigDecimal;
import java.math.BigInteger;

import static junit.framework.TestCase.assertEquals;

public class TransactionTest {
    static final KlayCredentials LUMAN = KlayCredentials.create(
            "0x45a915e4d060149eb4365960e6a7a45f334393093061116b197e3240065ff2d8"
    );

    static final KlayCredentials FEE_PAYER = KlayCredentials.create(
            "0xb9d5558443585bca6f225b935950e3f6e69f9da8a5809a83f51c3365dff53936",
            "0x5A0043070275d9f6054307Ee7348bD660849D90f"
    );

    AccountKey accountKey;

    @Before
    public void setUp() {
        BigDecimal bigDecimal = new BigDecimal("26377711632280541038264097194380963599732365852680103816037000824169036505917");
        BigDecimal bigDecimal2 = new BigDecimal("58099062755695798236614729978625811254910488488448333846918760849003458564531");

        accountKey = AccountKeyPublic.create(
                Numeric.prependHexPrefix(bigDecimal.toBigInteger().toString(16)),
                Numeric.prependHexPrefix(bigDecimal2.toBigInteger().toString(16))
        );
    }

    @Test
    public void testTxTypeLegacyTransaction() {
        TxTypeLegacyTransaction tx = TxTypeLegacyTransaction.createTransaction(
                BigInteger.valueOf(1234),
                BigInteger.valueOf(0x19),
                BigInteger.valueOf(0xf4240),
                LUMAN.getAddress(),
                "7b65b75d204abed71587c9e519a89277766ee1d0",
                BigInteger.valueOf(0xa),
                "0x31323334");

        String rawTx = tx.sign(LUMAN, 1).getValueAsString();
        TxType.Type type = KlayTransactionUtils.getType(rawTx);
        assertEquals(type, TxType.Type.LEGACY);
        assertEquals("0xf8668204d219830f4240947b65b75d204abed71587c9e519a89277766ee1d00a843132333425a0b2a5a15550ec298dc7dddde3774429ed75f864c82caeb5ee24399649ad731be9a029da1014d16f2011b3307f7bbe1035b6e699a4204fc416c763def6cefd976567",
                rawTx);
    }

    @Test
    public void testTxTypeValueTransfer() {
        TxTypeValueTransfer tx = TxTypeValueTransfer.createTransaction(
                BigInteger.valueOf(1234),
                BigInteger.valueOf(0x19),
                BigInteger.valueOf(0xf4240),
                LUMAN.getAddress(),
                "7b65b75d204abed71587c9e519a89277766ee1d0",
                BigInteger.valueOf(0xa));

        String rawTx = tx.sign(LUMAN, 1).getValueAsString();
        TxType.Type type = KlayTransactionUtils.getType(rawTx);
        assertEquals(type, TxType.Type.VALUE_TRANFSER);
        assertEquals("0x08f87a8204d219830f4240947b65b75d204abed71587c9e519a89277766ee1d00a94a94f5374fce5edbc8e2a8697c15331677e6ebf0bf845f84325a0f3d0cd43661cabf53425535817c5058c27781f478cb5459874feaa462ed3a29aa06748abe186269ff10b8100a4b7d7fea274b53ea2905acbf498dc8b5ab1bf4fbc",
                rawTx);
    }

    @Test
    public void testTxTypeFeeDelegatedValueTransfer() {
        /**
         * Client Side
         */
        TxTypeFeeDelegatedValueTransfer tx = TxTypeFeeDelegatedValueTransfer.createTransaction(
                BigInteger.valueOf(1234),
                BigInteger.valueOf(0x19),
                BigInteger.valueOf(0xf4240),
                LUMAN.getAddress(),
                "7b65b75d204abed71587c9e519a89277766ee1d0",
                BigInteger.valueOf(0xa));

        String rawTx = tx.sign(LUMAN, 1).getValueAsString();

        /**
         * Payer Side
         */
        TxType.Type type = KlayTransactionUtils.getType(rawTx);
        assertEquals(type, TxType.Type.FEE_DELEGATED_VALUE_TRANSFER);

        TxTypeFeeDelegatedValueTransfer senderTx = TxTypeFeeDelegatedValueTransfer.decodeFromRawTransaction(rawTx);
        KlayRawTransaction payerTx = new FeePayer(FEE_PAYER, 1).sign(senderTx);

        assertEquals("0x09f8d68204d219830f4240947b65b75d204abed71587c9e519a89277766ee1d00a94a94f5374fce5edbc8e2a8697c15331677e6ebf0bf845f84325a09f8e49e2ad84b0732984398749956e807e4b526c786af3c5f7416b293e638956a06bf88342092f6ff9fabe31739b2ebfa1409707ce54a54693e91a6b9bb77df0e7945a0043070275d9f6054307ee7348bd660849d90ff845f84326a0f45cf8d7f88c08e6b6ec0b3b562f34ca94283e4689021987abb6b0772ddfd80aa0298fe2c5aeabb6a518f4cbb5ff39631a5d88be505d3923374f65fdcf63c2955b",
                payerTx.getValueAsString());
    }

    @Test
    public void testTxTypeFeeDelegatedValueTransferWithRatio() {
        /**
         * Client Side
         */
        TxTypeFeeDelegatedValueTransferWithRatio tx = TxTypeFeeDelegatedValueTransferWithRatio.createTransaction(
                BigInteger.valueOf(1234),
                BigInteger.valueOf(0x19),
                BigInteger.valueOf(0xf4240),
                "0x7b65B75d204aBed71587c9E519a89277766EE1d0",
                BigInteger.valueOf(0xa),
                LUMAN.getAddress(),
                BigInteger.valueOf(30));
        String rawTx = tx.sign(LUMAN, 1).getValueAsString();

        /**
         * Payer Side
         */
        TxType.Type type = KlayTransactionUtils.getType(rawTx);
        assertEquals(type, TxType.Type.FEE_DELEGATED_VALUE_TRANSFER_WITH_RATIO);

        TxTypeFeeDelegatedValueTransferWithRatio senderTx
                = TxTypeFeeDelegatedValueTransferWithRatio.decodeFromRawTransaction(rawTx);
        KlayRawTransaction payerTx = new FeePayer(FEE_PAYER, 1).sign(senderTx);

        assertEquals("0x0af8d78204d219830f4240947b65b75d204abed71587c9e519a89277766ee1d00a94a94f5374fce5edbc8e2a8697c15331677e6ebf0b1ef845f84325a0dde32b8241f039a82b124fe94d3e556eb08f0d6f26d07dcc0f3fca621f1090caa01c8c336b358ab6d3a2bbf25de2adab4d01b754e2fb3b9b710069177d54c1e956945a0043070275d9f6054307ee7348bd660849d90ff845f84326a0091ecf53f91bb97bb694f2f2443f3563ac2b646d651497774524394aae396360a044228b88f275aa1ec1bab43681d21dc7e3a676786ed1906f6841d0a1a188f88a",
                payerTx.getValueAsString());
    }

    @Test
    public void testTxTypeValueTransferMemo() {
        TxTypeValueTransferMemo tx = TxTypeValueTransferMemo.createTransaction(
                BigInteger.valueOf(1234),
                BigInteger.valueOf(0x19),
                BigInteger.valueOf(0xf4240),
                "0x7b65B75d204aBed71587c9E519a89277766EE1d0",
                BigInteger.valueOf(0xa),
                LUMAN.getAddress(),
                Numeric.hexStringToByteArray("0x68656c6c6f"));

        String rawTx = tx.sign(LUMAN, 1).getValueAsString();
        TxType.Type type = KlayTransactionUtils.getType(rawTx);
        assertEquals(type, TxType.Type.VALUE_TRANSFER_MEMO);
        assertEquals("0x10f8808204d219830f4240947b65b75d204abed71587c9e519a89277766ee1d00a94a94f5374fce5edbc8e2a8697c15331677e6ebf0b8568656c6c6ff845f84325a07d2b0c89ee8afa502b3186413983bfe9a31c5776f4f820210cffe44a7d568d1ca02b1cbd587c73b0f54969f6b76ef2fd95cea0c1bb79256a75df9da696278509f3",
                rawTx);
    }

    @Test
    public void testTxTypeFeeDelegatedValueTransferMemo() {
        /**
         * Client Side
         */
        TxTypeFeeDelegatedValueTransferMemo tx = TxTypeFeeDelegatedValueTransferMemo.createTransaction(
                BigInteger.valueOf(1234),
                BigInteger.valueOf(0x19),
                BigInteger.valueOf(0xf4240),
                "0x7b65B75d204aBed71587c9E519a89277766EE1d0",
                BigInteger.valueOf(0xa),
                LUMAN.getAddress(),
                Numeric.hexStringToByteArray("0x68656c6c6f")
        );
        String rawTx = tx.sign(LUMAN, 1).getValueAsString();

        /**
         * Payer Side
         */
        TxType.Type type = KlayTransactionUtils.getType(rawTx);
        assertEquals(type, TxType.Type.FEE_DELEGATED_VALUE_TRANSFER_MEMO);

        TxTypeFeeDelegatedValueTransferMemo senderTx
                = TxTypeFeeDelegatedValueTransferMemo.decodeFromRawTransaction(rawTx);
        KlayRawTransaction payerTx = new FeePayer(FEE_PAYER, 1).sign(senderTx);

        assertEquals("0x11f8dc8204d219830f4240947b65b75d204abed71587c9e519a89277766ee1d00a94a94f5374fce5edbc8e2a8697c15331677e6ebf0b8568656c6c6ff845f84326a064e213aef0167fbd853f8f9989ef5d8b912a77457395ccf13d7f37009edd5c5ba05d0c2e55e4d8734fe2516ed56ac628b74c0eb02aa3b6eda51e1e25a1396093e1945a0043070275d9f6054307ee7348bd660849d90ff845f84326a087390ac14d3c34440b6ddb7b190d3ebde1a07d9a556e5a82ce7e501f24a060f9a037badbcb12cda1ed67b12b1831683a08a3adadee2ea760a07a46bdbb856fea44",
                payerTx.getValueAsString());
    }

    @Test
    public void testTxTypeFeeDelegatedValueTransferMemoWithRatio() {
        /**
         * Client Side
         */
        TxTypeFeeDelegatedValueTransferMemoWithRatio tx = TxTypeFeeDelegatedValueTransferMemoWithRatio.createTransaction(
                BigInteger.valueOf(1234),
                BigInteger.valueOf(0x19),
                BigInteger.valueOf(0xf4240),
                "0x7b65B75d204aBed71587c9E519a89277766EE1d0",
                BigInteger.valueOf(0xa),
                LUMAN.getAddress(),
                Numeric.hexStringToByteArray("0x68656c6c6f"),
                BigInteger.valueOf(30)
        );
        String rawTx = tx.sign(LUMAN, 1).getValueAsString();

        /**
         * Payer Side
         */
        TxType.Type type = KlayTransactionUtils.getType(rawTx);
        assertEquals(type, TxType.Type.FEE_DELEGATED_VALUE_TRANSFER_MEMO_WITH_RATIO);

        TxTypeFeeDelegatedValueTransferMemoWithRatio senderTx
                = TxTypeFeeDelegatedValueTransferMemoWithRatio.decodeFromRawTransaction(rawTx);
        KlayRawTransaction payerTx = new FeePayer(FEE_PAYER, 1).sign(senderTx);

        assertEquals("0x12f8dd8204d219830f4240947b65b75d204abed71587c9e519a89277766ee1d00a94a94f5374fce5edbc8e2a8697c15331677e6ebf0b8568656c6c6f1ef845f84326a0769f0afdc310289f9b24decb5bb765c8d7a87a6a4ae28edffb8b7085bbd9bc78a06a7b970eea026e60ac29bb52aee10661a4222e6bdcdfb3839a80586e584586b4945a0043070275d9f6054307ee7348bd660849d90ff845f84325a0c1c54bdc72ce7c08821329bf50542535fac74f4bba5de5b7881118a461d52834a03a3a64878d784f9af91c2e3ab9c90f17144c47cfd9951e3588c75063c0649ecd",
                payerTx.getValueAsString());
    }

    @Test
    @Ignore
    public void testTxTypeAccountCreation() {
        TxTypeAccountCreation tx = TxTypeAccountCreation.createTransaction(
                BigInteger.valueOf(1234),
                BigInteger.valueOf(0x19),
                BigInteger.valueOf(0xf4240),
                "0x7b65B75d204aBed71587c9E519a89277766EE1d0",
                BigInteger.valueOf(0xa),
                LUMAN.getAddress(),
                accountKey);

        String rawTx = tx.sign(LUMAN, 1).getValueAsString();
        TxType.Type type = KlayTransactionUtils.getType(rawTx);
        assertEquals(type, TxType.Type.ACCOUNT_CREATION);
        assertEquals("need test data", rawTx);
    }

    @Test
    @Ignore
    public void testTxTypeAccountUpdate() {
        TxTypeAccountUpdate tx = TxTypeAccountUpdate.createTransaction(
                BigInteger.valueOf(1234),
                BigInteger.valueOf(0x19),
                BigInteger.valueOf(0xf4240),
                LUMAN.getAddress(),
                accountKey);

        String rawTx = tx.sign(LUMAN, 1).getValueAsString();
        TxType.Type type = KlayTransactionUtils.getType(rawTx);
        assertEquals(type, TxType.Type.ACCOUNT_UPDATE);
        assertEquals("need test data", rawTx);
    }

    @Test
    @Ignore
    public void testTxTypeFeeDelegatedAccountUpdate() {
        /**
         * Client Side
         */
        TxTypeFeeDelegatedAccountUpdate tx = TxTypeFeeDelegatedAccountUpdate.createTransaction(
                BigInteger.valueOf(1234),
                BigInteger.valueOf(0x19),
                BigInteger.valueOf(0xf4240),
                LUMAN.getAddress(),
                accountKey);
        String rawTx = tx.sign(LUMAN, 1).getValueAsString();

        /**
         * Payer Side
         */
        TxType.Type type = KlayTransactionUtils.getType(rawTx);
        assertEquals(type, TxType.Type.FEE_DELEGATED_ACCOUNT_UPDATE);

        TxTypeFeeDelegatedAccountUpdate senderTx
                = TxTypeFeeDelegatedAccountUpdate.decodeFromRawTransaction(rawTx);

        KlayRawTransaction payerTx = new FeePayer(FEE_PAYER, 1).sign(senderTx);

        assertEquals("need test data", payerTx.getValueAsString());
    }

    @Test
    @Ignore
    public void testTxTypeFeeDelegatedAccountUpdateWithRatio() {
        /**
         * Client Side
         */
        TxTypeFeeDelegatedAccountUpdateWithRatio tx = TxTypeFeeDelegatedAccountUpdateWithRatio.createTransaction(
                BigInteger.valueOf(1234),
                BigInteger.valueOf(0x19),
                BigInteger.valueOf(0xf4240),
                LUMAN.getAddress(),
                accountKey,
                BigInteger.valueOf(30));
        String rawTx = tx.sign(LUMAN, 1).getValueAsString();

        /**
         * Payer Side
         */
        TxType.Type type = KlayTransactionUtils.getType(rawTx);
        assertEquals(type, TxType.Type.FEE_DELEGATED_ACCOUNT_UPDATE_WITH_RATIO);

        TxTypeFeeDelegatedAccountUpdateWithRatio senderTx
                = TxTypeFeeDelegatedAccountUpdateWithRatio.decodeFromRawTransaction(rawTx);
        KlayRawTransaction payerTx = new FeePayer(FEE_PAYER, 1).sign(senderTx);

        assertEquals("need test data", payerTx.getValueAsString());
    }

    @Test
    public void testTxTypeSmartContractDeploy() {
        TxTypeSmartContractDeploy tx = new TxTypeSmartContractDeploy(
                BigInteger.valueOf(1234),
                BigInteger.valueOf(0x19),
                BigInteger.valueOf(0xf4240),
                "0x7b65B75d204aBed71587c9E519a89277766EE1d0", // to
                BigInteger.valueOf(0xa), // value
                LUMAN.getAddress(), // from
                Numeric.hexStringToByteArray("0x608060405234801561001057600080fd5b506101de806100206000396000f3006080604052600436106100615763ffffffff7c01000000000000000000000000000000000000000000000000000000006000350416631a39d8ef81146100805780636353586b146100a757806370a08231146100ca578063fd6b7ef8146100f8575b3360009081526001602052604081208054349081019091558154019055005b34801561008c57600080fd5b5061009561010d565b60408051918252519081900360200190f35b6100c873ffffffffffffffffffffffffffffffffffffffff60043516610113565b005b3480156100d657600080fd5b5061009573ffffffffffffffffffffffffffffffffffffffff60043516610147565b34801561010457600080fd5b506100c8610159565b60005481565b73ffffffffffffffffffffffffffffffffffffffff1660009081526001602052604081208054349081019091558154019055565b60016020526000908152604090205481565b336000908152600160205260408120805490829055908111156101af57604051339082156108fc029083906000818181858888f193505050501561019c576101af565b3360009081526001602052604090208190555b505600a165627a7a72305820627ca46bb09478a015762806cc00c431230501118c7c26c30ac58c4e09e51c4f0029"),
                true,
                CodeFormat.EVM
        );

        String rawTx = tx.sign(LUMAN, 1).getValueAsString();
        TxType.Type type = KlayTransactionUtils.getType(rawTx);
        assertEquals(type, TxType.Type.SMART_CONTRACT_DEPLOY);
        assertEquals("0x28f9027d8204d219830f4240947b65b75d204abed71587c9e519a89277766ee1d00a94a94f5374fce5edbc8e2a8697c15331677e6ebf0bb901fe608060405234801561001057600080fd5b506101de806100206000396000f3006080604052600436106100615763ffffffff7c01000000000000000000000000000000000000000000000000000000006000350416631a39d8ef81146100805780636353586b146100a757806370a08231146100ca578063fd6b7ef8146100f8575b3360009081526001602052604081208054349081019091558154019055005b34801561008c57600080fd5b5061009561010d565b60408051918252519081900360200190f35b6100c873ffffffffffffffffffffffffffffffffffffffff60043516610113565b005b3480156100d657600080fd5b5061009573ffffffffffffffffffffffffffffffffffffffff60043516610147565b34801561010457600080fd5b506100c8610159565b60005481565b73ffffffffffffffffffffffffffffffffffffffff1660009081526001602052604081208054349081019091558154019055565b60016020526000908152604090205481565b336000908152600160205260408120805490829055908111156101af57604051339082156108fc029083906000818181858888f193505050501561019c576101af565b3360009081526001602052604090208190555b505600a165627a7a72305820627ca46bb09478a015762806cc00c431230501118c7c26c30ac58c4e09e51c4f00290180f845f84325a0fcd107738fb47750ba727610aefd6d5f51ac8163d62ce500e7ab7e15defe7088a0383d68220d0266490ea4173c1d7847f22fcbe22f8c8125e1c0589189845c902a",
                rawTx);
    }


    @Test
    public void testTxTypeFeeDelegatedSmartContractDeploy() {
        /**
         * Client Side
         */
        TxTypeFeeDelegatedSmartContractDeploy tx = new TxTypeFeeDelegatedSmartContractDeploy(
                BigInteger.valueOf(1234),
                BigInteger.valueOf(0x19),
                BigInteger.valueOf(0xf4240),
                "0x7b65B75d204aBed71587c9E519a89277766EE1d0", // to
                BigInteger.valueOf(0xa), // value
                LUMAN.getAddress(), // from
                Numeric.hexStringToByteArray("0x608060405234801561001057600080fd5b506101de806100206000396000f3006080604052600436106100615763ffffffff7c01000000000000000000000000000000000000000000000000000000006000350416631a39d8ef81146100805780636353586b146100a757806370a08231146100ca578063fd6b7ef8146100f8575b3360009081526001602052604081208054349081019091558154019055005b34801561008c57600080fd5b5061009561010d565b60408051918252519081900360200190f35b6100c873ffffffffffffffffffffffffffffffffffffffff60043516610113565b005b3480156100d657600080fd5b5061009573ffffffffffffffffffffffffffffffffffffffff60043516610147565b34801561010457600080fd5b506100c8610159565b60005481565b73ffffffffffffffffffffffffffffffffffffffff1660009081526001602052604081208054349081019091558154019055565b60016020526000908152604090205481565b336000908152600160205260408120805490829055908111156101af57604051339082156108fc029083906000818181858888f193505050501561019c576101af565b3360009081526001602052604090208190555b505600a165627a7a72305820627ca46bb09478a015762806cc00c431230501118c7c26c30ac58c4e09e51c4f0029"),
                true,
                CodeFormat.EVM
        );

        String rawTx = tx.sign(LUMAN, 1).getValueAsString();

        /**
         * Payer Side
         */
        TxType.Type type = KlayTransactionUtils.getType(rawTx);
        assertEquals(type, TxType.Type.FEE_DELEGATED_SMART_CONTRACT_DEPLOY);

        TxTypeFeeDelegatedSmartContractDeploy senderTx
                = TxTypeFeeDelegatedSmartContractDeploy.decodeFromRawTransaction(rawTx);
        KlayRawTransaction payerTx = new FeePayer(FEE_PAYER, 1).sign(senderTx);

        assertEquals("0x29f902d98204d219830f4240947b65b75d204abed71587c9e519a89277766ee1d00a94a94f5374fce5edbc8e2a8697c15331677e6ebf0bb901fe608060405234801561001057600080fd5b506101de806100206000396000f3006080604052600436106100615763ffffffff7c01000000000000000000000000000000000000000000000000000000006000350416631a39d8ef81146100805780636353586b146100a757806370a08231146100ca578063fd6b7ef8146100f8575b3360009081526001602052604081208054349081019091558154019055005b34801561008c57600080fd5b5061009561010d565b60408051918252519081900360200190f35b6100c873ffffffffffffffffffffffffffffffffffffffff60043516610113565b005b3480156100d657600080fd5b5061009573ffffffffffffffffffffffffffffffffffffffff60043516610147565b34801561010457600080fd5b506100c8610159565b60005481565b73ffffffffffffffffffffffffffffffffffffffff1660009081526001602052604081208054349081019091558154019055565b60016020526000908152604090205481565b336000908152600160205260408120805490829055908111156101af57604051339082156108fc029083906000818181858888f193505050501561019c576101af565b3360009081526001602052604090208190555b505600a165627a7a72305820627ca46bb09478a015762806cc00c431230501118c7c26c30ac58c4e09e51c4f00290180f845f84325a04ea37b8ecfed93795a9f99b1e4d554df6fb05a361965a7655abd4e4c4422a9e5a00b05e3fffe5a3c0892eaff31466f6c47b7edad80703d395d65bbfc1a2c6a2570945a0043070275d9f6054307ee7348bd660849d90ff845f84326a0c6738376304dfb32c77649bddd4ade925b947876cfe6b1fd2c06a2e4394504cca023817ba66a6b7c92fcf23f2d5506ea2a673aae5f1a1e4d742367971ae58a1576",
                payerTx.getValueAsString());
    }


    @Test
    public void testTxTypeFeeDelegatedSmartContractDeployWithRatio() {
        /**
         * Client Side
         */
        TxTypeFeeDelegatedSmartContractDeployWithRatio tx = new TxTypeFeeDelegatedSmartContractDeployWithRatio(
                BigInteger.valueOf(1234),
                BigInteger.valueOf(0x19),
                BigInteger.valueOf(0xf4240),
                "0x7b65B75d204aBed71587c9E519a89277766EE1d0", // to
                BigInteger.valueOf(0xa), // value
                LUMAN.getAddress(), // from
                Numeric.hexStringToByteArray("0x608060405234801561001057600080fd5b506101de806100206000396000f3006080604052600436106100615763ffffffff7c01000000000000000000000000000000000000000000000000000000006000350416631a39d8ef81146100805780636353586b146100a757806370a08231146100ca578063fd6b7ef8146100f8575b3360009081526001602052604081208054349081019091558154019055005b34801561008c57600080fd5b5061009561010d565b60408051918252519081900360200190f35b6100c873ffffffffffffffffffffffffffffffffffffffff60043516610113565b005b3480156100d657600080fd5b5061009573ffffffffffffffffffffffffffffffffffffffff60043516610147565b34801561010457600080fd5b506100c8610159565b60005481565b73ffffffffffffffffffffffffffffffffffffffff1660009081526001602052604081208054349081019091558154019055565b60016020526000908152604090205481565b336000908152600160205260408120805490829055908111156101af57604051339082156108fc029083906000818181858888f193505050501561019c576101af565b3360009081526001602052604090208190555b505600a165627a7a72305820627ca46bb09478a015762806cc00c431230501118c7c26c30ac58c4e09e51c4f0029"),
                true,
                BigInteger.valueOf(30),
                CodeFormat.EVM
        );

        String rawTx = tx.sign(LUMAN, 1).getValueAsString();

        /**
         * Payer Side
         */
        TxType.Type type = KlayTransactionUtils.getType(rawTx);
        assertEquals(type, TxType.Type.FEE_DELEGATED_SMART_CONTRACT_DEPLOY_WITH_RATIO);

        TxTypeFeeDelegatedSmartContractDeployWithRatio senderTx
                = TxTypeFeeDelegatedSmartContractDeployWithRatio.decodeFromRawTransaction(rawTx);
        KlayRawTransaction payerTx = new FeePayer(FEE_PAYER, 1).sign(senderTx);

        assertEquals("0x2af902da8204d219830f4240947b65b75d204abed71587c9e519a89277766ee1d00a94a94f5374fce5edbc8e2a8697c15331677e6ebf0bb901fe608060405234801561001057600080fd5b506101de806100206000396000f3006080604052600436106100615763ffffffff7c01000000000000000000000000000000000000000000000000000000006000350416631a39d8ef81146100805780636353586b146100a757806370a08231146100ca578063fd6b7ef8146100f8575b3360009081526001602052604081208054349081019091558154019055005b34801561008c57600080fd5b5061009561010d565b60408051918252519081900360200190f35b6100c873ffffffffffffffffffffffffffffffffffffffff60043516610113565b005b3480156100d657600080fd5b5061009573ffffffffffffffffffffffffffffffffffffffff60043516610147565b34801561010457600080fd5b506100c8610159565b60005481565b73ffffffffffffffffffffffffffffffffffffffff1660009081526001602052604081208054349081019091558154019055565b60016020526000908152604090205481565b336000908152600160205260408120805490829055908111156101af57604051339082156108fc029083906000818181858888f193505050501561019c576101af565b3360009081526001602052604090208190555b505600a165627a7a72305820627ca46bb09478a015762806cc00c431230501118c7c26c30ac58c4e09e51c4f0029011e80f845f84326a0cfe8dc29d31916b3f661a4774cb8d44d39ae700a9fb6ca04327f84bbe4de1486a01616e09ced403420cac1363d14e705b7a323518b1ce5124b16f06871c00ac424945a0043070275d9f6054307ee7348bd660849d90ff845f84325a0e29dae81defc027f059cd6a55ff74156b9c5bdb811460f09fc8d167c01aaaea1a04eba34d4d5ebbce60e4998f03b7a4658263bb21063ddf68ad3b088d670de47c8",
                payerTx.getValueAsString());
    }

    @Test
    public void testTxTypeSmartContractExecution() {
        TxTypeSmartContractExecution tx = TxTypeSmartContractExecution.createTransaction(
                BigInteger.valueOf(1234),
                BigInteger.valueOf(0x19),
                BigInteger.valueOf(0xf4240),
                "0x7b65B75d204aBed71587c9E519a89277766EE1d0", // to
                BigInteger.valueOf(0xa), // value
                LUMAN.getAddress(), // from
                Numeric.hexStringToByteArray("0x6353586b000000000000000000000000bc5951f055a85f41a3b62fd6f68ab7de76d299b2"));

        String rawTx = tx.sign(LUMAN, 1).getValueAsString();
        TxType.Type type = KlayTransactionUtils.getType(rawTx);
        assertEquals(type, TxType.Type.SMART_CONTRACT_EXECUTION);
        assertEquals("0x30f89f8204d219830f4240947b65b75d204abed71587c9e519a89277766ee1d00a94a94f5374fce5edbc8e2a8697c15331677e6ebf0ba46353586b000000000000000000000000bc5951f055a85f41a3b62fd6f68ab7de76d299b2f845f84326a0e4276df1a779274fbb04bc18a0184809eec1ce9770527cebb3d64f926dc1810ba04103b828a0671a48d64fe1a3879eae229699f05a684d9c5fd939015dcdd9709b",
                rawTx);
    }

    @Test
    public void testTxTypeFeeDelegatedSmartContractExecution() {
        /**
         * Client Side
         */
        TxTypeFeeDelegatedSmartContractExecution tx = TxTypeFeeDelegatedSmartContractExecution.createTransaction(
                BigInteger.valueOf(1234),
                BigInteger.valueOf(0x19),
                BigInteger.valueOf(0xf4240),
                "0x7b65B75d204aBed71587c9E519a89277766EE1d0", // to
                BigInteger.valueOf(0xa), // value
                LUMAN.getAddress(), // from
                Numeric.hexStringToByteArray("0x6353586b000000000000000000000000bc5951f055a85f41a3b62fd6f68ab7de76d299b2"));
        String rawTx = tx.sign(LUMAN, 1).getValueAsString();

        /**
         * Payer Side
         */
        TxType.Type type = KlayTransactionUtils.getType(rawTx);
        assertEquals(type, TxType.Type.FEE_DELEGATED_SMART_CONTRACT_EXECUTION);

        TxTypeFeeDelegatedSmartContractExecution senderTx
                = TxTypeFeeDelegatedSmartContractExecution.decodeFromRawTransaction(rawTx);
        KlayRawTransaction payerTx = new FeePayer(FEE_PAYER, 1).sign(senderTx);

        assertEquals("0x31f8fb8204d219830f4240947b65b75d204abed71587c9e519a89277766ee1d00a94a94f5374fce5edbc8e2a8697c15331677e6ebf0ba46353586b000000000000000000000000bc5951f055a85f41a3b62fd6f68ab7de76d299b2f845f84325a0253aea7d2c37160da45e84afbb45f6b3341cf1e8fc2df4ecc78f14adb512dc4fa022465b74015c2a8f8501186bb5e200e6ce44be52e9374615a7e7e21c41bc27b5945a0043070275d9f6054307ee7348bd660849d90ff845f84326a0e7c51db7b922c6fa2a941c9687884c593b1b13076bdf0c473538d826bf7b9d1aa05b0de2aabb84b66db8bf52d62f3d3b71b592e3748455630f1504c20073624d80",
                payerTx.getValueAsString());
    }

    @Test
    public void testTxTypeFeeDelegatedSmartContractExecutionWithRatio() {
        /**
         * Client Side
         */
        TxTypeFeeDelegatedSmartContractExecutionWithRatio tx = TxTypeFeeDelegatedSmartContractExecutionWithRatio.createTransaction(
                BigInteger.valueOf(1234),
                BigInteger.valueOf(0x19),
                BigInteger.valueOf(0xf4240),
                "0x7b65B75d204aBed71587c9E519a89277766EE1d0", // to
                BigInteger.valueOf(0xa), // value
                LUMAN.getAddress(), // from
                Numeric.hexStringToByteArray("0x6353586b000000000000000000000000bc5951f055a85f41a3b62fd6f68ab7de76d299b2"),
                BigInteger.valueOf(30));
        String rawTx = tx.sign(LUMAN, 1).getValueAsString();

        /**
         * Payer Side
         */
        TxType.Type type = KlayTransactionUtils.getType(rawTx);
        assertEquals(type, TxType.Type.FEE_DELEGATED_SMART_CONTRACT_EXECUTION_WITH_RATIO);

        TxTypeFeeDelegatedSmartContractExecutionWithRatio senderTx
                = TxTypeFeeDelegatedSmartContractExecutionWithRatio.decodeFromRawTransaction(rawTx);
        KlayRawTransaction payerTx = new FeePayer(FEE_PAYER, 1).sign(senderTx);

        assertEquals("0x32f8fc8204d219830f4240947b65b75d204abed71587c9e519a89277766ee1d00a94a94f5374fce5edbc8e2a8697c15331677e6ebf0ba46353586b000000000000000000000000bc5951f055a85f41a3b62fd6f68ab7de76d299b21ef845f84326a074ccfee18dc28932396b85617c53784ee366303bce39a2401d8eb602cf73766fa04c937a5ab9401d2cacb3f39ba8c29dbcd44588cc5c7d0b6b4113cfa7b7d9427b945a0043070275d9f6054307ee7348bd660849d90ff845f84325a04a4997524694d535976d7343c1e3a260f99ba53fcb5477e2b96216ec96ebb565a00f8cb31a35399d2b0fbbfa39f259c819a15370706c0449952c7cfc682d200d7c",
                payerTx.getValueAsString());
    }

    @Test
    public void testTxTypeCancel() {
        TxTypeCancel tx = TxTypeCancel.createTransaction(
                BigInteger.valueOf(1234),
                BigInteger.valueOf(0x19),
                BigInteger.valueOf(0xf4240),
                LUMAN.getAddress());

        String rawTx = tx.sign(LUMAN, 1).getValueAsString();
        TxType.Type type = KlayTransactionUtils.getType(rawTx);
        assertEquals(type, TxType.Type.CANCEL);
        assertEquals("0x38f8648204d219830f424094a94f5374fce5edbc8e2a8697c15331677e6ebf0bf845f84325a0fb2c3d53d2f6b7bb1deb5a09f80366a5a45429cc1e3956687b075a9dcad20434a05c6187822ee23b1001e9613d29a5d6002f990498d2902904f7f259ab3358216e",
                rawTx);
    }

    @Test
    public void testTxTypeFeeDelegatedCancel() {
        /**
         * Client Side
         */
        TxTypeFeeDelegatedCancel tx = TxTypeFeeDelegatedCancel.createTransaction(
                BigInteger.valueOf(1234),
                BigInteger.valueOf(0x19),
                BigInteger.valueOf(0xf4240),
                LUMAN.getAddress()
        );
        String rawTx = tx.sign(LUMAN, 1).getValueAsString();

        /**
         * Payer Side
         */
        TxType.Type type = KlayTransactionUtils.getType(rawTx);
        assertEquals(type, TxType.Type.FEE_DELEGATED_CANCEL);

        TxTypeFeeDelegatedCancel senderTx
                = TxTypeFeeDelegatedCancel.decodeFromRawTransaction(rawTx);
        KlayRawTransaction payerTx = new FeePayer(FEE_PAYER, 1).sign(senderTx);

        assertEquals("0x39f8c08204d219830f424094a94f5374fce5edbc8e2a8697c15331677e6ebf0bf845f84326a08409f5441d4725f90905ad87f03793857d124de7a43169bc67320cd2f020efa9a060af63e87bdc565d7f7de906916b2334336ee7b24d9a71c9521a67df02e7ec92945a0043070275d9f6054307ee7348bd660849d90ff845f84326a0044d5b25e8c649a1fdaa409dc3817be390ad90a17c25bc17c89b6d5d248495e0a073938e690d27b5267c73108352cf12d01de7fd0077b388e94721aa1fa32f85ec",
                payerTx.getValueAsString());
    }

    @Test
    public void testTxTypeFeeDelegatedCancelWithRatio() {
        /**
         * Client Side
         */
        TxTypeFeeDelegatedCancelWithRatio tx = TxTypeFeeDelegatedCancelWithRatio.createTransaction(
                BigInteger.valueOf(1234),
                BigInteger.valueOf(0x19),
                BigInteger.valueOf(0xf4240),
                LUMAN.getAddress(),
                BigInteger.valueOf(30));
        String rawTx = tx.sign(LUMAN, 1).getValueAsString();

        /**
         * Payer Side
         */
        TxType.Type type = KlayTransactionUtils.getType(rawTx);
        assertEquals(type, TxType.Type.FEE_DELEGATED_CANCEL_WITH_RATIO);

        TxTypeFeeDelegatedCancelWithRatio senderTx
                = TxTypeFeeDelegatedCancelWithRatio.decodeFromRawTransaction(rawTx);
        KlayRawTransaction payerTx = new FeePayer(FEE_PAYER, 1).sign(senderTx);

        assertEquals("0x3af8c18204d219830f424094a94f5374fce5edbc8e2a8697c15331677e6ebf0b1ef845f84326a072efa47960bef40b536c72d7e03ceaf6ca5f6061eb8a3eda3545b1a78fe52ef5a062006ddaf874da205f08b3789e2d014ae37794890fc2e575bf75201563a24ba9945a0043070275d9f6054307ee7348bd660849d90ff845f84326a06ba5ef20c3049323fc94defe14ca162e28b86aa64f7cf497ac8a5520e9615614a04a0a0fc61c10b416759af0ce4ce5c09ca1060141d56d958af77050c9564df6bf",
                payerTx.getValueAsString());
    }

    @Test
    @Ignore
    public void testTxTypeChainDataAnchoringTransaction() {
        KlayCredentials credentials = KlayCredentials.create(
                "0x91171fa75938c3c9938b389c632cd37ea47f63150816316251a6275e8bd244bc"
        );

        TxTypeChainDataAnchoringTransaction tx = TxTypeChainDataAnchoringTransaction.createTransaction(
                BigInteger.valueOf(19),
                new BigInteger("5d21dba00", 16),
                new BigInteger("174876e800", 16),
                credentials.getAddress(),
                Numeric.hexStringToByteArray("0xf8a6a00000000000000000000000000000000000000000000000000000000000000000a00000000000000000000000000000000000000000000000000000000000000001a00000000000000000000000000000000000000000000000000000000000000002a00000000000000000000000000000000000000000000000000000000000000003a0000000000000000000000000000000000000000000000000000000000000000405"));

        String rawTx = tx.sign(credentials, 1).getValueAsString();
        TxType.Type type = KlayTransactionUtils.getType(rawTx);

        TxTypeChainDataAnchoringTransaction encodedTx
                = TxTypeChainDataAnchoringTransaction.decodeFromRawTransaction(rawTx);

        assertEquals(Numeric.toHexString(tx.getAnchoredData()), Numeric.toHexString(encodedTx.getAnchoredData()));
        assertEquals(tx.getType(), encodedTx.getType());
        assertEquals(tx.getFrom(), encodedTx.getFrom());
        assertEquals(tx.getNonce(), encodedTx.getNonce());

        assertEquals(type, TxType.Type.CHAIN_DATA_ANCHROING);
        assertEquals("0x48f90113138505d21dba0085174876e8009466bc54224b4daae5a3b373f224f94a4c5e4d8281b8a8f8a6a00000000000000000000000000000000000000000000000000000000000000000a00000000000000000000000000000000000000000000000000000000000000001a00000000000000000000000000000000000000000000000000000000000000002a00000000000000000000000000000000000000000000000000000000000000003a0000000000000000000000000000000000000000000000000000000000000000405f845f84325a01f6f2a4e11f26505dce3f40261f8da5be36faacfa8731001a0bda3279d3d6755a0258e284cf05d8aab3aecacd02801a7e6db37003f3333c19f5a26c78e21cea552",
                rawTx);
    }

}
