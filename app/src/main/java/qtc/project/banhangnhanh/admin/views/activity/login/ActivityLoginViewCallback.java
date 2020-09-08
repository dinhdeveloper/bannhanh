package qtc.project.banhangnhanh.admin.views.activity.login;

public interface ActivityLoginViewCallback {
    void onClickLogin(String id_store,String username, String password);

    void goToFragmentReview();
}
