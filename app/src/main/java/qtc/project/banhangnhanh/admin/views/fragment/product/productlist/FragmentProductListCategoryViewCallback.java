package qtc.project.banhangnhanh.admin.views.fragment.product.productlist;

import qtc.project.banhangnhanh.admin.model.ProductListModel;

public interface FragmentProductListCategoryViewCallback {
    void onBackprogress();

    void goToProductListDetail(ProductListModel model);

    void searchProduct(String name);

    void callAllData();

    void reQuestList();

    void loadMore();
}
