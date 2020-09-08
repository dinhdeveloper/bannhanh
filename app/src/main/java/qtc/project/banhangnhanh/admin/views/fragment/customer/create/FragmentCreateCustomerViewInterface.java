package qtc.project.banhangnhanh.admin.views.fragment.customer.create;

import java.util.ArrayList;

import b.laixuantam.myaarlibrary.base.BaseViewInterface;
import qtc.project.banhangnhanh.activity.HomeActivity;
import qtc.project.banhangnhanh.admin.model.LevelCustomerModel;

public interface FragmentCreateCustomerViewInterface extends BaseViewInterface {
    void init(HomeActivity activity,FragmentCreateCustomerViewCallback callback);

    void mappingPopup(ArrayList<LevelCustomerModel> list);

    void showAlert();
}
