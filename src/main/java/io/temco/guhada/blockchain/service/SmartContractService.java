package io.temco.guhada.blockchain.service;

import io.temco.guhada.blockchain.model.request.ProductRequest;
import io.temco.guhada.blockchain.model.request.TransactRequest;
import io.temco.guhada.blockchain.model.response.BlockChainInfo;

import java.io.IOException;

public interface SmartContractService {
    String generateQrCode(String apiToken,ProductRequest blockChainInfo) throws Exception;

    String uploadToBlockchain(String apiToken,TransactRequest transactRequest) throws Exception;

    BlockChainInfo getBlockChainInfo(String apiToken,String hashId) throws Exception;
}
