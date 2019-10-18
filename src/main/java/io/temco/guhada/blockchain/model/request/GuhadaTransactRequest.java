package io.temco.guhada.blockchain.model.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Created by Shin Han
 * Since 2019-06-10
 */
@Setter
@Getter
public class GuhadaTransactRequest {

    private long productId;

    private long dealId;

    private String serialId;

    private String brandName;

    private String seller;

    private String productName;

    private BigDecimal price;

    private String certificateUrl;

    private String certificateName;

    private String certificateContentType;
}
