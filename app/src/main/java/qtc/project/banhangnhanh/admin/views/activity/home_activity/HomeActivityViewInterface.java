package qtc.project.banhangnhanh.admin.views.activity.home_activity;

import b.laixuantam.myaarlibrary.base.BaseViewInterface;
import qtc.project.banhangnhanh.activity.HomeActivity;

public interface HomeActivityViewInterface extends BaseViewInterface {

    void init(HomeActivity activity, HomeActivityViewCallback callback);

    void openDrawer();

    void closeDrawer();

    boolean isDrawerOpen();

    void updatePopup();

    void toggleNav();
}
