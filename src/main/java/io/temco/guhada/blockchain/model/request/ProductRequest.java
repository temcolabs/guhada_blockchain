package io.temco.guhada.blockchain.model.request;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Data
public class ProductRequest {
    private String productName;
    private long companyId;
    private String productCode;
    private String category;
    private String certificate;
    private LocalDateTime regDate;
    private LocalDateTime salesDate;
    private String orderNumber;
    private String boxSize;

}
