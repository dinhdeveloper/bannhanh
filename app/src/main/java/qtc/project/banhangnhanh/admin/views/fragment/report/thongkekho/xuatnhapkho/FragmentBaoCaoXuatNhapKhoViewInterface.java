package qtc.project.banhangnhanh.admin.views.fragment.report.thongkekho.xuatnhapkho;

import java.util.ArrayList;

import b.laixuantam.myaarlibrary.base.BaseViewInterface;
import qtc.project.banhangnhanh.activity.HomeActivity;
import qtc.project.banhangnhanh.admin.model.ReportXuatNhapKhoModel;

public interface FragmentBaoCaoXuatNhapKhoViewInterface extends BaseViewInterface {
    void init(HomeActivity activity,FragmentBaoCaoXuatNhapKhoViewCallback callback);

    void sendDataToView(ArrayList<ReportXuatNhapKhoModel> list);
}
