package qtc.project.banhangnhanh.admin.fragment.report.thongkekho.xuatnhapkho;

import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import b.laixuantam.myaarlibrary.api.ApiRequest;
import b.laixuantam.myaarlibrary.api.ErrorApiResponse;
import b.laixuantam.myaarlibrary.base.BaseFragment;
import b.laixuantam.myaarlibrary.base.BaseParameters;
import qtc.project.banhangnhanh.activity.HomeActivity;
import qtc.project.banhangnhanh.admin.api.report.xuatnhapkho.BaoCaoXuatNhapKhoRequest;
import qtc.project.banhangnhanh.admin.dependency.AppProvider;
import qtc.project.banhangnhanh.admin.fragment.report.thongkekho.xuatnhapkho.detail.FragmentDetailXuatNhapKho;
import qtc.project.banhangnhanh.admin.model.BaseResponseModel;
import qtc.project.banhangnhanh.admin.model.ReportXuatNhapKhoModel;
import qtc.project.banhangnhanh.admin.views.fragment.report.thongkekho.xuatnhapkho.FragmentBaoCaoXuatNhapKhoView;
import qtc.project.banhangnhanh.admin.views.fragment.report.thongkekho.xuatnhapkho.FragmentBaoCaoXuatNhapKhoViewCallback;
import qtc.project.banhangnhanh.admin.views.fragment.report.thongkekho.xuatnhapkho.FragmentBaoCaoXuatNhapKhoViewInterface;

public class FragmentBaoCaoXuatNhapKho extends BaseFragment<FragmentBaoCaoXuatNhapKhoViewInterface, BaseParameters> implements FragmentBaoCaoXuatNhapKhoViewCallback {

    HomeActivity activity;

//    public static FragmentBaoCaoXuatNhapKho newIntance(String thang, String nam) {
//        FragmentBaoCaoXuatNhapKho frag = new FragmentBaoCaoXuatNhapKho();
//        Bundle bundle = new Bundle();
//        bundle.putString("thang", thang);
//        bundle.putString("nam", nam);
//        frag.setArguments(bundle);
//        return frag;
//    }

    String nam;
    String thang;

    @Override
    protected void initialize() {
        activity = (HomeActivity) getActivity();
        view.init(activity, this);

        getDataToBundle();
    }

    private void getDataToBundle() {
        Bundle extras = getArguments();
        if (extras != null) {
            Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            // Trả về giá trị từ 0 - 11
            int month = c.get(Calendar.MONTH)+1;

            showProgress();
            BaoCaoXuatNhapKhoRequest.ApiParams params = new BaoCaoXuatNhapKhoRequest.ApiParams();
            params.id_business = AppProvider.getPreferences().getUserModel().getId_business();
            params.type_manager = "stock_in_out";
            params.date_start = year + "-" + month + "-01";
            params.date_end = year + "-" + month + "-31";

            AppProvider.getApiManagement().call(BaoCaoXuatNhapKhoRequest.class, params, new ApiRequest.ApiCallback<BaseResponseModel<ReportXuatNhapKhoModel>>() {
                @Override
                public void onSuccess(BaseResponseModel<ReportXuatNhapKhoModel> body) {
                    dismissProgress();
                    ArrayList<ReportXuatNhapKhoModel> list = new ArrayList<>();
                    list.addAll(Arrays.asList(body.getData()));
                    view.sendDataToView(list);
                }

                @Override
                public void onError(ErrorApiResponse error) {
                    dismissProgress();
                    Log.e("onError", error.message);
                }

                @Override
                public void onFail(ApiRequest.RequestError error) {
                    dismissProgress();
                    Log.e("onFail", error.name());
                }
            });
        }
    }

    @Override
    protected FragmentBaoCaoXuatNhapKhoViewInterface getViewInstance() {
        return new FragmentBaoCaoXuatNhapKhoView();
    }

    @Override
    protected BaseParameters getParametersContainer() {
        return null;
    }

    @Override
    public void onBackProgress() {
        if (activity != null)
            activity.checkBack();
    }

    @Override
    public void filterData() {
        activity.replaceFragment(new FragmentFilterXuatNhapKho(), true, null);
    }

    @Override
    public void goToDetailXuatNhapKho(ReportXuatNhapKhoModel model) {
        activity.addFragment(new FragmentDetailXuatNhapKho().newIntance(model), true, null);
    }

    @Override
    public void getAllData() {
        showProgress();
        BaoCaoXuatNhapKhoRequest.ApiParams params = new BaoCaoXuatNhapKhoRequest.ApiParams();
        params.id_business = AppProvider.getPreferences().getUserModel().getId_business();
        params.type_manager = "stock_in_out";

        Calendar calendar = Calendar.getInstance();
        String nam = String.valueOf(calendar.get(Calendar.YEAR));
        String thang = String.valueOf(calendar.get(Calendar.MONTH) + 1);

        params.date_start = nam + "-" + thang + "-01";
        params.date_end = nam + "-" + thang + "-31";

        AppProvider.getApiManagement().call(BaoCaoXuatNhapKhoRequest.class, params, new ApiRequest.ApiCallback<BaseResponseModel<ReportXuatNhapKhoModel>>() {
            @Override
            public void onSuccess(BaseResponseModel<ReportXuatNhapKhoModel> body) {
                dismissProgress();
                ArrayList<ReportXuatNhapKhoModel> list = new ArrayList<>();
                list.addAll(Arrays.asList(body.getData()));
                view.sendDataToView(list);
            }

            @Override
            public void onError(ErrorApiResponse error) {
                dismissProgress();
                Log.e("onError", error.message);
            }

            @Override
            public void onFail(ApiRequest.RequestError error) {
                dismissProgress();
                Log.e("onFail", error.name());
            }
        });
    }

    @Override
    public void goToSearch(String search) {

    }

    public void filterDataDate(String nam, String thang) {
        showProgress();
        BaoCaoXuatNhapKhoRequest.ApiParams params = new BaoCaoXuatNhapKhoRequest.ApiParams();
        params.id_business = AppProvider.getPreferences().getUserModel().getId_business();
        params.type_manager = "stock_in_out";
        params.date_start = nam + "-" + thang + "-01";
        params.date_end = nam + "-" + thang + "-31";

        AppProvider.getApiManagement().call(BaoCaoXuatNhapKhoRequest.class, params, new ApiRequest.ApiCallback<BaseResponseModel<ReportXuatNhapKhoModel>>() {
            @Override
            public void onSuccess(BaseResponseModel<ReportXuatNhapKhoModel> body) {
                dismissProgress();
                ArrayList<ReportXuatNhapKhoModel> list = new ArrayList<>();
                list.addAll(Arrays.asList(body.getData()));
                view.sendDataToView(list);
            }

            @Override
            public void onError(ErrorApiResponse error) {
                dismissProgress();
                Log.e("onError", error.message);
            }

            @Override
            public void onFail(ApiRequest.RequestError error) {
                dismissProgress();
                Log.e("onFail", error.name());
            }
        });
    }
}
