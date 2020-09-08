package qtc.project.banhangnhanh.sale.view.fragment.level;

import qtc.project.banhangnhanh.admin.model.LevelCustomerModel;

public interface FragmentLevelSaleCustomerViewCallback {
    void goToLevelDetail(LevelCustomerModel model);

    void goHome();

    void callNav();
}
