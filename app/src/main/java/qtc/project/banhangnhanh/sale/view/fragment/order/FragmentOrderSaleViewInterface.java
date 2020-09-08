package qtc.project.banhangnhanh.sale.view.fragment.order;

import b.laixuantam.myaarlibrary.base.BaseViewInterface;
import qtc.project.banhangnhanh.activity.SaleHomeActivity;
import qtc.project.banhangnhanh.sale.model.OrderModel;

public interface FragmentOrderSaleViewInterface extends BaseViewInterface {

    void init(SaleHomeActivity activity,FragmentOrderSaleViewCallback callback);

    void initRecyclerViewOrder(OrderModel[] list);

    void setNoMoreLoading();

    void clearnData();

    void setLayoutNull();

    void initRecyclerView(OrderModel[] list);
}
