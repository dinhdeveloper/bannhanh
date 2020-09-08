package qtc.project.banhangnhanh.admin.api.report.tomtatdoanhso;

import b.laixuantam.myaarlibrary.api.ApiRequest;
import b.laixuantam.myaarlibrary.api.BaseApiParams;
import qtc.project.banhangnhanh.admin.model.BaseResponseModel;
import qtc.project.banhangnhanh.admin.model.TongDoanhThuModel;
import qtc.project.banhangnhanh.helper.Consts;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

@ApiRequest.ApiName("income_manager")
public class TomTatDoanhSoRequest  extends ApiRequest<TomTatDoanhSoRequest.Service, BaseResponseModel<TongDoanhThuModel>, TomTatDoanhSoRequest.ApiParams> {

    public TomTatDoanhSoRequest() {
        super(TomTatDoanhSoRequest.Service.class, RequestOrigin.NONE, Consts.HOST_API, Consts.MODE, Consts.TRUST_CERTIFICATE);
    }

    @Override
    protected void postAfterRequest(BaseResponseModel<TongDoanhThuModel> result) throws Exception {

    }

    @Override
    protected Call<BaseResponseModel<TongDoanhThuModel>> call(TomTatDoanhSoRequest.ApiParams params) {
        params.detect = "statictis_report_manager";
        return getService().call(params);
    }


    interface Service {
        @Headers(Consts.HEADES)
        @POST(Consts.REST_ENDPOINT)
        Call<BaseResponseModel<TongDoanhThuModel>> call(@Body TomTatDoanhSoRequest.ApiParams params);
    }

    public static class ApiParams extends BaseApiParams {
        public String detect;
        public String id_business;
        public String date_option;
        public String product;
        public String type_manager;
        public String date_start;
        public String date_end;
        public String customer_id;
        public String product_id;
    }
}