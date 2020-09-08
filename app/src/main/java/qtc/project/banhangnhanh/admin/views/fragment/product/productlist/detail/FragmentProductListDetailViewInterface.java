package qtc.project.banhangnhanh.admin.views.fragment.product.productlist.detail;

import java.util.ArrayList;

import b.laixuantam.myaarlibrary.base.BaseViewInterface;
import qtc.project.banhangnhanh.activity.HomeActivity;
import qtc.project.banhangnhanh.admin.model.ProductCategoryModel;
import qtc.project.banhangnhanh.admin.model.ProductListModel;

public interface FragmentProductListDetailViewInterface extends BaseViewInterface {
    void  init(HomeActivity activity,FragmentProductListDetailViewCallback callback);

    void sendDataToView(ProductListModel model);
    void  setDataProductImage(String image);
    void initViewPopup(ArrayList<ProductCategoryModel> list);

    void showConfirm();

    void showConfirmDelete();
}
