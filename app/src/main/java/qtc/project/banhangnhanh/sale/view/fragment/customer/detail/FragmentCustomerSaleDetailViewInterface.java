package qtc.project.banhangnhanh.sale.view.fragment.customer.detail;

import b.laixuantam.myaarlibrary.base.BaseViewInterface;
import qtc.project.banhangnhanh.activity.SaleHomeActivity;
import qtc.project.banhangnhanh.admin.model.CustomerModel;

public interface FragmentCustomerSaleDetailViewInterface extends BaseViewInterface {

    void init(SaleHomeActivity activity,FragmentCustomerSaleDetailViewCallback callback);

    void setDataCustomerDetail(CustomerModel model);

    void resetView();
}
