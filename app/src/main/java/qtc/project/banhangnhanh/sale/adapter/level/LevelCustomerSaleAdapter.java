package qtc.project.banhangnhanh.sale.adapter.level;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import b.laixuantam.myaarlibrary.dependency.AppProvider;
import b.laixuantam.myaarlibrary.widgets.superadapter.SuperAdapter;
import b.laixuantam.myaarlibrary.widgets.superadapter.SuperViewHolder;
import qtc.project.banhangnhanh.R;
import qtc.project.banhangnhanh.admin.model.LevelCustomerModel;
import qtc.project.banhangnhanh.helper.Consts;

public class LevelCustomerSaleAdapter extends SuperAdapter<LevelCustomerModel> {

    private CustomerLevelAdapterListener listener;

    public LevelCustomerSaleAdapter(Context context, List<LevelCustomerModel> items) {
        super(context, items, R.layout.custom_item_level_customer_sale);
    }

    public interface CustomerLevelAdapterListener{
        void onItemClick(LevelCustomerModel model);
    }

    public void setListener(CustomerLevelAdapterListener listener) {
        this.listener = listener;
    }

    @Override
    public void onBind(SuperViewHolder holder, int viewType, int layoutPosition, LevelCustomerModel item) {
        ImageView imageLevel = holder.findViewById(R.id.imageLevel);
        TextView description = holder.findViewById(R.id.description);
        TextView nameLevel = holder.findViewById(R.id.nameLevel);
        TextView discount = holder.findViewById(R.id.discount);
        CardView layoutLevelCus = holder.findViewById(R.id.layoutLevelCus);

        AppProvider.getImageHelper().displayImage(Consts.HOST_API+item.getImage(), imageLevel, null, R.drawable.imageloading);
        if (item.getDescription()==null ||item.getDescription().isEmpty()){
            description.setText(item.getName());
        }
        else {
            description.setText(item.getDescription());
        }
        nameLevel.setText(item.getName());
        discount.setText("Có "+item.getTotal_customer()+" người hiển thị.");

        layoutLevelCus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener !=null){
                    listener.onItemClick(item);
                }
            }
        });
    }
}