package qtc.project.banhangnhanh.admin.views.fragment.report.thongkebanhang.sanpham_banchay;

import java.util.ArrayList;

import b.laixuantam.myaarlibrary.base.BaseViewInterface;
import qtc.project.banhangnhanh.activity.HomeActivity;
import qtc.project.banhangnhanh.admin.model.TopProductModel;

public interface FragmentSanPhamBanChayViewInterface extends BaseViewInterface {

    void init(HomeActivity activity,FragmentSanPhamBanChayViewCallback callback);

    void mappingRecyclerView(ArrayList<TopProductModel> list);
}
