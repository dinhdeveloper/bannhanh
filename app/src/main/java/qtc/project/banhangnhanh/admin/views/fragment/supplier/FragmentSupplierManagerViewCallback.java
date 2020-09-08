package qtc.project.banhangnhanh.admin.views.fragment.supplier;

import qtc.project.banhangnhanh.admin.model.SupplierModel;

public interface FragmentSupplierManagerViewCallback {
    void onBackProgress();

    void goToDetail(SupplierModel model);

    void createSupplier();

    void searchSupplier(String filter);

    void getAllData();
}
