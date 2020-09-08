package qtc.project.banhangnhanh.admin.views.fragment.product.product_disable;

import b.laixuantam.myaarlibrary.base.BaseViewInterface;
import qtc.project.banhangnhanh.activity.HomeActivity;
import qtc.project.banhangnhanh.admin.model.ProductListModel;

public interface FragmentProductDisableViewInterface extends BaseViewInterface {

    void init(HomeActivity activity,FragmentProductDisableViewCallback callback);

    void setNoMoreLoading();

    void setListData(ProductListModel[] data);

    void clearListDataProduct();
}
