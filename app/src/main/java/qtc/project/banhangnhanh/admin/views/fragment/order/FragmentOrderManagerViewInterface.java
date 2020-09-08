package qtc.project.banhangnhanh.admin.views.fragment.order;

import java.util.ArrayList;

import b.laixuantam.myaarlibrary.base.BaseViewInterface;
import qtc.project.banhangnhanh.activity.HomeActivity;
import qtc.project.banhangnhanh.admin.model.OrderCustomerModel;

public interface FragmentOrderManagerViewInterface extends BaseViewInterface {
    void init(HomeActivity activity,FragmentOrderManagerViewCallback callback);

    void initRecyclerViewOrder(ArrayList<OrderCustomerModel> list, String dateStart, String dateEnd);
}
