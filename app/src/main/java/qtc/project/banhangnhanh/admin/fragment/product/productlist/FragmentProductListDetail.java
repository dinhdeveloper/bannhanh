package qtc.project.banhangnhanh.admin.fragment.product.productlist;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import b.laixuantam.myaarlibrary.api.ApiRequest;
import b.laixuantam.myaarlibrary.api.ErrorApiResponse;
import b.laixuantam.myaarlibrary.base.BaseFragment;
import b.laixuantam.myaarlibrary.base.BaseParameters;
import b.laixuantam.myaarlibrary.widgets.dialog.alert.KAlertDialog;
import id.zelory.compressor.Compressor;
import qtc.project.banhangnhanh.R;
import qtc.project.banhangnhanh.activity.HomeActivity;
import qtc.project.banhangnhanh.activity.Qr_BarcodeActivity;
import qtc.project.banhangnhanh.admin.api.product.productcategory.ProductCategoryRequest;
import qtc.project.banhangnhanh.admin.api.product.productlist.ProductListRequest;
import qtc.project.banhangnhanh.admin.dependency.AppProvider;
import qtc.project.banhangnhanh.admin.model.BaseResponseModel;
import qtc.project.banhangnhanh.admin.model.ProductCategoryModel;
import qtc.project.banhangnhanh.admin.model.ProductListModel;
import qtc.project.banhangnhanh.admin.views.fragment.product.productlist.detail.FragmentProductListDetailView;
import qtc.project.banhangnhanh.admin.views.fragment.product.productlist.detail.FragmentProductListDetailViewCallback;
import qtc.project.banhangnhanh.admin.views.fragment.product.productlist.detail.FragmentProductListDetailViewInterface;

public class FragmentProductListDetail extends BaseFragment<FragmentProductListDetailViewInterface, BaseParameters> implements FragmentProductListDetailViewCallback {


    public static FragmentProductListDetail newIntance(ProductListModel item) {
        FragmentProductListDetail frag = new FragmentProductListDetail();
        Bundle bundle = new Bundle();
        bundle.putSerializable("model", item);
        frag.setArguments(bundle);
        return frag;
    }

    HomeActivity activity;

    @Override
    protected void initialize() {
        activity = (HomeActivity) getActivity();
        view.init(activity, this);
        callDataToBundle();
    }

    private void callDataToBundle() {
        Bundle extras = getArguments();
        if (extras != null) {
            ProductListModel model = (ProductListModel) extras.get("model");
            view.sendDataToView(model);
        }
    }

    public void setImageSelected(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            showAlert(getString(R.string.error_load_file_image), KAlertDialog.ERROR_TYPE);
            return;
        }

        reduceImageSize(filePath);
    }

    private void reduceImageSize(String filePath) {

        File fileImage = new File(filePath);

        if (fileImage.exists()) {

            try {
                File compressedImageFile = new Compressor(getContext()).compressToFile(fileImage);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (compressedImageFile.exists()) {
                            view.setDataProductImage(compressedImageFile.getAbsolutePath());
                        } else {
                            view.setDataProductImage(filePath);
                        }
                    }
                }, 300);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void gotoQr_BarcodeActivity(){
        startActivity(new Intent(activity, Qr_BarcodeActivity.class));
    }

    @Override
    public void getAllProductCategory() {
        showProgress();
        ProductCategoryRequest.ApiParams params = new ProductCategoryRequest.ApiParams();
        params.id_business = AppProvider.getPreferences().getUserModel().getId_business();
        params.type_manager = "list_category";

        AppProvider.getApiManagement().call(ProductCategoryRequest.class, params, new ApiRequest.ApiCallback<BaseResponseModel<ProductCategoryModel>>() {
            @Override
            public void onSuccess(BaseResponseModel<ProductCategoryModel> body) {
                dismissProgress();
                if (body != null) {
                    ArrayList<ProductCategoryModel> list = new ArrayList<>();
                    list.addAll(Arrays.asList(body.getData()));
                    view.initViewPopup(list);
                }
            }

            @Override
            public void onError(ErrorApiResponse error) {
                dismissProgress();
                Log.e("", error.message);
            }

            @Override
            public void onFail(ApiRequest.RequestError error) {
                dismissProgress();
                Log.e("", error.name());
            }
        });
    }

    @Override
    public void undateData(ProductListModel listModel) {
        showProgress();
        if (listModel != null) {
            ProductListRequest.ApiParams params = new ProductListRequest.ApiParams();
            params.id_business = AppProvider.getPreferences().getUserModel().getId_business();
            params.type_manager = "update_product";
            params.id_product = listModel.getId();
            params.id_code = listModel.getId_code();
            params.name = listModel.getName();
            params.description = listModel.getDescription();
            params.barcode = listModel.getBarcode();
            params.category_id = listModel.getCategory_id();
            params.quantity_safetystock = listModel.getQuantity_safetystock();
            params.qr_code = listModel.getQr_code();
            params.image = listModel.getImage();
            params.price_sell = listModel.getPrice_sell();

            AppProvider.getApiManagement().call(ProductListRequest.class, params, new ApiRequest.ApiCallback<BaseResponseModel<ProductListModel>>() {
                @Override
                public void onSuccess(BaseResponseModel<ProductListModel> body) {
                    dismissProgress();
                    if (body.getSuccess().equals("true")) {
                        view.showConfirm();
                    } else if (body.getSuccess().equals("false")) {
                        Toast.makeText(activity, "" + body.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onError(ErrorApiResponse error) {
                    dismissProgress();
                    Log.e("", error.message);
                }

                @Override
                public void onFail(ApiRequest.RequestError error) {
                    dismissProgress();
                    Log.e("", error.name());
                }
            });
        }
    }

    @Override
    public void deleteProduct(ProductListModel model) {
        showProgress();
        if (model!=null){
            ProductListRequest.ApiParams params = new ProductListRequest.ApiParams();
            params.id_business = AppProvider.getPreferences().getUserModel().getId_business();
            params.type_manager = "delete_product";
            params.id_product = model.getId();

            AppProvider.getApiManagement().call(ProductListRequest.class, params, new ApiRequest.ApiCallback<BaseResponseModel<ProductListModel>>() {
                @Override
                public void onSuccess(BaseResponseModel<ProductListModel> body) {
                    dismissProgress();
                    if (body.getSuccess().equals("true")) {
                        view.showConfirmDelete();
                    } else if (body.getSuccess().equals("false")) {
                        Toast.makeText(activity, "" + body.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onError(ErrorApiResponse error) {
                    dismissProgress();
                    Log.e("", error.message);
                }

                @Override
                public void onFail(ApiRequest.RequestError error) {
                    dismissProgress();
                    Log.e("", error.name());
                }
            });
        }
    }

    @Override
    public void onClickOptionSelectImageFromCamera() {
        if (activity!=null)
            activity.captureImageFromCamera();
    }

    @Override
    public void onClickOptionSelectImageFromGallery() {
        if (activity!=null)
            activity.changeToActivitySelectImage();
    }

    @Override
    protected FragmentProductListDetailViewInterface getViewInstance() {
        return new FragmentProductListDetailView();
    }

    @Override
    protected BaseParameters getParametersContainer() {
        return null;
    }

    @Override
    public void onBackprogress() {
        if (activity != null)
            {
                activity.deleteTempMedia();
                activity.checkBack();
            }
    }

}
