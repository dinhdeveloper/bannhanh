package qtc.project.banhangnhanh.admin.views.fragment.product.quanlylohang;

import java.util.ArrayList;

import b.laixuantam.myaarlibrary.base.BaseViewInterface;
import qtc.project.banhangnhanh.activity.HomeActivity;
import qtc.project.banhangnhanh.admin.model.ProductListModel;

public interface FragmentQuanLyLoHangViewInterface extends BaseViewInterface {
    void init(HomeActivity activity,FragmentQuanLyLoHangViewCallback callback);

    void mappingRecyclerView(ArrayList<ProductListModel> list);
}
