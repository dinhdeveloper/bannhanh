package qtc.project.banhangnhanh.sale.fragment.home;

import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

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
import qtc.project.banhangnhanh.sale.api.CreateOrderRequest;
import qtc.project.banhangnhanh.sale.api.ProductRequest;
import qtc.project.banhangnhanh.sale.database.DatabaseProvider;
import qtc.project.banhangnhanh.sale.fragment.customer.FragmentCustomerSaleFilter;
import qtc.project.banhangnhanh.sale.model.ListOrderModel;
import qtc.project.banhangnhanh.sale.model.ProductModel;
import qtc.project.banhangnhanh.sale.view.fragment.home.FragmentSaleHomeView;
import qtc.project.banhangnhanh.sale.view.fragment.home.FragmentSaleHomeViewCallback;
import qtc.project.banhangnhanh.sale.view.fragment.home.FragmentSaleHomeViewInterface;

public class FragmentSaleHome extends BaseFragment<FragmentSaleHomeViewInterface, BaseParameters> implements FragmentSaleHomeViewCallback {
    SaleHomeActivity activity;

    @Override
    protected void initialize() {
        activity = (SaleHomeActivity) getActivity();
        view.init(activity, this);
    }

    @Override
    protected FragmentSaleHomeViewInterface getViewInstance() {
        return new FragmentSaleHomeView();
    }

    @Override
    protected BaseParameters getParametersContainer() {
        return null;
    }

    @Override
    public void goToChooseCustomer() {
        if (activity != null)
            activity.replaceFragment(new FragmentCustomerSaleFilter(), true);
    }

    @Override
    public void callDataSearchProduct(String search) {
        ProductRequest.ApiParams params = new ProductRequest.ApiParams();
        params.id_business = AppProvider.getPreferences().getUserModel().getId_business();
        if (search != null) {
            params.product = search;
        }
        showProgress();
        AppProvider.getApiManagement().call(ProductRequest.class, params, new ApiRequest.ApiCallback<BaseResponseModel<ProductModel>>() {
            @Override
            public void onSuccess(BaseResponseModel<ProductModel> body) {
                dismissProgress();
                if (body != null) {
                    ArrayList<ProductModel> list = new ArrayList<>();
                    list.addAll(Arrays.asList(body.getData()));
                    view.initViewProduct(list);
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
    public void tinhTien(ArrayList<ListOrderModel> arList, String id_customer, String tongtien, String discount) {
        showProgress();
        CreateOrderRequest.ApiParams params = new CreateOrderRequest.ApiParams();
        params.id_business = AppProvider.getPreferences().getUserModel().getId_business();
        StringBuilder id_product_pack = new StringBuilder();
        StringBuilder quantity_product_pack = new StringBuilder();
        StringBuilder price_product_pack = new StringBuilder();

        for (ListOrderModel orderModel : arList) {
            params.employee_id = AppProvider.getPreferences().getUserModel().getId();
            if (!TextUtils.isEmpty(id_customer)){
                params.id_customer = id_customer;
            }
            id_product_pack.append(orderModel.getId() + ",");
            price_product_pack.append(orderModel.getPriceProduct() + ",");
            quantity_product_pack.append(orderModel.getQuantityProduct() + ",");
            // int tongtien = Integer.parseInt(orderModel.getPriceProduct()) * Integer.parseInt(orderModel.getQuantityProduct());
            params.total = tongtien;
        }
        params.id_product_pack = String.valueOf(id_product_pack);
        params.price_product_pack = String.valueOf(price_product_pack);
        params.quantity_product_pack = String.valueOf(quantity_product_pack);

        AppProvider.getApiManagement().call(CreateOrderRequest.class, params, new ApiRequest.ApiCallback<BaseResponseModel>() {
            @Override
            public void onSuccess(BaseResponseModel body) {
                dismissProgress();
                if (body.getSuccess().equals("true")) {
                    DatabaseProvider provider = new DatabaseProvider(activity);
                    provider.deleteDatabase();
                    //view.showBill(list);
                    view.truyenIntent(arList);
                } else if (body.getSuccess().equals("false")) {
                    Toast.makeText(getContext(), "" + body.getMessage(), Toast.LENGTH_SHORT).show();
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
    public void goToProductList(String search) {
        if (activity!=null)
            activity.replaceFragment(new FragmentProductSaleHome().newInstance(search),true);
    }

    @Override
    public void callNav() {
        if (activity!=null)
            activity.toggleNav();
    }

    public void setDataCustomer(CustomerModel model) {
        if (model != null) {
            view.initView(model);
        }
    }

    public void setDataProduct(ProductModel model) {
        if (model!=null)
            view.initViewProductClick(model);
    }
}