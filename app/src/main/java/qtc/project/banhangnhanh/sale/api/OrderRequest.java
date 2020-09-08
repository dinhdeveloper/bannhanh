package qtc.project.banhangnhanh.sale.api;

import b.laixuantam.myaarlibrary.api.ApiRequest;
import b.laixuantam.myaarlibrary.api.BaseApiParams;
import qtc.project.banhangnhanh.admin.model.BaseResponseModel;
import qtc.project.banhangnhanh.helper.Consts;
import qtc.project.banhangnhanh.sale.model.OrderModel;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

@ApiRequest.ApiName("list_order_ss")
public class OrderRequest extends ApiRequest<OrderRequest.Service, BaseResponseModel<OrderModel>, OrderRequest.ApiParams> {

    public OrderRequest() {
        super(OrderRequest.Service.class, RequestOrigin.NONE, Consts.HOST_API, Consts.MODE, Consts.TRUST_CERTIFICATE);
    }

    @Override
    protected void postAfterRequest(BaseResponseModel<OrderModel> result) throws Exception {

    }

    @Override
    protected Call<BaseResponseModel<OrderModel>> call(OrderRequest.ApiParams params) {
        params.detect = "list_order";
        return getService().call(params);
    }


    interface Service {
        @Headers(Consts.HEADES)
        @POST(Consts.REST_ENDPOINT)
        Call<BaseResponseModel<OrderModel>> call(@Body OrderRequest.ApiParams params);
    }

    public static class ApiParams extends BaseApiParams {
        public String detect;
        public String id_business;
        public String time_filter;
        public String order_id;
        public String order_code;
        public String filter;
        public String page;
        public String customer_id;
        public String customer_code;
    }
}
