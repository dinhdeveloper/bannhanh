package qtc.project.banhangnhanh.admin.views.fragment.customer.detail;

import java.util.ArrayList;

import b.laixuantam.myaarlibrary.base.BaseViewInterface;
import qtc.project.banhangnhanh.activity.HomeActivity;
import qtc.project.banhangnhanh.activity.SaleHomeActivity;
import qtc.project.banhangnhanh.admin.model.CustomerModel;
import qtc.project.banhangnhanh.admin.model.LevelCustomerModel;

public interface FragmentCustomerDetailViewInterface extends BaseViewInterface {

    void init(HomeActivity activity, FragmentCustomerDetailViewCallback callback);

    void sentDataToView(CustomerModel model);

    void mappingPopup(ArrayList<LevelCustomerModel> list);

    void confirmDialog();

    void showDialogDeleteSuccess();
}
