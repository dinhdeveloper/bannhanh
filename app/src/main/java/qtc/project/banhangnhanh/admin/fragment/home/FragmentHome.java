package qtc.project.banhangnhanh.admin.fragment.home;

import android.content.Intent;

import com.google.firebase.messaging.FirebaseMessaging;

import java.sql.Date;
import java.util.Calendar;

import b.laixuantam.myaarlibrary.base.BaseFragment;
import b.laixuantam.myaarlibrary.base.BaseParameters;
import b.laixuantam.myaarlibrary.widgets.dialog.alert.KAlertDialog;
import qtc.project.banhangnhanh.activity.HomeActivity;
import qtc.project.banhangnhanh.activity.LoginActivity;
import qtc.project.banhangnhanh.admin.dependency.AppProvider;
import qtc.project.banhangnhanh.admin.event.DoLogOutEvent;
import qtc.project.banhangnhanh.admin.model.EmployeeModel;
import qtc.project.banhangnhanh.admin.views.fragment.home.FragmentHomeView;
import qtc.project.banhangnhanh.admin.views.fragment.home.FragmentHomeViewCallback;
import qtc.project.banhangnhanh.admin.views.fragment.home.FragmentHomeViewInterface;

public class FragmentHome extends BaseFragment<FragmentHomeViewInterface, BaseParameters> implements FragmentHomeViewCallback {
    HomeActivity activity;
    EmployeeModel model;
    @Override
    protected void initialize() {
        activity = (HomeActivity) getActivity();
        view.init(activity, this);
         model = AppProvider.getPreferences().getUserModel();
    }
    @Override
    public void onStart() {
        super.onStart();
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String date1 = year + "-" + month + "-" + day;
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        // Định nghĩa 2 mốc thời gian ban đầu
        Date dateCurrrent = Date.valueOf(date1);
        Date dateEnd = Date.valueOf(model.getStore_end());
        cal1.setTime(dateCurrrent);
        cal2.setTime(dateEnd);
        if (dateCurrrent.compareTo(dateEnd) > 0) {
            String message = "Tài khoản của bạn đã hết hạn vui lòng liên hệ với nhà cung cấp để gia hạn hợp đồng";
            activity.showConfirmAlert("Liên hệ", message, "Liên hệ", null, new KAlertDialog.KAlertClickListener() {
                @Override
                public void onClick(KAlertDialog kAlertDialog) {
                    kAlertDialog.dismiss();
                    doLogOut();
                }
            }, null, -1);
        }
    }

    private void doLogOut() {
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
        startActivity(new Intent(activity, LoginActivity.class));
        DoLogOutEvent.post();
    }

    @Override
    protected FragmentHomeViewInterface getViewInstance() {
        return new FragmentHomeView();
    }

    @Override
    protected BaseParameters getParametersContainer() {
        return null;
    }

}
