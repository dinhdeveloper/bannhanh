package qtc.project.banhangnhanh.admin.views.fragment.product.category.categoryproduct;

import qtc.project.banhangnhanh.admin.model.ProductCategoryModel;

public interface FragmentCategoryProductViewCallback {
    void setBackProgress();

    void onSendData(ProductCategoryModel model);

    void callAllData();

    void loadMore();
}
