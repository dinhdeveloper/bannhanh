package qtc.project.banhangnhanh.admin.fragment.product.quanlylohang;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import b.laixuantam.myaarlibrary.api.ApiRequest;
import b.laixuantam.myaarlibrary.api.ErrorApiResponse;
import b.laixuantam.myaarlibrary.base.BaseFragment;
import b.laixuantam.myaarlibrary.base.BaseParameters;
import qtc.project.banhangnhanh.activity.HomeActivity;
import qtc.project.banhangnhanh.admin.fragment.supplier.FragmentSupplier;
import qtc.project.banhangnhanh.admin.api.product.packageproduct.PackageProductRequest;
import qtc.project.banhangnhanh.admin.dependency.AppProvider;
import qtc.project.banhangnhanh.admin.model.BaseResponseModel;
import qtc.project.banhangnhanh.admin.model.PackageInfoModel;
import qtc.project.banhangnhanh.admin.model.ProductListModel;
import qtc.project.banhangnhanh.admin.model.SupplierModel;
import qtc.project.banhangnhanh.admin.views.fragment.product.quanlylohang.detail.FragmentChiTietLoHangView;
import qtc.project.banhangnhanh.admin.views.fragment.product.quanlylohang.detail.FragmentChiTietLoHangViewCallback;
import qtc.project.banhangnhanh.admin.views.fragment.product.quanlylohang.detail.FragmentChiTietLoHangViewInterface;

public class FragmentChiTietLoHang extends BaseFragment<FragmentChiTietLoHangViewInterface, BaseParameters> implements FragmentChiTietLoHangViewCallback {

    HomeActivity activity;

    public static FragmentChiTietLoHang newIntance(PackageInfoModel infoModel, String name_product, String id_product) {
        FragmentChiTietLoHang frag = new FragmentChiTietLoHang();
        Bundle bundle = new Bundle();
        bundle.putSerializable("infoModel", infoModel);
        bundle.putString("name_product", name_product);
        bundle.putString("id_product", id_product);
        frag.setArguments(bundle);
        return frag;
    }

    @Override
    protected void initialize() {
        activity = (HomeActivity) getActivity();
        view.init(activity, this);
        getDataIntance();
    }

    public void setDataNhaCungUng(SupplierModel model) {
        if (view != null) {
            view.sendDataToViewTwo(model);
        }
    }

    private void getDataIntance() {
        Bundle extras = getArguments();
        if (extras != null) {
            PackageInfoModel infoModel = (PackageInfoModel) extras.get("infoModel");
            String name_product = (String) extras.get("name_product");
            String id_product = (String) extras.get("id_product");
            view.sendDataToView(infoModel, name_product, id_product);
        }
    }

    @Override
    protected FragmentChiTietLoHangViewInterface getViewInstance() {
        return new FragmentChiTietLoHangView();
    }

    @Override
    protected BaseParameters getParametersContainer() {
        return null;
    }

    @Override
    public void onBackProgress() {
        if (activity != null)
            activity.checkBack();
    }

    @Override
    public void getAllDataNhaCungUng() {
        activity.addFragment(new FragmentSupplier(), true, null);
    }

    @Override
    public void updateDataPackage(PackageInfoModel infoModel, String id_product) {
        if (infoModel != null) {
            PackageProductRequest.ApiParams params = new PackageProductRequest.ApiParams();
            params.id_business = AppProvider.getPreferences().getUserModel().getId_business();
            params.type_manager = "update_package";
            params.product_id = id_product;
            params.id_package = infoModel.getPack_id();
            params.manufacturer_id = infoModel.getManufacturer_id();
            params.description = infoModel.getDescription();
            params.employee_id = AppProvider.getPreferences().getUserModel().getId();
            params.expiry_date = infoModel.getExpiry_date();
            params.import_date = infoModel.getImport_date();
            params.import_price = infoModel.getImport_price();
            params.quantity_order = infoModel.getQuantity_order();
            params.quantity_storage = infoModel.getQuantity_storage();

            AppProvider.getApiManagement().call(PackageProductRequest.class, params, new ApiRequest.ApiCallback<BaseResponseModel<ProductListModel>>() {
                @Override
                public void onSuccess(BaseResponseModel<ProductListModel> body) {
                    dismissProgress();
                    if (body.getSuccess().equals("true")) {
                        view.showPopSuccess();
                    } else if (body.getSuccess().equals("false")) {
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
    }

    @Override
    public void taoDonTraHang(PackageInfoModel infoModel,String name,String id) {
        activity.addFragment(new FragmentDonTraHang().newIntance(infoModel,name,id),true,null);
    }

    private void onBack() {
        if (activity!=null){
            activity.checkBack();
        }
    }
}
