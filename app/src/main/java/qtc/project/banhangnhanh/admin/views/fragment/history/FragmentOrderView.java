package qtc.project.banhangnhanh.admin.views.fragment.history;

import b.laixuantam.myaarlibrary.base.BaseUiContainer;
import b.laixuantam.myaarlibrary.base.BaseView;
import qtc.project.banhangnhanh.R;
import qtc.project.banhangnhanh.activity.HomeActivity;

public class FragmentOrderView  extends BaseView<FragmentOrderView.UIContainer> implements FragmentOrderViewInterface {
    @Override
    public void init(HomeActivity activity, FragmentOrderViewCallback callback) {

    }

    @Override
    public BaseUiContainer getUiContainer() {
        return new FragmentOrderView.UIContainer();
    }

    @Override
    public int getViewId() {
        return R.layout.layout_fragment_order;

    }

    public static class UIContainer extends BaseUiContainer{
    }
}
