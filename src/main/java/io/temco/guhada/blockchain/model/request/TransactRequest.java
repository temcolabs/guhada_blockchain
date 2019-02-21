package io.temco.guhada.blockchain.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.temco.guhada.blockchain.model.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Data
@ApiModel(value = "TransactRequest", description = "물류정보 추가시 parameter 모델", parent = BaseEntity.class)
public class TransactRequest {
    @ApiModelProperty(name = "productId", value = "상품을 등록하여 발급받은 ID값", required = true, example = "2321")
    private long productId;
    @ApiModelProperty(name = "invoiceNumber", value = "운송장 번호", required = true, example = "5522135782")
    private String invoiceNumber;
    @ApiModelProperty(name = "deliveryCode", value = "운송회사 코드", required = true, example = "04")
    private String deliveryCode;
    @ApiModelProperty(name = "temperature", value = "상품 취급시 온도", required = true, example = "20")
    private int temperature;
}
