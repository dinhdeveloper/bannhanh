package qtc.project.banhangnhanh.admin.views.fragment.product.quanlylohang.create;

import qtc.project.banhangnhanh.admin.model.PackageInfoModel;

public interface FragmentCreateLoHangViewCallback {
    void onBackProgress();

    void getAllDataNhaCungUng();

    void getAllDataProduct(boolean check);

    void createPackageInfo(PackageInfoModel listModel, String id_product);
}
