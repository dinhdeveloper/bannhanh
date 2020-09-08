package qtc.project.banhangnhanh.admin.views.fragment.levelcustomer.create;

import b.laixuantam.myaarlibrary.base.BaseViewInterface;
import qtc.project.banhangnhanh.activity.HomeActivity;

public interface FragmentCreateLevelCustomerViewInterface extends BaseViewInterface {
    void init(HomeActivity activity, FragmentCreateLevelCustomerViewCallback callback);

    void setDataProductImage(String filePath);

    void showPopup();
}
