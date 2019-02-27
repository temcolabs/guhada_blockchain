package io.temco.guhada.blockchain.model.request;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ApiModel(value = "CompanyRequest", description = "회사 생성 parameter")
public class CompanyRequest {
    @ApiModelProperty(name = "companyName", value = "회사명", required = true, example = "템코네트웍스")
    private String companyName;
    @ApiModelProperty(name = "category", value = "회사분류(factory,warehouse,transportation,store)", required = true, example = "factory")
    private String category;
}
