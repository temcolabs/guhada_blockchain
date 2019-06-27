package io.temco.guhada.blockchain.feature;

import org.caverj.crpyto.KlayCredentials;
import org.caverj.wallet.KlayWalletUtils;
import org.junit.Test;

import java.io.File;

import static junit.framework.TestCase.assertEquals;

public class KlayWalletUtilsTest {

    @Test
    public void testGenerateNewWalletAndLoad() throws Exception {
        String keystoreFilePath = KlayWalletUtils.generateNewWalletFile(
                "password",
                new File(KlayWalletUtils.getBaobabKeyDirectory())
        );

        KlayCredentials credentials = KlayWalletUtils.loadCredentials("password", keystoreFilePath);

        new File(keystoreFilePath).delete();
    }

    @Test
    public void testGenerateFullNewWalletAndLoad() throws Exception {
        String humanReadableAddress = "brandon.klaytn";
        String keystoreFilePath = KlayWalletUtils.generateFullNewWalletFile(
                humanReadableAddress,
                "password",
                new File(KlayWalletUtils.getBaobabKeyDirectory())
        );

        KlayCredentials credentials = KlayWalletUtils.loadCredentials("password", keystoreFilePath);
        assertEquals(humanReadableAddress, credentials.getAddress());

        new File(keystoreFilePath).delete();
    }

    @Test
    public void testHraGenerateAndLoad() throws Exception {
        String customAddress = "hra01.klaytn";
        String keystoreFilePath = KlayWalletUtils.generateNewWalletFile(
                customAddress,
                "password",
                new File(KlayWalletUtils.getBaobabKeyDirectory())
        );

        KlayCredentials credentials = KlayWalletUtils.loadCredentials("password", keystoreFilePath);
        assertEquals(customAddress, credentials.getAddress());

        new File(keystoreFilePath).delete();
    }

    @Test
    public void testCreateKlayCredentialsWithHumanReadable() {
        String humanReadableAddress = "hello.klaytn";
        KlayCredentials credentials = KlayCredentials.create("0xd0fdd0999ca4dac89be8658c8a9a51a719c6f03060649bc39a3ac335b837a9b1", humanReadableAddress);
        assertEquals(credentials.getAddress(), humanReadableAddress);
    }

    @Test
    public void testCreateKlayCredentials() {
        KlayCredentials credentials = KlayCredentials.create("0xd0fdd0999ca4dac89be8658c8a9a51a719c6f03060649bc39a3ac335b837a9b1");
        assertEquals(credentials.getAddress(), "0x6f19c2f3c9694612a5c3e9f4341c243a26687110");
    }

}
