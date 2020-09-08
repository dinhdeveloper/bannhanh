package qtc.project.banhangnhanh.sale.view.fragment.level;

import java.util.ArrayList;

import b.laixuantam.myaarlibrary.base.BaseViewInterface;
import qtc.project.banhangnhanh.activity.SaleHomeActivity;
import qtc.project.banhangnhanh.admin.model.LevelCustomerModel;

public interface FragmentLevelSaleCustomerViewInterface extends BaseViewInterface {

    void init(SaleHomeActivity activity,FragmentLevelSaleCustomerViewCallback callback);

    void initRecyclerView(ArrayList<LevelCustomerModel> list);
}
