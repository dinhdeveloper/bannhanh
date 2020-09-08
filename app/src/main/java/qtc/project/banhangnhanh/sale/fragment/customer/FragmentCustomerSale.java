package qtc.project.banhangnhanh.sale.fragment.customer;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

import b.laixuantam.myaarlibrary.api.ApiRequest;
import b.laixuantam.myaarlibrary.api.ErrorApiResponse;
import b.laixuantam.myaarlibrary.base.BaseFragment;
import b.laixuantam.myaarlibrary.base.BaseParameters;
import qtc.project.banhangnhanh.activity.SaleHomeActivity;
import qtc.project.banhangnhanh.admin.dependency.AppProvider;
import qtc.project.banhangnhanh.admin.model.BaseResponseModel;
import qtc.project.banhangnhanh.admin.model.CustomerModel;
import qtc.project.banhangnhanh.sale.api.CustomerSaleRequest;
import qtc.project.banhangnhanh.sale.fragment.home.FragmentSaleHome;
import qtc.project.banhangnhanh.sale.view.fragment.customer.FragmentCustomerSaleView;
import qtc.project.banhangnhanh.sale.view.fragment.customer.FragmentCustomerSaleViewCallback;
import qtc.project.banhangnhanh.sale.view.fragment.customer.FragmentCustomerSaleViewInterface;

public class FragmentCustomerSale extends BaseFragment<FragmentCustomerSaleViewInterface, BaseParameters> implements FragmentCustomerSaleViewCallback {

    SaleHomeActivity activity;
    @Override
    protected void initialize() {
        activity  =(SaleHomeActivity)getActivity();
        view.init(activity,this);

        requestDataCustomer();
    }
    private void requestDataCustomer() {
        showProgress();
        CustomerSaleRequest.ApiParams params = new CustomerSaleRequest.ApiParams();
        params.id_business = AppProvider.getPreferences().getUserModel().getId_business();
        AppProvider.getApiManagement().call(CustomerSaleRequest.class, params, new ApiRequest.ApiCallback<BaseResponseModel<CustomerModel>>() {
            @Override
            public void onSuccess(BaseResponseModel<CustomerModel> body) {
                if (body != null) {
                    dismissProgress();
                    ArrayList<CustomerModel> list = new ArrayList<>();
                    list.addAll(Arrays.asList(body.getData()));
                    view.initRecyclerViewCustomer(list);
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
    protected FragmentCustomerSaleViewInterface getViewInstance() {
        return new FragmentCustomerSaleView();
    }

    @Override
    protected BaseParameters getParametersContainer() {
        return null;
    }

    @Override
    public void goHome() {
        if (activity!=null)
            activity.replaceFragment(new FragmentSaleHome(),false);
    }

    @Override
    public void goDetail(CustomerModel model) {
        if (activity!=null)
            activity.replaceFragment(new FragmentCustomerSaleDetail().newInstance(model),true);
    }

    @Override
    public void addCustomer() {
        if (activity!=null)
            activity.replaceFragment(new FragmentCustomerSaleCreate(),true);
    }

    @Override
    public void callAllDataCustomer() {
        requestDataCustomer();
    }

    @Override
    public void callDataSearchCus(String toString) {
        showProgress();
        CustomerSaleRequest.ApiParams params = new CustomerSaleRequest.ApiParams();
        params.id_business = AppProvider.getPreferences().getUserModel().getId_business();
        if (toString!=null){
            params.customer_filter = toString;
        }
        AppProvider.getApiManagement().call(CustomerSaleRequest.class, params, new ApiRequest.ApiCallback<BaseResponseModel<CustomerModel>>() {
            @Override
            public void onSuccess(BaseResponseModel<CustomerModel> body) {
                if (body != null) {
                    dismissProgress();
                    ArrayList<CustomerModel> list = new ArrayList<>();
                    list.addAll(Arrays.asList(body.getData()));
                    view.initRecyclerViewCustomer(list);
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
}