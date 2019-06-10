package io.temco.guhada.blockchain.model;

import io.temco.guhada.blockchain.model.request.GuhadaTransactRequest;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created by Shin Han
 * Since 2019-06-10
 */
@Entity
@Slf4j
@Data
@Table(name ="GUHADA_TRANSACT")
public class GuhadaTransact {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="guhada_transact_id")
    private int guhadaTransactId;

    @Column(name="product_id")
    private long productId;

    @Column(name="serial_id")
    private String serialId;

    @Column(name="brand_name")
    private String brandName;

    @Column(name="seller")
    private String seller;

    @Column(name="product_name")
    private String productName;

    @Column(name="price")
    private BigDecimal price;

    @Column(name="certificate_url")
    private String certificateUrl;

    @Column(name="certificate_name")
    private String certificateName;

    @Column(name="certificate_content_type")
    private String certificateContentType;

    @Column(name="hash")
    private String hash;

    @Column(name="contract_address")
    private String contractAddress;

    @Override
    public String toString(){
        return "Transact{" +
                "productId=" + productId +
                ", serialId=" + serialId +
                ", brandName=" + brandName +
                ", seller=" + seller +
                ", productName=" + productName +
                ", price='" + price +
                ", certificateUrl=" + certificateUrl +
                ", certificateName=" + certificateName +
                ", certificateContentType=" + certificateContentType +
                '}';
    }


    public static GuhadaTransact of(GuhadaTransactRequest guhadaTransactRequest){
        GuhadaTransact guhadaTransact = new GuhadaTransact();
        guhadaTransact.productId = guhadaTransactRequest.getProductId();
        guhadaTransact.serialId = guhadaTransactRequest.getSerialId();
        guhadaTransact.brandName = guhadaTransactRequest.getBrandName();
        guhadaTransact.seller = guhadaTransactRequest.getSeller();
        guhadaTransact.productName = guhadaTransactRequest.getProductName();
        guhadaTransact.price = guhadaTransactRequest.getPrice();
        guhadaTransact.certificateUrl = guhadaTransactRequest.getCertificateUrl();
        guhadaTransact.certificateName = guhadaTransactRequest.getCertificateName();
        guhadaTransact.certificateContentType = guhadaTransactRequest.getCertificateContentType();
        return guhadaTransact;
    }
}
