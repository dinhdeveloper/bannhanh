package qtc.project.banhangnhanh.admin.fragment.product.quanlylohang;

import android.os.Bundle;
import android.widget.Toast;

import b.laixuantam.myaarlibrary.api.ApiRequest;
import b.laixuantam.myaarlibrary.api.ErrorApiResponse;
import b.laixuantam.myaarlibrary.base.BaseFragment;
import b.laixuantam.myaarlibrary.base.BaseParameters;
import qtc.project.banhangnhanh.activity.HomeActivity;
import qtc.project.banhangnhanh.admin.api.product.packageproduct.PackageReturnRequest;
import qtc.project.banhangnhanh.admin.dependency.AppProvider;
import qtc.project.banhangnhanh.admin.model.BaseResponseModel;
import qtc.project.banhangnhanh.admin.model.PackageInfoModel;
import qtc.project.banhangnhanh.admin.model.PackageReturnModel;
import qtc.project.banhangnhanh.admin.views.fragment.product.quanlylohang.trahang.FragmentDonTraHangView;
import qtc.project.banhangnhanh.admin.views.fragment.product.quanlylohang.trahang.FragmentDonTraHangViewCallback;
import qtc.project.banhangnhanh.admin.views.fragment.product.quanlylohang.trahang.FragmentDonTraHangViewInterface;

public class FragmentDonTraHang extends BaseFragment<FragmentDonTraHangViewInterface, BaseParameters> implements FragmentDonTraHangViewCallback {

    HomeActivity activity;

    public static FragmentDonTraHang newIntance(PackageInfoModel infoModel, String name_product, String id_product) {
        FragmentDonTraHang frag = new FragmentDonTraHang();
        Bundle bundle = new Bundle();
        bundle.putSerializable("infoModel", infoModel);
        bundle.putString("name_product", name_product);
        bundle.putString("id_product", id_product);
        frag.setArguments(bundle);
        return frag;
    }

    @Override
    protected void initialize() {
        activity = (HomeActivity)getActivity();
        view.init(activity,this);
        
        getBundle();
    }

    private void getBundle() {
        Bundle extras = getArguments();
        if (extras != null) {
            PackageInfoModel infoModel = (PackageInfoModel) extras.get("infoModel");
            String name_product = (String) extras.get("name_product");
            String id_product = (String) extras.get("id_product");
            view.sendDataToView(infoModel, name_product, id_product);
        }
    }

    @Override
    protected FragmentDonTraHangViewInterface getViewInstance() {
        return new FragmentDonTraHangView();
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
    public void setDataDoiTraHang(PackageReturnModel returnModel) {
        if (returnModel!=null){
            PackageReturnRequest.ApiParams params = new PackageReturnRequest.ApiParams();
            params.id_business = AppProvider.getPreferences().getUserModel().getId_business();
            params.type_manager = "create_package_return";
            params.id_code = returnModel.getProduct_return_id_code();
            params.description = returnModel.getProduct_return_description();
            params.employee_id = AppProvider.getPreferences().getUserModel().getId();
            params.manufacturer_id = returnModel.getManufacturer_id();
            params.quantity_return = returnModel.getProduct_return_quantity();
            params.return_date = returnModel.getProduct_return_return_date();
            params.product_detail_id = returnModel.getProduct_return_id();

            AppProvider.getApiManagement().call(PackageReturnRequest.class, params, new ApiRequest.ApiCallback<BaseResponseModel<PackageReturnModel>>() {
                @Override
                public void onSuccess(BaseResponseModel<PackageReturnModel> body) {
                    if (body.getSuccess().equals("true")){
                        view.showSuccess();
                    } else if (body.getSuccess().equals("false")){
                        Toast.makeText(activity, ""+body.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onError(ErrorApiResponse error) {

                }

                @Override
                public void onFail(ApiRequest.RequestError error) {

                }
            });
        }
    }

    @Override
    public void setOnBack() {
        view.setOnBack();
    }
}
