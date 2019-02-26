package io.temco.guhada.blockchain.model;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@Table(name="PRODUCT")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="PRODUCT_ID")
    private long productId;

    @Column(name="PRODUCT_NAME")
    private String productName;

    @Column(name="COMPANY_ID")
    private long companyId;

    @Column(name="PRODUCT_CODE")
    private String productCode;

    @Column(name="CATEGORY")
    private String category;

    @Column(name="CERTIFICATE")
    private String certificate;

    @Column(name="REG_DATE")
    private LocalDate regDate;

    @Column(name="SALES_DATE")
    private LocalDate salesDate;

    @Column(name="ORDER_NUMBER")
    private String orderNumber;

    @Column(name="BOX_SIZE")
    private String boxSize;

    @Column(name="QR_CODE_URL")
    private String qrCodeUrl;

}
