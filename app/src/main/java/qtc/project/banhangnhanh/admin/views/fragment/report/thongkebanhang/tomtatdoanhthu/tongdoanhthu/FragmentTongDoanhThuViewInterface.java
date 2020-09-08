package qtc.project.banhangnhanh.admin.views.fragment.report.thongkebanhang.tomtatdoanhthu.tongdoanhthu;

import java.util.List;

import b.laixuantam.myaarlibrary.base.BaseViewInterface;
import qtc.project.banhangnhanh.activity.HomeActivity;
import qtc.project.banhangnhanh.admin.model.TongDoanhThuModel;

public interface FragmentTongDoanhThuViewInterface extends BaseViewInterface {

    void init(HomeActivity activity,FragmentTongDoanhThuViewCallback callback);

    void sentDataDayChoose(String name, String thang, int date);

    void sentDataTongDoanhThu(List<TongDoanhThuModel> models);
}
