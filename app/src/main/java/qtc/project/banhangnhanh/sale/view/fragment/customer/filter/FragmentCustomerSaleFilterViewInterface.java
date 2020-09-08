package qtc.project.banhangnhanh.sale.view.fragment.customer.filter;

import b.laixuantam.myaarlibrary.base.BaseViewInterface;
import qtc.project.banhangnhanh.activity.SaleHomeActivity;
import qtc.project.banhangnhanh.admin.model.CustomerModel;

public interface FragmentCustomerSaleFilterViewInterface extends BaseViewInterface {

    void init(SaleHomeActivity activity,FragmentCustomerSaleFilterViewCallback callback);

    void initCustomer(CustomerModel[] list);

    void setNoMoreLoading();

    void clearnData();
}
