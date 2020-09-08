package qtc.project.banhangnhanh.admin.views.fragment.product.productlist;

import b.laixuantam.myaarlibrary.base.BaseViewInterface;
import qtc.project.banhangnhanh.activity.HomeActivity;
import qtc.project.banhangnhanh.admin.model.ProductListModel;

public interface FragmentProductListCategoryViewInterface extends BaseViewInterface {
    void init(HomeActivity activity, FragmentProductListCategoryViewCallback callback);

    void mappingRecyclerView(ProductListModel[] list);

    void mappingDataFilterProduct(ProductListModel[] list,String name,String id);

    void setNoMoreLoading();

    void clearListDataProduct();
}
