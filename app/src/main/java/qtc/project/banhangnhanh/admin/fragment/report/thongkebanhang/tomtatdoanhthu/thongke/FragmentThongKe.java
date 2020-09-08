package qtc.project.banhangnhanh.admin.fragment.report.thongkebanhang.tomtatdoanhthu.thongke;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

import b.laixuantam.myaarlibrary.api.ApiRequest;
import b.laixuantam.myaarlibrary.api.ErrorApiResponse;
import b.laixuantam.myaarlibrary.base.BaseFragment;
import b.laixuantam.myaarlibrary.base.BaseParameters;
import qtc.project.banhangnhanh.activity.HomeActivity;
import qtc.project.banhangnhanh.admin.fragment.report.thongkebanhang.tomtatdoanhthu.FragmentFilterTomTatDoanhSo;
import qtc.project.banhangnhanh.admin.api.report.tomtatdoanhso.ThongKeRequest;
import qtc.project.banhangnhanh.admin.dependency.AppProvider;
import qtc.project.banhangnhanh.admin.fragment.report.thongkebanhang.tomtatdoanhthu.tongdoanhthu.FragmentTongDoanhThu;
import qtc.project.banhangnhanh.admin.model.BaseResponseModel;
import qtc.project.banhangnhanh.admin.model.TongDoanhThuModel;
import qtc.project.banhangnhanh.admin.views.fragment.report.thongkebanhang.tomtatdoanhthu.thongke.FragmentThongKeView;
import qtc.project.banhangnhanh.admin.views.fragment.report.thongkebanhang.tomtatdoanhthu.thongke.FragmentThongKeViewCallback;
import qtc.project.banhangnhanh.admin.views.fragment.report.thongkebanhang.tomtatdoanhthu.thongke.FragmentThongKeViewInterface;

public class FragmentThongKe extends BaseFragment<FragmentThongKeViewInterface, BaseParameters> implements FragmentThongKeViewCallback {


    HomeActivity activity;
    @Override
    protected void initialize() {
        activity = (HomeActivity)getActivity();
        view.init(activity,this);
    }

    @Override
    protected FragmentThongKeViewInterface getViewInstance() {
        return new FragmentThongKeView();
    }

    @Override
    protected BaseParameters getParametersContainer() {
        return null;
    }

    @Override
    public void goToChooseYear() {
        if (activity!=null)
            activity.addFragment(new FragmentFilterTomTatDoanhSo(),true,null);
    }

    @Override
    public void goToTongDoanhThu() {
        if (activity!=null)
            activity.replaceFragment(new FragmentTongDoanhThu(),true,null);
    }

    @Override
    public void onBackProgress() {
        if (activity!=null)
            activity.checkBack();
    }

    @Override
    public void getDataToYear(String nam) {
        showProgress();
        if (nam!=null){
            ThongKeRequest.ApiParams params = new ThongKeRequest.ApiParams();
            params.id_business = AppProvider.getPreferences().getUserModel().getId_business();
            params.type_manager = "income_manager";
            params.date_option = nam;

            AppProvider.getApiManagement().call(ThongKeRequest.class, params, new ApiRequest.ApiCallback<BaseResponseModel<TongDoanhThuModel>>() {
                @Override
                public void onSuccess(BaseResponseModel<TongDoanhThuModel> body) {
                    dismissProgress();
                    ArrayList<TongDoanhThuModel> list = new ArrayList<>();
                    list.addAll(Arrays.asList(body.getData()));
                    view.mappingYear(list);
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

    public void filterDataYear(String nam) {
        if (nam!=null){
            view.sentYearToView(nam);
        }
    }
}
