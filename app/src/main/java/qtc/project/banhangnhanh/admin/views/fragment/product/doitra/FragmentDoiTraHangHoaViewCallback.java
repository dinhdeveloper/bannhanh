package qtc.project.banhangnhanh.admin.views.fragment.product.doitra;

import qtc.project.banhangnhanh.admin.model.PackageReturnModel;

public interface FragmentDoiTraHangHoaViewCallback {
    void onBackProgress();

    void getDataDoiTraHangHoa();

    void sentDataToDetailDTHH(PackageReturnModel model);

    void getAllData();

    void searchData(String search);
}
