package io.temco.guhada.blockchain.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name="COMPANY")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="COMPANY_ID")
    private int companyId;

    @Column(name = "COMPANY_NAME")
    private String companyName;

    @Column(name="MONTH_LIMIT")
    private long monthLimit;

    @Column(name="DAY_LIMIT")
    private long dayLimit;

    @Column(name="API_TOKEN")
    private String apiToken;
}
