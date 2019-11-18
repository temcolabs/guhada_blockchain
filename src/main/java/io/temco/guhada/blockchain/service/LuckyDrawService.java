package io.temco.guhada.blockchain.service;

import io.temco.guhada.blockchain.model.LuckyDrawModel;
import io.temco.guhada.blockchain.model.request.LuckyDrawRequest;

/**
 * Created by Shin Han
 * Since 2019-11-13
 */
public interface LuckyDrawService {
    String entry(LuckyDrawRequest luckyDrawRequest) throws Exception;
    Long draw(long dealId) throws Exception;
    LuckyDrawModel getEntryUser(long dealId,long userId) throws Exception;
    Long getLuckyDrawWinner(long dealId) throws Exception;
}
