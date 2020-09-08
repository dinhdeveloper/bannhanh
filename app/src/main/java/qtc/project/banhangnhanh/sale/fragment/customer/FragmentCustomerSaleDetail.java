package qtc.project.banhangnhanh.sale.fragment.customer;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import b.laixuantam.myaarlibrary.api.ApiRequest;
import b.laixuantam.myaarlibrary.api.ErrorApiResponse;
import b.laixuantam.myaarlibrary.base.BaseFragment;
import b.laixuantam.myaarlibrary.base.BaseParameters;
import b.laixuantam.myaarlibrary.widgets.dialog.alert.KAlertDialog;
import qtc.project.banhangnhanh.activity.SaleHomeActivity;
import qtc.project.banhangnhanh.admin.dependency.AppProvider;
import qtc.project.banhangnhanh.admin.model.BaseResponseModel;
import qtc.project.banhangnhanh.admin.model.CustomerModel;
import qtc.project.banhangnhanh.sale.api.CustomerAdminRequest;
import qtc.project.banhangnhanh.sale.event.BackShowRootViewEvent;
import qtc.project.banhangnhanh.sale.event.UpdateListCustomerEvent;
import qtc.project.banhangnhanh.sale.view.fragment.customer.detail.FragmentCustomerSaleDetailView;
import qtc.project.banhangnhanh.sale.view.fragment.customer.detail.FragmentCustomerSaleDetailViewCallback;
import qtc.project.banhangnhanh.sale.view.fragment.customer.detail.FragmentCustomerSaleDetailViewInterface;

public class FragmentCustomerSaleDetail extends BaseFragment<FragmentCustomerSaleDetailViewInterface, BaseParameters> implements FragmentCustomerSaleDetailViewCallback {
    SaleHomeActivity activity;

    public static FragmentCustomerSaleDetail newInstance(CustomerModel model) {
        FragmentCustomerSaleDetail fm = new FragmentCustomerSaleDetail();
        Bundle bundle = new Bundle();
        bundle.putSerializable("model", model);
        fm.setArguments(bundle);
        return fm;
    }

    @Override
    protected void initialize() {
        activity = (SaleHomeActivity) getActivity();
        view.init(activity, this);

        getBundle();
    }

    private void getBundle() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            CustomerModel model = (CustomerModel) bundle.get("model");
            view.setDataCustomerDetail(model);
        } else {
            view.setDataCustomerDetail(null);
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
        if (activity != null) {
            activity.checkBack();
            BackShowRootViewEvent.post();
        }
    }

    @Override
    public void updateCustomer(CustomerModel customerModel, String id) {
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
                    if (!TextUtils.isEmpty(body.getSuccess()) && body.getSuccess().equalsIgnoreCase("true")) {
                        showAlert(body.getMessage(), KAlertDialog.SUCCESS_TYPE);
                        UpdateListCustomerEvent.post();
                    } else {
                        showAlert(body.getMessage(), KAlertDialog.ERROR_TYPE);
                    }
                }

                @Override
                public void onError(ErrorApiResponse error) {
                    dismissProgress();
                    showAlert("Không tải được dữ liệu", KAlertDialog.ERROR_TYPE);
                }

                @Override
                public void onFail(ApiRequest.RequestError error) {
                    dismissProgress();
                    showAlert("Không tải được dữ liệu", KAlertDialog.ERROR_TYPE);
                }
            });
        }
    }

    @Override
    public void createCustomer(CustomerModel customerModel) {
        if (customerModel != null) {
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
                    if (!TextUtils.isEmpty(body.getSuccess()) && body.getSuccess().equalsIgnoreCase("true")) {
                        showAlert(body.getMessage(), KAlertDialog.SUCCESS_TYPE);
                        UpdateListCustomerEvent.post();
                        view.resetView();
                    } else {
                        showAlert(body.getMessage(), KAlertDialog.ERROR_TYPE);
                    }
                }

                @Override
                public void onError(ErrorApiResponse error) {
                    dismissProgress();
                    showAlert("Không tải được dữ liệu", KAlertDialog.ERROR_TYPE);
                }

                @Override
                public void onFail(ApiRequest.RequestError error) {
                    dismissProgress();
                    showAlert("Không tải được dữ liệu", KAlertDialog.ERROR_TYPE);
                }
            });
        }
    }
}
