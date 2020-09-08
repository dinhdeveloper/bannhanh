package qtc.project.banhangnhanh.sale.view.fragment.product.detail;

import b.laixuantam.myaarlibrary.base.BaseViewInterface;
import qtc.project.banhangnhanh.activity.SaleHomeActivity;
import qtc.project.banhangnhanh.sale.model.ProductModel;

public interface FragmentProductSaleDetailViewInterface extends BaseViewInterface {

    void init(SaleHomeActivity activity,FragmentProductSaleDetailViewCallback callback);

    void initView(ProductModel model);

    void showUpdateSuccess();
}
