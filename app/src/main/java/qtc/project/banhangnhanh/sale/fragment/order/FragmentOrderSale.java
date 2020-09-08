package qtc.project.banhangnhanh.sale.fragment.order;

import android.text.TextUtils;
import android.util.Log;

import java.util.Objects;

import b.laixuantam.myaarlibrary.api.ApiRequest;
import b.laixuantam.myaarlibrary.api.ErrorApiResponse;
import b.laixuantam.myaarlibrary.base.BaseFragment;
import b.laixuantam.myaarlibrary.base.BaseParameters;
import b.laixuantam.myaarlibrary.widgets.dialog.alert.KAlertDialog;
import qtc.project.banhangnhanh.activity.SaleHomeActivity;
import qtc.project.banhangnhanh.admin.dependency.AppProvider;
import qtc.project.banhangnhanh.admin.model.BaseResponseModel;
import qtc.project.banhangnhanh.sale.api.OrderRequest;
import qtc.project.banhangnhanh.sale.fragment.home.FragmentSaleHome;
import qtc.project.banhangnhanh.sale.model.OrderModel;
import qtc.project.banhangnhanh.sale.view.fragment.order.FragmentOrderSaleView;
import qtc.project.banhangnhanh.sale.view.fragment.order.FragmentOrderSaleViewCallback;
import qtc.project.banhangnhanh.sale.view.fragment.order.FragmentOrderSaleViewInterface;

public class FragmentOrderSale extends BaseFragment<FragmentOrderSaleViewInterface, BaseParameters> implements FragmentOrderSaleViewCallback {
    SaleHomeActivity activity;
    int page =1;
    private int totalPage = 0;
    @Override
    protected void initialize() {
        activity = (SaleHomeActivity)getActivity();
        view.init(activity,this);

        requestData();
    }

    private void requestData() {
        showProgress();
        resetPage();
        OrderRequest.ApiParams params = new OrderRequest.ApiParams();
        params.id_business = AppProvider.getPreferences().getUserModel().getId_business();
        params.page = String.valueOf(page);
        AppProvider.getApiManagement().call(OrderRequest.class, params, new ApiRequest.ApiCallback<BaseResponseModel<OrderModel>>() {
            @Override
            public void onSuccess(BaseResponseModel<OrderModel> result) {
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
                    view.initRecyclerViewOrder(result.getData());
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
    protected FragmentOrderSaleViewInterface getViewInstance() {
        return new FragmentOrderSaleView();
    }

    @Override
    protected BaseParameters getParametersContainer() {
        return null;
    }

    @Override
    public void goToDetail(OrderModel model) {
        if (activity!=null)
            resetPage();
        activity.replaceFragment(new FragmentOrderDetailSale().newInstance(model),true);
    }

    private void resetPage() {
        page =1;
        totalPage = 0;
    }

    @Override
    public void goHome() {
        if (activity!=null)
            activity.replaceFragment(new FragmentSaleHome(),false);
    }

    @Override
    public void filter() {
//        if (activity!=null)
//            activity.addFragment(new FragmentFilterOrder(),true);
    }

    @Override
    public void reQuestData() {
        requestData();
    }

    @Override
    public void searchOrder(String search) {
        showProgress();
        OrderRequest.ApiParams params = new OrderRequest.ApiParams();
        params.id_business = AppProvider.getPreferences().getUserModel().getId_business();
        params.order_code = search;

        AppProvider.getApiManagement().call(OrderRequest.class, params, new ApiRequest.ApiCallback<BaseResponseModel<OrderModel>>() {
            @Override
            public void onSuccess(BaseResponseModel<OrderModel> result) {
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
                    view.initRecyclerViewOrder(result.getData());
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
    public void loadMore() {
        ++page;

        if (totalPage > 0 && page <= totalPage) {
            requestDataTwo();
        } else {
            view.setNoMoreLoading();
        }
    }

    private void requestDataTwo() {
        showProgress();
        OrderRequest.ApiParams params = new OrderRequest.ApiParams();
        params.id_business = AppProvider.getPreferences().getUserModel().getId_business();
        resetPage();
        params.page = String.valueOf(page);
        AppProvider.getApiManagement().call(OrderRequest.class, params, new ApiRequest.ApiCallback<BaseResponseModel<OrderModel>>() {
            @Override
            public void onSuccess(BaseResponseModel<OrderModel> result) {
                if (!TextUtils.isEmpty(result.getSuccess()) && Objects.requireNonNull(result.getSuccess()).equalsIgnoreCase("true")) {
                    if (!TextUtils.isEmpty(result.getTotal_page())) {
                        totalPage = Integer.valueOf(result.getTotal_page());
                        if (page == totalPage) {
                            view.setNoMoreLoading();
                        }
                    } else {
                        view.setNoMoreLoading();
                    }
                    view.initRecyclerViewOrder(result.getData());
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
    public void callNav() {
        if (activity!=null)
            activity.toggleNav();
    }

    public void filterData(OrderModel[] list) {
        view.clearnData();
        // view.setLayoutNull();
        if (list != null) {
            view.clearnData();
            view.initRecyclerView(list);
        }
    }

//    public void filterDataDate(ArrayList<OrderModel> list) {
//        if (list!=null){
//            view.initRecyclerViewOrder(list);
//        }
//    }
}