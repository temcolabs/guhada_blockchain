package io.temco.guhada.blockchain.model.request;

import io.temco.guhada.framework.model.benefit.enums.ServiceType;
import io.temco.guhada.framework.model.point.enums.PointType;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * Created by Shin Han
 * Since 2019-11-26
 */
@ToString
@Getter
@Builder
public class PointRequest {
    private Long chargePrice;
    private PointType pointType;
    private ServiceType serviceType;
}
