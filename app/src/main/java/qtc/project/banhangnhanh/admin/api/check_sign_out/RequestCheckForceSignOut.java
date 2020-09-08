package qtc.project.banhangnhanh.admin.api.check_sign_out;

import b.laixuantam.myaarlibrary.api.ApiRequest;
import b.laixuantam.myaarlibrary.api.BaseApiParams;
import qtc.project.banhangnhanh.admin.model.BaseResponseModel;
import qtc.project.banhangnhanh.admin.model.ForceSignOutModel;
import qtc.project.banhangnhanh.helper.Consts;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

@ApiRequest.ApiName("force_signout")
public class RequestCheckForceSignOut extends ApiRequest<RequestCheckForceSignOut.Service, BaseResponseModel<ForceSignOutModel>, RequestCheckForceSignOut.ApiParams> {

    public RequestCheckForceSignOut() {
        super(RequestCheckForceSignOut.Service.class, RequestOrigin.NONE, Consts.HOST_API, Consts.MODE, Consts.TRUST_CERTIFICATE);
    }

    @Override
    protected void postAfterRequest(BaseResponseModel result) {

    }

    @Override
    protected Call<BaseResponseModel<ForceSignOutModel>> call(ApiParams params) {
        params.detect = "force_signout";
        return getService().call(params);
    }

    interface Service {
        @Headers(Consts.HEADES)
        @POST(Consts.REST_ENDPOINT)
        Call<BaseResponseModel<ForceSignOutModel>> call(@Body RequestCheckForceSignOut.ApiParams params);
    }

    public static class ApiParams extends BaseApiParams {
        String detect;
        public String type_manager;
        public String id_business;
    }
}