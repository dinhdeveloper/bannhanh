package qtc.project.banhangnhanh.admin.views.fragment.supplier.list;

import java.util.ArrayList;

import b.laixuantam.myaarlibrary.base.BaseViewInterface;
import qtc.project.banhangnhanh.activity.HomeActivity;
import qtc.project.banhangnhanh.admin.model.SupplierModel;

public interface FragmentSupplierViewInterface extends BaseViewInterface {
    void init(HomeActivity activity,FragmentSupplierViewCallback callback);

    void mappingRecyclerView(ArrayList<SupplierModel> list);
}
