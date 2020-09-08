package qtc.project.banhangnhanh.admin.views.fragment.product.category.createcategoryproduct;

import b.laixuantam.myaarlibrary.base.BaseViewInterface;
import qtc.project.banhangnhanh.activity.HomeActivity;

public interface FragmentCreateProductCategoryViewInterface extends BaseViewInterface {
    void init(HomeActivity activity,FragmentCreateProductCategoryViewCallback callback);
    void setDataProductImage(String image);

    void onBack();

    void confirmDialog();
}
