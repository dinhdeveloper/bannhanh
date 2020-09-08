package qtc.project.banhangnhanh.admin.fragment.report.thongkekho.xuatnhapkho.detail;

import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

import b.laixuantam.myaarlibrary.api.ApiRequest;
import b.laixuantam.myaarlibrary.api.ErrorApiResponse;
import b.laixuantam.myaarlibrary.base.BaseFragment;
import b.laixuantam.myaarlibrary.base.BaseParameters;
import qtc.project.banhangnhanh.activity.HomeActivity;
import qtc.project.banhangnhanh.admin.api.report.xuatnhapkho.BaoCaoXNKhoDetailRequest;
import qtc.project.banhangnhanh.admin.dependency.AppProvider;
import qtc.project.banhangnhanh.admin.model.BaseResponseModel;
import qtc.project.banhangnhanh.admin.model.ProductListModel;
import qtc.project.banhangnhanh.admin.model.ReportXuatNhapKhoModel;
import qtc.project.banhangnhanh.admin.views.fragment.report.thongkekho.xuatnhapkho.detail.FragmentDetailXuatNhapKhoView;
import qtc.project.banhangnhanh.admin.views.fragment.report.thongkekho.xuatnhapkho.detail.FragmentDetailXuatNhapKhoViewCallback;
import qtc.project.banhangnhanh.admin.views.fragment.report.thongkekho.xuatnhapkho.detail.FragmentDetailXuatNhapKhoViewInterface;

public class FragmentDetailXuatNhapKho extends BaseFragment<FragmentDetailXuatNhapKhoViewInterface, BaseParameters> implements FragmentDetailXuatNhapKhoViewCallback {

    HomeActivity activity;

    public static FragmentDetailXuatNhapKho newIntance(ReportXuatNhapKhoModel item) {
        FragmentDetailXuatNhapKho frag = new FragmentDetailXuatNhapKho();
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
            showProgress();
            ReportXuatNhapKhoModel model = (ReportXuatNhapKhoModel) extras.get("model");

            BaoCaoXNKhoDetailRequest.ApiParams params = new BaoCaoXNKhoDetailRequest.ApiParams();
            params.id_business = AppProvider.getPreferences().getUserModel().getId_business();
            params.type_manager = "stock_manager";
            params.product = model.getProduct_name();

            AppProvider.getApiManagement().call(BaoCaoXNKhoDetailRequest.class, params, new ApiRequest.ApiCallback<BaseResponseModel<ProductListModel>>() {
                @Override
                public void onSuccess(BaseResponseModel<ProductListModel> body) {
                    if (body!=null){
                        dismissProgress();
                        ArrayList<ProductListModel> listModels = new ArrayList<>();
                        listModels.addAll(Arrays.asList(body.getData()));

                        view.sendDataToViewDetail(listModels);
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
    protected FragmentDetailXuatNhapKhoViewInterface getViewInstance() {
        return new FragmentDetailXuatNhapKhoView();
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
}
