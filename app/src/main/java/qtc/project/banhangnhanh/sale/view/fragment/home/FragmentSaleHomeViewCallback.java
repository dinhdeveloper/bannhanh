package qtc.project.banhangnhanh.sale.view.fragment.home;

import java.util.ArrayList;

import qtc.project.banhangnhanh.sale.model.ListOrderModel;
import qtc.project.banhangnhanh.sale.model.OrderModel;

public interface FragmentSaleHomeViewCallback {
    void goToChooseCustomer();

    void callDataSearchProduct(String id_code);

    void tinhTien(ArrayList<ListOrderModel> arList, String id_customer, String tongtien, String discount,long tiengiamtructiep);

    void goToProductList(String toString);

    void callNav();

    void connectBlutooth();

    void inBill(ArrayList<ListOrderModel> arList, String tongtien, long tienkhachdua, long tienthoilai, ArrayList<OrderModel> listHoanTat, String customer_id_code, long tiengiamtructiep);
}
