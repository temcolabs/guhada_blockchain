package io.temco.guhada.blockchain.service;

import io.temco.guhada.blockchain.model.LuckyDrawModel;
import io.temco.guhada.blockchain.model.request.LuckyDrawRequest;

import java.util.List;

/**
 * Created by Shin Han
 * Since 2019-11-13
 */
public interface LuckyDrawService {
    void entry(List<LuckyDrawRequest> luckyDrawRequestList) throws Exception;
    void draw(long dealId) throws Exception;
    LuckyDrawModel getEntryUser(long dealId,long userId) throws Exception;
    LuckyDrawModel getLuckyDrawWinner(long dealId) throws Exception;
}
