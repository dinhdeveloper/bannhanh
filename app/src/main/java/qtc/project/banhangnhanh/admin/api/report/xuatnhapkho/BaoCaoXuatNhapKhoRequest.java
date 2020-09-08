package qtc.project.banhangnhanh.admin.api.report.xuatnhapkho;

import b.laixuantam.myaarlibrary.api.ApiRequest;
import b.laixuantam.myaarlibrary.api.BaseApiParams;
import qtc.project.banhangnhanh.admin.model.BaseResponseModel;
import qtc.project.banhangnhanh.admin.model.ReportXuatNhapKhoModel;
import qtc.project.banhangnhanh.helper.Consts;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

@ApiRequest.ApiName("statictis_report_manager")
public class BaoCaoXuatNhapKhoRequest  extends ApiRequest<BaoCaoXuatNhapKhoRequest.Service, BaseResponseModel<ReportXuatNhapKhoModel>, BaoCaoXuatNhapKhoRequest.ApiParams> {

    public BaoCaoXuatNhapKhoRequest() {
        super(BaoCaoXuatNhapKhoRequest.Service.class, RequestOrigin.NONE, Consts.HOST_API, Consts.MODE, Consts.TRUST_CERTIFICATE);
    }

    @Override
    protected void postAfterRequest(BaseResponseModel<ReportXuatNhapKhoModel> result) throws Exception {

    }

    @Override
    protected Call<BaseResponseModel<ReportXuatNhapKhoModel>> call(BaoCaoXuatNhapKhoRequest.ApiParams params) {
        params.detect = "statictis_report_manager";
        return getService().call(params);
    }


    interface Service {
        @Headers(Consts.HEADES)
        @POST(Consts.REST_ENDPOINT)
        Call<BaseResponseModel<ReportXuatNhapKhoModel>> call(@Body BaoCaoXuatNhapKhoRequest.ApiParams params);
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