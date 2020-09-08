package qtc.project.banhangnhanh.sale.view.fragment.level.detail;

import java.util.ArrayList;

import b.laixuantam.myaarlibrary.base.BaseViewInterface;
import qtc.project.banhangnhanh.activity.SaleHomeActivity;
import qtc.project.banhangnhanh.admin.model.CustomerModel;
import qtc.project.banhangnhanh.admin.model.LevelCustomerModel;

public interface FragmentLevelSaleDetailViewInterface extends BaseViewInterface {
    void init(SaleHomeActivity activity,FragmentLevelSaleDetailViewCallback callback);

    void initLayout(LevelCustomerModel model);

    void showPopupListCustomer(ArrayList<CustomerModel> list);
}
