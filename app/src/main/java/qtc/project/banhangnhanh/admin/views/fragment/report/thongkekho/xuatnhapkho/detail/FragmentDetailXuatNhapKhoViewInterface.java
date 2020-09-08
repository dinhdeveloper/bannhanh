package qtc.project.banhangnhanh.admin.views.fragment.report.thongkekho.xuatnhapkho.detail;

import java.util.ArrayList;

import b.laixuantam.myaarlibrary.base.BaseViewInterface;
import qtc.project.banhangnhanh.activity.HomeActivity;
import qtc.project.banhangnhanh.admin.model.ProductListModel;

public interface FragmentDetailXuatNhapKhoViewInterface extends BaseViewInterface {

    void init(HomeActivity activity,FragmentDetailXuatNhapKhoViewCallback callback);

    void sendDataToViewDetail(ArrayList<ProductListModel> listModels);
}
