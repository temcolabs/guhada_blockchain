package io.temco.guhada.blockchain.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "API_LIMIT")
public class ApiLimit {
    @Id
    @Column(name = "API_LIMIT_ID")
    private int apiLimitId;

    @Column(name="COMPANY_ID")
    private int companyId;

    @Column(name="MONTH_LIMIT")
    private long monthLimit;

    @Column(name="DAY_LIMIT")
    private long dayLimit;

    @Column(name="API_TOKEN")
    private String apiToken;

}
