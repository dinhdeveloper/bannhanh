package qtc.project.banhangnhanh.sale.view.fragment.home.product;

import qtc.project.banhangnhanh.sale.model.ProductModel;

public interface FragmentProductSaleHomeViewCallback {
    void onBackP();

    void loadMore();

    void goToDetail(ProductModel model);

    void searchProduct(String search);

    void callAllData();

    void resetPage();
}
