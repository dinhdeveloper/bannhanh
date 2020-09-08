package qtc.project.banhangnhanh.admin.views.fragment.history.history.detail;

import b.laixuantam.myaarlibrary.base.BaseViewInterface;
import qtc.project.banhangnhanh.activity.HomeActivity;
import qtc.project.banhangnhanh.admin.model.OrderCustomerModel;

public interface FragmentOrderDetailCustomerViewInterface extends BaseViewInterface {
    void init(HomeActivity activity, FragmentOrderDetailCustomerViewCallback callback);

    void sentDataToView(OrderCustomerModel model);
}
