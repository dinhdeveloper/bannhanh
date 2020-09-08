package qtc.project.banhangnhanh.sale.view.fragment.customer.detail;

import qtc.project.banhangnhanh.admin.model.CustomerModel;

public interface FragmentCustomerSaleDetailViewCallback {
    void onBackP();

    void updateCustomer(CustomerModel customerModel, String id);

    void createCustomer(CustomerModel customerModel);
}
