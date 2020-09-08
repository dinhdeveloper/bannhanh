package qtc.project.banhangnhanh.sale.fragment.order;

import android.text.TextUtils;
import android.util.Log;

import b.laixuantam.myaarlibrary.api.ApiRequest;
import b.laixuantam.myaarlibrary.api.ErrorApiResponse;
import b.laixuantam.myaarlibrary.base.BaseFragment;
import b.laixuantam.myaarlibrary.base.BaseParameters;
import b.laixuantam.myaarlibrary.helper.KeyboardUtils;
import qtc.project.banhangnhanh.activity.SaleHomeActivity;
import qtc.project.banhangnhanh.admin.dependency.AppProvider;
import qtc.project.banhangnhanh.admin.model.BaseResponseModel;
import qtc.project.banhangnhanh.sale.api.OrderRequest;
import qtc.project.banhangnhanh.sale.event.BackFilterEvent;
import qtc.project.banhangnhanh.sale.event.BackShowRootViewEvent;
import qtc.project.banhangnhanh.sale.model.OrderModel;
import qtc.project.banhangnhanh.sale.view.fragment.order.filter.FragmentFilterSaleOrderView;
import qtc.project.banhangnhanh.sale.view.fragment.order.filter.FragmentFilterSaleOrderViewCallback;
import qtc.project.banhangnhanh.sale.view.fragment.order.filter.FragmentFilterSaleOrderViewInterface;

public class FragmentFilterSaleOrder extends BaseFragment<FragmentFilterSaleOrderViewInterface, BaseParameters> implements FragmentFilterSaleOrderViewCallback {
    SaleHomeActivity activity;

    @Override
    protected void initialize() {
        activity = (SaleHomeActivity) getActivity();
        view.init(activity, this);
        KeyboardUtils.setupUI(getView(), activity);
    }

    @Override
    protected FragmentFilterSaleOrderViewInterface getViewInstance() {
        return new FragmentFilterSaleOrderView();
    }

    @Override
    protected BaseParameters getParametersContainer() {
        return null;
    }

    @Override
    public void goBackHeader() {
        if (activity != null) {
            activity.checkBack();
            BackShowRootViewEvent.post();
        }
    }

    @Override
    public void filterData(String dates, String times, String id_order, String maKH) {
        if (activity != null) {
            activity.checkBack();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    activity.setDataFilterOrder(dates, times, id_order, maKH);
                    BackFilterEvent.post();
                    ;
                }
            }, 200);
        }
    }
}
