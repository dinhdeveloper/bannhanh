package qtc.project.banhangnhanh.admin.views.fragment.employee.create;

import qtc.project.banhangnhanh.admin.model.EmployeeModel;

public interface FragmentCreateEmployeeViewCallback {
    void onBackProgress();

    void callLevelEmployee();

    void createEmployee(EmployeeModel model);
}
