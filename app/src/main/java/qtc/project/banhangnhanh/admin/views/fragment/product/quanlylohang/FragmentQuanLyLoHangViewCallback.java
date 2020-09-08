package qtc.project.banhangnhanh.admin.views.fragment.product.quanlylohang;

import qtc.project.banhangnhanh.admin.model.PackageInfoModel;

public interface FragmentQuanLyLoHangViewCallback {
    void onBackprogress();
    void callDataSearch(String toString);

    void sentDataToDetail(PackageInfoModel model, String  name_product, String id_product);
}
