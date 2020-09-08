package qtc.project.banhangnhanh.sale.view.fragment.product;

import b.laixuantam.myaarlibrary.base.BaseViewInterface;
import qtc.project.banhangnhanh.activity.SaleHomeActivity;
import qtc.project.banhangnhanh.sale.model.ProductModel;

public interface FragmentProductSaleViewInterface extends BaseViewInterface {

    void init(SaleHomeActivity activity,FragmentProductSaleViewCallback callback);

    void setNoMoreLoading();

    void initRecyclerViewProduct(ProductModel[] data);

    void clearListDataProduct();
}
