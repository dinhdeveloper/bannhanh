package qtc.project.banhangnhanh.admin.views.fragment.product.category.categoryproductdetail;

import qtc.project.banhangnhanh.admin.model.ProductCategoryModel;

public interface FragmentCategoryProductDetailViewCallback {
    void onBackProgress();
    void showDialogSelecteImage();

    void showDialogTakePicture();

    void updateData(ProductCategoryModel categoryModel);

    void deleteProductCategoryModel(String id);

    void onClickOptionSelectImageFromCamera();

    void onClickOptionSelectImageFromGallery();
}
