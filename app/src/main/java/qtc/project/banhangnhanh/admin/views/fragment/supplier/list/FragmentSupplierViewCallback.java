package qtc.project.banhangnhanh.admin.views.fragment.supplier.list;

import qtc.project.banhangnhanh.admin.model.SupplierModel;

public interface FragmentSupplierViewCallback {
    void onBackProgress();

    void cancelNhaCungUng(SupplierModel model);
}
