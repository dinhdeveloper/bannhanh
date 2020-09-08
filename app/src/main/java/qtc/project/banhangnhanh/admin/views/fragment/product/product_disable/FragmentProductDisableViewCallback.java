package qtc.project.banhangnhanh.admin.views.fragment.product.product_disable;

import qtc.project.banhangnhanh.admin.model.ProductListModel;

public interface FragmentProductDisableViewCallback {
    void loadMore();

    void goToProductDisable(ProductListModel model);

    void onBackprogress();

    void searchProduct(String name);

    void callAllData();
}
