package qtc.project.banhangnhanh.sale.view.fragment.review_login;

import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import b.laixuantam.myaarlibrary.base.BaseUiContainer;
import b.laixuantam.myaarlibrary.base.BaseView;
import b.laixuantam.myaarlibrary.helper.AppUtils;
import b.laixuantam.myaarlibrary.widgets.touch_view_anim.scaletouchlistener.ScaleTouchListener;
import qtc.project.banhangnhanh.R;
import qtc.project.banhangnhanh.activity.LoginPreviewActivity;

public class FragmentLoginPreviewView extends BaseView<FragmentLoginPreviewView.UIContainer> implements FragmentLoginPreviewViewInterface {
    LoginPreviewActivity activity;
    FragmentLoginPreviewViewCallback callback;
    boolean checkStatus = true;
    @Override
    public void init(LoginPreviewActivity activity, FragmentLoginPreviewViewCallback callback) {
        this.activity = activity;
        this.callback = callback;

        ui.btnLoginAdmin.setOnClickListener(new ScaleTouchListener(confScaleTouch) {
            @Override
            public void onClick(View v) {
                ui.btnLoginEmployee.setEnabled(false);
                AppUtils.hideKeyBoard(ui.btnLoginAdmin);
                doLoginAdmin();
            }
        });
        ui.btnLoginEmployee.setOnClickListener(new ScaleTouchListener(confScaleTouch) {
            @Override
            public void onClick(View v) {
                ui.btnLoginAdmin.setEnabled(false);
                AppUtils.hideKeyBoard(ui.btnLoginEmployee);
                doLoginEmployee();
            }
        });

        ui.tvQuayLai.setOnClickListener(v -> {
            if (callback!=null)
                callback.checkBack();
        });

    }

    private void doLoginEmployee() {
        if (!TextUtils.isEmpty(ui.tvUsernameEmployee.getText()) && !TextUtils
                .isEmpty(ui.tvUsernameEmployee.getText())) {

            callback.onClickLogin(ui.tvStoreEmployee.getText().toString(),ui.tvUsernameEmployee.getText()
                    .toString(), ui.tvPassEmployee
                    .getText()
                    .toString());
        }
    }

    private void doLoginAdmin() {
        if (!TextUtils.isEmpty(ui.tvUsernameAdmin.getText()) && !TextUtils
                .isEmpty(ui.tvUsernameAdmin.getText())) {

            callback.onClickLogin(ui.tvStoreAdmin.getText().toString(),ui.tvUsernameAdmin.getText()
                    .toString(), ui.tvPassAdmin
                    .getText()
                    .toString());
        }
    }

    @Override
    public BaseUiContainer getUiContainer() {
        return new FragmentLoginPreviewView.UIContainer();
    }

    @Override
    public int getViewId() {
        return R.layout.layout_fragment_sale_login_review;
    }



    public class UIContainer extends BaseUiContainer {
        @UiElement(R.id.tvStoreAdmin)
        public TextView tvStoreAdmin;

        @UiElement(R.id.tvUsernameAdmin)
        public TextView tvUsernameAdmin;

        @UiElement(R.id.tvPassAdmin)
        public TextView tvPassAdmin;

        @UiElement(R.id.btnLoginAdmin)
        public LinearLayout btnLoginAdmin;

        @UiElement(R.id.tvStoreEmployee)
        public TextView tvStoreEmployee;

        @UiElement(R.id.tvUsernameEmployee)
        public TextView tvUsernameEmployee;

        @UiElement(R.id.tvPassEmployee)
        public TextView tvPassEmployee;

        @UiElement(R.id.btnLoginEmployee)
        public LinearLayout btnLoginEmployee;

        @UiElement(R.id.tvQuayLai)
        public TextView tvQuayLai;

    }
}
