package qtc.project.banhangnhanh.admin.views.fragment.levelcustomer.detail;

import qtc.project.banhangnhanh.admin.model.LevelCustomerModel;

public interface FragmentLevelCustomerDetailViewCallback {

    void onBackProgress();
    void showDialogSelecteImage();
    void showDialogTakePicture();

    void updateData(LevelCustomerModel levelCustomerModel);

    void xemSoNguoiCoCapDo(String  id);

    void callDataSearchCus(String toString,String id);

    void callAllDataCustomer();

    void deleteLevelCustomer(String level_id);

    void onClickOptionSelectImageFromCamera();

    void onClickOptionSelectImageFromGallery();
}
