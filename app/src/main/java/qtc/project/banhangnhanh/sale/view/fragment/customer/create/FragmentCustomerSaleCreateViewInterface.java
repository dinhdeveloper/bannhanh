package qtc.project.banhangnhanh.sale.view.fragment.customer.create;

import b.laixuantam.myaarlibrary.base.BaseViewInterface;
import qtc.project.banhangnhanh.activity.SaleHomeActivity;

public interface FragmentCustomerSaleCreateViewInterface extends BaseViewInterface {

    void init(SaleHomeActivity activity,FragmentCustomerSaleCreateViewCallback callback);

    void showDiaLogSucess();
}
