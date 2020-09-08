package qtc.project.banhangnhanh.sale.fragment.level;

import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

import b.laixuantam.myaarlibrary.api.ApiRequest;
import b.laixuantam.myaarlibrary.api.ErrorApiResponse;
import b.laixuantam.myaarlibrary.base.BaseFragment;
import b.laixuantam.myaarlibrary.base.BaseParameters;
import b.laixuantam.myaarlibrary.widgets.dialog.alert.KAlertDialog;
import qtc.project.banhangnhanh.activity.SaleHomeActivity;
import qtc.project.banhangnhanh.admin.dependency.AppProvider;
import qtc.project.banhangnhanh.admin.model.BaseResponseModel;
import qtc.project.banhangnhanh.admin.model.LevelCustomerModel;
import qtc.project.banhangnhanh.sale.api.LevelCustomerSaleRequest;
import qtc.project.banhangnhanh.sale.fragment.home.FragmentSaleHome;
import qtc.project.banhangnhanh.sale.view.fragment.level.FragmentLevelSaleCustomerView;
import qtc.project.banhangnhanh.sale.view.fragment.level.FragmentLevelSaleCustomerViewCallback;
import qtc.project.banhangnhanh.sale.view.fragment.level.FragmentLevelSaleCustomerViewInterface;

public class FragmentLevelSaleCustomer extends BaseFragment<FragmentLevelSaleCustomerViewInterface, BaseParameters> implements FragmentLevelSaleCustomerViewCallback {
    SaleHomeActivity activity;

    @Override
    protected void initialize() {
        activity = (SaleHomeActivity) getActivity();
        view.init(activity, this);
        requestDataLevelCustomer();
    }

    private void requestDataLevelCustomer() {
        showProgress();
        LevelCustomerSaleRequest.ApiParams params = new LevelCustomerSaleRequest.ApiParams();
        params.id_business = AppProvider.getPreferences().getUserModel().getId_business();
        AppProvider.getApiManagement().call(LevelCustomerSaleRequest.class, params, new ApiRequest.ApiCallback<BaseResponseModel<LevelCustomerModel>>() {
            @Override
            public void onSuccess(BaseResponseModel<LevelCustomerModel> body) {
                dismissProgress();
                if (!TextUtils.isEmpty(body.getSuccess()) && body.getSuccess().equalsIgnoreCase("true")) {
                    ArrayList<LevelCustomerModel> list = new ArrayList<>();
                    list.addAll(Arrays.asList(body.getData()));
                    view.initRecyclerView(list);
                } else {
                    showAlert("Không tải được dữ liệu", KAlertDialog.ERROR_TYPE);
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
    protected FragmentLevelSaleCustomerViewInterface getViewInstance() {
        return new FragmentLevelSaleCustomerView();
    }

    @Override
    protected BaseParameters getParametersContainer() {
        return null;
    }

    @Override
    public void goToLevelDetail(LevelCustomerModel model) {
        if (activity != null)
            activity.replaceFragment(new FragmentLevelSaleDetail().newInstance(model), true);
    }

    @Override
    public void goHome() {
        if (activity != null)
            activity.replaceFragment(new FragmentSaleHome(), false);
    }

    @Override
    public void callNav() {
        if (activity != null)
            activity.toggleNav();
    }
}