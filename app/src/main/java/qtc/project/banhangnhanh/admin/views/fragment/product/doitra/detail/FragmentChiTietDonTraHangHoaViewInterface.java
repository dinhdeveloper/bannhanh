package qtc.project.banhangnhanh.admin.views.fragment.product.doitra.detail;

import b.laixuantam.myaarlibrary.base.BaseViewInterface;
import qtc.project.banhangnhanh.activity.HomeActivity;
import qtc.project.banhangnhanh.admin.model.PackageReturnModel;

public interface FragmentChiTietDonTraHangHoaViewInterface extends BaseViewInterface {
    void init(HomeActivity activity, FragmentChiTietDonTraHangHoaViewCallback callback);

    void sendDataToView(PackageReturnModel infoModel);

    void showConfirmDelete();
}
