package qtc.project.banhangnhanh.admin.fragment.history;

import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

import b.laixuantam.myaarlibrary.api.ApiRequest;
import b.laixuantam.myaarlibrary.api.ErrorApiResponse;
import b.laixuantam.myaarlibrary.base.BaseFragment;
import b.laixuantam.myaarlibrary.base.BaseParameters;
import qtc.project.banhangnhanh.activity.HomeActivity;
import qtc.project.banhangnhanh.admin.api.history.HistoryOrderCustomerRequest;
import qtc.project.banhangnhanh.admin.dependency.AppProvider;
import qtc.project.banhangnhanh.admin.model.BaseResponseModel;
import qtc.project.banhangnhanh.admin.model.CustomerModel;
import qtc.project.banhangnhanh.admin.model.OrderCustomerModel;
import qtc.project.banhangnhanh.admin.views.fragment.history.history.FragmentHistoryOrderCustomerView;
import qtc.project.banhangnhanh.admin.views.fragment.history.history.FragmentHistoryOrderCustomerViewCallback;
import qtc.project.banhangnhanh.admin.views.fragment.history.history.FragmentHistoryOrderCustomerViewInterface;

public class FragmentHistoryOrderCustomer extends BaseFragment<FragmentHistoryOrderCustomerViewInterface, BaseParameters> implements FragmentHistoryOrderCustomerViewCallback {

    HomeActivity activity;
    public static FragmentHistoryOrderCustomer newIntance(CustomerModel item) {
        FragmentHistoryOrderCustomer frag = new FragmentHistoryOrderCustomer();
        Bundle bundle = new Bundle();
        bundle.putSerializable("model", item);
        frag.setArguments(bundle);
        return frag;
    }
    @Override
    protected void initialize() {
        activity = (HomeActivity)getActivity();
        view.init(activity,this);

        getDataToBundle();
    }

    private void getDataToBundle() {
        Bundle extras = getArguments();
        if (extras != null) {
            CustomerModel model = (CustomerModel) extras.get("model");
            showProgress();
            HistoryOrderCustomerRequest.ApiParams params = new HistoryOrderCustomerRequest.ApiParams();
            params.customer_id = model.getId();
            params.id_business = AppProvider.getPreferences().getUserModel().getId_business();
            AppProvider.getApiManagement().call(HistoryOrderCustomerRequest.class, params, new ApiRequest.ApiCallback<BaseResponseModel<OrderCustomerModel>>() {
                @Override
                public void onSuccess(BaseResponseModel<OrderCustomerModel> body) {
                    dismissProgress();
                    ArrayList<OrderCustomerModel> list = new ArrayList<>();
                    list.addAll(Arrays.asList(body.getData()));
                    view.sendDataToView(list,model.getId());
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

    @Override
    protected FragmentHistoryOrderCustomerViewInterface getViewInstance() {
        return new FragmentHistoryOrderCustomerView();
    }

    @Override
    protected BaseParameters getParametersContainer() {
        return null;
    }

    @Override
    public void onBackProgress() {
        if (activity!=null)
            activity.checkBack();
    }

    @Override
    public void sentDataToDetail(OrderCustomerModel model) {
        activity.addFragment(new FragmentOrderDetailCustomer().newIntance(model),true,null);
    }

    @Override
    public void searchHistoryOrderCustomer(String order_id, String customer_id) {
        showProgress();
        HistoryOrderCustomerRequest.ApiParams params = new HistoryOrderCustomerRequest.ApiParams();
        params.customer_id = customer_id;
        params.id_business = AppProvider.getPreferences().getUserModel().getId_business();
        params.order_code = order_id;
        AppProvider.getApiManagement().call(HistoryOrderCustomerRequest.class, params, new ApiRequest.ApiCallback<BaseResponseModel<OrderCustomerModel>>() {
            @Override
            public void onSuccess(BaseResponseModel<OrderCustomerModel> body) {
                dismissProgress();
                ArrayList<OrderCustomerModel> list = new ArrayList<>();
                list.addAll(Arrays.asList(body.getData()));
                view.sendDataToView(list,customer_id);
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
