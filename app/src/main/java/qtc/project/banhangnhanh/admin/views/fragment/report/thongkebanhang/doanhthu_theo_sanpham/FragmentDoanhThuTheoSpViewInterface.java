package qtc.project.banhangnhanh.admin.views.fragment.report.thongkebanhang.doanhthu_theo_sanpham;

import java.util.ArrayList;
import java.util.List;

import b.laixuantam.myaarlibrary.base.BaseViewInterface;
import qtc.project.banhangnhanh.activity.HomeActivity;
import qtc.project.banhangnhanh.admin.model.ProductListModel;
import qtc.project.banhangnhanh.admin.model.TongDoanhThuModel;

public interface FragmentDoanhThuTheoSpViewInterface extends BaseViewInterface {

    void init(HomeActivity activity,FragmentDoanhThuTheoSpViewCallback callback);

    void mappingListRecyclerview(ArrayList<ProductListModel> list);

    void mappingDateToView(String nam, String thang, int ngay);

    void sentYearToView(String nam);

    void sentDataTongDoanhThu(List<TongDoanhThuModel> models);

    void mappingYear(ArrayList<TongDoanhThuModel> list);
}
