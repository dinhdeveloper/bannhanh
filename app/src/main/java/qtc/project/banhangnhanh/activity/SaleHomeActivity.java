package qtc.project.banhangnhanh.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import b.laixuantam.myaarlibrary.api.ApiRequest;
import b.laixuantam.myaarlibrary.api.ErrorApiResponse;
import b.laixuantam.myaarlibrary.base.BaseFragment;
import b.laixuantam.myaarlibrary.base.BaseFragmentActivity;
import b.laixuantam.myaarlibrary.base.BaseParameters;
import b.laixuantam.myaarlibrary.helper.MyLog;
import b.laixuantam.myaarlibrary.widgets.dialog.alert.KAlertDialog;
import qtc.project.banhangnhanh.R;
import qtc.project.banhangnhanh.admin.api.check_sign_out.EmployeeSignOutRequest;
import qtc.project.banhangnhanh.admin.api.employee.EmployeeRequest;
import qtc.project.banhangnhanh.admin.dependency.AppProvider;
import qtc.project.banhangnhanh.admin.event.FroceSignoutEvent;
import qtc.project.banhangnhanh.admin.event.StatusEmployeeEvent;
import qtc.project.banhangnhanh.admin.model.BaseResponseModel;
import qtc.project.banhangnhanh.admin.model.CustomerModel;
import qtc.project.banhangnhanh.admin.model.EmployeeModel;
import qtc.project.banhangnhanh.admin.views.action_bar.base_main_actionbar.BaseMainActionbarViewInterface;
import qtc.project.banhangnhanh.sale.fragment.customer.FragmentCustomerSaleDetail;
import qtc.project.banhangnhanh.sale.fragment.customer.FragmentCustomerSaleFilter;
import qtc.project.banhangnhanh.sale.fragment.home.FragmentProductSaleHome;
import qtc.project.banhangnhanh.sale.fragment.home.FragmentSaleHome;
import qtc.project.banhangnhanh.sale.fragment.order.FragmentFilterSaleOrder;
import qtc.project.banhangnhanh.sale.fragment.order.FragmentOrderDetailSale;
import qtc.project.banhangnhanh.sale.fragment.order.FragmentOrderSale;
import qtc.project.banhangnhanh.sale.fragment.product.FragmentProductSaleDetail;
import qtc.project.banhangnhanh.sale.model.OrderModel;
import qtc.project.banhangnhanh.sale.model.ProductModel;
import qtc.project.banhangnhanh.sale.view.activity.SaleHomeActivityView;
import qtc.project.banhangnhanh.sale.view.activity.SaleHomeActivityViewCallback;
import qtc.project.banhangnhanh.sale.view.activity.SaleHomeActivityViewInterface;

public class SaleHomeActivity extends BaseFragmentActivity<SaleHomeActivityViewInterface, BaseMainActionbarViewInterface, BaseParameters> implements SaleHomeActivityViewCallback {
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    @Override
    protected void initialize(Bundle savedInstanceState) {
        view.init(this, this);
        setLayoutMain();

        FullScreencall();

        checkUpdateAppVersion();

//        BarcodeDetector detector =
//                new BarcodeDetector.Builder(getApplicationContext())
//                        .setBarcodeFormats(Barcode.DATA_MATRIX | Barcode.QR_CODE)
//                        .build();
//        if(!detector.isOperational()){
//            Toast.makeText(getApplicationContext(), "khong thiet lap may do", Toast.LENGTH_SHORT).show();
//            return;
//        }

        //checkPermissionApp();
    }

    @SuppressWarnings("unused")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onKeyboardForcSignout(FroceSignoutEvent event) {
        LayoutInflater layoutInflater = getLayoutInflater();
        View popupView = layoutInflater.inflate(R.layout.alert_dialog_canhbao, null);
        TextView title_text = popupView.findViewById(R.id.title_text);
        TextView content_text = popupView.findViewById(R.id.content_text);
        Button cancel_button = popupView.findViewById(R.id.cancel_button);
        Button custom_confirm_button = popupView.findViewById(R.id.custom_confirm_button);

        title_text.setVisibility(View.GONE);
        content_text.setText("Phiên đăng nhập đã kết thúc, vui lòng đăng nhập lại!");
        //title_text.setText("Cảnh báo");
        //content_text.setText("Bạn có muốn xóa khách hàng này không?");

        AlertDialog.Builder alert = new AlertDialog.Builder(SaleHomeActivity.this);
        alert.setView(popupView);
        AlertDialog dialog = alert.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();


        custom_confirm_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EmployeeModel employeeModel = AppProvider.getPreferences().getUserModel();
                if (employeeModel != null) {
                    if (!TextUtils.isEmpty(employeeModel.getLevel()) && employeeModel.getLevel().equalsIgnoreCase("1")) {
                        FirebaseMessaging.getInstance().unsubscribeFromTopic("pos_notifycation_employee_" + employeeModel.getId_business());
                        FirebaseMessaging.getInstance().unsubscribeFromTopic("pos_notifycation_employee_" + employeeModel.getId() + "_" + employeeModel.getId_business());
                    } else {
                        FirebaseMessaging.getInstance().unsubscribeFromTopic("pos_notifycation_app_admin");
                        FirebaseMessaging.getInstance().unsubscribeFromTopic("pos_notifycation_app_admin_" + employeeModel.getId_business());
                    }
                }
                AppProvider.getPreferences().saveUserModel(null);
                startActivity(new Intent(SaleHomeActivity.this, LoginActivity.class));
                dialog.dismiss();
                finish();

            }
        });
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
        new CheckAppVersionAsync(currentVersion, SaleHomeActivity.this).execute();
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

    public void changToFragmentDashBoard() {
        replaceFragment(new FragmentSaleHome(), false);
    }

    public void changToFragmentCustomerSaleDetail(CustomerModel model) {
        addFragment(FragmentCustomerSaleDetail.newInstance(model), true);
    }

    public void changToFragmentProductProductDetail(ProductModel model) {
        addFragment(FragmentProductSaleDetail.newInstance(model), true);
    }

    public void goToFragmentFilterOrder() {
        addFragment(new FragmentFilterSaleOrder(), true);
    }

    public void changToFragmentOrderDetail(OrderModel model) {
        addFragment(FragmentOrderDetailSale.newInstance(model), true);
    }

    public void setDataFilterOrder(String dates, String times,String id_order, String maKH) {
        BaseFragment baseFragment = getCurrentFragment();
        if (baseFragment instanceof FragmentOrderSale) {
            ((FragmentOrderSale) baseFragment).dataFilterOrder(dates,times,id_order,maKH);
        }
    }

    public void changToFragmentProductSaleHome(String search) {
       addFragment(FragmentProductSaleHome.newInstance(search),true,null);
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
                MyLog.e("latestversion", "---" + latestVersion);

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
    protected void onResume() {
        super.onResume();
        checkStatusEmployee();
        //checkSignOut();
    }

    private void checkStatusEmployee() {
        EmployeeSignOutRequest.ApiParams params = new EmployeeSignOutRequest.ApiParams();
        //params.type_manager = "check_status";
        params.id_user = AppProvider.getPreferences().getUserModel().getId();
        params.id_business = AppProvider.getPreferences().getUserModel().getId_business();

        AppProvider.getApiManagement().call(EmployeeSignOutRequest.class, params, new ApiRequest.ApiCallback<BaseResponseModel<EmployeeModel>>() {
            @Override
            public void onSuccess(BaseResponseModel<EmployeeModel> body) {
                if (!TextUtils.isEmpty(body.getSuccess()) && body.getSuccess().equalsIgnoreCase("true")) {
                    List<EmployeeModel> modelList = new ArrayList<>();
                    modelList.addAll(Arrays.asList(body.getData()));
                    if (modelList.get(0).getForce_sign_out().equalsIgnoreCase("1")) {
                        FroceSignoutEvent.post();
//                        LayoutInflater layoutInflater = getLayoutInflater();
//                        View popupView = layoutInflater.inflate(R.layout.alert_dialog_canhbao, null);
//                        TextView title_text = popupView.findViewById(R.id.title_text);
//                        TextView content_text = popupView.findViewById(R.id.content_text);
//                        Button cancel_button = popupView.findViewById(R.id.cancel_button);
//                        Button custom_confirm_button = popupView.findViewById(R.id.custom_confirm_button);
//
//                        title_text.setVisibility(View.GONE);
//                        content_text.setText("Phiên đăng nhập đã kết thúc");
//                        //title_text.setText("Cảnh báo");
//                        //content_text.setText("Bạn có muốn xóa khách hàng này không?");
//
//                        AlertDialog.Builder alert = new AlertDialog.Builder(SaleHomeActivity.this);
//                        alert.setView(popupView);
//                        AlertDialog dialog = alert.create();
//                        dialog.setCanceledOnTouchOutside(false);
//                        dialog.show();
//
//                        custom_confirm_button.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                EmployeeModel employeeModel = AppProvider.getPreferences().getUserModel();
//                                if (employeeModel != null) {
//                                    if (employeeModel.getLevel().equalsIgnoreCase("1")){
//                                        FirebaseMessaging.getInstance().unsubscribeFromTopic("pos_notifycation_employee_" + employeeModel.getId()+"_"+employeeModel.getId_business());
//                                        FirebaseMessaging.getInstance().unsubscribeFromTopic("pos_notifycation_employee_" + employeeModel.getId_business());
//                                    }
//                                    else {
//                                        FirebaseMessaging.getInstance().unsubscribeFromTopic("pos_notifycation_app_admin_"+employeeModel.getId_business());
//                                        FirebaseMessaging.getInstance().unsubscribeFromTopic("pos_notifycation_app_admin");
//                                    }
//                                }
//                                AppProvider.getPreferences().saveUserModel(null);
//                                startActivity(new Intent(SaleHomeActivity.this, LoginActivity.class));
//                                dialog.dismiss();
//                                finish();
//
//                            }
//                        });

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

    private void checkSignOut() {
        EmployeeSignOutRequest.ApiParams params = new EmployeeSignOutRequest.ApiParams();
        //params.type_manager = "check_status";
        params.id_user = AppProvider.getPreferences().getUserModel().getId();
        params.id_business = AppProvider.getPreferences().getUserModel().getId_business();

        AppProvider.getApiManagement().call(EmployeeSignOutRequest.class, params, new ApiRequest.ApiCallback<BaseResponseModel<EmployeeModel>>() {
            @Override
            public void onSuccess(BaseResponseModel<EmployeeModel> body) {
                if (body != null) {
                    List<EmployeeModel> modelList = new ArrayList<>();
                    modelList.addAll(Arrays.asList(body.getData()));
                    if (modelList.get(0).getStatus().equals("N")) {

                        LayoutInflater layoutInflater = getLayoutInflater();
                        View popupView = layoutInflater.inflate(R.layout.alert_dialog_canhbao, null);
                        TextView title_text = popupView.findViewById(R.id.title_text);
                        TextView content_text = popupView.findViewById(R.id.content_text);
                        Button cancel_button = popupView.findViewById(R.id.cancel_button);
                        Button custom_confirm_button = popupView.findViewById(R.id.custom_confirm_button);

                        title_text.setVisibility(View.GONE);
                        content_text.setText("Tài khoản đã bị khóa!");
                        //title_text.setText("Cảnh báo");
                        //content_text.setText("Bạn có muốn xóa khách hàng này không?");

                        AlertDialog.Builder alert = new AlertDialog.Builder(SaleHomeActivity.this);
                        alert.setView(popupView);
                        AlertDialog dialog = alert.create();
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.show();

                        custom_confirm_button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                EmployeeModel employeeModel = AppProvider.getPreferences().getUserModel();
                                if (employeeModel != null) {
                                    if (employeeModel.getLevel().equalsIgnoreCase("1")) {
                                        FirebaseMessaging.getInstance().unsubscribeFromTopic("pos_notifycation_employee_" + employeeModel.getId() + "_" + employeeModel.getId_business());
                                        FirebaseMessaging.getInstance().unsubscribeFromTopic("pos_notifycation_employee_" + employeeModel.getId_business());
                                    } else {
                                        FirebaseMessaging.getInstance().unsubscribeFromTopic("pos_notifycation_app_admin_" + employeeModel.getId_business());
                                        FirebaseMessaging.getInstance().unsubscribeFromTopic("pos_notifycation_app_admin");
                                    }
                                }
                                AppProvider.getPreferences().saveUserModel(null);
                                startActivity(new Intent(SaleHomeActivity.this, LoginActivity.class));
                                dialog.dismiss();
                                finish();

                            }
                        });

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

//    @SuppressWarnings("unused")
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onKeyboardForcSignout(FroceSignoutEvent event) {
//        if (view != null) {
//            LayoutInflater layoutInflater = getLayoutInflater();
//            View popupView = layoutInflater.inflate(R.layout.alert_dialog_canhbao, null);
//            TextView title_text = popupView.findViewById(R.id.title_text);
//            TextView content_text = popupView.findViewById(R.id.content_text);
//            Button cancel_button = popupView.findViewById(R.id.cancel_button);
//            Button custom_confirm_button = popupView.findViewById(R.id.custom_confirm_button);
//
//            title_text.setVisibility(View.GONE);
//            content_text.setText("Tài khoản đã bị khóa.");
//            //title_text.setText("Cảnh báo");
//            //content_text.setText("Bạn có muốn xóa khách hàng này không?");
//
//            AlertDialog.Builder alert = new AlertDialog.Builder(SaleHomeActivity.this);
//            alert.setView(popupView);
//            AlertDialog dialog = alert.create();
//            dialog.setCanceledOnTouchOutside(false);
//            dialog.show();
//
//
//            custom_confirm_button.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    EmployeeModel employeeModel = AppProvider.getPreferences().getUserModel();
//                    if (employeeModel != null) {
//                        FirebaseMessaging.getInstance().unsubscribeFromTopic("pos_notifycation_app_admin_"+employeeModel.getId_business());
//                        FirebaseMessaging.getInstance().unsubscribeFromTopic("pos_notifycation_app_admin");
//                    }
//                    AppProvider.getPreferences().saveUserModel(null);
//                    startActivity(new Intent(SaleHomeActivity.this, LoginActivity.class));
//                    dialog.dismiss();
//                    finish();
//
//                }
//            });
//        }
//    }


    private void setLayoutMain() {
        FullScreencall();
        replaceFragment(new FragmentSaleHome(), false, Animation.SLIDE_IN_OUT);
    }

    @Override
    protected SaleHomeActivityViewInterface getViewInstance() {
        return new SaleHomeActivityView();
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
        return R.id.content_frame;
    }

    @Override
    public void onClickItemNav(Fragment fragment) {
        replaceFragment((BaseFragment<?, ?>) fragment, false, Animation.SLIDE_IN_OUT);
    }

    @Override
    public void logOut() {
        showConfirmAlert("Đăng xuất", "Bạn muốn đăng xuất tài khoản?", "Từ chối", "Đồng ý", new KAlertDialog.KAlertClickListener() {
            @Override
            public void onClick(KAlertDialog kAlertDialog) {
                kAlertDialog.dismiss();
            }
        }, new KAlertDialog.KAlertClickListener() {
            @Override
            public void onClick(KAlertDialog kAlertDialog) {
                kAlertDialog.dismiss();
                EmployeeModel employeeModel = AppProvider.getPreferences().getUserModel();
                if (employeeModel != null) {
                    if (employeeModel.getLevel().equalsIgnoreCase("2")) {
                        FirebaseMessaging.getInstance().unsubscribeFromTopic("pos_notifycation_app_admin");
                        FirebaseMessaging.getInstance().unsubscribeFromTopic("pos_notifycation_app_admin_" + employeeModel.getId_business());
                    } else {
                        FirebaseMessaging.getInstance().unsubscribeFromTopic("pos_notifycation_employee_" + employeeModel.getId() + "_" + employeeModel.getId_business());
                        FirebaseMessaging.getInstance().unsubscribeFromTopic("pos_notifycation_employee_" + employeeModel.getId_business());
                    }
                }
                AppProvider.getPreferences().clear();
                finish();
                startActivity(new Intent(SaleHomeActivity.this, LoginActivity.class));
            }
        }, KAlertDialog.WARNING_TYPE);
    }

    public void FullScreencall() {
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    @Override
    public void onClickBottomBarMenuHome() {

    }

    @Override
    public void onClickBottomBarMenuCategory() {

    }

    @Override
    public void onClickBottomBarMenuAccount() {

    }

    @Override
    public void onClickBottomBarMenuShoppingCart() {

    }

    @Override
    public void onClickBottomBarMenuTransaction() {

    }

    @Override
    public void onClickBottomBarMenuOrder() {

    }

    public void toggleNav() {
        view.toggleNav();
    }

//    private KAlertDialog mCustomAlert;
//
//    public void showAlert(String title, String message, int type) {
//
//        if (mCustomAlert == null) {
//            mCustomAlert = new KAlertDialog(this);
//            mCustomAlert.setCancelable(false);
//            mCustomAlert.setCanceledOnTouchOutside(false);
//        }
//        mCustomAlert.showCancelButton(false);
//
//        mCustomAlert.setTitleText(title);
//
//        mCustomAlert
//                .setContentText(message)
//                .setConfirmText("OK")
//                .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
//                    @Override
//                    public void onClick(KAlertDialog kAlertDialog) {
//                        if (mCustomAlert != null)
//                            mCustomAlert.dismiss();
//                    }
//                }).changeAlertType(type);
//        mCustomAlert.show();
//    }
//
//    public void showAlert(String message) {
//        showAlert("", message, 0);
//    }
//
//    public void showConfirmAlert(String title, String mess, KAlertDialog.KAlertClickListener actionConfirm, int type) {
//        showConfirmAlert(title, mess, "", "", actionConfirm, null, type);
//    }
//
//    public void showConfirmAlert(String title, String mess, KAlertDialog.KAlertClickListener actionConfirm, KAlertDialog.KAlertClickListener actionCancel, int type) {
//        showConfirmAlert(title, mess, "", "", actionConfirm, actionCancel, type);
//    }
//
//    public void showConfirmAlert(String title, String mess, String titleButtonConfirm, String titleButtonCancel, KAlertDialog.KAlertClickListener actionConfirm, KAlertDialog.KAlertClickListener actionCancel, int type) {
//        if (mCustomAlert == null) {
//            mCustomAlert = new KAlertDialog(this);
//            mCustomAlert.setCancelable(false);
//            mCustomAlert.setCanceledOnTouchOutside(false);
//        }
//
//        mCustomAlert.setConfirmText(getString(R.string.KAlert_confirm_button_text));
//
//        mCustomAlert.setTitleText(Html.fromHtml(title).toString());
//
//        mCustomAlert.setContentText(Html.fromHtml(mess).toString());
//
//        if (!TextUtils.isEmpty(titleButtonConfirm)) {
//            mCustomAlert.setConfirmText(titleButtonConfirm);
//        } else {
//            mCustomAlert.setConfirmText(getString(R.string.KAlert_confirm_button_text));
//        }
//
//        switch (type) {
//            case KAlertDialog.SUCCESS_TYPE:
//                mCustomAlert.setCustomImage(R.drawable.ic_success);
//                mCustomAlert.setConfirmText("Tiếp tục");
//                break;
//            case KAlertDialog.WARNING_TYPE:
//                mCustomAlert.setCustomImage(R.drawable.ic_success);
//                break;
//        }
//
//        mCustomAlert.changeAlertType(KAlertDialog.CUSTOM_IMAGE_TYPE);
//
//        if (actionCancel != null) {
//            mCustomAlert.setCancelClickListener(actionCancel);
//
//            if (!TextUtils.isEmpty(titleButtonCancel)) {
//                mCustomAlert.setCancelText(titleButtonCancel);
//            } else {
//                mCustomAlert.setCancelText(getString(R.string.KAlert_cancel_button_text));
//            }
//        } else {
//            mCustomAlert.showCancelButton(false);
//        }
//        if (actionConfirm != null) {
//            mCustomAlert.setConfirmClickListener(actionConfirm);
//        } else {
//            mCustomAlert.setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
//                @Override
//                public void onClick(KAlertDialog kAlertDialog) {
//                    mCustomAlert.dismiss();
//                }
//            });
//        }
//        mCustomAlert.show();
//    }

    public void checkBack() {
        FullScreencall();

        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();
        }

    }

    //
    public void setDataCustomer(CustomerModel model) {
        BaseFragment baseFragment = getCurrentFragment();
        if (baseFragment instanceof FragmentSaleHome) {
            ((FragmentSaleHome) baseFragment).setDataCustomer(model);
        }
    }

    //
    public void setDataHome(ProductModel model) {
        BaseFragment baseFragment = getCurrentFragment();
        if (baseFragment instanceof FragmentSaleHome) {
            ((FragmentSaleHome) baseFragment).setDataProduct(model);
        }
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
                showCustomerImageAndBgButtonConfirmAlert(title, mess, titleButtonConfirm, R.drawable.button_cancel_alert_dialog, titleButtonCancel, R.drawable.button_confirm_alert_dialog, actionConfirm, actionCancel, R.drawable.ic_img_alert_warning);
                break;
            case -1:
                showCustomerImageAndBgButtonConfirmAlert(title, mess, titleButtonConfirm, R.drawable.button_confirm_alert_dialog, titleButtonCancel, R.drawable.button_cancel_alert_dialog, actionConfirm, actionCancel, R.drawable.ic_img_alert_warning_logout);
                break;
        }
    }

    public void showConfirmAlert(String title, String mess, String titleButtonConfirm, String titleButtonCancel, KAlertDialog.KAlertClickListener actionConfirm, KAlertDialog.KAlertClickListener actionCancel, int type,int status) {

        switch (status) {
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

    ///////////////////////////////////////////////////////////////////////////
    // CHANGE TO FRAGMENT
    ///////////////////////////////////////////////////////////////////////////
    public void changToFragmentCustomerSaleFilter() {
        addFragment(new FragmentCustomerSaleFilter(), true);
    }
}