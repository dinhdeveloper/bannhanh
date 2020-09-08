package qtc.project.banhangnhanh.admin.views.fragment.employee.detail;

import qtc.project.banhangnhanh.admin.model.EmployeeModel;

public interface FragmentEmployeeDetailViewCallback {
    void onBackProgress();

    void callLevelEmployee();

    void updateEmployee(EmployeeModel employeeModel);

    void deleteEmployee(EmployeeModel model);

    void reSetPass(String id_code,String newPass,String id_employee);
}
