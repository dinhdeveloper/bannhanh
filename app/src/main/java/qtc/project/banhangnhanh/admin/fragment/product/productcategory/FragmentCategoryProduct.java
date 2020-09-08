package qtc.project.banhangnhanh.admin.fragment.product.productcategory;

import android.text.TextUtils;
import android.util.Log;

import java.util.Objects;

import b.laixuantam.myaarlibrary.api.ApiRequest;
import b.laixuantam.myaarlibrary.api.ErrorApiResponse;
import b.laixuantam.myaarlibrary.base.BaseFragment;
import b.laixuantam.myaarlibrary.base.BaseParameters;
import b.laixuantam.myaarlibrary.widgets.dialog.alert.KAlertDialog;
import qtc.project.banhangnhanh.activity.HomeActivity;
import qtc.project.banhangnhanh.admin.api.product.productcategory.ProductCategoryRequest;
import qtc.project.banhangnhanh.admin.dependency.AppProvider;
import qtc.project.banhangnhanh.admin.model.BaseResponseModel;
import qtc.project.banhangnhanh.admin.model.ProductCategoryModel;
import qtc.project.banhangnhanh.admin.views.fragment.product.category.categoryproduct.FragmentCategoryProductView;
import qtc.project.banhangnhanh.admin.views.fragment.product.category.categoryproduct.FragmentCategoryProductViewCallback;
import qtc.project.banhangnhanh.admin.views.fragment.product.category.categoryproduct.FragmentCategoryProductViewInterface;

public class FragmentCategoryProduct extends BaseFragment<FragmentCategoryProductViewInterface, BaseParameters> implements FragmentCategoryProductViewCallback {
    HomeActivity activity;
    int page =1;
    private int totalPage = 0;

    @Override
    protected void initialize() {
        activity = (HomeActivity) getActivity();
        view.init(activity, this);

        requestDataProductCategory();
    }

    private void requestDataProductCategory() {
        showProgress();
        ProductCategoryRequest.ApiParams params = new ProductCategoryRequest.ApiParams();
        params.id_business = AppProvider.getPreferences().getUserModel().getId_business();
        params.type_manager = "list_category";
        params.page = String.valueOf(page);
        AppProvider.getApiManagement().call(ProductCategoryRequest.class, params, new ApiRequest.ApiCallback<BaseResponseModel<ProductCategoryModel>>() {
            @Override
            public void onSuccess(BaseResponseModel<ProductCategoryModel> result) {
                dismissProgress();
                if (!TextUtils.isEmpty(result.getSuccess()) && Objects.requireNonNull(result.getSuccess()).equalsIgnoreCase("true")) {
                    if (!TextUtils.isEmpty(result.getTotal_page())) {
                        totalPage = Integer.valueOf(result.getTotal_page());
                        if (page == totalPage) {
                            view.setNoMoreLoading();
                        }
                    } else {
                        view.setNoMoreLoading();
                    }
                    view.clearnData();
                    view.initGetListCategoryProduct(result.getData());
                } else {
                    if (!TextUtils.isEmpty(result.getMessage()))
                        showAlert(result.getMessage(), KAlertDialog.ERROR_TYPE);
                    else
                        showAlert("Không thể tải dữ liệu.", KAlertDialog.ERROR_TYPE);
                }
            }

            @Override
            public void onError(ErrorApiResponse error) {
                dismissProgress();
                Log.e("onError", error.message);
            }

            @Override
            public void onFail(ApiRequest.RequestError error) {
                dismissProgress();
                Log.e("onFail", error.name());
            }
        });
    }

    @Override
    protected FragmentCategoryProductViewInterface getViewInstance() {
        return new FragmentCategoryProductView();
    }

    @Override
    protected BaseParameters getParametersContainer() {
        return null;
    }


    @Override
    public void setBackProgress() {
        if (activity != null)
            activity.checkBack();
    }

    @Override
    public void onSendData(ProductCategoryModel model) {
        activity.replaceFragment( FragmentCategoryProductDetail.newIntance(model),true,null);
    }

    @Override
    public void callAllData() {
        requestDataProductCategory();
    }

    @Override
    public void loadMore() {
        ++page;

        if (totalPage > 0 && page <= totalPage) {

            requestDataProductCategory();
        } else {
            view.setNoMoreLoading();
        }
    }

}
