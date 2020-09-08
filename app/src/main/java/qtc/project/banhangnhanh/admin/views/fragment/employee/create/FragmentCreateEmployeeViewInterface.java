package qtc.project.banhangnhanh.admin.views.fragment.employee.create;

import java.util.ArrayList;

import b.laixuantam.myaarlibrary.base.BaseViewInterface;
import qtc.project.banhangnhanh.activity.HomeActivity;
import qtc.project.banhangnhanh.admin.model.LevelEmployeeModel;

public interface FragmentCreateEmployeeViewInterface extends BaseViewInterface {
    void init(HomeActivity activity,FragmentCreateEmployeeViewCallback callback);

    void sendLevelEmployee(ArrayList<LevelEmployeeModel> arrayList);

    void showDiaLogSucess();
}
