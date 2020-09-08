package qtc.project.banhangnhanh.sale.database;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class ProductModelRoom {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "customer_id")
    private String customer_id;

    @ColumnInfo(name = "order_id_code")
    private String order_id_code;

    @ColumnInfo(name = "nameProduct")
    private String nameProduct;

    @ColumnInfo(name = "quantityProduct")
    private String quantityProduct;

    @ColumnInfo(name = "priceProduct")
    private String priceProduct;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getOrder_id_code() {
        return order_id_code;
    }

    public void setOrder_id_code(String order_id_code) {
        this.order_id_code = order_id_code;
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
}
