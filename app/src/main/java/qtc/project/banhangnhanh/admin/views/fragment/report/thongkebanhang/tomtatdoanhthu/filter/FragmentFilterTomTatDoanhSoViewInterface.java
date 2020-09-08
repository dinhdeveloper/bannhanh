package qtc.project.banhangnhanh.admin.views.fragment.report.thongkebanhang.tomtatdoanhthu.filter;

import b.laixuantam.myaarlibrary.base.BaseViewInterface;
import qtc.project.banhangnhanh.activity.HomeActivity;

public interface FragmentFilterTomTatDoanhSoViewInterface extends BaseViewInterface {

    void init(HomeActivity activity,FragmentFilterTomTatDoanhSoViewCallback callback);

    void sentDataDayChoose(int date);
}
