package qtc.project.banhangnhanh.admin.fragment.product.product_disable;

import android.text.TextUtils;
import android.util.Log;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Objects;

import b.laixuantam.myaarlibrary.api.ApiRequest;
import b.laixuantam.myaarlibrary.api.ErrorApiResponse;
import b.laixuantam.myaarlibrary.base.BaseFragment;
import b.laixuantam.myaarlibrary.base.BaseParameters;
import b.laixuantam.myaarlibrary.widgets.dialog.alert.KAlertDialog;
import qtc.project.banhangnhanh.R;
import qtc.project.banhangnhanh.activity.HomeActivity;
import qtc.project.banhangnhanh.admin.api.product.productlist.ProductListRequest;
import qtc.project.banhangnhanh.admin.dependency.AppProvider;
import qtc.project.banhangnhanh.admin.event.UpdateServiceItemEvent;
import qtc.project.banhangnhanh.admin.model.BaseResponseModel;
import qtc.project.banhangnhanh.admin.model.ProductListModel;
import qtc.project.banhangnhanh.admin.views.fragment.product.product_disable.FragmentProductDisableView;
import qtc.project.banhangnhanh.admin.views.fragment.product.product_disable.FragmentProductDisableViewCallback;
import qtc.project.banhangnhanh.admin.views.fragment.product.product_disable.FragmentProductDisableViewInterface;

public class FragmentProductDisable extends BaseFragment<FragmentProductDisableViewInterface, BaseParameters> implements FragmentProductDisableViewCallback {
    HomeActivity activity;
    boolean checked;
    int page =1;
    private int totalPage = 0;

    @Override
    protected void initialize() {
        activity = (HomeActivity) getActivity();
        view.init(activity, this);

        requestListProductProduct();
    }

    private void requestListProductProduct() {
        showProgress();
        ProductListRequest.ApiParams params = new ProductListRequest.ApiParams();
        params.id_business = AppProvider.getPreferences().getUserModel().getId_business();
        params.type_manager = "list_product";
        params.status_product = "N";
        params.page = String.valueOf(page);
        AppProvider.getApiManagement().call(ProductListRequest.class, params, new ApiRequest.ApiCallback<BaseResponseModel<ProductListModel>>() {
            @Override
            public void onSuccess(BaseResponseModel<ProductListModel> result) {
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
                    view.setListData(result.getData());
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
    public void callAllData() {
        view.clearListDataProduct();
        requestListProductProduct();
    }

    @Override
    public void loadMore() {
        ++page;

        if (totalPage > 0 && page <= totalPage) {
            requestListProductProduct();
        } else {
            view.setNoMoreLoading();
        }
    }

    @Override
    public void goToProductDisable(ProductListModel model) {
        final KAlertDialog mCustomAlert = new KAlertDialog(getContext());
        mCustomAlert.setContentText("Đang xử lý...")
                .showCancelButton(false)
                .setCancelClickListener(null)
                .changeAlertType(KAlertDialog.PROGRESS_TYPE);

        mCustomAlert.setCancelable(false);
        mCustomAlert.setCanceledOnTouchOutside(false);
        mCustomAlert.show();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (!AppProvider.getConnectivityHelper().hasInternetConnection()) {
                    showAlert(getString(R.string.error_connect_internet), KAlertDialog.ERROR_TYPE);
                    return;
                }
                showProgress();

                showProgress();
                ProductListRequest.ApiParams params = new ProductListRequest.ApiParams();
                params.id_business = AppProvider.getPreferences().getUserModel().getId_business();
                params.type_manager = "update_status_product";
                params.id_product  = model.getId();
                params.status_product   = "Y";
                AppProvider.getApiManagement().call(ProductListRequest.class, params, new ApiRequest.ApiCallback<BaseResponseModel<ProductListModel>>() {
                    @Override
                    public void onSuccess(BaseResponseModel<ProductListModel> body) {
                        dismissProgress();

                        if (!TextUtils.isEmpty(body.getSuccess()) && body.getSuccess().equalsIgnoreCase("true")) {
                            mCustomAlert.setContentText("Cập nhật sản phẩm thành công.")
                                    .setConfirmText("OK")
                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                        @Override
                                        public void onClick(KAlertDialog kAlertDialog) {
                                            mCustomAlert.dismissWithAnimation();
                                            onBackprogress();
                                            //UpdateServiceItemEvent.post();
                                        }
                                    })
                                    .changeAlertType(KAlertDialog.SUCCESS_TYPE);
                        } else {
                            if (!TextUtils.isEmpty(body.getMessage())) {
                                showAlert(body.getMessage(), KAlertDialog.ERROR_TYPE);
                            } else {
                                activity.showAlert("Không thể tải dịch vụ", KAlertDialog.ERROR_TYPE);
                            }
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
        }, 500);
    }

    @SuppressWarnings("unused")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateServiceItemEvent(UpdateServiceItemEvent event) {
        if (view != null) {
           // view.clearListDataProduct();
            handler.postDelayed(() -> {
                requestListProductProduct();
            }, 500);
        }
    }

    @Override
    public void searchProduct(String name) {
        showProgress();
        ProductListRequest.ApiParams params = new ProductListRequest.ApiParams();
        params.id_business = AppProvider.getPreferences().getUserModel().getId_business();
        params.type_manager = "list_product";
        params.status_product = "N";
        params.product = name;
        AppProvider.getApiManagement().call(ProductListRequest.class, params, new ApiRequest.ApiCallback<BaseResponseModel<ProductListModel>>() {
            @Override
            public void onSuccess(BaseResponseModel<ProductListModel> body) {
                dismissProgress();
                if (body != null) {
                    view.clearListDataProduct();
                    view.setNoMoreLoading();
                    view.setListData(body.getData());
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
    public void onBackprogress() {
        if (activity!=null){
            activity.checkBack();
            requestListProductProduct();
        }
    }

    @Override
    protected FragmentProductDisableViewInterface getViewInstance() {
        return new FragmentProductDisableView();
    }

    @Override
    protected BaseParameters getParametersContainer() {
        return null;
    }

//    @SuppressWarnings("unused")
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onUpdateServiceItemEvent(UpdateServiceItemEvent event) {
//        if (view != null) {
//            view.clearListData();
//            requestGetListServiceDisible();
//        }
    //}
}
