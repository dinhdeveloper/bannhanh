package qtc.project.banhangnhanh.admin.views.fragment.employee.lichsubanhang;

import java.util.ArrayList;

import b.laixuantam.myaarlibrary.base.BaseViewInterface;
import qtc.project.banhangnhanh.activity.HomeActivity;
import qtc.project.banhangnhanh.admin.model.OrderCustomerModel;

public interface FragmentHistorySaleEmployeeViewInterface extends BaseViewInterface {
    void init(HomeActivity activity,FragmentHistorySaleEmployeeViewCallback callback);

    void sendDataToView(ArrayList<OrderCustomerModel> list);
}
