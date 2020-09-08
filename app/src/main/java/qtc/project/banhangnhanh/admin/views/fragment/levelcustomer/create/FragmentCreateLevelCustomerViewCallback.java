package qtc.project.banhangnhanh.admin.views.fragment.levelcustomer.create;

import qtc.project.banhangnhanh.admin.model.LevelCustomerModel;

public interface FragmentCreateLevelCustomerViewCallback {
    void onBackProgress();

    void showDialogSelecteImage();

    void createLevelCustomer(LevelCustomerModel levelCustomerModel);

    void onClickOptionSelectImageFromCamera();

    void onClickOptionSelectImageFromGallery();
}
