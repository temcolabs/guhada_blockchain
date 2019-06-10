package io.temco.guhada.blockchain.model.response;

import io.temco.guhada.blockchain.model.GuhadaTransact;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by Shin Han
 * Since 2019-06-10
 */
@Data
public class GuhadaTransactResponse {
    private String serialId;
    private String brandName;
    private String seller;
    private String productName;
    private BigDecimal price;
    private String certificateUrl;
    private String certificateName;
    private String certificateContentType;
    private String hash;
    private String contractAddress;


    public static GuhadaTransactResponse of(GuhadaTransact guhadaTransact){
        GuhadaTransactResponse guhadaTransactResponse = new GuhadaTransactResponse();
        guhadaTransactResponse.serialId = guhadaTransact.getSerialId();
        guhadaTransactResponse.brandName = guhadaTransact.getBrandName();
        guhadaTransactResponse.seller = guhadaTransact.getSeller();
        guhadaTransactResponse.productName = guhadaTransact.getProductName();
        guhadaTransactResponse.price = guhadaTransact.getPrice();
        guhadaTransactResponse.certificateUrl = guhadaTransact.getCertificateUrl();
        guhadaTransactResponse.certificateName = guhadaTransact.getCertificateName();
        guhadaTransactResponse.certificateContentType = guhadaTransact.getCertificateContentType();
        guhadaTransactResponse.hash = guhadaTransact.getHash();
        guhadaTransactResponse.contractAddress = guhadaTransact.getContractAddress();
        return guhadaTransactResponse;
    }
}
