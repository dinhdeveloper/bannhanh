package qtc.project.banhangnhanh.sale.adapter.home;

import android.content.Context;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.List;

import b.laixuantam.myaarlibrary.widgets.superadapter.SuperAdapter;
import b.laixuantam.myaarlibrary.widgets.superadapter.SuperViewHolder;
import qtc.project.banhangnhanh.R;
import qtc.project.banhangnhanh.sale.model.ListOrderModel;

public class ListItemClickAdapter extends SuperAdapter<ListOrderModel> {

    private ListItemClickAdapterListener listener;
    Context context;

    public ListItemClickAdapter(Context context, List<ListOrderModel> items) {
        super(context, items, R.layout.custom_item_list_product_home);
        this.context = context;
    }

    public interface ListItemClickAdapterListener {
        void onItemClick(ListOrderModel model);

        void onClickThemSoLuong(ListOrderModel model);

        void onClickGiamSoLuong(ListOrderModel model);

        void onClickXoaItem(ListOrderModel model);

        void onChangeSoLuong(ListOrderModel item);
    }

    public void setListener(ListItemClickAdapterListener listener) {
        this.listener = listener;
    }

    @Override
    public void onBind(SuperViewHolder holder, int viewType, int layoutPosition, ListOrderModel item) {
        ImageView imageView_close = holder.findViewById(R.id.imageView_close);
        ImageView button_remove = holder.findViewById(R.id.button_remove);
        ImageView button_add = holder.findViewById(R.id.button_add);
        TextView edtNameProduct = holder.findViewById(R.id.nameProduct);
        EditText edtQuantity = holder.findViewById(R.id.quantity);
        TextView priceProduct = holder.findViewById(R.id.priceProduct);

        String pattern = "###,###.###";
        DecimalFormat decimalFormat = new DecimalFormat(pattern);

        edtNameProduct.setText(item.getNameProduct());
        long price = (long) (Long.valueOf(item.getPriceProduct()) * Double.valueOf(item.getQuantityProduct()));
        priceProduct.setText(decimalFormat.format(price) + " đ");
        edtQuantity.setText(item.getQuantityProduct());
        edtQuantity.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (!TextUtils.isEmpty(edtQuantity.getText().toString())) {
                        if (Double.valueOf(edtQuantity.getText().toString()) > Double.valueOf(item.getInventory())) {
                            Toast.makeText(getContext(), "Tồn kho không đủ.", Toast.LENGTH_SHORT).show();
                            return false;
                        }
                        double new_quantity = Double.valueOf(edtQuantity.getText().toString());
                        edtQuantity.setText(String.valueOf(new_quantity));
                        item.setQuantityProduct(String.valueOf(new_quantity));
                        long price = (long) (Long.valueOf(item.getPriceProduct()) * new_quantity);
                        priceProduct.setText(decimalFormat.format(price) + " đ");
                        listener.onChangeSoLuong(item);
                        return true;
                    } else {
                        Toast.makeText(context, "Vui lòng nhập số lượng.", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                }
                return false;
            }
        });
        imageView_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClickXoaItem(item);
                }
            }
        });

        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    if (Double.valueOf(item.getInventory()) > Double.valueOf(item.getQuantityProduct())) {
                        double new_quantity = Double.valueOf(item.getQuantityProduct()) + 1.0;
                        edtQuantity.setText(String.valueOf(new_quantity));
                        long price = (long) (Long.valueOf(item.getPriceProduct()) * new_quantity);
                        priceProduct.setText(decimalFormat.format(price) + " đ");
                        listener.onClickThemSoLuong(item);
                    } else {
                        if (Double.valueOf(edtQuantity.getText().toString()) > Double.valueOf(item.getInventory())) {
                            double new_quantity = Double.valueOf(item.getInventory());
                            edtQuantity.setText(String.valueOf(new_quantity));
                            long price = (long) (Long.valueOf(item.getPriceProduct()) * new_quantity);
                            priceProduct.setText(decimalFormat.format(price) + " đ");
                            listener.onClickThemSoLuong(item);
                            button_add.setEnabled(false);
                            Toast.makeText(getContext(), "Hàng tồn kho đã hết", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });

        button_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    double new_quantity = Double.valueOf(edtQuantity.getText().toString()) - 1.0;
                    if (new_quantity > 1.0) {
                        edtQuantity.setText(String.valueOf(new_quantity));
                        long price = (long) (Double.valueOf(item.getPriceProduct()) * new_quantity);
                        priceProduct.setText(decimalFormat.format(price) + " đ");
                    } else {
                        edtQuantity.setText(String.valueOf(1.0));
                        long price = (long) (Long.valueOf(item.getPriceProduct()) * 1.0);
                        priceProduct.setText(decimalFormat.format(price) + " đ");
                    }
                    listener.onClickGiamSoLuong(item);
                }
            }
        });
    }
}