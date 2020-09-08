package qtc.project.banhangnhanh.admin.fragment.levelcustomer;

import android.text.TextUtils;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import b.laixuantam.myaarlibrary.api.ApiRequest;
import b.laixuantam.myaarlibrary.api.ErrorApiResponse;
import b.laixuantam.myaarlibrary.base.BaseFragment;
import b.laixuantam.myaarlibrary.base.BaseParameters;
import b.laixuantam.myaarlibrary.helper.KeyboardUtils;
import b.laixuantam.myaarlibrary.widgets.dialog.alert.KAlertDialog;
import id.zelory.compressor.Compressor;
import qtc.project.banhangnhanh.R;
import qtc.project.banhangnhanh.activity.HomeActivity;
import qtc.project.banhangnhanh.admin.api.levelcustomer.LevelCustomerRequest;
import qtc.project.banhangnhanh.admin.dependency.AppProvider;
import qtc.project.banhangnhanh.admin.model.BaseResponseModel;
import qtc.project.banhangnhanh.admin.model.LevelCustomerModel;
import qtc.project.banhangnhanh.admin.views.fragment.levelcustomer.create.FragmentCreateLevelCustomerView;
import qtc.project.banhangnhanh.admin.views.fragment.levelcustomer.create.FragmentCreateLevelCustomerViewCallback;
import qtc.project.banhangnhanh.admin.views.fragment.levelcustomer.create.FragmentCreateLevelCustomerViewInterface;

public class FragmentCreateLevelCustomer extends BaseFragment<FragmentCreateLevelCustomerViewInterface, BaseParameters> implements FragmentCreateLevelCustomerViewCallback {

    HomeActivity activity;

    @Override
    protected void initialize() {
        activity = (HomeActivity) getActivity();
        view.init(activity, this);
        KeyboardUtils.setupUI(getView(),activity);
    }

    @Override
    protected FragmentCreateLevelCustomerViewInterface getViewInstance() {
        return new FragmentCreateLevelCustomerView();
    }

    @Override
    protected BaseParameters getParametersContainer() {
        return null;
    }

    @Override
    public void onBackProgress() {
        if (activity != null) {
            activity.deleteTempMedia();
            activity.checkBack();
        }
    }

    public void setImageSelected(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            showAlert(getString(R.string.error_load_file_image), KAlertDialog.ERROR_TYPE);
            return;
        }

        reduceImageSize(filePath);
    }

    private void reduceImageSize(String filePath) {

        File fileImage = new File(filePath);

        if (fileImage.exists()) {

            try {
                File compressedImageFile = new Compressor(getContext()).compressToFile(fileImage);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (compressedImageFile.exists()) {
                            view.setDataProductImage(compressedImageFile.getAbsolutePath());
                        } else {
                            view.setDataProductImage(filePath);
                        }
                    }
                }, 300);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void showDialogSelecteImage() {
        if (activity != null)
            activity.changeToActivitySelectImage();
    }

    @Override
    public void createLevelCustomer(LevelCustomerModel levelCustomerModel) {
        if (levelCustomerModel != null) {
            LevelCustomerRequest.ApiParams params = new LevelCustomerRequest.ApiParams();
            params.id_business = AppProvider.getPreferences().getUserModel().getId_business();
            params.type_manager = "create_level";
            params.id_code = levelCustomerModel.getId_code();
            params.name = levelCustomerModel.getName();
            params.discount = levelCustomerModel.getDiscount();
            params.description = levelCustomerModel.getDescription();
            params.image = levelCustomerModel.getImage();

            AppProvider.getApiManagement().call(LevelCustomerRequest.class, params, new ApiRequest.ApiCallback<BaseResponseModel<LevelCustomerModel>>() {
                @Override
                public void onSuccess(BaseResponseModel<LevelCustomerModel> body) {
                    if (body.getSuccess().equals("true")) {
                        Toast.makeText(activity, "" + body.getMessage(), Toast.LENGTH_SHORT).show();
                    } else if (body.getSuccess().equals("false")) {
                        Toast.makeText(activity, "" + body.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onError(ErrorApiResponse error) {

                }

                @Override
                public void onFail(ApiRequest.RequestError error) {

                }
            });
        }
    }

    @Override
    public void onClickOptionSelectImageFromCamera() {
        if (activity != null)
            activity.captureImageFromCamera();
    }

    @Override
    public void onClickOptionSelectImageFromGallery() {
        if (activity != null)
            activity.changeToActivitySelectImage();
    }
}
