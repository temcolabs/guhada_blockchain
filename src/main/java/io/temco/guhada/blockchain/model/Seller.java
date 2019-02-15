package io.temco.guhada.blockchain.model;

import io.temco.guhada.blockchain.util.StringUtil;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "SELLER")
@Table(name = "SELLER")
public class Seller implements Serializable {

    private static final long serialVersionUID = -504500692558692398L;

    @Id
    @Column(name="ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "EMAIL")
    private String email;

    public Seller() {}
    public Seller(String email) {
        this.email = StringUtil.getLowerCase(email);
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = StringUtil.getLowerCase(email); }

    @Override
    public String toString() {
        return "Seller: [id: "+id+", email:"+email+"]";
    }


}
