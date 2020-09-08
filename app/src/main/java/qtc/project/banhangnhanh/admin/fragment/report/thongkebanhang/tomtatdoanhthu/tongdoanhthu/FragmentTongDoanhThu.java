package qtc.project.banhangnhanh.admin.fragment.report.thongkebanhang.tomtatdoanhthu.tongdoanhthu;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import b.laixuantam.myaarlibrary.api.ApiRequest;
import b.laixuantam.myaarlibrary.api.ErrorApiResponse;
import b.laixuantam.myaarlibrary.base.BaseFragment;
import b.laixuantam.myaarlibrary.base.BaseParameters;
import qtc.project.banhangnhanh.activity.HomeActivity;
import qtc.project.banhangnhanh.admin.fragment.report.thongkebanhang.tomtatdoanhthu.FragmentFilterTomTatDoanhSo;
import qtc.project.banhangnhanh.admin.fragment.report.thongkebanhang.tomtatdoanhthu.thongke.FragmentThongKe;
import qtc.project.banhangnhanh.admin.api.report.tomtatdoanhso.TomTatDoanhSoRequest;
import qtc.project.banhangnhanh.admin.dependency.AppProvider;
import qtc.project.banhangnhanh.admin.model.BaseResponseModel;
import qtc.project.banhangnhanh.admin.model.TongDoanhThuModel;
import qtc.project.banhangnhanh.admin.views.fragment.report.thongkebanhang.tomtatdoanhthu.tongdoanhthu.FragmentTongDoanhThuView;
import qtc.project.banhangnhanh.admin.views.fragment.report.thongkebanhang.tomtatdoanhthu.tongdoanhthu.FragmentTongDoanhThuViewCallback;
import qtc.project.banhangnhanh.admin.views.fragment.report.thongkebanhang.tomtatdoanhthu.tongdoanhthu.FragmentTongDoanhThuViewInterface;

public class FragmentTongDoanhThu extends BaseFragment<FragmentTongDoanhThuViewInterface, BaseParameters> implements FragmentTongDoanhThuViewCallback {

    HomeActivity activity;

    @Override
    protected void initialize() {
        activity = (HomeActivity) getActivity();
        view.init(activity, this);

        // getDataBundle();
    }



    @Override
    protected FragmentTongDoanhThuViewInterface getViewInstance() {
        return new FragmentTongDoanhThuView();
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
    public void chooseTimeStart(int i) {
        activity.addFragment(new FragmentFilterTomTatDoanhSo().newIntance(i), true, null);
    }

    @Override
    public void chooseTimeEnd(int i) {
        activity.addFragment(new FragmentFilterTomTatDoanhSo().newIntance(i), true, null);
    }

    @Override
    public void searchData(String date_start, String date_end) {
        showProgress();
        TomTatDoanhSoRequest.ApiParams params = new TomTatDoanhSoRequest.ApiParams();
        params.id_business = AppProvider.getPreferences().getUserModel().getId_business();
        params.type_manager = "income_manager";

        params.date_start = date_start + "-01";
        params.date_end = date_end + "-31";
        AppProvider.getApiManagement().call(TomTatDoanhSoRequest.class, params, new ApiRequest.ApiCallback<BaseResponseModel<TongDoanhThuModel>>() {
            @Override
            public void onSuccess(BaseResponseModel<TongDoanhThuModel> body) {
                dismissProgress();
                if (body != null) {
                    List<TongDoanhThuModel> models = new ArrayList<>();
                    models.addAll(Arrays.asList(body.getData()));
                    view.sentDataTongDoanhThu(models);
                }
            }

            @Override
            public void onError(ErrorApiResponse error) {
                dismissProgress();
                Log.e("", error.message);
            }

            @Override
            public void onFail(ApiRequest.RequestError error) {
                dismissProgress();
                Log.e("", error.name());
            }
        });
    }

    @Override
    public void goToFragmentThongKe() {
        if (activity!=null)
            activity.replaceFragment(new FragmentThongKe(),true,null);
    }

    public void filterDataTheoThang(String nam, String thang, int ngay) {
        if (view != null) {
            view.sentDataDayChoose(nam, thang, ngay);
        }
    }
}
