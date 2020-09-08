package qtc.project.banhangnhanh.admin.views.fragment.list_base;

import qtc.project.banhangnhanh.admin.dialog.option.OptionModel;

public interface FragmentAdminManagerListBaseViewCallback {
    void onClickBackHeader();

    void refreshLoadingList();

    void onRequestLoadMoreList();

    void onRequestSearchWithFilter(String status, String key);

    void onItemListSelected(OptionModel item);

    void onClickAddItem();

    void onDeleteItemSelected(OptionModel item);
}
