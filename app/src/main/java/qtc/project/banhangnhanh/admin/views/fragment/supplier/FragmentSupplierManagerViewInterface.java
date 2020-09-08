package qtc.project.banhangnhanh.admin.views.fragment.supplier;

import java.util.ArrayList;

import b.laixuantam.myaarlibrary.base.BaseViewInterface;
import qtc.project.banhangnhanh.activity.HomeActivity;
import qtc.project.banhangnhanh.admin.model.SupplierModel;

public interface FragmentSupplierManagerViewInterface extends BaseViewInterface {
    void init(HomeActivity activity,FragmentSupplierManagerViewCallback callback);

    void mappingRecyclerView(ArrayList<SupplierModel> list);
}
