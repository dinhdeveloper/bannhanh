package qtc.project.banhangnhanh.sale.adapter.level;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import b.laixuantam.myaarlibrary.widgets.superadapter.SuperAdapter;
import b.laixuantam.myaarlibrary.widgets.superadapter.SuperViewHolder;
import qtc.project.banhangnhanh.R;
import qtc.project.banhangnhanh.admin.model.CustomerModel;

public class CustomerLevelAdapter extends SuperAdapter<CustomerModel> {

    private CustomerLevelAdapterListener listener;

    public CustomerLevelAdapter(Context context, List<CustomerModel> items) {
        super(context, items, R.layout.custom_item_customer_level_detail);
    }
    public interface CustomerLevelAdapterListener{
        void onItemClick(CustomerModel model);
    }

    public void setListener(CustomerLevelAdapterListener listener) {
        this.listener = listener;
    }

    @Override
    public void onBind(SuperViewHolder holder, int viewType, int layoutPosition, CustomerModel item) {
        TextView nameCustomer = holder.findViewById(R.id.nameCustomer);
        TextView phoneCustomer = holder.findViewById(R.id.phoneCustomer);
        TextView addressCustomer = holder.findViewById(R.id.addressCustomer);
        LinearLayout btn_detail_Customer = holder.findViewById(R.id.btn_detail_Customer);
        LinearLayout layout_customer = holder.findViewById(R.id.layout_customer);


        nameCustomer.setText(item.getFull_name());
        phoneCustomer.setText(item.getPhone_number());
        addressCustomer.setText(item.getAddress());

        layout_customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener !=null){
                    listener.onItemClick(item);
                }
            }
        });
    }
}