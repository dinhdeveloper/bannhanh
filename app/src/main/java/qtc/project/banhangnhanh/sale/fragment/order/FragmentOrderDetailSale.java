package qtc.project.banhangnhanh.sale.fragment.order;

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
import qtc.project.banhangnhanh.sale.api.CancelOrderRequest;
import qtc.project.banhangnhanh.sale.model.OrderModel;
import qtc.project.banhangnhanh.sale.view.fragment.order.detail.FragmentOrderDetailSaleView;
import qtc.project.banhangnhanh.sale.view.fragment.order.detail.FragmentOrderDetailSaleViewCallback;
import qtc.project.banhangnhanh.sale.view.fragment.order.detail.FragmentOrderDetailSaleViewInterface;

public class FragmentOrderDetailSale  extends BaseFragment<FragmentOrderDetailSaleViewInterface, BaseParameters> implements FragmentOrderDetailSaleViewCallback {
    SaleHomeActivity activity;

    public static  FragmentOrderDetailSale newInstance(OrderModel model){
        FragmentOrderDetailSale fm = new FragmentOrderDetailSale();
        Bundle bundle = new Bundle();
        bundle.putSerializable("model",model);
        fm.setArguments(bundle);

        return fm;
    }
    @Override
    protected void initialize() {
        activity =(SaleHomeActivity)getActivity();
        view.init(activity,this);

        getBundle();
    }

    private void getBundle() {
        Bundle bundle = getArguments();
        if (bundle!=null){
            OrderModel model = (OrderModel)bundle.get("model");
            view.initView(model);
        }
    }

    @Override
    protected FragmentOrderDetailSaleViewInterface getViewInstance() {
        return new FragmentOrderDetailSaleView();
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
    public void cancelOrder(String id_order) {
        showProgress();
        CancelOrderRequest.ApiParams params = new CancelOrderRequest.ApiParams();
        params.id_business = AppProvider.getPreferences().getUserModel().getId_business();
        if (id_order != null) {
            params.id_order = id_order;
        }
        AppProvider.getApiManagement().call(CancelOrderRequest.class, params, new ApiRequest.ApiCallback<BaseResponseModel>() {
            @Override
            public void onSuccess(BaseResponseModel body) {
                if (body.getSuccess().equalsIgnoreCase("true")){
                    dismissProgress();
                    view.showSuccess();
                }else if (body.getSuccess().equalsIgnoreCase("false")){
                    Toast.makeText(activity, ""+body.getMessage(), Toast.LENGTH_SHORT).show();
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
    public void reQuestData() {
    }
}
