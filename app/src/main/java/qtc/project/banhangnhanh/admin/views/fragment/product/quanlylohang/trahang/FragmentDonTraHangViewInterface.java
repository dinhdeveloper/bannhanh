package qtc.project.banhangnhanh.admin.views.fragment.product.quanlylohang.trahang;

import b.laixuantam.myaarlibrary.base.BaseViewInterface;
import qtc.project.banhangnhanh.activity.HomeActivity;
import qtc.project.banhangnhanh.admin.model.PackageInfoModel;

public interface FragmentDonTraHangViewInterface extends BaseViewInterface {
    void init(HomeActivity activity, FragmentDonTraHangViewCallback callback);

    void sendDataToView(PackageInfoModel infoModel, String name, String id);

    void setOnBack();

    void showSuccess();
}
