package io.temco.guhada.blockchain.model.request;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Data
public class TransactRequest {
    private long productId;
    private LocalDateTime transactTime;
    private String invoiceNumber;
    private String deliveryCode;
    private int temperature;
}
