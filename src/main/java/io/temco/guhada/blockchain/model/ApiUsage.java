package io.temco.guhada.blockchain.model;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Builder
@Entity
@Table(name = "API_USAGE")
public class ApiUsage {
    @Id
    @Column(name = "API_USAGE_ID")
    private int apiUsageId;

    @Column(name="API_LIMIT_ID")
    private int apiLimitId;

    @Column(name="MONTH")
    private int month;

    @Column(name="DAY")
    private int day;

    @Column(name="USAGE_COUNT")
    private long usageCount;

}
