package qtc.project.banhangnhanh.admin.views.fragment.supplier.create;

import qtc.project.banhangnhanh.admin.model.SupplierModel;

public interface FragmentCreateSupplierViewCallback {
    void onBackProgress();

    void createSupplier(SupplierModel supplierModel);
}
