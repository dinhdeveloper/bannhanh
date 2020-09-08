package qtc.project.banhangnhanh.admin.views.fragment.levelcustomer.detail;

import java.util.ArrayList;

import b.laixuantam.myaarlibrary.base.BaseViewInterface;
import qtc.project.banhangnhanh.activity.HomeActivity;
import qtc.project.banhangnhanh.admin.model.CustomerModel;
import qtc.project.banhangnhanh.admin.model.LevelCustomerModel;

public interface FragmentLevelCustomerDetailViewInterface extends BaseViewInterface {
    void init(HomeActivity activity, FragmentLevelCustomerDetailViewCallback callback);

    void sendDataToView(LevelCustomerModel model);

    void setDataLevelImage(String filePath);

    void initCustomer(ArrayList<CustomerModel> list);

}
