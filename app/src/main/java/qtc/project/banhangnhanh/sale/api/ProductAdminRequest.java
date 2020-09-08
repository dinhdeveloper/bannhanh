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

@ApiRequest.ApiName("product_manager")
public class ProductAdminRequest extends ApiRequest<ProductAdminRequest.Service, BaseResponseModel<ProductModel>, ProductAdminRequest.ApiParams> {

    public ProductAdminRequest() {
        super(ProductAdminRequest.Service.class, RequestOrigin.NONE, Consts.HOST_API, Consts.MODE, Consts.TRUST_CERTIFICATE);
    }

    @Override
    protected void postAfterRequest(BaseResponseModel<ProductModel> result) throws Exception {

    }

    @Override
    protected Call<BaseResponseModel<ProductModel>> call(ProductAdminRequest.ApiParams params) {
        params.detect = "product_manager";
        return getService().call(params);
    }


    interface Service {
        @Headers(Consts.HEADES)
        @POST(Consts.REST_ENDPOINT)
        Call<BaseResponseModel<ProductModel>> call(@Body ProductAdminRequest.ApiParams params);
    }

    public static class ApiParams extends BaseApiParams {
        public String detect;
        public String id_business;
        public String id_product;
        public String name;
        public String category_id;
        public String id_code;
        public String type_manager;
        public String description;
        public String barcode;
        public String qr_code;
        public String quantity_safetystock;
    }
}
