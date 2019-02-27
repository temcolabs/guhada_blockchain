package io.temco.guhada.blockchain.model.response;

import io.temco.guhada.blockchain.model.BaseEntity;
import io.temco.guhada.blockchain.model.Transact;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TransactResponse extends BaseEntity {
    private long productId;
    private LocalDateTime transactTime;
    private String invoiceNumber;
    private String deliveryCode;
    private int temperature;
    private String hash;
    private String contractAddress;
    private double longitude;
    private double latitude;

    public static TransactResponse of(Transact transact){
        TransactResponse transactResponse = new TransactResponse();
        transactResponse.productId = transact.getProductId();
        transactResponse.transactTime = transact.getTransactTime();
        transactResponse.invoiceNumber = transact.getInvoiceNumber();
        transactResponse.deliveryCode = transact.getDeliveryCode();
        transactResponse.temperature = transact.getTemperature();
        transactResponse.hash = transact.getHash();
        transactResponse.contractAddress = transact.getContractAddress();
        transactResponse.longitude = transact.getLongitude();
        transactResponse.latitude = transact.getLatitude();
        return transactResponse;
    }
}
