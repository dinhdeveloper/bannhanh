package qtc.project.banhangnhanh.admin.views.fragment.employee.list;

import qtc.project.banhangnhanh.admin.model.EmployeeModel;

public interface FragmentEmployeeListViewCallback {
    void getAllDataEmployee();

    void onBackProgress();

    void goToDetailEmployee(EmployeeModel model);

    void updateEmployee(EmployeeModel item);

    void createEmployee();

    void getHistoryOrderEmployee(EmployeeModel model);
}
