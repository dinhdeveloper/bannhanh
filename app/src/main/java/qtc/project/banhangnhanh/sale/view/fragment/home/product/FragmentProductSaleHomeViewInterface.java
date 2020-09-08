package qtc.project.banhangnhanh.sale.view.fragment.home.product;

import b.laixuantam.myaarlibrary.base.BaseViewInterface;
import qtc.project.banhangnhanh.activity.SaleHomeActivity;
import qtc.project.banhangnhanh.sale.model.ProductModel;

public interface FragmentProductSaleHomeViewInterface extends BaseViewInterface {

    void init(SaleHomeActivity activity,FragmentProductSaleHomeViewCallback callback);

    void setNoMoreLoading();

    void clearListDataProduct();

    void initRecyclerViewProduct(ProductModel[] data);
}
