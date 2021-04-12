package qtc.project.banhangnhanh.activity;

import android.os.Bundle;

import b.laixuantam.myaarlibrary.base.BaseFragmentActivity;
import b.laixuantam.myaarlibrary.base.BaseParameters;
import qtc.project.banhangnhanh.R;
import qtc.project.banhangnhanh.admin.views.action_bar.base_main_actionbar.BaseMainActionbarViewInterface;
import qtc.project.banhangnhanh.sale.view.testinbill.TestInActivityView;
import qtc.project.banhangnhanh.sale.view.testinbill.TestInActivityViewCallback;
import qtc.project.banhangnhanh.sale.view.testinbill.TestInActivityViewInterface;

public class TestInActivity extends BaseFragmentActivity<TestInActivityViewInterface, BaseMainActionbarViewInterface, BaseParameters> implements TestInActivityViewCallback {

    @Override
    protected void initialize(Bundle savedInstanceState) {
        view.init(this, this);
    }

    @Override
    protected TestInActivityViewInterface getViewInstance() {
        return new TestInActivityView();
    }

    @Override
    protected BaseMainActionbarViewInterface getActionbarInstance() {
        return null;
    }

    @Override
    protected BaseParameters getParametersContainer() {
        return null;
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.layoutRoot;
    }
}
