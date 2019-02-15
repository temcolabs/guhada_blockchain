package io.temco.guhada.blockchain.model.client;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GoodsItem {

    private String email;
    private String brand;
    private String model;
    private String serialNumber;

    public GoodsItem() {
    }

}
