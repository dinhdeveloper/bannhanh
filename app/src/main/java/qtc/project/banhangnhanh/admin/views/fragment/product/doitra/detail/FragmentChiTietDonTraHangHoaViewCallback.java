package qtc.project.banhangnhanh.admin.views.fragment.product.doitra.detail;

import qtc.project.banhangnhanh.admin.model.PackageReturnModel;

public interface FragmentChiTietDonTraHangHoaViewCallback {
    void onBackProgress();

    void updateStatus(PackageReturnModel infoModel);

    void deleteStatus(PackageReturnModel infoModel);
}
