package io.temco.guhada.blockchain.scenario;

import org.caverj.tx.account.AccountKeyPublic;
import org.caverj.crpyto.KlayCredentials;
import org.caverj.fee.FeePayerManager;
import org.caverj.methods.request.CallObject;
import org.caverj.methods.response.Bytes;
import org.caverj.methods.response.Bytes32;
import org.caverj.tx.manager.PollingTransactionReceiptProcessor;
import org.caverj.tx.model.KlayRawTransaction;
import org.caverj.tx.type.TxTypeAccountCreation;
import org.caverj.tx.type.TxTypeFeeDelegatedSmartContractDeployWithRatio;
import org.caverj.tx.type.TxTypeSmartContractExecution;
import org.caverj.utils.CodeFormat;
import org.caverj.utils.Convert;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.web3j.crypto.Hash;
import org.web3j.crypto.Keys;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.slf4j.LoggerFactory.getLogger;

public class HumanReadableIT extends Scenario {

    private static final Logger log = getLogger(HumanReadableIT.class);
    static final int CHANGE_VALUE = 27;
    static final String CONTRACT_INPUT_DATA = "0x60806040526000805534801561001457600080fd5b50610116806100246000396000f3006080604052600436106053576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806306661abd14605857806342cbb15c146080578063d14e62b81460a8575b600080fd5b348015606357600080fd5b50606a60d2565b6040518082815260200191505060405180910390f35b348015608b57600080fd5b50609260d8565b6040518082815260200191505060405180910390f35b34801560b357600080fd5b5060d06004803603810190808035906020019092919050505060e0565b005b60005481565b600043905090565b80600081905550505600a165627a7a7230582064856de85a2706463526593b08dd790054536042ef66d3204018e6790a2208d10029";

    @Test
    @Ignore
    public void testHumanReadableIT() throws Exception {

        String humandReadableAddress = "human" + new Random().nextInt(1000) + ".klaytn";
        KlayCredentials credentials = KlayCredentials.create(Keys.createEcKeyPair());
        TxTypeAccountCreation accountCreation = TxTypeAccountCreation.createTransaction(
                getNonce(BRANDON.getAddress()),
                GAS_PRICE,
                GAS_LIMIT,
                humandReadableAddress,
                Convert.toPeb("0.2", Convert.Unit.KLAY).toBigInteger(),
                BRANDON.getAddress(),
                AccountKeyPublic.create(credentials.getEcKeyPair().getPublicKey())
        );
        KlayRawTransaction signedAccountCreation = accountCreation.sign(BRANDON, BAOBAB_CHAIN_ID);
        sendTxAndGetReceipt(signedAccountCreation.getValueAsString());

        // Sender Client
        String contractAddress = "contract" + new Random().nextInt(10000);
        log.info("contractAddress : {}", contractAddress);
        TxTypeFeeDelegatedSmartContractDeployWithRatio contractDeploy = TxTypeFeeDelegatedSmartContractDeployWithRatio.createTransaction(
                getNonce(humandReadableAddress),
                GAS_PRICE,
                GAS_LIMIT,
                contractAddress,
                BigInteger.ZERO,
                humandReadableAddress,
                Numeric.hexStringToByteArray(CONTRACT_INPUT_DATA),
                BigInteger.valueOf(33),
                CodeFormat.EVM
        );
        KlayRawTransaction signedContractDeploy = contractDeploy.sign(credentials, BAOBAB_CHAIN_ID);
        String rawSignedContractDeploy = signedContractDeploy.getValueAsString();

        // FeePayer Client
        FeePayerManager feePayerManager = new FeePayerManager.Builder(this.caverj, FEE_PAYER)
                .setTransactionReceiptProcessor(new PollingTransactionReceiptProcessor(caverj, 1000, 15))
                .build();
        sendTxAndGetReceipt(feePayerManager.sign(rawSignedContractDeploy).getValueAsString());

        TxTypeSmartContractExecution typeSmartContractExecution = TxTypeSmartContractExecution.createTransaction(
                getNonce(humandReadableAddress),
                GAS_PRICE,
                GAS_LIMIT,
                contractAddress,
                BigInteger.ZERO,
                humandReadableAddress,
                getChangePayload()
        );

        KlayRawTransaction signedExecutionTx = typeSmartContractExecution.sign(credentials, BAOBAB_CHAIN_ID);
        Bytes32 contractExecutionHash = caverj.klay().sendSignedTransaction(signedExecutionTx.getValueAsString()).send();
        waitForTransactionReceipt(contractExecutionHash.getResult());

        CallObject callObject = new CallObject(
                humandReadableAddress,
                contractAddress,
                GAS_LIMIT,
                GAS_PRICE,
                BigInteger.ZERO,
                Numeric.prependHexPrefix(Hash.sha3String("count()").substring(2, 10)));
        Bytes changedValue = caverj.klay().call(callObject, DefaultBlockParameterName.LATEST).send();
        assertEquals(CHANGE_VALUE, Numeric.toBigInt(changedValue.getResult()).intValue());
    }

    private byte[] getChangePayload() {
        String setCommand = "setCount(uint256)";
        BigInteger replaceValue = BigInteger.valueOf(CHANGE_VALUE);
        String payLoadNoCommand = Numeric.toHexStringNoPrefix(Numeric.toBytesPadded(replaceValue, 32));
        String payLoad = new StringBuilder(Hash.sha3String(setCommand)
                .substring(2, 10))
                .append(payLoadNoCommand)
                .toString();
        return Numeric.hexStringToByteArray(payLoad);
    }

}
