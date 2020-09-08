package qtc.project.banhangnhanh.admin.model;

public class SignOutModel extends BaseResponseModel {

    /**
     * id : 52
     * force_sign_out : 1
     */

    private String id;
    private String force_sign_out;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getForce_sign_out() {
        return force_sign_out;
    }

    public void setForce_sign_out(String force_sign_out) {
        this.force_sign_out = force_sign_out;
    }
}
