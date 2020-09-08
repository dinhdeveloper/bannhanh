package qtc.project.banhangnhanh.admin.views.fragment.history.history;

import qtc.project.banhangnhanh.admin.model.OrderCustomerModel;

public interface FragmentHistoryOrderCustomerViewCallback {
    void onBackProgress();

    void sentDataToDetail(OrderCustomerModel model);

    void searchHistoryOrderCustomer(String order_id, String customer_id);
}
