package qtc.project.banhangnhanh.admin.views.fragment.employee.lichsubanhang.detail;

import b.laixuantam.myaarlibrary.base.BaseViewInterface;
import qtc.project.banhangnhanh.activity.HomeActivity;
import qtc.project.banhangnhanh.admin.model.OrderCustomerModel;

public interface FragmentOrderDetailEmployeeViewInterface extends BaseViewInterface {
    void init(HomeActivity activity,FragmentOrderDetailEmployeeViewCallback callback);

    void sentDataToView(OrderCustomerModel model);
}
