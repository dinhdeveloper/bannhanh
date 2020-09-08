package qtc.project.banhangnhanh.admin.views.fragment.employee.list;

import java.util.ArrayList;

import b.laixuantam.myaarlibrary.base.BaseViewInterface;
import qtc.project.banhangnhanh.activity.HomeActivity;
import qtc.project.banhangnhanh.admin.model.EmployeeModel;

public interface FragmentEmployeeListViewInterface extends BaseViewInterface {
    void init(HomeActivity activity,FragmentEmployeeListViewCallback callback);

    void mappingRecyclerView(ArrayList<EmployeeModel> list);

    void showPopup();
}
