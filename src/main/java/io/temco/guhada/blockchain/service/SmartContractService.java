package io.temco.guhada.blockchain.service;

import io.temco.guhada.blockchain.model.request.CompanyRequest;
import io.temco.guhada.blockchain.model.request.GenerateQrCodeRequest;
import io.temco.guhada.blockchain.model.request.TransactRequest;
import io.temco.guhada.blockchain.model.response.BlockChainInfo;
import io.temco.guhada.blockchain.model.response.CompanyResponse;
import io.temco.guhada.blockchain.model.response.GenerateQrCodeResponse;

public interface SmartContractService {
    GenerateQrCodeResponse generateQrCode(String apiToken, GenerateQrCodeRequest blockChainInfo) throws Exception;

    String uploadToBlockchain(String apiToken,TransactRequest transactRequest) throws Exception;

    BlockChainInfo getBlockChainInfo(String apiToken,String hashId) throws Exception;

    CompanyResponse registerCompany(CompanyRequest companyRequest);
}
