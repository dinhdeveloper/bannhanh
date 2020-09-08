package qtc.project.banhangnhanh.admin.views.fragment.product.quanlylohang.detail;

import b.laixuantam.myaarlibrary.base.BaseViewInterface;
import qtc.project.banhangnhanh.activity.HomeActivity;
import qtc.project.banhangnhanh.admin.model.PackageInfoModel;
import qtc.project.banhangnhanh.admin.model.SupplierModel;

public interface FragmentChiTietLoHangViewInterface extends BaseViewInterface {
    void init(HomeActivity activity,FragmentChiTietLoHangViewCallback callback);

    void sendDataToView(PackageInfoModel infoModel, String name, String id);

    void sendDataToViewTwo(SupplierModel supplierModel);

    void showPopSuccess();
}
