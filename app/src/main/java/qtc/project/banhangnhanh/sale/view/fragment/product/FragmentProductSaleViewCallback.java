package qtc.project.banhangnhanh.sale.view.fragment.product;

import qtc.project.banhangnhanh.sale.model.ProductModel;

public interface FragmentProductSaleViewCallback {
    void onBackP();

    void loadMore();

    void goToDetail(ProductModel model);

    void searchProduct(String search);

    void callAllData();

    void resetPage();

    void callNav();
}
