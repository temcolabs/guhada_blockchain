package io.temco.guhada.blockchain.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ApiModel(value = "TransactRequest", description = "물류정보 추가시 parameter 모델")
public class TransactRequest {
    @ApiModelProperty(name = "productId", value = "상품을 등록하여 발급받은 ID값", required = true, example = "2321")
    private long productId;
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

    public static TransactRequest of(long productId, GenerateQrCodeRequest generateQrCodeRequest){
        TransactRequest transactRequest = new TransactRequest();
        transactRequest.productId = productId;
        transactRequest.invoiceNumber = generateQrCodeRequest.getInvoiceNumber();
        transactRequest.deliveryCode = generateQrCodeRequest.getDeliveryCode();
        transactRequest.temperature = generateQrCodeRequest.getTemperature();
        transactRequest.longitude = generateQrCodeRequest.getLongitude();
        transactRequest.latitude = generateQrCodeRequest.getLatitude();
        return transactRequest;
    }
}