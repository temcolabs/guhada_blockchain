package io.temco.guhada.blockchain.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "TRANSACTION")
@Table(name = "TRANSACTION")
@Getter
@Setter
public class Transaction implements Serializable {
    private static final long serialVersionUID = -2178777568052742080L;

    @Id
    @Column(name="ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "SELLER_ID")
    private Seller seller;

    @Column(name="HASH")
    private String hash;

    public Transaction() {}
    public Transaction(Seller seller, String hash) {
        this.seller = seller;
        this.hash = hash;
    }


    @Override
    public String toString() {
        return String.format("Transaction [id %d, seller %s, hash %s]",
                id, seller.getEmail(), hash);
    }


}
