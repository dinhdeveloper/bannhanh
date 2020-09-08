package qtc.project.banhangnhanh.admin.views.fragment.product.quanlylohang.create;

import b.laixuantam.myaarlibrary.base.BaseViewInterface;
import qtc.project.banhangnhanh.activity.HomeActivity;
import qtc.project.banhangnhanh.admin.model.ProductListModel;
import qtc.project.banhangnhanh.admin.model.SupplierModel;

public interface FragmentCreateLoHangViewInterface extends BaseViewInterface {

    void init(HomeActivity activity,FragmentCreateLoHangViewCallback callback);

    void sendDataToView(SupplierModel model);

    void sentDataProductToView(ProductListModel model);

    void onShowSuccess();
}
