package qtc.project.banhangnhanh.admin.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TonKho_Vs_DoanhThuModel extends BaseResponseModel {


    private IncomeModel[] income;
    private StockModel[] stock;
    private String id_business;

    public String getId_business() {
        return id_business;
    }

    public void setId_business(String id_business) {
        this.id_business = id_business;
    }

    public IncomeModel[] getIncome() {
        return income;
    }

    public void setIncome(IncomeModel[] income) {
        this.income = income;
    }

    public StockModel[] getStock() {
        return stock;
    }

    public void setStock(StockModel[] stock) {
        this.stock = stock;
    }

    public List<StockModel> getListStockModel() {
        if (stock == null) {
            return null;
        }
        else {
            List<StockModel> list = new ArrayList<>();
            list.addAll(Arrays.asList(stock));
            return list;
        }
    }

    public List<IncomeModel> getListIncomeModel() {
        if (income == null) {
            return null;
        }
        else {
            List<IncomeModel> list = new ArrayList<>();
            list.addAll(Arrays.asList(income));
            return list;
        }
    }

}
