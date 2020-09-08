package qtc.project.banhangnhanh.sale.model;

import java.io.Serializable;

public class ListOrderModel implements Serializable {
    private String id;
    private String id_business;
    private String customer_id;
    private String nameProduct;
    private String quantityProduct;
    private String priceProduct;
    private String inventory;
    private String totalStore;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_business() {
        return id_business;
    }

    public void setId_business(String id_business) {
        this.id_business = id_business;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getNameProduct() {
        return nameProduct;
    }

    public void setNameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
    }

    public String getQuantityProduct() {
        return quantityProduct;
    }

    public void setQuantityProduct(String quantityProduct) {
        this.quantityProduct = quantityProduct;
    }

    public String getPriceProduct() {
        return priceProduct;
    }

    public void setPriceProduct(String priceProduct) {
        this.priceProduct = priceProduct;
    }

    public String getInventory() {
        return inventory;
    }

    public void setInventory(String inventory) {
        this.inventory = inventory;
    }

    public String getTotalStore() {
        return totalStore;
    }

    public void setTotalStore(String totalStore) {
        this.totalStore = totalStore;
    }
}
