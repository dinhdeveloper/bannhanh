package qtc.project.banhangnhanh.admin.views.fragment.product.doitra;

import java.util.ArrayList;

import b.laixuantam.myaarlibrary.base.BaseViewInterface;
import qtc.project.banhangnhanh.activity.HomeActivity;
import qtc.project.banhangnhanh.admin.model.PackageReturnModel;

public interface FragmentDoiTraHangHoaViewInterface extends BaseViewInterface {
    void init(HomeActivity activity,FragmentDoiTraHangHoaViewCallback callback);

    void mappingRecyclerView(ArrayList<PackageReturnModel> list);
}
