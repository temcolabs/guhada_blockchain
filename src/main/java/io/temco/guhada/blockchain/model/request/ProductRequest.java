package io.temco.guhada.blockchain.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.temco.guhada.blockchain.model.BaseEntity;
import lombok.Data;

import java.time.LocalDate;

@Data
@ApiModel(value = "ProductRequest", description = "상품 생성시 parameter 모델", parent = BaseEntity.class)
public class ProductRequest extends BaseEntity{
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

}
