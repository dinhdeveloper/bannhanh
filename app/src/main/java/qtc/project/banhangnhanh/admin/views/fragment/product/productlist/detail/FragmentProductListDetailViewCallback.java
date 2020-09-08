package qtc.project.banhangnhanh.admin.views.fragment.product.productlist.detail;

import qtc.project.banhangnhanh.admin.model.ProductListModel;

public interface FragmentProductListDetailViewCallback {
    void onBackprogress();

    //void showDialogSelecteImage();

    void getAllProductCategory();

    void undateData(ProductListModel listModel);

    void deleteProduct(ProductListModel model);

    void onClickOptionSelectImageFromCamera();

    void onClickOptionSelectImageFromGallery();
}
