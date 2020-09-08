package qtc.project.banhangnhanh.sale.view.fragment.product.detail;

import qtc.project.banhangnhanh.sale.model.ProductModel;

public interface FragmentProductSaleDetailViewCallback {
    void onBackP();

    void updateProductDetail(ProductModel productModel);

    void inBarCode(String product_name, String barcode, String status);
}
