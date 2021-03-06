package qtc.project.banhangnhanh.admin.api.employee;

import b.laixuantam.myaarlibrary.api.ApiRequest;
import b.laixuantam.myaarlibrary.api.BaseApiParams;
import qtc.project.banhangnhanh.admin.model.BaseResponseModel;
import qtc.project.banhangnhanh.admin.model.EmployeeModel;
import qtc.project.banhangnhanh.helper.Consts;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

@ApiRequest.ApiName("employee_manager")
public class EmployeeRequest  extends ApiRequest<EmployeeRequest.Service, BaseResponseModel<EmployeeModel>, EmployeeRequest.ApiParams> {
    public EmployeeRequest() {
        super(EmployeeRequest.Service.class, RequestOrigin.NONE, Consts.HOST_API, Consts.MODE, Consts.TRUST_CERTIFICATE);
    }

    @Override
    protected void postAfterRequest(BaseResponseModel<EmployeeModel> result) throws Exception {

    }

    @Override
    protected Call<BaseResponseModel<EmployeeModel>> call(EmployeeRequest.ApiParams params) {
        params.detect = "employee_manager";
        return getService().call(params);
    }


    interface Service {
        @Headers(Consts.HEADES)
        @POST(Consts.REST_ENDPOINT)
        Call<BaseResponseModel<EmployeeModel>> call(@Body EmployeeRequest.ApiParams params);
    }

    public static class ApiParams extends BaseApiParams {
        public String detect;
        public String id_business;
        public String type_manager;
        public String id_code;
        public String full_name;
        public String email;
        public String phone_number;
        public String address;
        public String birthday;
        public String employee_level;
        public String password;
        public String id_employee;
        public String new_password;
        public String old_password;
        public String status;
        public String password_reset;

        //cap nhat pw


    }
}
