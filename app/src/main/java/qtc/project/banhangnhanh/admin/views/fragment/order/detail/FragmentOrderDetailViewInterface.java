package qtc.project.banhangnhanh.admin.views.fragment.order.detail;

import b.laixuantam.myaarlibrary.base.BaseViewInterface;
import qtc.project.banhangnhanh.activity.HomeActivity;
import qtc.project.banhangnhanh.admin.model.OrderCustomerModel;

public interface FragmentOrderDetailViewInterface extends BaseViewInterface {

    void init(HomeActivity activity,FragmentOrderDetailViewCallback callback);

    void sentDataToView(OrderCustomerModel model);
}
