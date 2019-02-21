package io.temco.guhada.blockchain.model.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ProductRequest {
    private String productName;
    private String productCode;
    private String category;
    private String certificate;
    private LocalDate regDate;
    private LocalDate salesDate;
    private String orderNumber;
    private String boxSize;

}
