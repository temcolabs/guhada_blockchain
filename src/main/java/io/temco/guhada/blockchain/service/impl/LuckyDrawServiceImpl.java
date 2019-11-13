package io.temco.guhada.blockchain.service.impl;

import com.klaytn.caver.Caver;
import com.klaytn.caver.crypto.KlayCredentials;
import com.klaytn.caver.utils.ChainId;
import io.temco.guhada.blockchain.model.LuckyDrawModel;
import io.temco.guhada.blockchain.service.LuckyDrawService;
import io.temco.guhada.blockchain.smartcontract.LuckyDraw;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.web3j.tx.gas.DefaultGasProvider;

import javax.annotation.PostConstruct;
import java.math.BigInteger;
import java.security.GeneralSecurityException;

/**
 * Created by Shin Han
 * Since 2019-11-13
 */
@Service
@Slf4j
public class LuckyDrawServiceImpl implements LuckyDrawService {

    private final String data ="608060405234801561001057600080fd5b50610c9e806100206000396000f3fe608060405234801561001057600080fd5b50600436106100575760003560e01c80633b3041471461005c5780633c0202f014610103578063ac3a5e9314610269578063cf1ab1af146103ab578063fe85f40a14610452575b600080fd5b6100886004803603602081101561007257600080fd5b8101908080359060200190929190505050610503565b6040518080602001828103825283818151815260200191508051906020019080838360005b838110156100c85780820151818401526020810190506100ad565b50505050905090810190601f1680156100f55780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b6102676004803603608081101561011957600080fd5b810190808035906020019064010000000081111561013657600080fd5b82018360208201111561014857600080fd5b8035906020019184600183028401116401000000008311171561016a57600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f8201169050808301925050505050505091929192908035906020019092919080359060200190929190803590602001906401000000008111156101e157600080fd5b8201836020820111156101f357600080fd5b8035906020019184600183028401116401000000008311171561021557600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f82011690508083019250505050505050919291929050505061058f565b005b6103226004803603602081101561027f57600080fd5b810190808035906020019064010000000081111561029c57600080fd5b8201836020820111156102ae57600080fd5b803590602001918460018302840111640100000000831117156102d057600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f8201169050808301925050505050505091929192905050506106c6565b6040518084815260200183815260200180602001828103825283818151815260200191508051906020019080838360005b8381101561036e578082015181840152602081019050610353565b50505050905090810190601f16801561039b5780820380516001836020036101000a031916815260200191505b5094505050505060405180910390f35b6103d7600480360360208110156103c157600080fd5b810190808035906020019092919050505061079e565b6040518080602001828103825283818151815260200191508051906020019080838360005b838110156104175780820151818401526020810190506103fc565b50505050905090810190601f1680156104445780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b6104886004803603604081101561046857600080fd5b81019080803590602001909291908035906020019092919050505061084e565b6040518080602001828103825283818151815260200191508051906020019080838360005b838110156104c85780820151818401526020810190506104ad565b50505050905090810190601f1680156104f55780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b6060600060016000848152602001908152602001600020805490509050600061052b82610914565b905060016000858152602001908152602001600020818154811061054b57fe5b9060005260206000200160026000868152602001908152602001600020908054600181600116156101000203166002900461058792919061095b565b505050919050565b6040518060600160405280848152602001838152602001828152506000856040518082805190602001908083835b602083106105e057805182526020820191506020810190506020830392506105bd565b6001836020036101000a0380198251168184511680821785525050505050509050019150509081526020016040518091039020600082015181600001556020820151816001015560408201518160020190805190602001906106439291906109e2565b5090505060006001600085815260200190815260200160002090508085908060018154018082558091505090600182039060005260206000200160009091929091909150908051906020019061069a929190610a62565b505080600160008681526020019081526020016000209080546106be929190610ae2565b505050505050565b600081805160208101820180518482526020830160208501208183528095505050505050600091509050806000015490806001015490806002018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156107945780601f1061076957610100808354040283529160200191610794565b820191906000526020600020905b81548152906001019060200180831161077757829003601f168201915b5050505050905083565b60026020528060005260406000206000915090508054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156108465780601f1061081b57610100808354040283529160200191610846565b820191906000526020600020905b81548152906001019060200180831161082957829003601f168201915b505050505081565b6001602052816000526040600020818154811061086757fe5b90600052602060002001600091509150508054600181600116156101000203166002900480601f01602080910402602001604051908101604052809291908181526020018280546001816001161561010002031660029004801561090c5780601f106108e15761010080835404028352916020019161090c565b820191906000526020600020905b8154815290600101906020018083116108ef57829003601f168201915b505050505081565b600081424460405160200180838152602001828152602001925050506040516020818303038152906040528051906020012060001c8161095057fe5b0660ff169050919050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061099457805485556109d1565b828001600101855582156109d157600052602060002091601f016020900482015b828111156109d05782548255916001019190600101906109b5565b5b5090506109de9190610b52565b5090565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f10610a2357805160ff1916838001178555610a51565b82800160010185558215610a51579182015b82811115610a50578251825591602001919060010190610a35565b5b509050610a5e9190610b52565b5090565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f10610aa357805160ff1916838001178555610ad1565b82800160010185558215610ad1579182015b82811115610ad0578251825591602001919060010190610ab5565b5b509050610ade9190610b52565b5090565b828054828255906000526020600020908101928215610b415760005260206000209182015b82811115610b405782829080546001816001161561010002031660029004610b30929190610b77565b5091600101919060010190610b07565b5b509050610b4e9190610bfe565b5090565b610b7491905b80821115610b70576000816000905550600101610b58565b5090565b90565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f10610bb05780548555610bed565b82800160010185558215610bed57600052602060002091601f016020900482015b82811115610bec578254825591600101919060010190610bd1565b5b509050610bfa9190610b52565b5090565b610c2791905b80821115610c235760008181610c1a9190610c2a565b50600101610c04565b5090565b90565b50805460018160011615610100020316600290046000825580601f10610c505750610c6f565b601f016020900490600052602060002090810190610c6e9190610b52565b5b5056fea165627a7a72305820aaaa0dbf1a77639dc1968ab220ff3a1b792a78dcaca0c1ff152cf5ef14a6c3700029";
    private String privateKey = "";
    private String PublicKey = "";
    private String contractAddress = "";

    private Caver caver;
    private LuckyDraw luckyDraw;
    KlayCredentials credentials = KlayCredentials.create(privateKey);


    @PostConstruct
    private void initCaverJava() throws java.io.IOException, GeneralSecurityException {

        caver = Caver.build(Caver.BAOBAB_URL);
        log.info(caver.klay().getGasPrice().send().toString());

        luckyDraw = luckyDraw.load(contractAddress, caver, credentials, ChainId.BAOBAB_TESTNET, new DefaultGasProvider());

    }


    @Override
    public void entry(LuckyDrawModel luckyDrawRequest) throws Exception {
        String eventId = luckyDrawRequest.getDealId() + "_" + luckyDrawRequest.getUserId();
        luckyDraw.entry(eventId,
                BigInteger.valueOf(luckyDrawRequest.getDealId()),
                BigInteger.valueOf(luckyDrawRequest.getUserId()),
                "**" + luckyDrawRequest.getUserEmail().substring(2,luckyDrawRequest.getUserEmail().length()))
        .send();
    }

    @Override
    public void draw(long dealId) throws Exception {
        luckyDraw.draw(BigInteger.valueOf(dealId)).send();
    }

    @Override
    public LuckyDrawModel getLuckyDrawEntrys(long dealId) {
//        luckyDraw.luckyDrawEntrys()
        return null;
    }

    @Override
    public LuckyDrawModel getLuckyDrawWinner(long dealId) throws Exception {
        String response = luckyDraw.luckyDrawWinner(BigInteger.valueOf(dealId)).send();
        log.error(response);
        return null;
    }
}
