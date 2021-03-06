package qtc.project.banhangnhanh.admin.views.activity.home_activity;

import android.support.v4.app.Fragment;

public interface HomeActivityViewCallback {

    void onClickBottomBarMenuHome();

    void onClickBottomBarMenuCategory();

    void onClickBottomBarMenuAccount();

    void onClickBottomBarMenuShoppingCart();

    void onClickBottomBarMenuTransaction();

    void onClickBottomBarMenuOrder();

    void onClickItemNav(Fragment fragment);

    void logOut();

    void onClickLogin(String oldPass, String newPass,String employee_id);
}
