package qtc.project.banhangnhanh.admin.views.fragment.supplier.create;

import b.laixuantam.myaarlibrary.base.BaseViewInterface;
import qtc.project.banhangnhanh.activity.HomeActivity;

public interface FragmentCreateSupplierViewInterface extends BaseViewInterface {
    void init(HomeActivity activity,FragmentCreateSupplierViewCallback callback);

    void showAlerSucess();
}
