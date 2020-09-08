package qtc.project.banhangnhanh.admin.views.fragment.product.category.createcategoryproduct;

import qtc.project.banhangnhanh.admin.model.ProductCategoryModel;

public interface FragmentCreateProductCategoryViewCallback {

    void onBackProgress();
   // void showDialogSelecteImage();
    //void showDialogTakePicture();

    void createCategoryProduct(ProductCategoryModel categoryModel);

    void onClickOptionSelectImageFromCamera();

    void onClickOptionSelectImageFromGallery();
}
