package io.temco.guhada.blockchain.model.response;

import io.temco.guhada.blockchain.model.Transact;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TransactResponse {
    private long productId;
    private LocalDateTime transactTime;
    private String invoiceNumber;
    private String deliveryCode;
    private int temperature;
    private String hash;
    private String contractAddress;

    public static TransactResponse of(Transact transact){
        TransactResponse transactResponse = new TransactResponse();
        transactResponse.productId = transact.getProductId();
        transactResponse.transactTime = transact.getTransactTime();
        transactResponse.invoiceNumber = transact.getInvoiceNumber();
        transactResponse.deliveryCode = transact.getDeliveryCode();
        transactResponse.temperature = transact.getTemperature();
        transactResponse.hash = transact.getHash();
        transactResponse.contractAddress = transact.getContractAddress();
        return transactResponse;
    }
}
