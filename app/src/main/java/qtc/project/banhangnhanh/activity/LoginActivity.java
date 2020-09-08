package qtc.project.banhangnhanh.activity;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import com.google.firebase.messaging.FirebaseMessaging;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.Objects;

import b.laixuantam.myaarlibrary.api.ApiRequest;
import b.laixuantam.myaarlibrary.api.ErrorApiResponse;
import b.laixuantam.myaarlibrary.base.BaseFragmentActivity;
import b.laixuantam.myaarlibrary.base.BaseParameters;
import b.laixuantam.myaarlibrary.helper.MyLog;
import b.laixuantam.myaarlibrary.widgets.dialog.alert.KAlertDialog;
import qtc.project.banhangnhanh.R;
import qtc.project.banhangnhanh.admin.api.account.login.LoginRequest;
import qtc.project.banhangnhanh.admin.dependency.AppProvider;
import qtc.project.banhangnhanh.admin.event.DoLogOutEvent;
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
        if (!AppProvider.getConnectivityHelper().hasInternetConnection()) {
            showAlert(getString(R.string.error_connect_internet), KAlertDialog.ERROR_TYPE);
            return;
        }
        //checkUpdateAppVersion();
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
        } else {
            checkUpdateAppVersion();
        }
    }

    public void checkUpdateAppVersion() {
        PackageManager packageManager = this.getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String currentVersion = packageInfo.versionName;
        new CheckAppVersionAsync(currentVersion, LoginActivity.this).execute();
    }

    private void showAlertUpdateAppVersion() {
        String title = "Thông báo cập nhật!!!";
        String message = "Ứng dụng có bản cập nhật mới, vui lòng cập nhật để sử dụng.";
        showConfirmAlert(title, message, kAlertDialog -> {
            kAlertDialog.dismiss();
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName()));
            startActivity(intent);
        }, null, KAlertDialog.WARNING_TYPE);
    }

    public class CheckAppVersionAsync extends AsyncTask<String, String, JSONObject> {

        private String latestVersion;
        private String currentVersion;
        private Context context;

        public CheckAppVersionAsync(String currentVersion, Context context) {
            this.currentVersion = currentVersion;
            this.context = context;
        }

        @Override
        protected JSONObject doInBackground(String... params) {

            try {
                latestVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=" + context.getPackageName() + "&hl=vi")
                        .timeout(30000)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com")
                        .get()
                        .select("div.hAyfc:nth-child(4) > span:nth-child(2) > div:nth-child(1) > span:nth-child(1)")
                        .first()
                        .ownText();
                MyLog.e("versionAAA", currentVersion + "---" + latestVersion);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return new JSONObject();
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            if (latestVersion != null) {
                if (!currentVersion.equalsIgnoreCase(latestVersion)) {
                    showAlertUpdateDialog();
                }
            }
            super.onPostExecute(jsonObject);
        }

        public void showAlertUpdateDialog() {

            showAlertUpdateAppVersion();
        }
    }

    @Override
    public void goToFragmentReview() {
        startActivity(new Intent(LoginActivity.this, LoginPreviewActivity.class));
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

    public static final int REQUEST_PHONE_CALL = 101;

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
                    userModel.setName_business(id_store);
                    //goi firebase
                    if (userModel.getLevel().equalsIgnoreCase("2")) {
                        FirebaseMessaging.getInstance().subscribeToTopic("pos_notifycation_app_admin");
                        FirebaseMessaging.getInstance().subscribeToTopic("pos_notifycation_app_admin_" + userModel.getId_business());
                    } else {
                        FirebaseMessaging.getInstance().subscribeToTopic("pos_notifycation_employee_" + userModel.getId() + "_" + userModel.getId_business());
                        FirebaseMessaging.getInstance().subscribeToTopic("pos_notifycation_employee_" + userModel.getId_business());
                    }
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
                    if (!TextUtils.isEmpty(result.getError_code()) && result.getError_code().equalsIgnoreCase("502")) {
                        activity.showConfirmAlert("Liên hệ", result.getMessage(), "Liên hệ", null, new KAlertDialog.KAlertClickListener() {
                            @Override
                            public void onClick(KAlertDialog kAlertDialog) {
                                kAlertDialog.dismiss();
                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                callIntent.setData(Uri.parse("tel:0945600055"));
                                if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);
                                } else {
                                    startActivity(callIntent);
                                }
                            }
                        }, null, -1);
                    } else {
                        showAlert(result.getMessage(), KAlertDialog.ERROR_TYPE);
                    }
                }
            }

            @Override
            public void onError(ErrorApiResponse error) {
                showAlert("Đăng nhập không thành công", KAlertDialog.ERROR_TYPE);
            }

            @Override
            public void onFail(ApiRequest.RequestError error) {
                showAlert("Đăng nhập không thành công", KAlertDialog.ERROR_TYPE);
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

    public void showConfirmAlert(String title, String mess, KAlertDialog.KAlertClickListener actionConfirm, int type) {
        showConfirmAlert(title, mess, "", "", actionConfirm, null, type);
    }

    public void showConfirmAlert(String title, String mess, KAlertDialog.KAlertClickListener actionConfirm, KAlertDialog.KAlertClickListener actionCancel, int type) {
        showConfirmAlert(title, mess, "", "", actionConfirm, actionCancel, type);
    }

    public void showConfirmAlert(String title, String mess, String titleButtonConfirm, String titleButtonCancel, KAlertDialog.KAlertClickListener actionConfirm, KAlertDialog.KAlertClickListener actionCancel, int type) {

        switch (type) {
            case KAlertDialog.SUCCESS_TYPE:
                showCustomerImageAndBgButtonConfirmAlert(title, mess, titleButtonConfirm, R.drawable.button_confirm_alert_dialog, titleButtonCancel, R.drawable.button_cancel_alert_dialog, actionConfirm, actionCancel, R.drawable.ic_img_alert_success);
                break;
            case KAlertDialog.WARNING_TYPE:
                showCustomerImageAndBgButtonConfirmAlert(title, mess, titleButtonConfirm, R.drawable.button_confirm_alert_dialog, titleButtonCancel, R.drawable.button_cancel_alert_dialog, actionConfirm, actionCancel, R.drawable.ic_img_alert_warning);
                break;
            case -1:
                showCustomerImageAndBgButtonConfirmAlert(title, mess, titleButtonConfirm, R.drawable.button_confirm_alert_dialog, titleButtonCancel, R.drawable.button_cancel_alert_dialog, actionConfirm, actionCancel, R.drawable.ic_img_alert_warning_logout);
                break;
        }

    }

    @SuppressWarnings("unused")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDoLogOutEvent(DoLogOutEvent event) {
        if (view != null) {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:0945600055"));
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);
            } else {
                startActivity(callIntent);
            }
        }
    }
}