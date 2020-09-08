package qtc.project.banhangnhanh.admin.views.fragment.order;

import qtc.project.banhangnhanh.admin.model.OrderCustomerModel;

public interface FragmentOrderManagerViewCallback {
    //void callDataOrder();

    void onBackProgress();

    void goToOrderDetail(OrderCustomerModel model);

    void goToFilter();

    void getAllData();

    void searchOrder(String search);
}
