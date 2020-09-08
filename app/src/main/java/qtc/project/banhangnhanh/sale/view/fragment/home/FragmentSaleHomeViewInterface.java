package qtc.project.banhangnhanh.sale.view.fragment.home;

import java.util.ArrayList;

import b.laixuantam.myaarlibrary.base.BaseViewInterface;
import qtc.project.banhangnhanh.activity.SaleHomeActivity;
import qtc.project.banhangnhanh.admin.model.CustomerModel;
import qtc.project.banhangnhanh.sale.model.ListOrderModel;
import qtc.project.banhangnhanh.sale.model.ProductModel;

public interface FragmentSaleHomeViewInterface extends BaseViewInterface {

    void init(SaleHomeActivity activity,FragmentSaleHomeViewCallback callback);

    void initView(CustomerModel model);

    void initViewProduct(ArrayList<ProductModel> list);

    void truyenIntent(ArrayList<ListOrderModel> arList);

    void initViewProductClick(ProductModel model);
}
