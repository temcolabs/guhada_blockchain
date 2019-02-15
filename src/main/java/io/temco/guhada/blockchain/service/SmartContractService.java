package io.temco.guhada.blockchain.service;

import io.temco.guhada.blockchain.model.Goods;

public interface SmartContractService {

    String insert(Goods goods) throws Exception;
}
