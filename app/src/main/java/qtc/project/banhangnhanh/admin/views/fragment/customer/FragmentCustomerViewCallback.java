package qtc.project.banhangnhanh.admin.views.fragment.customer;

import qtc.project.banhangnhanh.admin.model.CustomerModel;

public interface FragmentCustomerViewCallback {
    void onBackprogress();

    void getDataCustomer();

    void getCustomerDetail(CustomerModel model);

    void getHistoryOrderCustomer(CustomerModel model);

    void createCustomer();

    void callDataSearchCus(String name);
}
