package io.temco.guhada.blockchain.model.request;

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
public class LuckyDrawRequest {
    private long dealId;
    private long userId;
}
