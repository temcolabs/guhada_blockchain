//package io.temco.guhada.blockchain.model.response;
//
//import io.temco.guhada.blockchain.model.BaseEntity;
//import io.temco.guhada.blockchain.model.Product;
//import lombok.Getter;
//import lombok.Setter;
//
//import java.time.LocalDate;
//
//@Getter
//@Setter
//public class ProductResponse extends BaseEntity {
//    private String productName;
//    private String productCode;
//    private String category;
//    private String certificate;
//    private LocalDate regDate;
//    private LocalDate salesDate;
//    private String orderNumber;
//    private String boxSize;
//
//    public static ProductResponse of(Product product){
//        ProductResponse productResponse = new ProductResponse();
//        productResponse.productName = product.getProductName();
//        productResponse.productCode = product.getProductCode();
//        productResponse.category = product.getCategory();
//        productResponse.certificate = product.getCertificate();
//        productResponse.regDate = product.getRegDate();
//        productResponse.salesDate = product.getSalesDate();
//        productResponse.orderNumber = product.getOrderNumber();
//        productResponse.boxSize = product.getBoxSize();
//        return productResponse;
//    }
//}
