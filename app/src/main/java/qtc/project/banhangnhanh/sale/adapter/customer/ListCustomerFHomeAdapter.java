package qtc.project.banhangnhanh.sale.adapter.customer;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.List;

import qtc.project.banhangnhanh.R;
import qtc.project.banhangnhanh.admin.model.CustomerModel;

public class ListCustomerFHomeAdapter  extends RecyclerView.Adapter<ListCustomerFHomeAdapter.ViewHolder>{

    private ListCustomerAdapterListener listener;

    Context context;
    List<CustomerModel> items;

    // if checkedPosition = -1, there is no default selection
    // if checkedPosition = 0, 1st item is selected by default
    private int checkedPosition = -1;

    public interface ListCustomerAdapterListener{
        void onItemClick(CustomerModel model);
    }

    public void setListener(ListCustomerAdapterListener listener) {
        this.listener = listener;
    }

    public ListCustomerFHomeAdapter(Context context, List<CustomerModel> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_item_list_customer_checked_home,viewGroup,false);
        return  new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.bind(items.get(i));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameCustomer;
        TextView addressCustomer ;
        TextView phoneCustomer ;
        RadioButton checked;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameCustomer = itemView.findViewById(R.id.nameCustomer);
            addressCustomer = itemView.findViewById(R.id.addressCustomer);
            phoneCustomer = itemView.findViewById(R.id.phoneCustomer);
            checked = itemView.findViewById(R.id.checked);
        }
        void bind(final CustomerModel item){
            if (checkedPosition == -1) {
                checked.setChecked(false);
            } else {
                if (checkedPosition == getAdapterPosition()) {
                    checked.setChecked(true);

                } else {
                    checked.setChecked(false);
                }
            }
            nameCustomer.setText(item.getFull_name());
            addressCustomer.setText(item.getAddress());
            phoneCustomer.setText(item.getPhone_number());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    checked.setChecked(true);
                    if (checkedPosition != getAdapterPosition()) {
                        notifyItemChanged(checkedPosition);
                        checkedPosition = getAdapterPosition();
                        if (listener!=null)
                            listener.onItemClick(item);
                    }
                }
            });
        }
    }
}