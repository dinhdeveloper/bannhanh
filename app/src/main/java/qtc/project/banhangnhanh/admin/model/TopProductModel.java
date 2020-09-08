package qtc.project.banhangnhanh.admin.model;

public class TopProductModel extends BaseResponseModel {

    private String top;
    private String id_business;
    private String product_id;
    private String product_name;
    private String total_payment_item_order;
    private String total_quantity_item_order;

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getId_business() {
        return id_business;
    }

    public void setId_business(String id_business) {
        this.id_business = id_business;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getTotal_payment_item_order() {
        return total_payment_item_order;
    }

    public void setTotal_payment_item_order(String total_payment_item_order) {
        this.total_payment_item_order = total_payment_item_order;
    }

    public String getTotal_quantity_item_order() {
        return total_quantity_item_order;
    }

    public void setTotal_quantity_item_order(String total_quantity_item_order) {
        this.total_quantity_item_order = total_quantity_item_order;
    }

    public String getTop() {
        return top;
    }

    public void setTop(String top) {
        this.top = top;
    }
}
