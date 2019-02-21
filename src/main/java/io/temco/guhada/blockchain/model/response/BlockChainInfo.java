package io.temco.guhada.blockchain.model.response;

import lombok.Data;

@Data
public class BlockChainInfo {

    private ProductResponse productResponse;
    private TransactResponse transactResponse;

    public BlockChainInfo(ProductResponse productResponse, TransactResponse transactResponse){
        this.productResponse = productResponse;
        this.transactResponse = transactResponse;
    }

}
