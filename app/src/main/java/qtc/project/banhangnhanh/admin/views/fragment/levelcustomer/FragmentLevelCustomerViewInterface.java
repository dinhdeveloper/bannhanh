package qtc.project.banhangnhanh.admin.views.fragment.levelcustomer;

import java.util.ArrayList;

import b.laixuantam.myaarlibrary.base.BaseViewInterface;
import qtc.project.banhangnhanh.activity.HomeActivity;
import qtc.project.banhangnhanh.admin.model.LevelCustomerModel;

public interface FragmentLevelCustomerViewInterface extends BaseViewInterface {
    void init(HomeActivity activity,FragmentLevelCustomerViewCallback callback);

    void initRecyclerView(ArrayList<LevelCustomerModel> list);
}
