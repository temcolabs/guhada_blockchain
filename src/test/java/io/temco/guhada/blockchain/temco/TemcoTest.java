package io.temco.guhada.blockchain.temco;

import com.klaytn.caver.Caver;
import org.junit.Test;

/**
 * Created by Shin Han
 * Since 2019-06-27
 */

public class TemcoTest {

    Caver caver  = Caver.build(Caver.BAOBAB_URL);

    @Test
    public void EcKeyTest() throws Exception {

        String email = "shin.han@temco.io";
        String userEmail = email.split("@")[0];
        String userEmailDomain = email.split("@")[1];
        String emailString = "**" + userEmail.substring(2) + "@***" + userEmailDomain.substring(3);

        System.out.println(emailString);

    }

//    @Test
//    public void valueTransferTest() throws Exception {
//
//        String privateKey = "0xb52673131d1f9c35b641a037682b1d0ebd6ad9f176aa41b45f60545a2f8b1a9a";
//
//        String contractAddress = "0x3dc22c969a7af9fc97f8f9feed1ab8d829be574f";
//        String chianUrl = "https://ropsten.infura.io/v3/67b3ebcb7a574122967fb41c7a968b90";
//
//        KlayCredentials credentials = KlayCredentials.create(privateKey);
//        KlayTransactionReceipt.TransactionReceipt transactionReceipt = ValueTransfer.sendFunds(
//                caver, credentials, "0xe97f27e9a5765ce36a7b919b1cb6004c7209217e",
//                BigDecimal.ONE, Convert.Unit.PEB, BigInteger.valueOf(100_000))
//                .send();
//
//        System.out.println(transactionReceipt);
//    }
//
//    @Test
//    public void feeDelegation() throws Exception {
//
//        String privateKey = "0xb52673131d1f9c35b641a037682b1d0ebd6ad9f176aa41b45f60545a2f8b1a9a";
//        KlayCredentials credentials = KlayCredentials.create(privateKey);
//
//        TransactionManager transactionManager = new TransactionManager.Builder(caver,credentials).build();
//        ValueTransferTransaction valueTransferTransaction = ValueTransferTransaction.create(
//                credentials.getAddress(),  // fromAddress
//                "0xe97f27e9a5765ce36a7b919b1cb6004c7209217e",  // toAddress
//                BigInteger.ONE,  // value
//                BigInteger.valueOf(100_000)  // gasLimit
//        );
//        String senderRawTransaction = transactionManager.sign(valueTransferTransaction, true).getValueAsString();  // isFeeDelegated : true
//    }
}
