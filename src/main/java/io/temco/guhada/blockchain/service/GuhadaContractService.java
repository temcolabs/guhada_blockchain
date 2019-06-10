package io.temco.guhada.blockchain.service;

import io.temco.guhada.blockchain.model.GuhadaTransact;
import io.temco.guhada.blockchain.model.request.GuhadaTransactRequest;
import io.temco.guhada.blockchain.model.response.GuhadaTransactResponse;

import java.util.List;

public interface GuhadaContractService {
    GuhadaTransact uploadToBlockchain(GuhadaTransactRequest guhadaTransactRequest) throws Exception;

    List<GuhadaTransactResponse> getTransactData(long productId);
}
