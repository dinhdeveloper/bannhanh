package qtc.project.banhangnhanh.sale.view.fragment.level.byId;

import b.laixuantam.myaarlibrary.base.BaseViewInterface;
import qtc.project.banhangnhanh.activity.SaleHomeActivity;
import qtc.project.banhangnhanh.admin.model.CustomerModel;

public interface FragmentCustomerSaleListViewInterface extends BaseViewInterface {

    void init(SaleHomeActivity activity,FragmentCustomerSaleListViewCallback callback);
    void initView(String name, CustomerModel[] list);

    void resetData();

    void clearnData();
}
