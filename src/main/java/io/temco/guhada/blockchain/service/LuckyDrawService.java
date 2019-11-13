package io.temco.guhada.blockchain.service;

import io.temco.guhada.blockchain.model.LuckyDrawModel;

/**
 * Created by Shin Han
 * Since 2019-11-13
 */
public interface LuckyDrawService {
    void entry(LuckyDrawModel luckyDrawRequest) throws Exception;
    void draw(long dealId) throws Exception;
    LuckyDrawModel getLuckyDrawEntrys(long dealId);
    LuckyDrawModel getLuckyDrawWinner(long dealId) throws Exception;
}
