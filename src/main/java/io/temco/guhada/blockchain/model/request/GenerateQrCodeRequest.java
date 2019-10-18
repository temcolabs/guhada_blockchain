package io.temco.guhada.blockchain.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Setter
@Getter
@ApiModel(value = "GenerateQrCodeRequest", description = "상품 생성시 parameter 모델")
public class GenerateQrCodeRequest {
    @ApiModelProperty(name = "productName", value = "상품명", required = true, example = "소고기")
    private String productName;
    @ApiModelProperty(name = "productCode", value = "상품 코드", example = "cow-meat-44231")
    private String productCode;
    @ApiModelProperty(name = "category", value = "상품 종류", example = "Meat")
    private String category;
    @ApiModelProperty(name = "certificate", value = "상품의 상태", example = "organic")
    private String certificate;
    @ApiModelProperty(name = "regDate", value = "상품 등록일자",example = "2019-02-03")
    private LocalDate regDate;
    @ApiModelProperty(name = "salesDate", value = "상품 판매일자", example = "2019-03-02")
    private LocalDate salesDate;
    @ApiModelProperty(name = "orderNumber", value = "상품의 주문번호", example = "order-2311")
    private String orderNumber;
    @ApiModelProperty(name = "boxSize", value = "박스 크기", example = "small")
    private String boxSize;
    @ApiModelProperty(name = "invoiceNumber", value = "운송장 번호", required = true, example = "5522135782")
    private String invoiceNumber;
    @ApiModelProperty(name = "deliveryCode", value = "운송회사 코드", required = true, example = "04")
    private String deliveryCode;
    @ApiModelProperty(name = "temperature", value = "상품 취급시 온도", required = true, example = "20")
    private int temperature;
    @ApiModelProperty(name = "longitude", value = "경도", required = true, example = "127.027666")
    private double longitude;
    @ApiModelProperty(name = "latitude", value = "위도", required = true, example = "37.497904")
    private double latitude;

}
