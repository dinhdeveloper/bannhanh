package qtc.project.banhangnhanh.admin.fragment.report.thongkebanhang.doanhthu_theo_khachhang;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import b.laixuantam.myaarlibrary.api.ApiRequest;
import b.laixuantam.myaarlibrary.api.ErrorApiResponse;
import b.laixuantam.myaarlibrary.base.BaseFragment;
import b.laixuantam.myaarlibrary.base.BaseParameters;
import qtc.project.banhangnhanh.activity.HomeActivity;
import qtc.project.banhangnhanh.admin.api.customer.CustomerRequest;
import qtc.project.banhangnhanh.admin.api.report.tomtatdoanhso.ThongKeRequest;
import qtc.project.banhangnhanh.admin.api.report.tomtatdoanhso.TomTatDoanhSoRequest;
import qtc.project.banhangnhanh.admin.dependency.AppProvider;
import qtc.project.banhangnhanh.admin.fragment.report.thongkebanhang.tomtatdoanhthu.FragmentFilterTomTatDoanhSo;
import qtc.project.banhangnhanh.admin.model.BaseResponseModel;
import qtc.project.banhangnhanh.admin.model.CustomerModel;
import qtc.project.banhangnhanh.admin.model.TongDoanhThuModel;
import qtc.project.banhangnhanh.admin.views.fragment.report.thongkebanhang.doanhthu_theo_khachhang.FragmentDoanhThuTheoKhachHangView;
import qtc.project.banhangnhanh.admin.views.fragment.report.thongkebanhang.doanhthu_theo_khachhang.FragmentDoanhThuTheoKhachHangViewCallback;
import qtc.project.banhangnhanh.admin.views.fragment.report.thongkebanhang.doanhthu_theo_khachhang.FragmentDoanhThuTheoKhachHangViewInterface;

public class FragmentDoanhThuTheoKhachHang extends BaseFragment<FragmentDoanhThuTheoKhachHangViewInterface, BaseParameters> implements FragmentDoanhThuTheoKhachHangViewCallback {

    HomeActivity activity;

    @Override
    protected void initialize() {
        activity = (HomeActivity) getActivity();
        view.init(activity, this);
    }

    @Override
    protected FragmentDoanhThuTheoKhachHangViewInterface getViewInstance() {
        return new FragmentDoanhThuTheoKhachHangView();
    }

    @Override
    protected BaseParameters getParametersContainer() {
        return null;
    }

    @Override
    public void onBackProgress() {
        if (activity!=null)
            activity.checkBack();
    }

    @Override
    public void searchCustomer(String sdt) {
        showProgress();
        CustomerRequest.ApiParams params = new CustomerRequest.ApiParams();
        params.id_business = AppProvider.getPreferences().getUserModel().getId_business();
        params.type_manager = "list_customer";
        params.customer_filter = sdt;

        AppProvider.getApiManagement().call(CustomerRequest.class, params, new ApiRequest.ApiCallback<BaseResponseModel<CustomerModel>>() {
            @Override
            public void onSuccess(BaseResponseModel<CustomerModel> body) {
                if (body != null) {
                    dismissProgress();
                    ArrayList<CustomerModel> list = new ArrayList<>();
                    list.addAll(Arrays.asList(body.getData()));
                    view.mappingView(list);
                }
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
    public void goToFragmentFilter(int status) {
        activity.addFragment(new FragmentFilterTomTatDoanhSo().newIntance(status),true,null);
    }

    @Override
    public void getDataFilterTime(String date_start, String date_end,String id_customer) {
        showProgress();
        TomTatDoanhSoRequest.ApiParams params = new TomTatDoanhSoRequest.ApiParams();
        params.id_business = AppProvider.getPreferences().getUserModel().getId_business();
        params.type_manager = "income_manager";
        params.customer_id = id_customer;
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
    public void chooseYear() {
        if (activity!=null)
            activity.addFragment(new FragmentFilterTomTatDoanhSo(),true,null);
    }

    @Override
    public void getDataToYear(String nam,String customer_id) {
        showProgress();
        if (nam!=null){
            ThongKeRequest.ApiParams params = new ThongKeRequest.ApiParams();
            params.id_business = AppProvider.getPreferences().getUserModel().getId_business();
            params.type_manager = "income_manager";
            params.date_option = nam;
            params.customer_id = customer_id;

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

    public void filterDataTheoThang(String nam, String thang, int ngay) {
        if (view!=null)
            view.mappingDateToView(nam,thang,ngay);
    }

    public void filterDataYear(String nam) {
        if (nam!=null){
            view.sentYearToView(nam);
        }
    }
}
