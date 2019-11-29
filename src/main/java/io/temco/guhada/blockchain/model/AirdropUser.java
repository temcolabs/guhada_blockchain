package io.temco.guhada.blockchain.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Created by Shin Han
 * Since 2019-11-29
 */
@Getter
@NoArgsConstructor
@ToString
public class AirdropUser {
    private int id;
    private String address;
    private String guhadaAmount;
    private boolean success;
}
