package qtc.project.banhangnhanh.admin.fragment.employee;

import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

import b.laixuantam.myaarlibrary.api.ApiRequest;
import b.laixuantam.myaarlibrary.api.ErrorApiResponse;
import b.laixuantam.myaarlibrary.base.BaseFragment;
import b.laixuantam.myaarlibrary.base.BaseParameters;
import qtc.project.banhangnhanh.activity.HomeActivity;
import qtc.project.banhangnhanh.admin.api.history.HistoryOrderCustomerRequest;
import qtc.project.banhangnhanh.admin.dependency.AppProvider;
import qtc.project.banhangnhanh.admin.model.BaseResponseModel;
import qtc.project.banhangnhanh.admin.model.EmployeeModel;
import qtc.project.banhangnhanh.admin.model.OrderCustomerModel;
import qtc.project.banhangnhanh.admin.views.fragment.employee.lichsubanhang.FragmentHistorySaleEmployeeView;
import qtc.project.banhangnhanh.admin.views.fragment.employee.lichsubanhang.FragmentHistorySaleEmployeeViewCallback;
import qtc.project.banhangnhanh.admin.views.fragment.employee.lichsubanhang.FragmentHistorySaleEmployeeViewInterface;

public class FragmentHistorySaleEmployee extends BaseFragment<FragmentHistorySaleEmployeeViewInterface, BaseParameters> implements FragmentHistorySaleEmployeeViewCallback {

    HomeActivity activity;

    public static FragmentHistorySaleEmployee newInstance(EmployeeModel model) {
        FragmentHistorySaleEmployee detail = new FragmentHistorySaleEmployee();
        Bundle bundle = new Bundle();
        bundle.putSerializable("model", model);
        detail.setArguments(bundle);
        return detail;
    }

    @Override
    protected void initialize() {
        activity = (HomeActivity) getActivity();
        view.init(activity, this);

        getDataToBundle();
    }

    private void getDataToBundle() {
        Bundle extras = getArguments();
        if (extras != null) {
            EmployeeModel model = (EmployeeModel) extras.get("model");
            HistoryOrderCustomerRequest.ApiParams params = new HistoryOrderCustomerRequest.ApiParams();
            params.employee_id = model.getId();
            params.id_business = AppProvider.getPreferences().getUserModel().getId_business();
            AppProvider.getApiManagement().call(HistoryOrderCustomerRequest.class, params, new ApiRequest.ApiCallback<BaseResponseModel<OrderCustomerModel>>() {
                @Override
                public void onSuccess(BaseResponseModel<OrderCustomerModel> body) {
                    dismissProgress();
                    ArrayList<OrderCustomerModel> list = new ArrayList<>();
                    list.addAll(Arrays.asList(body.getData()));
                    view.sendDataToView(list);
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
    protected FragmentHistorySaleEmployeeViewInterface getViewInstance() {
        return new FragmentHistorySaleEmployeeView();
    }

    @Override
    protected BaseParameters getParametersContainer() {
        return null;
    }

    @Override
    public void onBackProgress() {
        if (activity!=null)
            activity.checkBack();
    }

    @Override
    public void getAllDataHistory() {
//        showProgress();
//        HistoryOrderCustomerRequest.ApiParams params = new HistoryOrderCustomerRequest.ApiParams();
//        AppProvider.getApiManagement().call(HistoryOrderCustomerRequest.class, params, new ApiRequest.ApiCallback<BaseResponseModel<OrderCustomerModel>>() {
//            @Override
//            public void onSuccess(BaseResponseModel<OrderCustomerModel> body) {
//                dismissProgress();
//                ArrayList<OrderCustomerModel> list = new ArrayList<>();
//                list.addAll(Arrays.asList(body.getData()));
//                view.sendDataToView(list);
//            }
//
//            @Override
//            public void onError(ErrorApiResponse error) {
//                dismissProgress();
//                Log.e("onError", error.message);
//            }
//
//            @Override
//            public void onFail(ApiRequest.RequestError error) {
//                dismissProgress();
//                Log.e("onFail", error.name());
//            }
//        });
    }

    @Override
    public void goToDetailOrder(OrderCustomerModel model) {
        activity.addFragment(new FragmentOrderDetailEmployee().newInstance(model),true,null);
    }
}
