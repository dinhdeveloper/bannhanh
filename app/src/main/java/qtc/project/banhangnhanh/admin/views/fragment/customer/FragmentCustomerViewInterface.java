package qtc.project.banhangnhanh.admin.views.fragment.customer;

import java.util.ArrayList;

import b.laixuantam.myaarlibrary.base.BaseViewInterface;
import qtc.project.banhangnhanh.activity.HomeActivity;
import qtc.project.banhangnhanh.admin.model.CustomerModel;

public interface FragmentCustomerViewInterface extends BaseViewInterface {
    void init(HomeActivity activity, FragmentCustomerViewCallback callback);

    void mappingRecyclerView(ArrayList<CustomerModel> list);
}
