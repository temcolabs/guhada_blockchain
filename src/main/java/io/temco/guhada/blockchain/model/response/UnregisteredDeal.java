package io.temco.guhada.blockchain.model.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * Created by Shin Han
 * Since 2019-10-18
 */
@Getter
@NoArgsConstructor
@ToString
public class UnregisteredDeal {
    private long dealId;
    private long productId;
    private String productName;
    private String seller;
    private BigDecimal price;
    private String brandName;
}
