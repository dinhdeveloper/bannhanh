package qtc.project.banhangnhanh.admin.fragment.employee;

import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

import b.laixuantam.myaarlibrary.api.ApiRequest;
import b.laixuantam.myaarlibrary.api.ErrorApiResponse;
import b.laixuantam.myaarlibrary.base.BaseFragment;
import b.laixuantam.myaarlibrary.base.BaseParameters;
import qtc.project.banhangnhanh.activity.HomeActivity;
import qtc.project.banhangnhanh.admin.api.employee.EmployeeRequest;
import qtc.project.banhangnhanh.admin.dependency.AppProvider;
import qtc.project.banhangnhanh.admin.model.BaseResponseModel;
import qtc.project.banhangnhanh.admin.model.EmployeeModel;
import qtc.project.banhangnhanh.admin.views.fragment.employee.list.FragmentEmployeeListView;
import qtc.project.banhangnhanh.admin.views.fragment.employee.list.FragmentEmployeeListViewCallback;
import qtc.project.banhangnhanh.admin.views.fragment.employee.list.FragmentEmployeeListViewInterface;

public class FragmentEmployeeList extends BaseFragment<FragmentEmployeeListViewInterface, BaseParameters> implements FragmentEmployeeListViewCallback {

    HomeActivity activity;

    @Override
    protected void initialize() {
        activity = (HomeActivity) getActivity();
        view.init(activity, this);
    }

    @Override
    protected FragmentEmployeeListViewInterface getViewInstance() {
        return new FragmentEmployeeListView();
    }

    @Override
    protected BaseParameters getParametersContainer() {
        return null;
    }

    @Override
    public void getAllDataEmployee() {
        showProgress();
        EmployeeRequest.ApiParams params = new EmployeeRequest.ApiParams();
        params.type_manager = "list_employee";
        params.id_business = AppProvider.getPreferences().getUserModel().getId_business();

        AppProvider.getApiManagement().call(EmployeeRequest.class, params, new ApiRequest.ApiCallback<BaseResponseModel<EmployeeModel>>() {
            @Override
            public void onSuccess(BaseResponseModel<EmployeeModel> body) {
                if (body != null) {
                    dismissProgress();
                    ArrayList<EmployeeModel> list = new ArrayList<>();
                    list.addAll(Arrays.asList(body.getData()));
                    view.mappingRecyclerView(list);
                }
            }

            @Override
            public void onError(ErrorApiResponse error) {
                dismissProgress();
                Log.e("onError", error.message);
            }

            @Override
            public void onFail(ApiRequest.RequestError error) {
                dismissProgress();
                Log.e("onFail", error.name());
            }
        });
    }

    @Override
    public void onBackProgress() {
        if (activity != null)
            activity.checkBack();
    }

    @Override
    public void goToDetailEmployee(EmployeeModel model) {
        activity.replaceFragment(new FragmentEmployeeDetail().newInstance(model), true, null);
    }

    @Override
    public void updateEmployee(EmployeeModel employeeModel) {
        if (employeeModel != null) {
            showProgress();
            EmployeeRequest.ApiParams params = new EmployeeRequest.ApiParams();
            params.type_manager = "update_employee_status";
            params.id_business = AppProvider.getPreferences().getUserModel().getId_business();
            params.id_employee = employeeModel.getId();
            params.status = employeeModel.getStatus();

            AppProvider.getApiManagement().call(EmployeeRequest.class, params, new ApiRequest.ApiCallback<BaseResponseModel<EmployeeModel>>() {
                @Override
                public void onSuccess(BaseResponseModel<EmployeeModel> body) {
                    dismissProgress();
                    if (body.getSuccess().equals("true")) {
                        view.showPopup();
                    } else if (body.getSuccess().equals("false")) {
                        Toast.makeText(activity, "" + body.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onError(ErrorApiResponse error) {
                    dismissProgress();
                    Log.e("onError", error.message);
                }

                @Override
                public void onFail(ApiRequest.RequestError error) {
                    dismissProgress();
                    Log.e("onFail", error.name());
                }
            });
        }
    }

    @Override
    public void createEmployee() {
        activity.replaceFragment(new FragmentCreateEmployee(), true, null);
    }

    @Override
    public void getHistoryOrderEmployee(EmployeeModel model) {
        activity.addFragment(new FragmentHistorySaleEmployee().newInstance(model), true, null);
    }
}
