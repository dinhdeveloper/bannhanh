package qtc.project.banhangnhanh.admin.api.check_sign_out;

import b.laixuantam.myaarlibrary.api.ApiRequest;
import b.laixuantam.myaarlibrary.api.BaseApiParams;
import qtc.project.banhangnhanh.admin.api.employee.EmployeeRequest;
import qtc.project.banhangnhanh.admin.model.BaseResponseModel;
import qtc.project.banhangnhanh.admin.model.EmployeeModel;
import qtc.project.banhangnhanh.helper.Consts;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

@ApiRequest.ApiName("check_sign_out")
public class EmployeeSignOutRequest  extends ApiRequest<EmployeeSignOutRequest.Service, BaseResponseModel<EmployeeModel>, EmployeeSignOutRequest.ApiParams> {
    public EmployeeSignOutRequest() {
        super(EmployeeSignOutRequest.Service.class, RequestOrigin.NONE, Consts.HOST_API, Consts.MODE, Consts.TRUST_CERTIFICATE);
    }

    @Override
    protected void postAfterRequest(BaseResponseModel<EmployeeModel> result) throws Exception {

    }

    @Override
    protected Call<BaseResponseModel<EmployeeModel>> call(EmployeeSignOutRequest.ApiParams params) {
        params.detect = "check_sign_out";
        return getService().call(params);
    }


    interface Service {
        @Headers(Consts.HEADES)
        @POST(Consts.REST_ENDPOINT)
        Call<BaseResponseModel<EmployeeModel>> call(@Body EmployeeSignOutRequest.ApiParams params);
    }

    public static class ApiParams extends BaseApiParams {
        public String detect;
        public String id_business;
        public String type_manager;
        public String id_user;

    }
}
