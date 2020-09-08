package qtc.project.banhangnhanh.sale.api;

import b.laixuantam.myaarlibrary.api.ApiRequest;
import b.laixuantam.myaarlibrary.api.BaseApiParams;
import qtc.project.banhangnhanh.admin.model.BaseResponseModel;
import qtc.project.banhangnhanh.helper.Consts;
import qtc.project.banhangnhanh.sale.model.ListOrderModel;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

@ApiRequest.ApiName("create_order")
public class CreateOrderRequest extends ApiRequest<CreateOrderRequest.Service, BaseResponseModel<ListOrderModel>, CreateOrderRequest.ApiParams> {

    public CreateOrderRequest() {
        super(CreateOrderRequest.Service.class, RequestOrigin.NONE, Consts.HOST_API, Consts.MODE, Consts.TRUST_CERTIFICATE);
    }

    @Override
    protected void postAfterRequest(BaseResponseModel<ListOrderModel> result) throws Exception {

    }

    @Override
    protected Call<BaseResponseModel<ListOrderModel>> call(CreateOrderRequest.ApiParams params) {
        params.detect = "create_order";
        return getService().call(params);
    }


    interface Service {
        @Headers(Consts.HEADES)
        @POST(Consts.REST_ENDPOINT)
        Call<BaseResponseModel<ListOrderModel>> call(@Body CreateOrderRequest.ApiParams params);
    }

    public static class ApiParams extends BaseApiParams {
        public String detect;
        public String id_business;
        public String employee_id;
        public String id_customer;
        public String id_product_pack;
        public String price_product_pack;
        public String quantity_product_pack;
        public String total;
        public String id_code;
    }
}
