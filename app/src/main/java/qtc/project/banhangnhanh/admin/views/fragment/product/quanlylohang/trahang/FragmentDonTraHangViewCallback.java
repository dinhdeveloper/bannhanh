package qtc.project.banhangnhanh.admin.views.fragment.product.quanlylohang.trahang;

import qtc.project.banhangnhanh.admin.model.PackageReturnModel;

public interface FragmentDonTraHangViewCallback {
    void onBackProgress();

    void setDataDoiTraHang(PackageReturnModel returnModel);

    void setOnBack();
}
