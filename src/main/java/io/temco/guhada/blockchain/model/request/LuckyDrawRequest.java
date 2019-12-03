package io.temco.guhada.blockchain.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by Shin Han
 * Since 2019-11-18
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LuckyDrawRequest {
    private long dealId;
    private long userId;
}
