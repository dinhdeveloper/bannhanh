package qtc.project.banhangnhanh.sale.view.fragment.home;

import java.util.ArrayList;

import qtc.project.banhangnhanh.sale.model.ListOrderModel;

public interface FragmentSaleHomeViewCallback {
    void goToChooseCustomer();

    void callDataSearchProduct(String id_code);

    void tinhTien(ArrayList<ListOrderModel> arList, String id_customer, String tongtien, String discount);

    void goToProductList(String toString);

    void callNav();
}
