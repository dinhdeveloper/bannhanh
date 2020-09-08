package qtc.project.banhangnhanh.admin.views.fragment.customer.detail;

import qtc.project.banhangnhanh.admin.model.CustomerModel;

public interface FragmentCustomerDetailViewCallback {
    void onBackProgress();

    void getAllLevelCustomer();

    void updateCustomerDetail(CustomerModel model);

    void deleteCustomer(CustomerModel model);
}
