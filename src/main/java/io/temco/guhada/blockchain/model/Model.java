package io.temco.guhada.blockchain.model;

import io.temco.guhada.blockchain.util.StringUtil;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "MODEL")
@Table(name = "MODEL")
public class Model implements Serializable {

    private static final long serialVersionUID = -4435847900692889142L;

    @Id
    @Column(name="ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "NAME")
    private String name;

    public Model() {}
    public Model(String name) {
        this.name = StringUtil.getLowerCase(name);
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = StringUtil.getLowerCase(name); }

    @Override
    public String toString() {
        return "Model: [id: "+id+", name:"+name+"]";
    }
}
