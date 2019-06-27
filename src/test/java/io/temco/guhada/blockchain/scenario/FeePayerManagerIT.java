package io.temco.guhada.blockchain.scenario;

import org.caverj.tx.account.AccountKeyPublic;
import org.caverj.crpyto.KlayCredentials;
import org.caverj.fee.FeePayerManager;
import org.caverj.methods.response.KlayTransactionReceipt;
import org.caverj.tx.model.AccountCreationTransaction;
import org.caverj.tx.model.SmartContractDeployTransaction;
import org.caverj.tx.manager.PollingTransactionReceiptProcessor;
import org.caverj.tx.manager.TransactionManager;
import org.caverj.utils.CodeFormat;
import org.caverj.utils.Convert;
import org.caverj.wallet.KlayWalletUtils;
import org.junit.Test;
import org.web3j.crypto.Keys;
import org.web3j.utils.Numeric;

import java.io.File;
import java.math.BigInteger;

import static org.junit.Assert.assertEquals;

public class FeePayerManagerIT extends Scenario {

    static final String CONTRACT_INPUT_DATA = "0x60806040526000805534801561001457600080fd5b50610116806100246000396000f3006080604052600436106053576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806306661abd14605857806342cbb15c146080578063d14e62b81460a8575b600080fd5b348015606357600080fd5b50606a60d2565b6040518082815260200191505060405180910390f35b348015608b57600080fd5b50609260d8565b6040518082815260200191505060405180910390f35b34801560b357600080fd5b5060d06004803603810190808035906020019092919050505060e0565b005b60005481565b600043905090565b80600081905550505600a165627a7a7230582064856de85a2706463526593b08dd790054536042ef66d3204018e6790a2208d10029";
    static final String PASSWORD = "password";

    @Test
    public void testFeePayerManager() throws Exception {
        String keystoreFilePath = KlayWalletUtils.generateNewWalletFile(
                PASSWORD,
                new File(KlayWalletUtils.getBaobabKeyDirectory())
        );
        KlayCredentials credentials = KlayWalletUtils.loadCredentials(PASSWORD, keystoreFilePath);
        createAccount(credentials);

        KlayCredentials contractAddress = KlayCredentials.create(Keys.createEcKeyPair());
        SmartContractDeployTransaction smartContractDeploy = SmartContractDeployTransaction.create(
                credentials.getAddress(),
                contractAddress.getAddress(),
                BigInteger.ZERO,
                Numeric.hexStringToByteArray(CONTRACT_INPUT_DATA),
                GAS_LIMIT,
                CodeFormat.EVM
        );

        TransactionManager transactionManager = new TransactionManager.Builder(caverj, credentials).build();
        String senderTx = transactionManager.sign(smartContractDeploy, true).getValueAsString();

        FeePayerManager feePayerManager = new FeePayerManager.Builder(caverj, LUMAN)
                .setTransactionReceiptProcessor(new PollingTransactionReceiptProcessor(caverj, 1000, 15))
                .build();
        KlayTransactionReceipt.TransactionReceipt transactionReceipt = feePayerManager.executeTransaction(senderTx);
        assertEquals(CONTRACT_INPUT_DATA, transactionReceipt.getInput());
        assertEquals("0x1", transactionReceipt.getStatus());
    }

    private void createAccount(KlayCredentials credentials) {
        TransactionManager transactionManager = new TransactionManager.Builder(caverj, BRANDON)
                .setTransactionReceiptProcessor(new PollingTransactionReceiptProcessor(caverj, 1000, 15))
                .build();

        AccountCreationTransaction accountCreationTransaction = AccountCreationTransaction.create(
                BRANDON.getAddress(),
                credentials.getAddress(),
                Convert.toPeb("0.2", Convert.Unit.KLAY).toBigInteger(),
                GAS_LIMIT,
                AccountKeyPublic.create(credentials.getEcKeyPair().getPublicKey())
        );
        transactionManager.executeTransaction(accountCreationTransaction);
    }
}
