//package io.temco.guhada.blockchain.model;
//
//import lombok.Data;
//
//import javax.persistence.*;
//import java.time.LocalDateTime;
//
//@Data
//@Entity
//@Table(name="TRANSACT")
//public class Transact {
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    @Column(name="TRANSACT_ID")
//    private long transactId;
//
//    @Column(name="PRODUCT_ID")
//    private long productId;
//
//    @Column(name="COMPANY_ID")
//    private long companyId;
//
//    @Column(name="TRANSACT_TIME")
//    private LocalDateTime transactTime;
//
//    @Column(name="INVOICE_NUMBER")
//    private String invoiceNumber;
//
//    @Column(name="DELIVERY_CODE")
//    private String deliveryCode;
//
//    @Column(name="TEMPERATURE")
//    private int temperature;
//
//    @Column(name="HASH")
//    private String hash;
//
//    @Column(name="CONTRACT_ADDRESS")
//    private String contractAddress;
//
//    @Column(name="LONGITUDE")
//    private double longitude;
//
//    @Column(name="LATITUDE")
//    private double latitude;
//    // pdf 추후
//
//    @Override
//    public String toString(){
//        return "Transact{" +
//                "transactId=" + transactId +
//                ", productId=" + productId +
//                ", companyId=" + companyId +
//                ", transactTime=" + transactTime +
//                ", invoiceNumber='" + invoiceNumber +
//                ", temperature=" + temperature +
//                ", deliveryCode=" + deliveryCode +
//                ", longitude=" + longitude +
//                ", latitude=" + latitude +
//                '}';
//    }
//}
