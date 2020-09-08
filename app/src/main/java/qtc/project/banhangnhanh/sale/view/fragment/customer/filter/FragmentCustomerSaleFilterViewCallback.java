package qtc.project.banhangnhanh.sale.view.fragment.customer.filter;

import qtc.project.banhangnhanh.admin.model.CustomerModel;

public interface FragmentCustomerSaleFilterViewCallback {
    void setCustomerToHome(CustomerModel model);

    void onBackP();

    void getAllData();

    void searchCustomer(String search);
}
