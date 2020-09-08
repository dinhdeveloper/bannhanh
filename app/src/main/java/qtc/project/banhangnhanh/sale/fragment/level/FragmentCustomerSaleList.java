package qtc.project.banhangnhanh.sale.fragment.level;

import android.os.Bundle;
import android.util.Log;

import b.laixuantam.myaarlibrary.api.ApiRequest;
import b.laixuantam.myaarlibrary.api.ErrorApiResponse;
import b.laixuantam.myaarlibrary.base.BaseFragment;
import b.laixuantam.myaarlibrary.base.BaseParameters;
import qtc.project.banhangnhanh.activity.SaleHomeActivity;
import qtc.project.banhangnhanh.admin.dependency.AppProvider;
import qtc.project.banhangnhanh.admin.model.BaseResponseModel;
import qtc.project.banhangnhanh.admin.model.CustomerModel;
import qtc.project.banhangnhanh.sale.api.CustomerSaleRequest;
import qtc.project.banhangnhanh.sale.view.fragment.level.byId.FragmentCustomerSaleListView;
import qtc.project.banhangnhanh.sale.view.fragment.level.byId.FragmentCustomerSaleListViewCallback;
import qtc.project.banhangnhanh.sale.view.fragment.level.byId.FragmentCustomerSaleListViewInterface;

public class FragmentCustomerSaleList extends BaseFragment<FragmentCustomerSaleListViewInterface, BaseParameters> implements FragmentCustomerSaleListViewCallback {

    SaleHomeActivity activity;
    String name;
    public  static FragmentCustomerSaleList newInstance(String name, CustomerModel[] list){
        FragmentCustomerSaleList fm = new FragmentCustomerSaleList();
        Bundle bundle = new Bundle();
        bundle.putSerializable("model",list);
        bundle.putString("name",name);
        fm.setArguments(bundle);

        return fm;
    }
    @Override
    protected void initialize() {
        activity = (SaleHomeActivity) getActivity();
        view.init(activity,this);

        getBundle();
    }

    private void getBundle() {
        Bundle bundle = getArguments();
        if (bundle!=null){
            CustomerModel[] list = (CustomerModel[])bundle.get("model");
            name = (String)bundle.get("name");
            view.initView(name,list);
        }
    }

    @Override
    protected FragmentCustomerSaleListViewInterface getViewInstance() {
        return new FragmentCustomerSaleListView();
    }

    @Override
    protected BaseParameters getParametersContainer() {
        return null;
    }

    @Override
    public void onBackP() {
        if (activity!=null)
            activity.checkBack();
    }

    @Override
    public void getAllData() {
        view.resetData();
        getBundle();
    }

    @Override
    public void seachCustomer(String search,String level_id) {
        showProgress();
        CustomerSaleRequest.ApiParams params = new CustomerSaleRequest.ApiParams();
        params.id_business = AppProvider.getPreferences().getUserModel().getId_business();
        if (search!=null){
            params.customer_filter = search;
        }
        params.level_id = level_id;
        AppProvider.getApiManagement().call(CustomerSaleRequest.class, params, new ApiRequest.ApiCallback<BaseResponseModel<CustomerModel>>() {
            @Override
            public void onSuccess(BaseResponseModel<CustomerModel> body) {
                if (body != null) {
                    dismissProgress();
                    view.clearnData();
                    view.initView(name,body.getData());
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
}
