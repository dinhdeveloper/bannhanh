package qtc.project.banhangnhanh.admin.views.fragment.employee.detail;

import java.util.ArrayList;

import b.laixuantam.myaarlibrary.base.BaseViewInterface;
import qtc.project.banhangnhanh.activity.HomeActivity;
import qtc.project.banhangnhanh.admin.model.EmployeeModel;
import qtc.project.banhangnhanh.admin.model.LevelEmployeeModel;

public interface FragmentEmployeeDetailViewInterface extends BaseViewInterface {
    void init(HomeActivity activity,FragmentEmployeeDetailViewCallback callback);

    void sentDataToView(EmployeeModel model);

    void sendLevelEmployee(ArrayList<LevelEmployeeModel> arrayList);

    void showDiaLogUpdate();

    void showDiaLogDelete();
}
