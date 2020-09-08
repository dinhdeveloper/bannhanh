package qtc.project.banhangnhanh.admin.views.fragment.account.password_manager;

public interface FragmentPasswordManagerViewCallback {
    void onClickBackHeader();

    void onSubmitChangePassword(String oldPassword, String newPass);

    void onClickForgotPassword();
}
