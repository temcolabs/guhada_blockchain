package io.temco.guhada.blockchain.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "BaseEntity", description = "상위 엔티티 정보")
public class BaseEntity {

    @ApiModelProperty(name = "message", value = "response message", required = true, example = "Good.")
    private String message;
}