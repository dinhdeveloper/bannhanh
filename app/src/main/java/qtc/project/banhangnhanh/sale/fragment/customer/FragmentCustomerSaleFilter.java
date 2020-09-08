package qtc.project.banhangnhanh.sale.fragment.customer;

import android.text.TextUtils;
import android.util.Log;

import java.util.Objects;

import b.laixuantam.myaarlibrary.api.ApiRequest;
import b.laixuantam.myaarlibrary.api.ErrorApiResponse;
import b.laixuantam.myaarlibrary.base.BaseFragment;
import b.laixuantam.myaarlibrary.base.BaseParameters;
import b.laixuantam.myaarlibrary.widgets.dialog.alert.KAlertDialog;
import qtc.project.banhangnhanh.activity.SaleHomeActivity;
import qtc.project.banhangnhanh.admin.api.customer.CustomerRequest;
import qtc.project.banhangnhanh.admin.dependency.AppProvider;
import qtc.project.banhangnhanh.admin.model.BaseResponseModel;
import qtc.project.banhangnhanh.admin.model.CustomerModel;
import qtc.project.banhangnhanh.sale.api.CustomerSaleRequest;
import qtc.project.banhangnhanh.sale.event.BackShowRootViewEvent;
import qtc.project.banhangnhanh.sale.view.fragment.customer.filter.FragmentCustomerSaleFilterView;
import qtc.project.banhangnhanh.sale.view.fragment.customer.filter.FragmentCustomerSaleFilterViewCallback;
import qtc.project.banhangnhanh.sale.view.fragment.customer.filter.FragmentCustomerSaleFilterViewInterface;

public class FragmentCustomerSaleFilter extends BaseFragment<FragmentCustomerSaleFilterViewInterface, BaseParameters> implements FragmentCustomerSaleFilterViewCallback {

    SaleHomeActivity activity;
    int page = 1;
    private int totalPage = 0;

    @Override
    protected void initialize() {
        activity = (SaleHomeActivity) getActivity();
        view.init(activity, this);

        callDataCustomer();
    }

    private void callDataCustomer() {
        showProgress();
        CustomerSaleRequest.ApiParams params = new CustomerSaleRequest.ApiParams();
        params.id_business = AppProvider.getPreferences().getUserModel().getId_business();
        params.page = String.valueOf(page);
        AppProvider.getApiManagement().call(CustomerSaleRequest.class, params, new ApiRequest.ApiCallback<BaseResponseModel<CustomerModel>>() {
            @Override
            public void onSuccess(BaseResponseModel<CustomerModel> result) {
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
                    view.initCustomer(result.getData());
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
    protected FragmentCustomerSaleFilterViewInterface getViewInstance() {
        return new FragmentCustomerSaleFilterView();
    }

    @Override
    protected BaseParameters getParametersContainer() {
        return null;
    }

    @Override
    public void setCustomerToHome(CustomerModel model) {
        if (activity != null) {
            onBackP();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    activity.setDataCustomer(model);
                }
            }, 100);
        }
    }

    @Override
    public void onBackP() {
        if (activity != null)
            activity.checkBack();
            BackShowRootViewEvent.post();
    }

    @Override
    public void getAllData() {
        //showProgress();
        resetPage();
        callDataCustomer();
//        CustomerRequest.ApiParams params = new CustomerRequest.ApiParams();
//        params.id_business = AppProvider.getPreferences().getUserModel().getId_business();
//        params.page = String.valueOf(page);
//        AppProvider.getApiManagement().call(CustomerRequest.class, params, new ApiRequest.ApiCallback<BaseResponseModel<CustomerModel>>() {
//            @Override
//            public void onSuccess(BaseResponseModel<CustomerModel> result) {
//                dismissProgress();
//                if (!TextUtils.isEmpty(result.getSuccess()) && Objects.requireNonNull(result.getSuccess()).equalsIgnoreCase("true")) {
//                    if (!TextUtils.isEmpty(result.getTotal_page())) {
//                        totalPage = Integer.valueOf(result.getTotal_page());
//                        if (page == totalPage) {
//                            view.setNoMoreLoading();
//                        }
//                    } else {
//                        view.setNoMoreLoading();
//                    }
//                    view.clearnData();
//                    view.initCustomer(result.getData());
//                } else {
//                    if (!TextUtils.isEmpty(result.getMessage()))
//                        showAlert(result.getMessage(), KAlertDialog.ERROR_TYPE);
//                    else
//                        showAlert("Không thể tải dữ liệu.", KAlertDialog.ERROR_TYPE);
//                }
//            }
//
//            @Override
//            public void onError(ErrorApiResponse error) {
//                dismissProgress();
//                Log.e("onError", error.message);
//            }
//
//            @Override
//            public void onFail(ApiRequest.RequestError error) {
//                dismissProgress();
//                Log.e("onFail", error.name());
//            }
//        });
    }

    @Override
    public void searchCustomer(String search) {
        showProgress();
        CustomerSaleRequest.ApiParams params = new CustomerSaleRequest.ApiParams();
        params.id_business = AppProvider.getPreferences().getUserModel().getId_business();
        params.type_manager = "list_customer";
        if (search != null) {
            params.customer_filter = search;
        }
        AppProvider.getApiManagement().call(CustomerSaleRequest.class, params, new ApiRequest.ApiCallback<BaseResponseModel<CustomerModel>>() {
            @Override
            public void onSuccess(BaseResponseModel<CustomerModel> body) {
                if (body != null) {
                    dismissProgress();
                    view.clearnData();
                    view.initCustomer(body.getData());
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

    private void resetPage() {
        page = 1;
        totalPage = 0;
    }
}