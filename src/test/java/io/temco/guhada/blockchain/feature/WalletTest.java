package io.temco.guhada.blockchain.feature;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.caverj.wallet.KlayWalletUtils;
import org.caverj.wallet.Wallet;
import org.caverj.wallet.WalletFile;
import org.junit.Test;
import org.web3j.crypto.Keys;

import java.io.File;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static junit.framework.TestCase.assertTrue;

public class WalletTest {

    @Test
    public void testCreateHraWallet() throws Exception {
        String hra = "hra123.klaytn";

        WalletFile walletFile = Wallet.createStandard(
                "password",
                Keys.createEcKeyPair(),
                hra
        );

        String keyDirectory = KlayWalletUtils.getBaobabKeyDirectory();
        File destinationDir = new File(keyDirectory);
        if (!destinationDir.exists()) {
            if (!destinationDir.mkdirs()) {
                assertTrue("Unable to create destination directory [" + destinationDir.getAbsolutePath() + "], exiting...",
                        false);
            }
        }
        String fileName = getWalletFileName(walletFile);
        File destination = new File(keyDirectory, fileName);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        objectMapper.writeValue(destination, walletFile);

        // FIXME : Validate json file
        destination.delete();
    }

    @Test
    public void testCreateWallet() throws Exception {
        // Create Wallet File
        WalletFile walletFile = Wallet.createStandard(
                "password",
                Keys.createEcKeyPair()
        );

        String keyDirectory = KlayWalletUtils.getBaobabKeyDirectory();
        File destinationDir = new File(keyDirectory);
        if (!destinationDir.exists()) {
            if (!destinationDir.mkdirs()) {
                assertTrue("Unable to create destination directory [" + destinationDir.getAbsolutePath() + "], exiting...",
                        false);
            }
        }
        String fileName = getWalletFileName(walletFile);
        File destination = new File(keyDirectory, fileName);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        objectMapper.writeValue(destination, walletFile);

        // FIXME : Validate json file
        destination.delete();
    }

    private static String getWalletFileName(WalletFile walletFile) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern(
                "'UTC--'yyyy-MM-dd'T'HH-mm-ss.nVV'--'");
        ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
        if (walletFile.getAddressAsHumanReadableString() != null)
            if (!walletFile.getAddressAsHumanReadableString().isEmpty())
                return now.format(format) + walletFile.getAddressAsHumanReadableString() + ".json";

        return now.format(format) + walletFile.getAddress() + ".json";
    }
}
