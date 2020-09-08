package qtc.project.banhangnhanh.admin.views.fragment.levelcustomer;

import qtc.project.banhangnhanh.admin.model.LevelCustomerModel;

public interface FragmentLevelCustomerViewCallback {
    void callDataLevelCustomer();

    void sendDataToDetail(LevelCustomerModel model);

    void onBackProgress();
}
