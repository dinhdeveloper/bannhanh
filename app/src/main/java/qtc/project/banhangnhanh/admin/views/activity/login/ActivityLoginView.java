package qtc.project.banhangnhanh.admin.views.activity.login;

import android.annotation.SuppressLint;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;

import b.laixuantam.myaarlibrary.base.BaseUiContainer;
import b.laixuantam.myaarlibrary.base.BaseView;
import b.laixuantam.myaarlibrary.helper.AppUtils;
import b.laixuantam.myaarlibrary.helper.KeyboardUtils;
import b.laixuantam.myaarlibrary.widgets.scaletouchlistener.ScaleTouchListener;
import qtc.project.banhangnhanh.R;
import qtc.project.banhangnhanh.activity.LoginActivity;

public class ActivityLoginView extends BaseView<ActivityLoginView.UiContainer> implements ActivityLoginViewInterface {

    LoginActivity activity;
    ActivityLoginViewCallback callback;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void initialize(LoginActivity activity,final ActivityLoginViewCallback callback) {
        this.activity = activity;
        this.callback = callback;

        KeyboardUtils.setupUI(getView(),activity);

        ui.edtLoginIdShop.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (ui.edtLoginIdShop.getText().length() > 0) {
                    ui.edtLoginIdShop.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (ui.edtLoginIdShop.getText().length() > 0) {
                    ui.edtLoginIdShop.setError(null);
                }
            }
        });

        ui.edtLoginName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (ui.edtLoginName.getText().length() > 0) {
                    ui.edtLoginName.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (ui.edtLoginName.getText().length() > 0) {
                    ui.edtLoginName.setError(null);
                }
            }
        });
        ui.edtLoginPassowrd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (ui.edtLoginPassowrd.getText().length() > 0) {
                    ui.edtLoginPassowrd.setError(null);
                }
            }
        });
        ui.edtLoginPassowrd.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                doLogin();
            }
            return false;
        });



        ui.btnLoginButton.setOnClickListener(new ScaleTouchListener(confScaleTouch) {
            @Override
            public void onClick(View v) {
                AppUtils.hideKeyBoard(ui.btnLoginButton);
                doLogin();
            }
        });
    }


    private void doLogin() {
        if (!TextUtils.isEmpty(ui.edtLoginName.getText()) && !TextUtils
                .isEmpty(ui.edtLoginName.getText())) {

            callback.onClickLogin(ui.edtLoginIdShop.getText().toString(),ui.edtLoginName.getText()
                    .toString(), ui.edtLoginPassowrd
                    .getText()
                    .toString());
        }
        else if (TextUtils.isEmpty(ui.edtLoginIdShop.getText())) {
            ui.edtLoginIdShop.setError("Mã cửa hàng");
            ui.edtLoginIdShop.requestFocus();
        }
        else if (TextUtils.isEmpty(ui.edtLoginName.getText())) {
            ui.edtLoginName.setError("Tên đăng nhập");
            ui.edtLoginName.requestFocus();
        } else {
            ui.edtLoginPassowrd.setError("Mật khẩu");
            ui.edtLoginPassowrd.requestFocus();
        }
    }


    @Override
    public BaseUiContainer getUiContainer() {
        return new UiContainer();
    }

    @Override
    public int getViewId() {
        return R.layout.activity_login;
    }

    public class UiContainer extends BaseUiContainer {
        @UiElement(R.id.edtLoginIdShop)
        public EditText edtLoginIdShop;

        @UiElement(R.id.edtLoginName)
        public EditText edtLoginName;

        @UiElement(R.id.edtLoginPassowrd)
        public EditText edtLoginPassowrd;

        @UiElement(R.id.btnLoginButton)
        public LinearLayout btnLoginButton;
    }
}
