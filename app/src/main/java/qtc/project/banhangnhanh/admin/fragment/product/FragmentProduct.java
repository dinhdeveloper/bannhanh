package qtc.project.banhangnhanh.admin.fragment.product;

import b.laixuantam.myaarlibrary.base.BaseFragment;
import b.laixuantam.myaarlibrary.base.BaseParameters;
import qtc.project.banhangnhanh.activity.HomeActivity;
import qtc.project.banhangnhanh.admin.views.fragment.product.FragmentProductView;
import qtc.project.banhangnhanh.admin.views.fragment.product.FragmentProductViewCallback;
import qtc.project.banhangnhanh.admin.views.fragment.product.FragmentProductViewInterface;

public class FragmentProduct extends BaseFragment<FragmentProductViewInterface, BaseParameters> implements FragmentProductViewCallback {

    HomeActivity activity;

    @Override
    protected void initialize() {
        activity = (HomeActivity) getActivity();
        view.init(activity, this);
    }

    @Override
    protected FragmentProductViewInterface getViewInstance() {
        return new FragmentProductView();
    }

    @Override
    protected BaseParameters getParametersContainer() {
        return null;
    }

    @Override
    public void onClickBackHeader() {
        if (activity != null)
            activity.checkBack();
    }
}
