package qtc.project.banhangnhanh.admin.views.fragment.account.register;


import qtc.project.banhangnhanh.admin.model.UserRegisterModel;

public interface FragmentRegisterViewCallback {
    void onClickBackHeader();

    void onClickShowLoginForm();

    void onSignUp(UserRegisterModel userRegisterModel);

    void onRequestCheckPhoneRegister(UserRegisterModel userRegisterModel);

}
