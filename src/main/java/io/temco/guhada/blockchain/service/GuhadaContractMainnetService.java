package io.temco.guhada.blockchain.service;

import io.temco.guhada.blockchain.model.GuhadaTransact;
import io.temco.guhada.blockchain.model.request.GuhadaTransactRequest;

import java.io.IOException;

/**
 * Created by Shin Han
 * Since 2019-10-15
 */
public interface GuhadaContractMainnetService {    

    GuhadaTransact uploadToBlockchainFeeDelegationMainNet(GuhadaTransactRequest guhadaTransactRequest) throws IOException;
}
