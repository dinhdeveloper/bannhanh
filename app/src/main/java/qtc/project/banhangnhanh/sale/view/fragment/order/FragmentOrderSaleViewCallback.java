package qtc.project.banhangnhanh.sale.view.fragment.order;

import qtc.project.banhangnhanh.sale.model.OrderModel;

public interface FragmentOrderSaleViewCallback {
    void goToDetail(OrderModel model);

    void goHome();

    void filter();

    void reQuestData();

    void searchOrder(String search);

    void loadMore();

    void callNav();
}
