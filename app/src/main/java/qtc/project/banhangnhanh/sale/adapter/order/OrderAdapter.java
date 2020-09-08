package qtc.project.banhangnhanh.sale.adapter.order;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import b.laixuantam.myaarlibrary.widgets.superadapter.SuperAdapter;
import b.laixuantam.myaarlibrary.widgets.superadapter.SuperViewHolder;
import qtc.project.banhangnhanh.R;
import qtc.project.banhangnhanh.sale.model.OrderModel;

public class OrderAdapter extends SuperAdapter<OrderModel> {

    OrderAdapterListener listener;

    public OrderAdapter(Context context, List<OrderModel> items) {
        super(context, items, R.layout.custom_item_order_sale);
    }

    public interface OrderAdapterListener {
        void onItemClick(OrderModel model);
    }

    public void setListener(OrderAdapter.OrderAdapterListener listener) {
        this.listener = listener;
    }

    @Override
    public void onBind(SuperViewHolder holder, int viewType, int layoutPosition, OrderModel item) {
        TextView nameCustomer = holder.findViewById(R.id.nameCustomer);
        TextView orderId = holder.findViewById(R.id.orderId);
        TextView phoneCustomer = holder.findViewById(R.id.phoneCustomer);
        TextView addressCustomer = holder.findViewById(R.id.addressCustomer);
        TextView createDay = holder.findViewById(R.id.createDay);
        TextView status = holder.findViewById(R.id.status);
        LinearLayout btnSubmit = holder.findViewById(R.id.btnSubmit);
        LinearLayout layoutItemOrder = holder.findViewById(R.id.layoutItemOrder);
        try {

            if (item != null) {
                if (item.getCustomer_fullname() == null || item.getCustomer_fullname().isEmpty()) {
                    nameCustomer.setText("Khách vãng lai");
                } else {
                    nameCustomer.setText(item.getCustomer_fullname());
                }
                if (item.getOrder_id_code() == null || item.getOrder_id_code().isEmpty()) {
                    orderId.setText("Không có mã đơn hàng");
                } else {
                    orderId.setText("Mã đơn: " + item.getOrder_id_code());
                }
                if (item.getCustomer_phone_number() == null || item.getCustomer_phone_number().isEmpty()) {
                    phoneCustomer.setText("Không có số điện thoại");
                } else {
                    phoneCustomer.setText(item.getCustomer_phone_number());
                }
                if (item.getEmployee_address() == null || item.getEmployee_address().isEmpty()) {
                    addressCustomer.setText("Không có địa chỉ");
                } else {
                    addressCustomer.setText(item.getEmployee_address());
                }
                createDay.setText(item.getOrder_created_date());
                if (item.getOrder_status().equalsIgnoreCase("N")) {
                    status.setText("Đã hủy");
                    status.setTextColor(ContextCompat.getColor(getContext(), R.color.colorYellow));
                    btnSubmit.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.custom_border_button_item_ordel));

                } else if (item.getOrder_status().equalsIgnoreCase("Y")) {
                    status.setText("Hoàn thành");
                    status.setTextColor(ContextCompat.getColor(getContext(), R.color.color_success));
                    btnSubmit.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.custom_border_button_success));
                }
                layoutItemOrder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (listener != null) {
                            listener.onItemClick(item);
                        }
                    }
                });
            }

        } catch (Exception e) {
            Log.e("Ex", e.getMessage());
        }
    }
}