package qtc.project.banhangnhanh.admin.views.fragment.product.quanlylohang.detail;

import qtc.project.banhangnhanh.admin.model.PackageInfoModel;

public interface FragmentChiTietLoHangViewCallback {
    void onBackProgress();

    void getAllDataNhaCungUng();

    void updateDataPackage(PackageInfoModel infoModel, String id);

    void taoDonTraHang(PackageInfoModel infoModel,String name,String id);
}
