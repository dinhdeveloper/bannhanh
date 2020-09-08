package qtc.project.banhangnhanh.sale.view.fragment.order.detail;

import java.util.ArrayList;

import qtc.project.banhangnhanh.admin.model.OrderDetailModel;
import qtc.project.banhangnhanh.sale.model.OrderModel;

public interface FragmentOrderDetailSaleViewCallback {

    void onBackP();

    void cancelOrder(String id_order);

    void reQuestData();

    void inBill(OrderModel model, ArrayList<OrderDetailModel> detailModels, float tiengiam);
}
