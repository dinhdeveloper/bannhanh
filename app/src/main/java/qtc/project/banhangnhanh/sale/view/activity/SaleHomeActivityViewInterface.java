package qtc.project.banhangnhanh.sale.view.activity;

import b.laixuantam.myaarlibrary.base.BaseViewInterface;
import qtc.project.banhangnhanh.activity.SaleHomeActivity;

public interface SaleHomeActivityViewInterface extends BaseViewInterface {

    void init(SaleHomeActivity activity,SaleHomeActivityViewCallback callback);

    void openDrawer();

    void closeDrawer();

    boolean isDrawerOpen();

    void toggleNav();

}
