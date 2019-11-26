package io.temco.guhada.blockchain.service.retrofit;

import io.temco.guhada.blockchain.model.request.PointRequest;
import io.temco.guhada.framework.model.order.response.GuhadaJsonResponse;
import io.temco.guhada.framework.model.point.response.SavedPointResponse;
import io.temco.guhada.framework.model.user.SellerDepartureOrReturnAddress;
import io.temco.guhada.framework.model.user.SellerUser;
import io.temco.guhada.framework.model.user.User;
import io.temco.guhada.framework.model.user.UserShippingAddress;
import io.temco.guhada.framework.model.user.request.ShippingAddressParam;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

/**
 * Created by Shin HanchargePrice
 * Since 2019-03-22
 */
public interface BenefitApiService {
    @POST("/process/save/")
    Call<GuhadaJsonResponse<SavedPointResponse>> savePoint(@Body PointRequest pointRequest);

}
