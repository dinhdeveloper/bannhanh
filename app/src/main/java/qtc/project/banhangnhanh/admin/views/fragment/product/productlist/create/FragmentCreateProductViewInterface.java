package qtc.project.banhangnhanh.admin.views.fragment.product.productlist.create;

import java.util.ArrayList;

import b.laixuantam.myaarlibrary.base.BaseViewInterface;
import qtc.project.banhangnhanh.activity.HomeActivity;
import qtc.project.banhangnhanh.admin.model.ProductCategoryModel;

public interface FragmentCreateProductViewInterface extends BaseViewInterface {

    void init(HomeActivity activity,FragmentCreateProductViewCallback callback);

    void  setDataProductImage(String image);

    void initViewPopup(ArrayList<ProductCategoryModel> list);

    void showPopupSuccess();
}
