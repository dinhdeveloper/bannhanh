package qtc.project.banhangnhanh.admin.views.fragment.supplier.detail;

import qtc.project.banhangnhanh.admin.model.SupplierModel;

public interface FragmentSupplierDetailViewCallback {
    void onBackProgress();

    void updateSupplier(SupplierModel supplierModel);

    void deleteSupplier(String id);
}
