package qtc.project.banhangnhanh.admin.fragment.cuongchedangxuat;

import android.app.Dialog;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;

import b.laixuantam.myaarlibrary.api.ApiRequest;
import b.laixuantam.myaarlibrary.api.ErrorApiResponse;
import b.laixuantam.myaarlibrary.base.BaseFragment;
import b.laixuantam.myaarlibrary.base.BaseParameters;
import b.laixuantam.myaarlibrary.widgets.dialog.alert.KAlertDialog;
import qtc.project.banhangnhanh.R;
import qtc.project.banhangnhanh.activity.HomeActivity;
import qtc.project.banhangnhanh.admin.api.check_sign_out.RequestCheckForceSignOut;
import qtc.project.banhangnhanh.admin.dependency.AppProvider;
import qtc.project.banhangnhanh.admin.model.BaseResponseModel;
import qtc.project.banhangnhanh.admin.views.fragment.cuongchedangxuat.FragmentCuongCheDangXuatView;
import qtc.project.banhangnhanh.admin.views.fragment.cuongchedangxuat.FragmentCuongCheDangXuatViewCallback;
import qtc.project.banhangnhanh.admin.views.fragment.cuongchedangxuat.FragmentCuongCheDangXuatViewInterface;

public class FragmentCuongCheDangXuat extends BaseFragment<FragmentCuongCheDangXuatViewInterface, BaseParameters> implements FragmentCuongCheDangXuatViewCallback {
    HomeActivity activity;

    @Override
    protected void initialize() {
        activity = (HomeActivity) getActivity();
        view.init(activity, this);
    }

    @Override
    public void exitEmployee() {
        RequestCheckForceSignOut.ApiParams params = new RequestCheckForceSignOut.ApiParams();
        if (!AppProvider.getConnectivityHelper().hasInternetConnection()) {
            showAlert(getString(R.string.error_connect_internet), KAlertDialog.ERROR_TYPE);
            return;
        }
        showProgress();
        params.type_manager = "app_admin";
        params.id_business = AppProvider.getPreferences().getUserModel().getId_business();
        AppProvider.getApiManagement().call(RequestCheckForceSignOut.class, params, new ApiRequest.ApiCallback<BaseResponseModel>() {
            @Override
            public void onSuccess(BaseResponseModel result) {
                dismissProgress();
                if (!TextUtils.isEmpty(result.getSuccess()) && result.getSuccess().equalsIgnoreCase("true")) {

                } else {
                    if (!TextUtils.isEmpty(result.getMessage()))
                        showAlert(result.getMessage(), KAlertDialog.ERROR_TYPE);
                    else
                        showAlert("Không thể tải dữ liệu.", KAlertDialog.ERROR_TYPE);
                }
            }

            @Override
            public void onError(ErrorApiResponse error) {
                dismissProgress();
                Log.e("onError", error.message);
                //activity.showAlert("Không thể tải", KAlertDialog.ERROR_TYPE);
            }

            @Override
            public void onFail(ApiRequest.RequestError error) {
                dismissProgress();
                Log.e("onFail", error.name());
                //activity.showAlert("Không thể tải", KAlertDialog.ERROR_TYPE);
            }
        });
    }

    @Override
    public void exitCustomer() {
        RequestCheckForceSignOut.ApiParams params = new RequestCheckForceSignOut.ApiParams();
        if (!AppProvider.getConnectivityHelper().hasInternetConnection()) {
            showAlert(getString(R.string.error_connect_internet), KAlertDialog.ERROR_TYPE);
            return;
        }
        showProgress();
        params.type_manager = "app_employee";
        params.id_business = AppProvider.getPreferences().getUserModel().getId_business();
        AppProvider.getApiManagement().call(RequestCheckForceSignOut.class, params, new ApiRequest.ApiCallback<BaseResponseModel>() {
            @Override
            public void onSuccess(BaseResponseModel result) {
                dismissProgress();
                if (!TextUtils.isEmpty(result.getSuccess()) && result.getSuccess().equalsIgnoreCase("true")) {
                    //FroceSignoutEvent.post();
                } else {
                    if (!TextUtils.isEmpty(result.getMessage()))
                        showAlert(result.getMessage(), KAlertDialog.ERROR_TYPE);
                    else
                        showAlert("Không thể tải dữ liệu.", KAlertDialog.ERROR_TYPE);
                }
            }

            @Override
            public void onError(ErrorApiResponse error) {
                dismissProgress();
                Log.e("onError", error.message);
                //activity.showAlert("Không thể tải", KAlertDialog.ERROR_TYPE);
            }

            @Override
            public void onFail(ApiRequest.RequestError error) {
                dismissProgress();
                Log.e("onFail", error.name());
                //activity.showAlert("Không thể tải", KAlertDialog.ERROR_TYPE);
            }
        });
    }

    @Override
    public void onBackHeader() {
        if (activity != null)
            activity.checkBack();
    }

    @Override
    protected FragmentCuongCheDangXuatViewInterface getViewInstance() {
        return new FragmentCuongCheDangXuatView();
    }

    @Override
    protected BaseParameters getParametersContainer() {
        return null;
    }
}
