package io.temco.guhada.blockchain.model.response;

import io.temco.guhada.blockchain.model.BaseEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BlockChainInfo extends BaseEntity {

    private ProductResponse productResponse;
    private TransactResponse transactResponse;

    public BlockChainInfo(ProductResponse productResponse, TransactResponse transactResponse){
        this.productResponse = productResponse;
        this.transactResponse = transactResponse;
    }

}
