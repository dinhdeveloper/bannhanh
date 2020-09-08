package qtc.project.banhangnhanh.sale.view.fragment.customer;

import java.util.ArrayList;

import b.laixuantam.myaarlibrary.base.BaseViewInterface;
import qtc.project.banhangnhanh.activity.SaleHomeActivity;
import qtc.project.banhangnhanh.admin.model.CustomerModel;

public interface FragmentCustomerSaleViewInterface extends BaseViewInterface {

    void init(SaleHomeActivity activity,FragmentCustomerSaleViewCallback callback);

    void initRecyclerViewCustomer(ArrayList<CustomerModel> list);
}
