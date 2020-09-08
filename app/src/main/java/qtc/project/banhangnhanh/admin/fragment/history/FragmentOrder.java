package qtc.project.banhangnhanh.admin.fragment.history;

import b.laixuantam.myaarlibrary.base.BaseFragment;
import b.laixuantam.myaarlibrary.base.BaseParameters;
import qtc.project.banhangnhanh.activity.HomeActivity;
import qtc.project.banhangnhanh.admin.views.fragment.history.FragmentOrderView;
import qtc.project.banhangnhanh.admin.views.fragment.history.FragmentOrderViewCallback;
import qtc.project.banhangnhanh.admin.views.fragment.history.FragmentOrderViewInterface;

public class FragmentOrder extends BaseFragment<FragmentOrderViewInterface, BaseParameters> implements FragmentOrderViewCallback {
    @Override
    protected void initialize() {
        HomeActivity activity = (HomeActivity) getActivity();
        view.init(activity, this);
    }

    @Override
    protected FragmentOrderViewInterface getViewInstance() {
        return new FragmentOrderView();
    }

    @Override
    protected BaseParameters getParametersContainer() {
        return null;
    }
}
