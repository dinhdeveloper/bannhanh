package qtc.project.banhangnhanh.admin.fragment.employee;

import b.laixuantam.myaarlibrary.base.BaseFragment;
import b.laixuantam.myaarlibrary.base.BaseParameters;
import qtc.project.banhangnhanh.activity.HomeActivity;
import qtc.project.banhangnhanh.admin.views.fragment.employee.FragmentEmployeeManagerView;
import qtc.project.banhangnhanh.admin.views.fragment.employee.FragmentEmployeeManagerViewCallback;
import qtc.project.banhangnhanh.admin.views.fragment.employee.FragmentEmployeeManagerViewInterface;

public class FragmentEmployeeManager extends BaseFragment<FragmentEmployeeManagerViewInterface, BaseParameters> implements FragmentEmployeeManagerViewCallback {

    HomeActivity activity;

    @Override
    protected void initialize() {
        activity = (HomeActivity) getActivity();
        view.init(activity, this);
    }

    @Override
    protected FragmentEmployeeManagerViewInterface getViewInstance() {
        return new FragmentEmployeeManagerView();
    }

    @Override
    protected BaseParameters getParametersContainer() {
        return null;
    }

    @Override
    public void onBackProgress() {
        if (activity != null)
            activity.checkBack();
    }

    @Override
    public void goToDSNV() {
        activity.addFragment(new FragmentEmployeeList(), true, null);
    }
//
//    @Override
//    public void goToKSLSBH() {
//        activity.addFragment(new FragmentHistorySaleEmployee(), true, null);
//    }
}
