package qtc.project.banhangnhanh.admin.fragment.order;

import b.laixuantam.myaarlibrary.base.BaseFragment;
import b.laixuantam.myaarlibrary.base.BaseParameters;
import qtc.project.banhangnhanh.activity.HomeActivity;
import qtc.project.banhangnhanh.admin.views.fragment.order.filter.FragmentFilterOrderView;
import qtc.project.banhangnhanh.admin.views.fragment.order.filter.FragmentFilterOrderViewCallback;
import qtc.project.banhangnhanh.admin.views.fragment.order.filter.FragmentFilterOrderViewInterface;

public class FragmentFilterOrder extends BaseFragment<FragmentFilterOrderViewInterface, BaseParameters> implements FragmentFilterOrderViewCallback {

    HomeActivity activity;

    @Override
    protected void initialize() {
        activity = (HomeActivity)getActivity();
        view.init(activity,this);
    }

    @Override
    protected FragmentFilterOrderViewInterface getViewInstance() {
        return new FragmentFilterOrderView();
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
    public void filterOnDay(String dateStartSelected, String dateEndSelected) {
        if (activity != null) {
            activity.checkBack();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    activity.setDataDateOrder(dateStartSelected, dateEndSelected);
                }
            }, 100);
        }

        //activity.replaceFragment(new FragmentOrderManager().newIntance(dateStartSelected,dateEndSelected),true,null);
    }
}
