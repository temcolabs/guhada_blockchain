package io.temco.guhada.blockchain.model;

import io.temco.guhada.blockchain.model.request.LuckyDrawRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by Shin Han
 * Since 2019-11-13
 */
@Getter
@Setter
@NoArgsConstructor
public class LuckyDrawModel extends LuckyDrawRequest {
    private String userEmail;
}
