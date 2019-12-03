package io.temco.guhada.blockchain.service.retrofit;

import io.temco.guhada.blockchain.model.request.LuckyDrawRequest;
import io.temco.guhada.blockchain.model.request.PointRequest;
import io.temco.guhada.framework.model.order.response.GuhadaJsonResponse;
import io.temco.guhada.framework.model.point.response.SavedPointResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Shin HanchargePrice
 * Since 2019-03-22
 */
public interface ProductApiService {
    @POST("/lucky-draws/winner/")
    Call<GuhadaJsonResponse<SavedPointResponse>> luckyDraw(@Body LuckyDrawRequest luckyDrawRequest);

}
