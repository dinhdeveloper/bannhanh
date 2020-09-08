package qtc.project.banhangnhanh.activity;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import java.util.Objects;

import b.laixuantam.myaarlibrary.api.ApiRequest;
import b.laixuantam.myaarlibrary.api.ErrorApiResponse;
import b.laixuantam.myaarlibrary.base.BaseFragmentActivity;
import b.laixuantam.myaarlibrary.base.BaseParameters;
import b.laixuantam.myaarlibrary.widgets.dialog.alert.KAlertDialog;
import qtc.project.banhangnhanh.R;
import qtc.project.banhangnhanh.admin.api.account.login.LoginRequest;
import qtc.project.banhangnhanh.admin.dependency.AppProvider;
import qtc.project.banhangnhanh.admin.model.BaseResponseModel;
import qtc.project.banhangnhanh.admin.model.EmployeeModel;
import qtc.project.banhangnhanh.admin.views.action_bar.base_main_actionbar.BaseMainActionbarViewInterface;
import qtc.project.banhangnhanh.admin.views.activity.login.ActivityLoginView;
import qtc.project.banhangnhanh.admin.views.activity.login.ActivityLoginViewCallback;
import qtc.project.banhangnhanh.admin.views.activity.login.ActivityLoginViewInterface;

//public class LoginActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login);
//
//        LinearLayout btnLogin = findViewById(R.id.btnLogin);
//        btnLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
//            }
//        });
//    }
//}

public class LoginActivity extends BaseFragmentActivity<ActivityLoginViewInterface, BaseMainActionbarViewInterface, BaseParameters> implements ActivityLoginViewCallback {

    LoginActivity activity;

    @Override
    protected void initialize(Bundle savedInstanceState) {
        activity = LoginActivity.this;
        view.initialize(activity, this);

        if (AppProvider.getPreferences().getUserModel() != null) {
            switch (AppProvider.getPreferences().getUserModel().getLevel()) {
                case "2":
                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    finish();
                    break;

                case "1":
                    startActivity(new Intent(LoginActivity.this, SaleHomeActivity.class));
                    finish();
                    break;
                default:
                    showAlert("Bạn không có quyền vào ứng dụng", KAlertDialog.ERROR_TYPE);
            }
        }
    }

    @Override
    protected ActivityLoginViewInterface getViewInstance() {
        return new ActivityLoginView();
    }

    @Override
    protected BaseMainActionbarViewInterface getActionbarInstance() {
        return null;
    }

    @Override
    protected BaseParameters getParametersContainer() {
        return null;
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.activityLogin;
    }

    @Override
    public void onClickLogin(String id_store, String username, String password) {
        requestLogin(id_store, username, password);
    }

    private void requestLogin(String id_store, String username, String password) {
        if (!AppProvider.getConnectivityHelper().hasInternetConnection()) {
            showAlert(getString(R.string.error_connect_internet), KAlertDialog.ERROR_TYPE);
            return;
        }
        showProgress(getString(R.string.loading));
        LoginRequest.ApiParams params = new LoginRequest.ApiParams();
        params.id_code = username;
        params.store_code = id_store;
        params.detect = "employee_login";
        params.password = password;

        AppProvider.getApiManagement().call(LoginRequest.class, params, new ApiRequest.ApiCallback<BaseResponseModel<EmployeeModel>>() {
            @Override
            public void onSuccess(BaseResponseModel<EmployeeModel> result) {
                dismissProgress();

                if (!TextUtils.isEmpty(result.getSuccess()) && Objects.requireNonNull(result.getSuccess()).equalsIgnoreCase("true")) {

                    EmployeeModel userModel = result.getData()[0];
                    //luu trang thai login.
                    AppProvider.getPreferences().saveStatusLogin(true);
                    //goi firebase
//                    FirebaseMessaging.getInstance().subscribeToTopic("pos_notifycation_employee_" + userModel.getId());
//                    FirebaseMessaging.getInstance().subscribeToTopic("pos_notifycation_app_admin");
                    if (result.getData() != null && result.getData().length > 0) {
                        AppProvider.getPreferences().saveUserModel(userModel);
                    }
                    if (activity != null) {
                        activity.goToHome();
                    }
//                    if (userModel.getLevel().equals("2")) {
//
//                    } else {
//                        Toast.makeText(activity, "Bạn không có quyền!!!", Toast.LENGTH_SHORT).show();
//                        finish();
//                    }


                } else {
                    if (!TextUtils.isEmpty(result.getMessage())) {
                        showAlert(result.getMessage(), KAlertDialog.ERROR_TYPE);
                    }
                }
            }

            @Override
            public void onError(ErrorApiResponse error) {

            }

            @Override
            public void onFail(ApiRequest.RequestError error) {

            }
        });
    }

    private void goToHome() {
        switch (AppProvider.getPreferences().getUserModel().getLevel()) {
            case "2":
                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                finish();
                break;
            case "1":
                startActivity(new Intent(LoginActivity.this, SaleHomeActivity.class));
                finish();
                break;
            default:
                showAlert("Bạn không có quyền vào ứng dụng", KAlertDialog.ERROR_TYPE);
        }
//        if (AppProvider.getPreferences().getUserModel().getLevel().equalsIgnoreCase("1")){
//
//        }
//        if (AppProvider.getPreferences().getUserModel().getLevel().equalsIgnoreCase("2")){
//
//        }
//        else {
//            showAlert("Bạn không có quyền vào ứng dụng",KAlertDialog.ERROR_TYPE);
//        }
    }
}