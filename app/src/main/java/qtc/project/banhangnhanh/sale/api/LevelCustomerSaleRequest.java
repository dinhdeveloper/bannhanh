package qtc.project.banhangnhanh.sale.api;

import b.laixuantam.myaarlibrary.api.ApiRequest;
import b.laixuantam.myaarlibrary.api.BaseApiParams;
import qtc.project.banhangnhanh.admin.model.BaseResponseModel;
import qtc.project.banhangnhanh.admin.model.LevelCustomerModel;
import qtc.project.banhangnhanh.helper.Consts;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

@ApiRequest.ApiName("list_level_sale")
public class LevelCustomerSaleRequest   extends ApiRequest<LevelCustomerSaleRequest.Service, BaseResponseModel<LevelCustomerModel>, LevelCustomerSaleRequest.ApiParams> {

    public LevelCustomerSaleRequest() {
        super(LevelCustomerSaleRequest.Service.class, RequestOrigin.NONE, Consts.HOST_API, Consts.MODE, Consts.TRUST_CERTIFICATE);
    }

    @Override
    protected void postAfterRequest(BaseResponseModel<LevelCustomerModel> result) throws Exception {

    }

    @Override
    protected Call<BaseResponseModel<LevelCustomerModel>> call(ApiParams params) {
        params.detect = "list_level";
        return getService().call(params);
    }


    interface Service {
        @Headers(Consts.HEADES)
        @POST(Consts.REST_ENDPOINT)
        Call<BaseResponseModel<LevelCustomerModel>> call(@Body LevelCustomerSaleRequest.ApiParams params);
    }

    public static class ApiParams extends BaseApiParams {
        public String detect;
        public String id_business;
    }
}
