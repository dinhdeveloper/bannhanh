package qtc.project.banhangnhanh.admin.fragment.supplier;

import android.util.Log;
import java.util.ArrayList;
import java.util.Arrays;
import b.laixuantam.myaarlibrary.api.ApiRequest;
import b.laixuantam.myaarlibrary.api.ErrorApiResponse;
import b.laixuantam.myaarlibrary.base.BaseFragment;
import b.laixuantam.myaarlibrary.base.BaseParameters;
import qtc.project.banhangnhanh.activity.HomeActivity;
import qtc.project.banhangnhanh.admin.api.supplier.SupplierRequest;
import qtc.project.banhangnhanh.admin.dependency.AppProvider;
import qtc.project.banhangnhanh.admin.model.BaseResponseModel;
import qtc.project.banhangnhanh.admin.model.SupplierModel;
import qtc.project.banhangnhanh.admin.views.fragment.supplier.FragmentSupplierManagerView;
import qtc.project.banhangnhanh.admin.views.fragment.supplier.FragmentSupplierManagerViewCallback;
import qtc.project.banhangnhanh.admin.views.fragment.supplier.FragmentSupplierManagerViewInterface;

public class FragmentSupplierManager extends BaseFragment<FragmentSupplierManagerViewInterface, BaseParameters> implements FragmentSupplierManagerViewCallback {

    HomeActivity activity;
    @Override
    protected void initialize() {
        activity = (HomeActivity)getActivity();
        view.init(activity,this);
        callApiDataSupp();
    }

    private void callApiDataSupp() {
        showProgress();
        SupplierRequest.ApiParams params = new SupplierRequest.ApiParams();
        params.id_business = AppProvider.getPreferences().getUserModel().getId_business();
        params.type_manager = "list_supplier";
        AppProvider.getApiManagement().call(SupplierRequest.class, params, new ApiRequest.ApiCallback<BaseResponseModel<SupplierModel>>() {
            @Override
            public void onSuccess(BaseResponseModel<SupplierModel> body) {
                dismissProgress();
                if (body != null) {
                    ArrayList<SupplierModel> list = new ArrayList<>();
                    list.addAll(Arrays.asList(body.getData()));
                    view.mappingRecyclerView(list);
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
    protected FragmentSupplierManagerViewInterface getViewInstance() {
        return new FragmentSupplierManagerView();
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
    public void goToDetail(SupplierModel model) {
        activity.replaceFragment( FragmentSupplierDetail.newIntance(model),true,null);
    }

    @Override
    public void createSupplier() {
        activity.replaceFragment(new FragmentCreateSupplier(),true,null);
    }

    @Override
    public void searchSupplier(String filter) {
        showProgress();
        SupplierRequest.ApiParams params = new SupplierRequest.ApiParams();
        params.id_business = AppProvider.getPreferences().getUserModel().getId_business();
        params.type_manager = "list_supplier";
        params.filter = filter;
        AppProvider.getApiManagement().call(SupplierRequest.class, params, new ApiRequest.ApiCallback<BaseResponseModel<SupplierModel>>() {
            @Override
            public void onSuccess(BaseResponseModel<SupplierModel> body) {
                dismissProgress();
                if (body != null) {
                    ArrayList<SupplierModel> list = new ArrayList<>();
                    list.addAll(Arrays.asList(body.getData()));
                    view.mappingRecyclerView(list);
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
    public void getAllData() {
        callApiDataSupp();
    }
}
