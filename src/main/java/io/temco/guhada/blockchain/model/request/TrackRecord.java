package io.temco.guhada.blockchain.model.request;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Data
@Table(name ="track_record")
public class TrackRecord {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)    	   
    @Column(name="id")
	private int trackRecordId;
	
	@Column(name="order_id")
	private long orderId;
	
	@Column(name="deal_id")
    private long dealId;
	
	@Column(name="serial_id")
    private String serialId = "";    
	
	@Column(name="product_name")
    private String productName;
    
	@Column(name="brand_name")
    private String brandName;
    
	@Column(name="price")
    private long price;
    
	@Column(name="color")
    private String color = "";
    
	@Column(name="owner")
    private String owner;

	@Column(name="hash")
    private String hash;
	
	@Column(name="tx")
    private String txUrl;
        
}
