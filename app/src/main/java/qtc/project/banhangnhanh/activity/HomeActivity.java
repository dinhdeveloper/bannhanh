package qtc.project.banhangnhanh.activity;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import com.google.firebase.messaging.FirebaseMessaging;

import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import b.laixuantam.myaarlibrary.api.ApiRequest;
import b.laixuantam.myaarlibrary.api.ErrorApiResponse;
import b.laixuantam.myaarlibrary.base.BaseFragment;
import b.laixuantam.myaarlibrary.base.BaseFragmentActivity;
import b.laixuantam.myaarlibrary.base.BaseParameters;
import b.laixuantam.myaarlibrary.helper.MyLog;
import b.laixuantam.myaarlibrary.helper.map.location.PermissionUtils;
import b.laixuantam.myaarlibrary.widgets.dialog.alert.KAlertDialog;
import b.laixuantam.myaarlibrary.widgets.multiple_media_picker.Gallery;
import cn.pedant.SweetAlert.SweetAlertDialog;
import qtc.project.banhangnhanh.R;
import qtc.project.banhangnhanh.admin.api.check_sign_out.EmployeeSignOutRequest;
import qtc.project.banhangnhanh.admin.api.employee.EmployeeRequest;
import qtc.project.banhangnhanh.admin.api.employee.UpdatePassEmployeeRequest;
import qtc.project.banhangnhanh.admin.dependency.AppProvider;
import qtc.project.banhangnhanh.admin.event.FroceSignoutEvent;
import qtc.project.banhangnhanh.admin.event.StatusEmployeeEvent;
import qtc.project.banhangnhanh.admin.fragment.home.FragmentHome;
import qtc.project.banhangnhanh.admin.fragment.levelcustomer.FragmentCreateLevelCustomer;
import qtc.project.banhangnhanh.admin.fragment.levelcustomer.FragmentLevelCustomerDetail;
import qtc.project.banhangnhanh.admin.fragment.order.FragmentOrderManager;
import qtc.project.banhangnhanh.admin.fragment.product.doitra.FragmentDoiTraHangHoa;
import qtc.project.banhangnhanh.admin.fragment.product.productcategory.FragmentCategoryProductDetail;
import qtc.project.banhangnhanh.admin.fragment.product.productcategory.FragmentCreateProductCategory;
import qtc.project.banhangnhanh.admin.fragment.product.productlist.FragmentProductList;
import qtc.project.banhangnhanh.admin.fragment.product.productlist.FragmentProductListDetail;
import qtc.project.banhangnhanh.admin.fragment.product.productlist.create.FragmentCreateProduct;
import qtc.project.banhangnhanh.admin.fragment.product.productlist.filter.FragmentFilterSanPham;
import qtc.project.banhangnhanh.admin.fragment.product.quanlylohang.FragmentChiTietLoHang;
import qtc.project.banhangnhanh.admin.fragment.product.quanlylohang.FragmentCreateLoHang;
import qtc.project.banhangnhanh.admin.fragment.product.quanlylohang.FragmentDonTraHang;
import qtc.project.banhangnhanh.admin.fragment.report.thongkebanhang.doanhthu_theo_khachhang.FragmentDoanhThuTheoKhachHang;
import qtc.project.banhangnhanh.admin.fragment.report.thongkebanhang.doanhthu_theo_sanpham.FragmentDoanhThuTheoSp;
import qtc.project.banhangnhanh.admin.fragment.report.thongkebanhang.sanpham_banchay.FragmentSanPhamBanChay;
import qtc.project.banhangnhanh.admin.fragment.report.thongkebanhang.tomtatdoanhthu.FragmentTomTatDoanhThu;
import qtc.project.banhangnhanh.admin.fragment.report.thongkebanhang.tomtatdoanhthu.thongke.FragmentThongKe;
import qtc.project.banhangnhanh.admin.fragment.report.thongkebanhang.tomtatdoanhthu.tongdoanhthu.FragmentTongDoanhThu;
import qtc.project.banhangnhanh.admin.fragment.report.thongkekho.tonkho_vs_doanhthu.FragmentTK_TonKho_VS_DoanhThu;
import qtc.project.banhangnhanh.admin.fragment.report.thongkekho.xuatnhapkho.FragmentBaoCaoXuatNhapKho;
import qtc.project.banhangnhanh.admin.model.BaseResponseModel;
import qtc.project.banhangnhanh.admin.model.EmployeeModel;
import qtc.project.banhangnhanh.admin.model.ProductListModel;
import qtc.project.banhangnhanh.admin.model.SupplierModel;
import qtc.project.banhangnhanh.admin.views.action_bar.base_main_actionbar.BaseMainActionbarViewInterface;
import qtc.project.banhangnhanh.admin.views.activity.home_activity.HomeActivityView;
import qtc.project.banhangnhanh.admin.views.activity.home_activity.HomeActivityViewCallback;
import qtc.project.banhangnhanh.admin.views.activity.home_activity.HomeActivityViewInterface;

public class HomeActivity extends BaseFragmentActivity<HomeActivityViewInterface, BaseMainActionbarViewInterface, BaseParameters> implements HomeActivityViewCallback {
    private final int OPEN_MEDIA_PICKER = 10101;
    private final int OPEN_MEDIA_PICKER_PERMISSION = 10102;
    private static final int CAMERA_REQUEST = 10103;
    public static final int EMAIL_SEND = 10104;
    private final int MY_CAMERA_PERMISSION_CODE = 10104;
    public static final int MY_BARCODE_PERMISSION_CODE = 10105;
    private ArrayList<String> permissions = new ArrayList<>();
    private PermissionUtils permissionUtils;

    @Override
    protected void initialize(Bundle savedInstanceState) {
        view.init(this, this);

        // [END subscribe_topics]

        setLayoutMain();

        checkUpdateAppVersion();
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
        new CheckAppVersionAsync(currentVersion, HomeActivity.this).execute();
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
        checkStatusUser();
        checkSignOut();
    }

    private void checkSignOut() {
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

                        AlertDialog.Builder alert = new AlertDialog.Builder(HomeActivity.this);
                        alert.setView(popupView);
                        AlertDialog dialog = alert.create();
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.show();

                        custom_confirm_button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                EmployeeModel employeeModel = AppProvider.getPreferences().getUserModel();
                                if (employeeModel != null) {
                                    if (employeeModel.getLevel().equalsIgnoreCase("1")){
                                        FirebaseMessaging.getInstance().unsubscribeFromTopic("pos_notifycation_employee_" + employeeModel.getId()+"_"+employeeModel.getId_business());
                                        FirebaseMessaging.getInstance().unsubscribeFromTopic("pos_notifycation_employee_" + employeeModel.getId_business());
                                    }
                                    else {
                                        FirebaseMessaging.getInstance().unsubscribeFromTopic("pos_notifycation_app_admin_"+employeeModel.getId_business());
                                        FirebaseMessaging.getInstance().unsubscribeFromTopic("pos_notifycation_app_admin");
                                    }
                                }
                                AppProvider.getPreferences().saveUserModel(null);
                                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
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

    private void checkStatusUser() {
        EmployeeRequest.ApiParams params = new EmployeeRequest.ApiParams();
        params.type_manager = "check_status";
        params.id_employee = AppProvider.getPreferences().getUserModel().getId();

        AppProvider.getApiManagement().call(EmployeeRequest.class, params, new ApiRequest.ApiCallback<BaseResponseModel<EmployeeModel>>() {
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
                        content_text.setText("Tài khoản đã bị khóa.");
                        //title_text.setText("Cảnh báo");
                        //content_text.setText("Bạn có muốn xóa khách hàng này không?");

                        AlertDialog.Builder alert = new AlertDialog.Builder(HomeActivity.this);
                        alert.setView(popupView);
                        AlertDialog dialog = alert.create();
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.show();

                        custom_confirm_button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                EmployeeModel employeeModel = AppProvider.getPreferences().getUserModel();
                                if (employeeModel != null) {
                                    if (employeeModel.getLevel().equalsIgnoreCase("1")){
                                        FirebaseMessaging.getInstance().unsubscribeFromTopic("pos_notifycation_employee_" + employeeModel.getId()+"_"+employeeModel.getId_business());
                                        FirebaseMessaging.getInstance().unsubscribeFromTopic("pos_notifycation_employee_" + employeeModel.getId_business());
                                    }
                                    else {
                                        FirebaseMessaging.getInstance().unsubscribeFromTopic("pos_notifycation_app_admin_"+employeeModel.getId_business());
                                        FirebaseMessaging.getInstance().unsubscribeFromTopic("pos_notifycation_app_admin");
                                    }
                                }
                                AppProvider.getPreferences().saveUserModel(null);
                                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
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
//    public void onKeyboardDissmiss(FroceSignoutEvent event) {
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
//            AlertDialog.Builder alert = new AlertDialog.Builder(HomeActivity.this);
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
//                        FirebaseMessaging.getInstance().unsubscribeFromTopic("pos_notifycation_employee_" + employeeModel.getId()+"_"+employeeModel.getId_business());
//                        FirebaseMessaging.getInstance().unsubscribeFromTopic("pos_notifycation_employee_" + employeeModel.getId_business());
//                    }
//                    AppProvider.getPreferences().saveUserModel(null);
//                    startActivity(new Intent(HomeActivity.this, LoginActivity.class));
//                    dialog.dismiss();
//                    finish();
//
//                }
//            });
//        }
//    }

    @SuppressWarnings("unused")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onKeyboardForcSignout(FroceSignoutEvent event) {
        if (view != null) {
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

            AlertDialog.Builder alert = new AlertDialog.Builder(HomeActivity.this);
            alert.setView(popupView);
            AlertDialog dialog = alert.create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();


            custom_confirm_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EmployeeModel employeeModel = AppProvider.getPreferences().getUserModel();
                    if (employeeModel != null) {
                        if (employeeModel.getLevel().equalsIgnoreCase("1")){
                            FirebaseMessaging.getInstance().unsubscribeFromTopic("pos_notifycation_employee_" + employeeModel.getId()+"_"+employeeModel.getId_business());
                            FirebaseMessaging.getInstance().unsubscribeFromTopic("pos_notifycation_employee_" + employeeModel.getId_business());
                        }
                        else {
                            FirebaseMessaging.getInstance().unsubscribeFromTopic("pos_notifycation_app_admin_"+employeeModel.getId_business());
                            FirebaseMessaging.getInstance().unsubscribeFromTopic("pos_notifycation_app_admin");
                        }
                    }
                    AppProvider.getPreferences().saveUserModel(null);
                    startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                    dialog.dismiss();
                    finish();

                }
            });
        }
    }

    public void toggleNav(){
        view.toggleNav();
    }

    public void alerUpdate() {
        LayoutInflater layoutInflater = getLayoutInflater();
        View popupView = layoutInflater.inflate(R.layout.alert_dialog_success, null);
        TextView title_text = popupView.findViewById(R.id.title_text);
        TextView content_text = popupView.findViewById(R.id.content_text);
        Button custom_confirm_button = popupView.findViewById(R.id.custom_confirm_button);

        title_text.setText("Xác nhận");
        content_text.setText("Bạn đã cập nhật thành công!");

        AlertDialog.Builder alert = new AlertDialog.Builder(HomeActivity.this);
        alert.setView(popupView);
        AlertDialog dialog = alert.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        custom_confirm_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public void alertSuccess() {
        LayoutInflater layoutInflater = getLayoutInflater();
        View popupView = layoutInflater.inflate(R.layout.alert_dialog_success, null);
        TextView title_text = popupView.findViewById(R.id.title_text);
        TextView content_text = popupView.findViewById(R.id.content_text);
        Button custom_confirm_button = popupView.findViewById(R.id.custom_confirm_button);

        title_text.setText("Xác nhận");
        content_text.setText("Bạn đã xóa sản phẩm thành công!");

        AlertDialog.Builder alert = new AlertDialog.Builder(HomeActivity.this);
        alert.setView(popupView);
        AlertDialog dialog = alert.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        custom_confirm_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBack();
                dialog.dismiss();
            }
        });
    }

    public void alertDelete() {
        LayoutInflater layoutInflater = getLayoutInflater();
        View popupView = layoutInflater.inflate(R.layout.alert_dialog_waiting, null);
        TextView title_text = popupView.findViewById(R.id.title_text);
        TextView content_text = popupView.findViewById(R.id.content_text);
        Button cancel_button = popupView.findViewById(R.id.cancel_button);
        Button custom_confirm_button = popupView.findViewById(R.id.custom_confirm_button);

        title_text.setText("Cảnh báo");
        content_text.setText("Bạn có muốn xóa sản phẩm này không?");

        AlertDialog.Builder alert = new AlertDialog.Builder(HomeActivity.this);
        alert.setView(popupView);
        AlertDialog dialog = alert.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }


    private void setLayoutMain() {

        FullScreencall();
        replaceFragment(new FragmentHome(), true, Animation.SLIDE_IN_OUT);
    }


    @Override
    public void onBackPressed() {
        BaseFragment baseFragment = getCurrentFragment();
        if (baseFragment instanceof FragmentHome) {
            super.onBackPressed();
        } else if (baseFragment instanceof FragmentSanPhamBanChay) {
            checkBack();
        } else if (baseFragment instanceof FragmentOrderManager) {
            checkBack();
        } else if (baseFragment instanceof FragmentBaoCaoXuatNhapKho) {
            checkBack();
        } else if (baseFragment instanceof FragmentDoiTraHangHoa) {
            checkBack();
        }
    }

    public void setDataSearchProduct(ProductListModel[] list, String name, String id) {
        BaseFragment baseFragment = getCurrentFragment();
        if (baseFragment instanceof FragmentProductList) {
            ((FragmentProductList) baseFragment).setDataSearchProduct(list, name, id);
        }
        if (baseFragment instanceof FragmentFilterSanPham) {
            ((FragmentFilterSanPham) baseFragment).setOnBack();
        }
    }

    public void setDataNhaCungUng(SupplierModel model) {
        BaseFragment baseFragment = getCurrentFragment();
        if (baseFragment instanceof FragmentChiTietLoHang) {
            ((FragmentChiTietLoHang) baseFragment).setDataNhaCungUng(model);
        }

        if (baseFragment instanceof FragmentCreateLoHang) {
            ((FragmentCreateLoHang) baseFragment).setDataNhaCungUng(model);
        }

        if (baseFragment instanceof FragmentDonTraHang) {
            ((FragmentDonTraHang) baseFragment).setOnBack();
        }
    }

    public void setDataProduct(ProductListModel model) {
        BaseFragment baseFragment = getCurrentFragment();
        if (baseFragment instanceof FragmentCreateLoHang) {
            ((FragmentCreateLoHang) baseFragment).setDataProduct(model);
        }
//        if (baseFragment instanceof FragmentCreateLoHang) {
//            ((FragmentCreateLoHang) baseFragment).setOnBack();
//        }
    }

    public void setDataDate(String nam, String thang, int ngay) {
        BaseFragment baseFragment = getCurrentFragment();
        if (baseFragment instanceof FragmentTongDoanhThu) {
            ((FragmentTongDoanhThu) baseFragment).filterDataTheoThang(nam, thang, ngay);
        }

        if (baseFragment instanceof FragmentTK_TonKho_VS_DoanhThu) {
            ((FragmentTK_TonKho_VS_DoanhThu) baseFragment).filterDataDate(nam, thang, ngay);
        }

        if (baseFragment instanceof FragmentDoanhThuTheoKhachHang) {
            ((FragmentDoanhThuTheoKhachHang) baseFragment).filterDataTheoThang(nam, thang, ngay);
        }

        if (baseFragment instanceof FragmentDoanhThuTheoSp) {
            ((FragmentDoanhThuTheoSp) baseFragment).filterDataTheoThang(nam, thang, ngay);
        }


        if (baseFragment instanceof FragmentTomTatDoanhThu) {
            ((FragmentTomTatDoanhThu) baseFragment).onBackProgress();
        }
    }

    public void setDataYear(String nam) {
        BaseFragment baseFragment = getCurrentFragment();
        if (baseFragment instanceof FragmentThongKe) {
            ((FragmentThongKe) baseFragment).filterDataYear(nam);
        }

        if (baseFragment instanceof FragmentDoanhThuTheoKhachHang) {
            ((FragmentDoanhThuTheoKhachHang) baseFragment).filterDataYear(nam);
        }
        if (baseFragment instanceof FragmentDoanhThuTheoSp) {
            ((FragmentDoanhThuTheoSp) baseFragment).filterDataYear(nam);
        }

        if (baseFragment instanceof FragmentTomTatDoanhThu) {
            ((FragmentTomTatDoanhThu) baseFragment).onBackProgress();
        }
    }

    public void setDataDateSP_BChay(String nam, String thang) {
        BaseFragment baseFragment = getCurrentFragment();
        if (baseFragment instanceof FragmentSanPhamBanChay) {
            ((FragmentSanPhamBanChay) baseFragment).filterDataTheoThang(nam, thang);
        }

        if (baseFragment instanceof FragmentBaoCaoXuatNhapKho) {
            ((FragmentBaoCaoXuatNhapKho) baseFragment).filterDataDate(nam, thang);
        }

//        if (baseFragment instanceof FragmentSanPhamBanChay) {
//            ((FragmentSanPhamBanChay) baseFragment).onBackProgress();
//        }
    }

    public void setDataDateOrder(String dateStartSelected, String dateEndSelected) {
        BaseFragment baseFragment = getCurrentFragment();
        if (baseFragment instanceof FragmentOrderManager) {
            ((FragmentOrderManager) baseFragment).filterDataDate(dateStartSelected, dateEndSelected);
        }
//        if (baseFragment instanceof FragmentOrderManager) {
//            ((FragmentOrderManager) baseFragment).onBackProgress();
//        }
    }

    private int isShowContainer = 0;

    public void checkBack() {
        FullScreencall();

        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();
        }

    }

    public void checkFragment() {

        FragmentManager fm = getSupportFragmentManager();

        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();

        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected HomeActivityViewInterface getViewInstance() {
        return new HomeActivityView();
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
                    if (employeeModel.getLevel().equalsIgnoreCase("2")){
                        FirebaseMessaging.getInstance().unsubscribeFromTopic("pos_notifycation_app_admin");
                        FirebaseMessaging.getInstance().unsubscribeFromTopic("pos_notifycation_app_admin_" + employeeModel.getId_business());
                    }
                    else {
                        FirebaseMessaging.getInstance().unsubscribeFromTopic("pos_notifycation_employee_" + employeeModel.getId() + "_" + employeeModel.getId_business());
                        FirebaseMessaging.getInstance().unsubscribeFromTopic("pos_notifycation_employee_" + employeeModel.getId_business());
                    }
                }
                AppProvider.getPreferences().clear();
                finish();
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
            }
        }, KAlertDialog.WARNING_TYPE);
    }

    @Override
    public void onClickLogin(String oldPass, String newPass, String employee_id) {
        showProgress();
        if (oldPass != null && newPass != null) {
            UpdatePassEmployeeRequest.ApiParams params = new UpdatePassEmployeeRequest.ApiParams();
            params.id_business = AppProvider.getPreferences().getUserModel().getId_business();
            params.id_employee = employee_id;
            params.old_password = oldPass;
            params.new_password = newPass;

            AppProvider.getApiManagement().call(UpdatePassEmployeeRequest.class, params, new ApiRequest.ApiCallback<BaseResponseModel<EmployeeModel>>() {
                @Override
                public void onSuccess(BaseResponseModel<EmployeeModel> body) {
                    dismissProgress();
                    if (body.getSuccess().equals("true")) {
                        view.updatePopup();
                    } else if (body.getSuccess().equals("false")) {
                        Toast.makeText(getApplicationContext(), "" + body.getMessage(), Toast.LENGTH_SHORT).show();
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

    public void showAlr() {
        new SweetAlertDialog(HomeActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Xác nhận")
                .setContentText("Bạn đã cập nhật thành công!")
                .setConfirmText("Xác nhận")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                    }
                })
                .show();
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

    public void changeToActivitySelectImage() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(getApplicationContext()), Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, OPEN_MEDIA_PICKER_PERMISSION);
            } else {

                intentGallerySelectImage();
            }

        } else {
            intentGallerySelectImage();
        }
    }

    private File photoFile = null;

    public void captureImageFromCamera() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(getApplicationContext()), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, MY_CAMERA_PERMISSION_CODE);
            } else {

                intentCameraCaptureImage();
            }

        } else {
            intentCameraCaptureImage();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == OPEN_MEDIA_PICKER_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            intentGallerySelectImage();
        } else if (requestCode == MY_CAMERA_PERMISSION_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (cameraIntent.resolveActivity(getPackageManager()) != null) {
//            Create the File where the photo should go
                try {
                    photoFile = createMediaFile();

                } catch (IOException ex) {
                    // Error occurred while creating the File
                    ex.printStackTrace();

                }
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (photoFile != null) {

                            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                            StrictMode.setVmPolicy(builder.build());

                            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                            startActivityForResult(cameraIntent, CAMERA_REQUEST);
                        } else {
                            showAlert(getString(R.string.error_load_file_image), KAlertDialog.ERROR_TYPE);
                        }
                    }
                }, 300);
                // Continue only if the File was successfully created

            }
        } else if (requestCode == MY_BARCODE_PERMISSION_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            BaseFragment baseFragment = getCurrentFragment();
            if (baseFragment instanceof FragmentProductListDetail)
                ((FragmentProductListDetail) baseFragment).gotoQr_BarcodeActivity();
        }
    }

    private PermissionUtils.PermissionResultCallback permissionResultCallback = new PermissionUtils.PermissionResultCallback() {

        @Override
        public void PermissionGranted(int request_code) {

            Intent intent = new Intent(HomeActivity.this, Gallery.class);
            // Set the title
            intent.putExtra("title", "Chọn hình ảnh");
            // Mode 1 for both images and videos selection, 2 for images only and 3 for videos!
            intent.putExtra("mode", 2);
            intent.putExtra("maxSelection", 1); // Optional
            startActivityForResult(intent, OPEN_MEDIA_PICKER);

        }

        @Override
        public void PartialPermissionGranted(int request_code, ArrayList<String> granted_permissions) {

        }

        @Override
        public void PermissionDenied(int request_code) {

        }

        @Override
        public void NeverAskAgain(int request_code) {

        }
    };

    private void intentGallerySelectImage() {
        Intent intent = new Intent(HomeActivity.this, Gallery.class);
        // Set the title
        intent.putExtra("title", "Chọn hình ảnh");
        // Mode 1 for both images and videos selection, 2 for images only and 3 for videos!
        intent.putExtra("mode", 2);
        intent.putExtra("maxSelection", 1); // Optional
        startActivityForResult(intent, OPEN_MEDIA_PICKER);
    }

    private void intentCameraCaptureImage() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
//            Create the File where the photo should go
            try {
                photoFile = createMediaFile();

            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {

                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());

                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                showAlert(getString(R.string.error_load_file_image), KAlertDialog.ERROR_TYPE);
            }
        } else {
            showAlert("Camera không khả dụng.", KAlertDialog.ERROR_TYPE);
        }
    }

    private File createMediaFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        File mediaFile = null;

        String rootPath = Environment.getExternalStorageDirectory()
                .getAbsolutePath() + "/POS_Images/";
        File root = new File(rootPath);
        if (!root.exists()) {
            root.mkdirs();
        }
//        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String fileName = "JPEG_" + timeStamp + "_";
        mediaFile = File.createTempFile(fileName,  // prefix
                ".jpg",         // suffix
                root      // directory
        );

        return mediaFile;
    }

    public void deleteTempMedia() {
        String rootPath = Environment.getExternalStorageDirectory()
                .getAbsolutePath() + "/POS_Images/";
        File root = new File(rootPath);
        if (root.exists()) {
            String[] children = root.list();
            if (children != null && children.length > 0) {
                for (int i = 0; i < children.length; i++) {
                    new File(root, children[i]).delete();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == OPEN_MEDIA_PICKER) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> selectionResult = data.getStringArrayListExtra("result");
                if (selectionResult != null && selectionResult.size() > 0) {
                    BaseFragment fragment = getCurrentFragment();
                    if (fragment instanceof FragmentCategoryProductDetail) {
                        ((FragmentCategoryProductDetail) fragment).setImageSelected(selectionResult.get(0));
                    }
                    if (fragment instanceof FragmentCreateProductCategory) {
                        ((FragmentCreateProductCategory) fragment).setImageSelected(selectionResult.get(0));
                    }
                    if (fragment instanceof FragmentLevelCustomerDetail) {
                        ((FragmentLevelCustomerDetail) fragment).setImageSelected(selectionResult.get(0));
                    }
                    if (fragment instanceof FragmentCreateLevelCustomer) {
                        ((FragmentCreateLevelCustomer) fragment).setImageSelected(selectionResult.get(0));
                    }
                    if (fragment instanceof FragmentProductListDetail) {
                        ((FragmentProductListDetail) fragment).setImageSelected(selectionResult.get(0));
                    }
                    if (fragment instanceof FragmentCreateProduct) {
                        ((FragmentCreateProduct) fragment).setImageSelected(selectionResult.get(0));
                    }
                }
            }
        } else if (requestCode == CAMERA_REQUEST) {
            if (resultCode == RESULT_OK) {
                BaseFragment fragment = getCurrentFragment();

                if (fragment instanceof FragmentCategoryProductDetail) {
                    if (photoFile != null)
                        ((FragmentCategoryProductDetail) fragment).setImageSelected(photoFile.getAbsolutePath());
                }
                if (fragment instanceof FragmentCreateProductCategory) {
                    if (photoFile != null)
                        ((FragmentCreateProductCategory) fragment).setImageSelected(photoFile.getAbsolutePath());
                }
                if (fragment instanceof FragmentLevelCustomerDetail) {
                    if (photoFile != null)
                        ((FragmentLevelCustomerDetail) fragment).setImageSelected(photoFile.getAbsolutePath());
                }
                if (fragment instanceof FragmentCreateLevelCustomer) {
                    if (photoFile != null)
                        ((FragmentCreateLevelCustomer) fragment).setImageSelected(photoFile.getAbsolutePath());
                }
                if (fragment instanceof FragmentProductListDetail) {
                    if (photoFile != null)
                        ((FragmentProductListDetail) fragment).setImageSelected(photoFile.getAbsolutePath());
                }
                if (fragment instanceof FragmentCreateProduct) {
                    if (photoFile != null)
                        ((FragmentCreateProduct) fragment).setImageSelected(photoFile.getAbsolutePath());
                }
            }
        } else if (requestCode == EMAIL_SEND) {
            if (resultCode == RESULT_OK || resultCode == RESULT_CANCELED) {
                BaseFragment fragment = getCurrentFragment();

//                if (fragment instanceof FragmentStatictisReportDetail) {
//                    ((FragmentStatictisReportDetail) fragment).deleteTempMedia();
//                }
            }
        }
    }

    public void setDataSearch(String thang, String nam) {
        BaseFragment baseFragment = getCurrentFragment();
        if (baseFragment instanceof FragmentDoiTraHangHoa) {
            ((FragmentDoiTraHangHoa) baseFragment).filterDataDate(thang, nam);
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
                showCustomerImageAndBgButtonConfirmAlert(title, mess, titleButtonConfirm, R.drawable.button_confirm_alert_dialog, titleButtonCancel, R.drawable.button_cancel_alert_dialog, actionConfirm, actionCancel, R.drawable.ic_img_alert_warning);
                break;
            case -1:
                showCustomerImageAndBgButtonConfirmAlert(title, mess, titleButtonConfirm, R.drawable.button_confirm_alert_dialog, titleButtonCancel, R.drawable.button_cancel_alert_dialog, actionConfirm, actionCancel, R.drawable.ic_img_alert_warning_logout);
                break;
        }

    }
}