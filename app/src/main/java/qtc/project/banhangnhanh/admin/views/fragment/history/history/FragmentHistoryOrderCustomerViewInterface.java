package qtc.project.banhangnhanh.admin.views.fragment.history.history;

import java.util.ArrayList;

import b.laixuantam.myaarlibrary.base.BaseViewInterface;
import qtc.project.banhangnhanh.activity.HomeActivity;
import qtc.project.banhangnhanh.admin.model.OrderCustomerModel;

public interface FragmentHistoryOrderCustomerViewInterface extends BaseViewInterface {
    void init(HomeActivity activity,FragmentHistoryOrderCustomerViewCallback callback);

    void sendDataToView(ArrayList<OrderCustomerModel> model, String id_customer);
}
