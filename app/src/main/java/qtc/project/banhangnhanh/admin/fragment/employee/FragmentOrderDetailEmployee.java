package qtc.project.banhangnhanh.admin.fragment.employee;

import android.os.Bundle;

import b.laixuantam.myaarlibrary.base.BaseFragment;
import b.laixuantam.myaarlibrary.base.BaseParameters;
import qtc.project.banhangnhanh.activity.HomeActivity;
import qtc.project.banhangnhanh.admin.model.OrderCustomerModel;
import qtc.project.banhangnhanh.admin.views.fragment.employee.lichsubanhang.detail.FragmentOrderDetailEmployeeView;
import qtc.project.banhangnhanh.admin.views.fragment.employee.lichsubanhang.detail.FragmentOrderDetailEmployeeViewCallback;
import qtc.project.banhangnhanh.admin.views.fragment.employee.lichsubanhang.detail.FragmentOrderDetailEmployeeViewInterface;

public class FragmentOrderDetailEmployee extends BaseFragment<FragmentOrderDetailEmployeeViewInterface, BaseParameters> implements FragmentOrderDetailEmployeeViewCallback {
    HomeActivity activity;

    public static FragmentOrderDetailEmployee newInstance(OrderCustomerModel model) {
        FragmentOrderDetailEmployee detail = new FragmentOrderDetailEmployee();
        Bundle bundle = new Bundle();
        bundle.putSerializable("model", model);
        detail.setArguments(bundle);

        return detail;
    }

    @Override
    protected void initialize() {
        activity = (HomeActivity)getActivity();
        view.init(activity,this);

        getDataToBundle();
    }

    private void getDataToBundle() {
        Bundle extras = getArguments();
        if (extras != null) {
            OrderCustomerModel model = (OrderCustomerModel) extras.get("model");
            view.sentDataToView(model);
        }
    }

    @Override
    protected FragmentOrderDetailEmployeeViewInterface getViewInstance() {
        return new FragmentOrderDetailEmployeeView();
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
}
