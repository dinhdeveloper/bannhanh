package qtc.project.banhangnhanh.admin.views.fragment.account.forgot_password;

public interface FragmentForgotPasswordViewCallback {
    void onClickBackHeader();

    void onRequestCheckPhoneRegister(String phone);

    void verifyVerificationCode(String code);

    void onRequestAuthPhoneNumber(String phone);

    void onRequestUpdatePassword(String phone, String newPassword);
}
