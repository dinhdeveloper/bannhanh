package qtc.project.banhangnhanh.admin.views.fragment.product.productlist.create;

import qtc.project.banhangnhanh.admin.model.ProductListModel;

public interface FragmentCreateProductViewCallback {
    void onBackprogress();

    //void showDialogSelecteImage();

    void getAllProductCategory();

    void createProduct(ProductListModel listModel);

    void onClickOptionSelectImageFromCamera();

    void onClickOptionSelectImageFromGallery();
}
