package qtc.project.banhangnhanh.sale.adapter.customer;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import b.laixuantam.myaarlibrary.widgets.superadapter.SuperAdapter;
import b.laixuantam.myaarlibrary.widgets.superadapter.SuperViewHolder;
import qtc.project.banhangnhanh.R;
import qtc.project.banhangnhanh.admin.dialog.option.OptionModel;
import qtc.project.banhangnhanh.admin.model.CustomerModel;

public class CustomerSaleAdapter extends SuperAdapter<OptionModel> {

    private CustomerAdapterListener listener;

    public CustomerSaleAdapter(Context context, List<OptionModel> items) {
        super(context, items, R.layout.custom_item_customer_profile_sale);
    }

    public interface CustomerAdapterListener {
        void onItemClick(OptionModel model);
    }

    public void setListener(CustomerSaleAdapter.CustomerAdapterListener listener) {
        this.listener = listener;
    }

    @Override
    public void onBind(SuperViewHolder holder, int viewType, int layoutPosition, OptionModel model) {
        TextView nameCustomer = holder.findViewById(R.id.nameCustomer);
        TextView phoneCustomer = holder.findViewById(R.id.phoneCustomer);
        TextView addressCustomer = holder.findViewById(R.id.addressCustomer);
        LinearLayout btn_detail_Customer = holder.findViewById(R.id.btn_detail_Customer);
        LinearLayout layout_customer = holder.findViewById(R.id.layout_customer);

        CustomerModel item = (CustomerModel) model.getDtaCustom();

        if (TextUtils.isEmpty(item.getFull_name())) {
            nameCustomer.setText("Không xác định");
        } else {
            nameCustomer.setText(item.getFull_name());
        }
        if (TextUtils.isEmpty(item.getPhone_number())) {
            phoneCustomer.setText("Không có số điện thoại");
        } else {
            phoneCustomer.setText(item.getPhone_number());
        }
        if (TextUtils.isEmpty(item.getAddress())) {
            addressCustomer.setText("Không có địa chỉ");
        } else {
            addressCustomer.setText(item.getAddress());
        }
        layout_customer.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(model);
            }
        });
    }
}