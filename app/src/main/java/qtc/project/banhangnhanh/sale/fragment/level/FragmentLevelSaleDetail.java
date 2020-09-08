package qtc.project.banhangnhanh.sale.fragment.level;

import android.os.Bundle;
import android.util.Log;

import b.laixuantam.myaarlibrary.api.ApiRequest;
import b.laixuantam.myaarlibrary.api.ErrorApiResponse;
import b.laixuantam.myaarlibrary.base.BaseFragment;
import b.laixuantam.myaarlibrary.base.BaseParameters;
import qtc.project.banhangnhanh.activity.SaleHomeActivity;
import qtc.project.banhangnhanh.admin.dependency.AppProvider;
import qtc.project.banhangnhanh.admin.model.BaseResponseModel;
import qtc.project.banhangnhanh.admin.model.CustomerModel;
import qtc.project.banhangnhanh.admin.model.LevelCustomerModel;
import qtc.project.banhangnhanh.sale.api.CustomerSaleRequest;
import qtc.project.banhangnhanh.sale.fragment.home.FragmentSaleHome;
import qtc.project.banhangnhanh.sale.view.fragment.level.detail.FragmentLevelSaleDetailView;
import qtc.project.banhangnhanh.sale.view.fragment.level.detail.FragmentLevelSaleDetailViewCallback;
import qtc.project.banhangnhanh.sale.view.fragment.level.detail.FragmentLevelSaleDetailViewInterface;

public class FragmentLevelSaleDetail extends BaseFragment<FragmentLevelSaleDetailViewInterface, BaseParameters> implements FragmentLevelSaleDetailViewCallback {
    SaleHomeActivity activity;

    public static FragmentLevelSaleDetail newInstance(LevelCustomerModel model) {
        FragmentLevelSaleDetail fm = new FragmentLevelSaleDetail();
        Bundle bundle = new Bundle();
        bundle.putSerializable("model", model);
        fm.setArguments(bundle);
        return fm;
    }

    @Override
    protected void initialize() {
        activity = (SaleHomeActivity) getActivity();
        view.init(activity, this);
        getBundle();
    }

    private void getBundle() {
        Bundle extras = getArguments();
        if (extras != null) {
            LevelCustomerModel model = (LevelCustomerModel) extras.get("model");
            view.initLayout(model);
        }
    }

    @Override
    protected FragmentLevelSaleDetailViewInterface getViewInstance() {
        return new FragmentLevelSaleDetailView();
    }

    @Override
    protected BaseParameters getParametersContainer() {
        return null;
    }

    @Override
    public void onBackP() {
        if (activity != null)
            activity.checkBack();
    }

    @Override
    public void callPopup(String name,LevelCustomerModel model) {
        callDataCustomerById(name,model.getId());
    }
    private void callDataCustomerById(String name,String id) {
        showProgress();
        CustomerSaleRequest.ApiParams params = new CustomerSaleRequest.ApiParams();
        params.id_business = AppProvider.getPreferences().getUserModel().getId_business();
        if (id != null) {
            params.level_id = id;
        }
        AppProvider.getApiManagement().call(CustomerSaleRequest.class, params, new ApiRequest.ApiCallback<BaseResponseModel<CustomerModel>>() {
            @Override
            public void onSuccess(BaseResponseModel<CustomerModel> body) {
                if (body != null) {
                    dismissProgress();
                    showPopup(name,body.getData());
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

    private void showPopup(String name,CustomerModel[] list) {
        if (activity!=null){
            activity.replaceFragment(new FragmentCustomerSaleList().newInstance(name,list),true);
        }
    }

    @Override
    public void goHome() {
        if (activity != null)
            activity.replaceFragment(new FragmentSaleHome(), false);
    }
}
