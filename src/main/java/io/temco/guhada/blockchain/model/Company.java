package io.temco.guhada.blockchain.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name="COMPANY")
public class Company {

    @Id
    @Column(name="TRANSACT_ID")
    private int companyId;

    @Column(name = "COMPANY_NAME")
    private String companyName;
}
