package qtc.project.banhangnhanh.sale.api;

import b.laixuantam.myaarlibrary.api.ApiRequest;
import b.laixuantam.myaarlibrary.api.BaseApiParams;
import qtc.project.banhangnhanh.admin.model.BaseResponseModel;
import qtc.project.banhangnhanh.admin.model.CustomerModel;
import qtc.project.banhangnhanh.helper.Consts;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

@ApiRequest.ApiName("customer_manager")
public class CustomerAdminRequest  extends ApiRequest<CustomerAdminRequest.Service, BaseResponseModel<CustomerModel>, CustomerAdminRequest.ApiParams> {
    public CustomerAdminRequest() {
        super(CustomerAdminRequest.Service.class, RequestOrigin.NONE, Consts.HOST_API, Consts.MODE, Consts.TRUST_CERTIFICATE);
    }

    @Override
    protected void postAfterRequest(BaseResponseModel<CustomerModel> result) throws Exception {

    }

    @Override
    protected Call<BaseResponseModel<CustomerModel>> call(CustomerAdminRequest.ApiParams params) {
        params.detect = "customer_manager";
        return getService().call(params);
    }


    interface Service {
        @Headers(Consts.HEADES)
        @POST(Consts.REST_ENDPOINT)
        Call<BaseResponseModel<CustomerModel>> call(@Body CustomerAdminRequest.ApiParams params);
    }

    public static class ApiParams extends BaseApiParams {
        public String detect;
        public String id_business;
        public String level_id;
        public String customer_filter;
        public String type_manager;

        //update
        public String employee_id;
        public String id_customer;
        public String full_name;
        public String email;
        public String phone_number;
        public String address;
    }
}
