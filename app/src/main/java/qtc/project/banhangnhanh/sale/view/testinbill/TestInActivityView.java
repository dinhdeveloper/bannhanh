package qtc.project.banhangnhanh.sale.view.testinbill;

import b.laixuantam.myaarlibrary.base.BaseUiContainer;
import b.laixuantam.myaarlibrary.base.BaseView;
import qtc.project.banhangnhanh.R;
import qtc.project.banhangnhanh.activity.TestInActivity;

public class TestInActivityView extends BaseView<TestInActivityView.UiContainer> implements TestInActivityViewInterface {
    TestInActivity activity;
    TestInActivityViewCallback callback;

    @Override
    public void init(TestInActivity activity, TestInActivityViewCallback callback) {
        this.activity = activity;
        this.callback = callback;
    }

    @Override
    public BaseUiContainer getUiContainer() {
        return new TestInActivityView.UiContainer();
    }

    @Override
    public int getViewId() {
        return R.layout.test_in_bill;
    }

    public class UiContainer extends BaseUiContainer {

    }
}
