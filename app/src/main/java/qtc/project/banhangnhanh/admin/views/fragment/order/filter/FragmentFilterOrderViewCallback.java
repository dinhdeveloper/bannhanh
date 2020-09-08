package qtc.project.banhangnhanh.admin.views.fragment.order.filter;

public interface FragmentFilterOrderViewCallback {
    void onBackProgress();

    void filterOnDay(String dateStartSelected, String dateEndSelected);
}
