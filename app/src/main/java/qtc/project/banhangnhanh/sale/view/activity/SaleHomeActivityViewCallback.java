package qtc.project.banhangnhanh.sale.view.activity;

import android.support.v4.app.Fragment;

public interface SaleHomeActivityViewCallback {
    void onClickBottomBarMenuHome();

    void onClickBottomBarMenuCategory();

    void onClickBottomBarMenuAccount();

    void onClickBottomBarMenuShoppingCart();

    void onClickBottomBarMenuTransaction();

    void onClickBottomBarMenuOrder();

    void onClickItemNav(Fragment fragment);

    void logOut();
}
