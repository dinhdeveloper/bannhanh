package qtc.project.banhangnhanh.sale.adapter.level;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import b.laixuantam.myaarlibrary.helper.AccentRemove;
import b.laixuantam.myaarlibrary.widgets.superadapter.SuperAdapter;
import b.laixuantam.myaarlibrary.widgets.superadapter.SuperViewHolder;
import qtc.project.banhangnhanh.R;
import qtc.project.banhangnhanh.admin.model.CustomerModel;

public class ListCustomerLevelApdater extends SuperAdapter<CustomerModel> {

    List<CustomerModel> lists;
    public ListCustomerLevelApdater(Context context, List<CustomerModel> items) {
        super(context, items, R.layout.custom_item_customer_level_sale);
        this.lists = items;
        filter = new ListCustomerLevelFilter();
    }

    @Override
    public void onBind(SuperViewHolder holder, int viewType, int layoutPosition, CustomerModel item) {
        TextView nameCustomer = holder.findViewById(R.id.nameCustomer);
        TextView addressCustomer = holder.findViewById(R.id.addressCustomer);
        TextView phoneCustomer = holder.findViewById(R.id.phoneCustomer);

        nameCustomer.setText(item.getFull_name());
        addressCustomer.setText(item.getAddress());
        phoneCustomer.setText(item.getPhone_number());

    }
    private String filterString;
    private ArrayList<CustomerModel> listData = new ArrayList<>();
    private ArrayList<CustomerModel> listDataBackup = new ArrayList<>();
    private ListCustomerLevelFilter filter;

    public ListCustomerLevelFilter getFilter() {
        return filter;
    }

    public ArrayList<CustomerModel> getListData() {
        return listData;
    }

    public ArrayList<CustomerModel> getListDataBackup() {
        return listDataBackup;
    }

    public class ListCustomerLevelFilter extends Filter {
        public ListCustomerLevelFilter() {
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (!TextUtils.isEmpty(constraint)) {
                filterString = constraint.toString().toLowerCase();
                FilterResults results = new FilterResults();
                if (listData != null && listData.size() > 0) {
                    int count = listData.size();
                    List<CustomerModel> tempItems = new ArrayList<CustomerModel>();

                    // search exactly
                    for (int i = 0; i < count; i++) {
                        String name = listData.get(i).getFull_name().toLowerCase();
                        if (name.contains(filterString)) {
                            tempItems.add(listData.get(i));
                        }
                    }
                    // search for no accent if no exactly result
                    filterString = AccentRemove.removeAccent(filterString);
                    if (tempItems.size() == 0) {
                        for (int i = 0; i < count; i++) {
                            String name = AccentRemove.removeAccent(listData.get(i).getFull_name().toLowerCase());
                            if (name.contains(filterString)) {
                                tempItems.add(listData.get(i));
                            }
                        }
                    }
                    results.values = tempItems;
                    results.count = tempItems.size();
                    return results;
                } else {
                    return null;
                }
            } else {
                filterString = "";
                return null;
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            listData.clear();
            if (results != null) {
                List<CustomerModel> listProductResult = (List<CustomerModel>) results.values;
                if (listProductResult != null && listProductResult.size() > 0) {
                    listData.addAll(listProductResult);
                }
            } else {
                listData.addAll(listDataBackup);
            }

            replaceAll(listData);
        }
    }

    private void replaceAll(ArrayList<CustomerModel> listData) {
        lists.clear();
        lists.addAll(listData);
        notifyDataSetChanged();
    }
}