package qtc.project.banhangnhanh.sale.view.fragment.level.detail;

import qtc.project.banhangnhanh.admin.model.LevelCustomerModel;

public interface FragmentLevelSaleDetailViewCallback {
    void onBackP();

    void callPopup(String name, LevelCustomerModel model);

    void goHome();
}
