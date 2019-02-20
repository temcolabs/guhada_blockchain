package io.temco.guhada.blockchain.model.response;

import io.temco.guhada.blockchain.model.Product;
import io.temco.guhada.blockchain.model.Transact;
import lombok.Data;

@Data
public class BlockChainInfo {

    private Product product;
    private Transact transact;

    public BlockChainInfo(Product product, Transact transact){
        this.product = product;
        this.transact = transact;
    }

}
