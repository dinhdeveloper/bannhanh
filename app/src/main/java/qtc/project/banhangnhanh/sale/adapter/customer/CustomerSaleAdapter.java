package qtc.project.banhangnhanh.sale.adapter.customer;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import b.laixuantam.myaarlibrary.widgets.superadapter.SuperAdapter;
import b.laixuantam.myaarlibrary.widgets.superadapter.SuperViewHolder;
import qtc.project.banhangnhanh.R;
import qtc.project.banhangnhanh.admin.model.CustomerModel;

public class CustomerSaleAdapter extends SuperAdapter<CustomerModel> {

    private CustomerAdapterListener listener;

    public CustomerSaleAdapter(Context context, List<CustomerModel> items) {
        super(context, items, R.layout.custom_item_customer_profile_sale);
    }
    public interface CustomerAdapterListener{
        void onItemClick(CustomerModel model);
    }

    public void setListener(CustomerSaleAdapter.CustomerAdapterListener listener) {
        this.listener = listener;
    }

    @Override
    public void onBind(SuperViewHolder holder, int viewType, int layoutPosition, CustomerModel item) {
        TextView nameCustomer = holder.findViewById(R.id.nameCustomer);
        TextView phoneCustomer = holder.findViewById(R.id.phoneCustomer);
        TextView addressCustomer = holder.findViewById(R.id.addressCustomer);
        LinearLayout btn_detail_Customer = holder.findViewById(R.id.btn_detail_Customer);
        LinearLayout layout_customer = holder.findViewById(R.id.layout_customer);


        if (item.getFull_name()==null|| item.getFull_name().isEmpty()){
            nameCustomer.setText("Khách vãng lai");
        }
        else {
            nameCustomer.setText(item.getFull_name());
        }
        if (item.getPhone_number()==null|| item.getPhone_number().isEmpty()){
            phoneCustomer.setText("Không có số điện thoại");
        }else {
            phoneCustomer.setText(item.getPhone_number());
        }
        if (item.getAddress()==null|| item.getAddress().isEmpty()){
            addressCustomer.setText("Không có địa chỉ");
        }else {
            addressCustomer.setText(item.getAddress());
        }
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