package qtc.project.banhangnhanh.sale.view.fragment.list_base;

import b.laixuantam.myaarlibrary.base.BaseViewInterface;
import qtc.project.banhangnhanh.activity.SaleHomeActivity;
import qtc.project.banhangnhanh.admin.model.BaseResponseModel;

public interface FragmentListBaseViewInterface extends BaseViewInterface {

    void init(SaleHomeActivity activity, FragmentListBaseViewCallback callback);

    void showEmptyList();

    void hideEmptyList();

    void setDataList(BaseResponseModel dataList);

    void setNoMoreLoading();

    void resetListData();

    void hideRootView();

    void showRootView();

    void clearListData();
}
