package qtc.project.banhangnhanh.sale.fragment.customer;

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
import qtc.project.banhangnhanh.sale.view.fragment.customer.create.FragmentCustomerSaleCreateView;
import qtc.project.banhangnhanh.sale.view.fragment.customer.create.FragmentCustomerSaleCreateViewCallback;
import qtc.project.banhangnhanh.sale.view.fragment.customer.create.FragmentCustomerSaleCreateViewInterface;

public class FragmentCustomerSaleCreate  extends BaseFragment<FragmentCustomerSaleCreateViewInterface, BaseParameters> implements FragmentCustomerSaleCreateViewCallback {
    SaleHomeActivity activity;
    @Override
    protected void initialize() {
        activity =(SaleHomeActivity)getActivity();
        view.init(activity,this);
    }

    @Override
    protected FragmentCustomerSaleCreateViewInterface getViewInstance() {
        return new FragmentCustomerSaleCreateView();
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
    public void createCustomer(CustomerModel customerModel) {
        if (customerModel !=null){
            showProgress();
            CustomerAdminRequest.ApiParams params = new CustomerAdminRequest.ApiParams();

            params.id_business = AppProvider.getPreferences().getUserModel().getId_business();
            params.type_manager = "create_customer";
            params.employee_id = String.valueOf(1);
            params.full_name = customerModel.getFull_name();
            params.address = customerModel.getAddress();
            params.email = customerModel.getEmail();
            params.phone_number = customerModel.getPhone_number();

            AppProvider.getApiManagement().call(CustomerAdminRequest.class, params, new ApiRequest.ApiCallback<BaseResponseModel<CustomerModel>>() {
                @Override
                public void onSuccess(BaseResponseModel<CustomerModel> body) {
                    dismissProgress();
                    if (body.getSuccess().equalsIgnoreCase("true")) {
                        view.showDiaLogSucess();
                    } else if (body.getSuccess().equalsIgnoreCase("false")){
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

