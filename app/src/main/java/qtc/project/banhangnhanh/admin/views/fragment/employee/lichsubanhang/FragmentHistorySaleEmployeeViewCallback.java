package qtc.project.banhangnhanh.admin.views.fragment.employee.lichsubanhang;

import qtc.project.banhangnhanh.admin.model.OrderCustomerModel;

public interface FragmentHistorySaleEmployeeViewCallback {
    void onBackProgress();

    void getAllDataHistory();

    void goToDetailOrder(OrderCustomerModel model);
}
