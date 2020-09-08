package qtc.project.banhangnhanh.sale.api;

import b.laixuantam.myaarlibrary.api.ApiRequest;
import b.laixuantam.myaarlibrary.api.BaseApiParams;
import qtc.project.banhangnhanh.admin.model.BaseResponseModel;
import qtc.project.banhangnhanh.helper.Consts;
import qtc.project.banhangnhanh.sale.model.ProductModel;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

@ApiRequest.ApiName("list_product")
public class ProductRequest  extends ApiRequest<ProductRequest.Service, BaseResponseModel<ProductModel>, ProductRequest.ApiParams> {

    public ProductRequest() {
        super(ProductRequest.Service.class, RequestOrigin.NONE, Consts.HOST_API, Consts.MODE, Consts.TRUST_CERTIFICATE);
    }

    @Override
    protected void postAfterRequest(BaseResponseModel<ProductModel> result) throws Exception {

    }

    @Override
    protected Call<BaseResponseModel<ProductModel>> call(ProductRequest.ApiParams params) {
        params.detect = "list_product";
        return getService().call(params);
    }


    interface Service {
        @Headers(Consts.HEADES)
        @POST(Consts.REST_ENDPOINT)
        Call<BaseResponseModel<ProductModel>> call(@Body ProductRequest.ApiParams params);
    }

    public static class ApiParams extends BaseApiParams {
        public String detect;
        public String id_business;
        public String id_category;
        public String product;
        public String bar_code;
        public String qr_code;
        public String page;
        public String limit;
    }
}
