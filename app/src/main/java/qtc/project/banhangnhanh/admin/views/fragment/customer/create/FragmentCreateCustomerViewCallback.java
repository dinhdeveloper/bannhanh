package qtc.project.banhangnhanh.admin.views.fragment.customer.create;

import qtc.project.banhangnhanh.admin.model.CustomerModel;

public interface FragmentCreateCustomerViewCallback {
    void onBackProgress();

    void getAllLevelCustomer();

    void createCustomer(CustomerModel customerModel);
}
