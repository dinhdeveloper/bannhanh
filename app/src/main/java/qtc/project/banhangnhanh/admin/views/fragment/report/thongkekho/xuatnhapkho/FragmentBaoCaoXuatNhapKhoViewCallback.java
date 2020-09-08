package qtc.project.banhangnhanh.admin.views.fragment.report.thongkekho.xuatnhapkho;

import qtc.project.banhangnhanh.admin.model.ReportXuatNhapKhoModel;

public interface FragmentBaoCaoXuatNhapKhoViewCallback {
    void onBackProgress();

    void filterData();

    void goToDetailXuatNhapKho(ReportXuatNhapKhoModel model);

    void getAllData();

    void goToSearch(String search);
}
