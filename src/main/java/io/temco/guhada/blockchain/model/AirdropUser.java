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
    private long userId;
    private String phone;
    private String amount;
    private boolean success;
}
