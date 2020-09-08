package qtc.project.banhangnhanh.admin.views.fragment.report.thongkebanhang.tomtatdoanhthu.thongke;

import java.util.ArrayList;

import b.laixuantam.myaarlibrary.base.BaseViewInterface;
import qtc.project.banhangnhanh.activity.HomeActivity;
import qtc.project.banhangnhanh.admin.model.TongDoanhThuModel;

public interface FragmentThongKeViewInterface extends BaseViewInterface {

    void init(HomeActivity activity,FragmentThongKeViewCallback callback);

    void mappingYear(ArrayList<TongDoanhThuModel> list);

    void sentYearToView(String nam);
}
