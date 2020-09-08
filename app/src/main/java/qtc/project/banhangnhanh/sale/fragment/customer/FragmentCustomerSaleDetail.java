package qtc.project.banhangnhanh.sale.fragment.customer;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import b.laixuantam.myaarlibrary.api.ApiRequest;
import b.laixuantam.myaarlibrary.api.ErrorApiResponse;
import b.laixuantam.myaarlibrary.base.BaseFragment;
import b.laixuantam.myaarlibrary.base.BaseParameters;
import qtc.project.banhangnhanh.activity.SaleHomeActivity;
import qtc.project.banhangnhanh.admin.dependency.AppProvider;
import qtc.project.banhangnhanh.admin.model.BaseResponseModel;
import qtc.project.banhangnhanh.admin.model.CustomerModel;
import qtc.project.banhangnhanh.sale.api.CustomerAdminRequest;
import qtc.project.banhangnhanh.sale.view.fragment.customer.detail.FragmentCustomerSaleDetailView;
import qtc.project.banhangnhanh.sale.view.fragment.customer.detail.FragmentCustomerSaleDetailViewCallback;
import qtc.project.banhangnhanh.sale.view.fragment.customer.detail.FragmentCustomerSaleDetailViewInterface;

public class FragmentCustomerSaleDetail  extends BaseFragment<FragmentCustomerSaleDetailViewInterface, BaseParameters> implements FragmentCustomerSaleDetailViewCallback {
    SaleHomeActivity activity;

    public static FragmentCustomerSaleDetail newInstance(CustomerModel model){
        FragmentCustomerSaleDetail fm = new FragmentCustomerSaleDetail();
        Bundle bundle = new Bundle();
        bundle.putSerializable("model",model);
        fm.setArguments(bundle);
        return  fm;
    }
    @Override
    protected void initialize() {
        activity = (SaleHomeActivity)getActivity();
        view.init(activity,this);

        getBundle();
    }

    private void getBundle() {
        Bundle bundle = getArguments();
        if (bundle!=null){
            CustomerModel model = (CustomerModel)bundle.get("model");
            view.initLayout(model);
        }
    }

    @Override
    protected FragmentCustomerSaleDetailViewInterface getViewInstance() {
        return new FragmentCustomerSaleDetailView();
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
    public void updateCustomer(CustomerModel customerModel,String id) {
        if (customerModel != null) {
            showProgress();
            CustomerAdminRequest.ApiParams params = new CustomerAdminRequest.ApiParams();
            params.id_business = AppProvider.getPreferences().getUserModel().getId_business();
            params.type_manager = "update_customer";

            params.id_customer = id;
            params.employee_id = AppProvider.getPreferences().getUserModel().getId();
            params.full_name = customerModel.getFull_name();
            params.address = customerModel.getAddress();
            params.email = customerModel.getEmail();
            params.phone_number = customerModel.getPhone_number();

            AppProvider.getApiManagement().call(CustomerAdminRequest.class, params, new ApiRequest.ApiCallback<BaseResponseModel<CustomerModel>>() {
                @Override
                public void onSuccess(BaseResponseModel<CustomerModel> body) {
                    dismissProgress();
                    if (body.getSuccess().equalsIgnoreCase("true")) {
                        view.showPopUpSuccess();
                        dismissProgress();
                    } else if(body.getSuccess().equalsIgnoreCase("false")){
                        Toast.makeText(getActivity(), "" + body.getMessage(), Toast.LENGTH_SHORT).show();
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
}
