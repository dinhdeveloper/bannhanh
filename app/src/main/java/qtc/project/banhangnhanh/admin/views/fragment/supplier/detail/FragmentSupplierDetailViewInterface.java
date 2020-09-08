package qtc.project.banhangnhanh.admin.views.fragment.supplier.detail;

import b.laixuantam.myaarlibrary.base.BaseViewInterface;
import qtc.project.banhangnhanh.activity.HomeActivity;
import qtc.project.banhangnhanh.admin.model.SupplierModel;

public interface FragmentSupplierDetailViewInterface extends BaseViewInterface {
    void init(HomeActivity activity,FragmentSupplierDetailViewCallback callback);

    void sendDataToView(SupplierModel infoModel);

    void showDialog();

    void showDialogSucc();
}
