package qtc.project.banhangnhanh.sale.adapter.order;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.util.List;

import b.laixuantam.myaarlibrary.widgets.superadapter.SuperAdapter;
import b.laixuantam.myaarlibrary.widgets.superadapter.SuperViewHolder;
import qtc.project.banhangnhanh.R;
import qtc.project.banhangnhanh.admin.model.OrderDetailModel;
import qtc.project.banhangnhanh.helper.Consts;
import qtc.project.banhangnhanh.sale.model.OrderModel;

public class OrderDetailAdapter extends SuperAdapter<OrderDetailModel> {

    OrderDetailAdapterListener listener;

    public OrderDetailAdapter(Context context, List<OrderDetailModel> items) {
        super(context, items, R.layout.custom_item_order_detail_sale);
    }

    public interface OrderDetailAdapterListener {
        void onItemClick(OrderModel model);
    }

    public void setListener(OrderDetailAdapterListener listener) {
        this.listener = listener;
    }

    @Override
    public void onBind(SuperViewHolder holder, int viewType, int layoutPosition, OrderDetailModel item) {
        TextView nameProduct = holder.findViewById(R.id.nameProduct);
        ImageView imageOrderDetail = holder.findViewById(R.id.imageOrderDetail);
        ImageView iconAdd = holder.findViewById(R.id.iconAdd);
        TextView allPriceOrderDetail = holder.findViewById(R.id.allpriceOrderDetail);
        TextView quantityOrderDetail = holder.findViewById(R.id.quantityOrderDetail);
        TextView priceOrderDetail = holder.findViewById(R.id.priceOrderDetail);

        if (item != null) {
            nameProduct.setText((layoutPosition+1)+". "+item.getName());
            Glide.with(getContext()).load(Consts.HOST_API+item.getImage()).into(imageOrderDetail);
//            AppProvider.getImageHelper().displayImage(item.getImage().toString(), imageOrderDetail, null, R.drawable.imageloading);
            quantityOrderDetail.setText("X"+item.getQuantity());
            String pattern = "###,###.###";
            DecimalFormat decimalFormat = new DecimalFormat(pattern);
            priceOrderDetail.setText(decimalFormat.format(Double.valueOf(item.getPrice())));
            long allPriceItem = (long) (Long.valueOf(item.getPrice())* Double.valueOf(item.getQuantity()));
            allPriceOrderDetail.setText(decimalFormat.format(allPriceItem));

        }
    }
}