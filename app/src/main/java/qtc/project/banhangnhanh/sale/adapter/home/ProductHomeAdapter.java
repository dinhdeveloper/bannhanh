package qtc.project.banhangnhanh.sale.adapter.home;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

import b.laixuantam.myaarlibrary.dependency.AppProvider;
import b.laixuantam.myaarlibrary.widgets.superadapter.SuperAdapter;
import b.laixuantam.myaarlibrary.widgets.superadapter.SuperViewHolder;
import qtc.project.banhangnhanh.R;
import qtc.project.banhangnhanh.helper.Consts;
import qtc.project.banhangnhanh.sale.model.ProductModel;

public class ProductHomeAdapter  extends SuperAdapter<ProductModel> {

    ProductHomeAdapterListener listener;
    boolean isChecked = true;
    public ProductHomeAdapter(Context context, List<ProductModel> items) {
        super(context, items, R.layout.custom_item_product_home);
    }

    public interface ProductHomeAdapterListener {
        void onItemClick(ProductModel model);

        void onRequestLoadMoreProduct();
    }

    public void setListener(ProductHomeAdapterListener listener) {
        this.listener = listener;
    }

    @Override
    public void onBind(SuperViewHolder holder, int viewType, int layoutPosition, ProductModel item) {
        ImageView imageProduct = holder.findViewById(R.id.imageProduct);
        ImageView checkedProduct = holder.findViewById(R.id.checkedProduct);
        TextView nameProduct = holder.findViewById(R.id.nameProduct);
        //LinearLayout layoutHetHang = holder.findViewById(R.id.layoutHetHang);
        LinearLayout layoutConHang = holder.findViewById(R.id.layoutConHang);
        TextView priceProduct = holder.findViewById(R.id.priceProduct);
        TextView warehouseProduct = holder.findViewById(R.id.warehouseProduct);
        CardView item_product = holder.findViewById(R.id.item_product);
        LinearLayout btnOk = holder.findViewById(R.id.btnOk);

        if (!item.getListDataProduct().isEmpty() && Double.valueOf(item.getTotal_stock())>0){
            layoutConHang.setVisibility(View.VISIBLE);
            //layoutHetHang.setVisibility(View.GONE);
            AppProvider.getImageHelper().displayImage(Consts.HOST_API + item.getImage(), imageProduct, null, R.drawable.imageloading);
            nameProduct.setText(item.getName().toString());
            String pattern = "###,###.###";
            DecimalFormat decimalFormat = new DecimalFormat(pattern);
            if (!item.getSale_price().isEmpty()){
                priceProduct.setText(decimalFormat.format(Double.valueOf(item.getSale_price())) + " đ");
            }
            warehouseProduct.setText(decimalFormat.format(Double.valueOf(item.getTotal_stock())));
            btnOk.setVisibility(View.VISIBLE);
            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isChecked) {
                        if (listener != null) {
                            listener.onItemClick(item);
                        }
                        //checkedProduct.setVisibility(View.VISIBLE);
                    } else {
                        if (listener != null) {
                            listener.onItemClick(item);
                        }
                        //checkedProduct.setVisibility(View.GONE);
                    }
                }
            });

        }
        else {
            AppProvider.getImageHelper().displayImage(Consts.HOST_API + item.getImage(), imageProduct, null, R.drawable.imageloading);
            nameProduct.setText(item.getName().toString());
            String pattern = "###,###.###";
            DecimalFormat decimalFormat = new DecimalFormat(pattern);
            if (!item.getSale_price().isEmpty()){
                priceProduct.setText(decimalFormat.format(Double.valueOf(item.getSale_price())) + " đ");
            }
            warehouseProduct.setText(decimalFormat.format(Double.valueOf(item.getTotal_stock())));

            layoutConHang.setVisibility(View.GONE);
            //layoutHetHang.setVisibility(View.VISIBLE);
            btnOk.setVisibility(View.GONE);
        }
        if (layoutPosition == getCount() - 1) {
            if (listener != null)
                listener.onRequestLoadMoreProduct();
        }
    }
}