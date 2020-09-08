package qtc.project.banhangnhanh.sale.view.fragment.order.detail;

import b.laixuantam.myaarlibrary.base.BaseViewInterface;
import qtc.project.banhangnhanh.activity.SaleHomeActivity;
import qtc.project.banhangnhanh.sale.model.OrderModel;

public interface FragmentOrderDetailSaleViewInterface extends BaseViewInterface {

    void init(SaleHomeActivity activity,FragmentOrderDetailSaleViewCallback callback);

    void initView(OrderModel model);

    void showSuccess();
}
