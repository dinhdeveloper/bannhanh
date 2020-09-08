package qtc.project.banhangnhanh.sale.view.fragment.customer;

import qtc.project.banhangnhanh.admin.model.CustomerModel;

public interface FragmentCustomerSaleViewCallback {
    void goHome();

    void goDetail(CustomerModel model);

    void addCustomer();

    void callAllDataCustomer();

    void callDataSearchCus(String toString);

    void callNav();

    void loadMore();
}
