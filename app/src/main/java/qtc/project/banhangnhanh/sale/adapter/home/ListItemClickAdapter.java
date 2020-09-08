package qtc.project.banhangnhanh.sale.adapter.home;

import android.content.Context;
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

public class ListItemClickAdapter  extends SuperAdapter<ListOrderModel> {

    private ListItemClickAdapterListener listener;
    int tonkho = 0;

    public ListItemClickAdapter(Context context, List<ListOrderModel> items) {
        super(context, items, R.layout.custom_item_list_product_home);
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
        TextView nameProduct = holder.findViewById(R.id.nameProduct);
        EditText quantity = holder.findViewById(R.id.quantity);
        TextView priceProduct = holder.findViewById(R.id.priceProduct);

        String pattern = "###,###.###";
        DecimalFormat decimalFormat = new DecimalFormat(pattern);

        nameProduct.setText(item.getNameProduct());
        long price = Long.valueOf(item.getPriceProduct()) * Long.valueOf(item.getQuantityProduct());
        priceProduct.setText(decimalFormat.format(price) + " đ");
        quantity.setText(item.getQuantityProduct());
        quantity.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (!quantity.getText().toString().trim().isEmpty()){
                        if (Long.valueOf(quantity.getText().toString()) > Long.valueOf(item.getInventory())) {
                            Toast.makeText(getContext(), "Tồn kho không đủ.", Toast.LENGTH_SHORT).show();
                            quantity.setText(item.getQuantityProduct());
                        } else if (quantity.getText().toString().equalsIgnoreCase("0") || quantity.getText().toString().trim().isEmpty()) {
                            quantity.setText(String.valueOf(1));
                            item.setQuantityProduct(String.valueOf(1));
                            priceProduct.setText(decimalFormat.format(1 * Long.valueOf(item.getPriceProduct())));
                            listener.onChangeSoLuong(item);
                        } else {
                            item.setQuantityProduct(quantity.getText().toString());
                            priceProduct.setText(decimalFormat.format(Long.valueOf(item.getQuantityProduct()) * Long.valueOf(item.getPriceProduct())));
                            listener.onChangeSoLuong(item);
                        }
                    }else {
                        quantity.setText(String.valueOf(1));
                        item.setQuantityProduct(String.valueOf(1));
                        priceProduct.setText(decimalFormat.format(1 * Long.valueOf(item.getPriceProduct())));
                        listener.onChangeSoLuong(item);
                    }
                }
                return true;
            }
        });
//        quantity.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
//                    if (!quantity.getText().toString().trim().isEmpty()){
//                        if (Long.valueOf(quantity.getText().toString()) > Long.valueOf(item.getInventory())) {
//                            Toast.makeText(getContext(), "Tồn kho không đủ.", Toast.LENGTH_SHORT).show();
//                            quantity.setText(item.getQuantityProduct());
//                        } else if (quantity.getText().toString().equalsIgnoreCase("0") || quantity.getText().toString().trim().isEmpty()) {
//                            quantity.setText(String.valueOf(1));
//                            item.setQuantityProduct(String.valueOf(1));
//                            priceProduct.setText(decimalFormat.format(1 * Long.valueOf(item.getPriceProduct())));
//                            listener.onChangeSoLuong(item);
//                        } else {
//                            item.setQuantityProduct(quantity.getText().toString());
//                            priceProduct.setText(decimalFormat.format(Long.valueOf(item.getQuantityProduct()) * Long.valueOf(item.getPriceProduct())));
//                            listener.onChangeSoLuong(item);
//                        }
//                    }else {
//                        quantity.setText(String.valueOf(1));
//                        item.setQuantityProduct(String.valueOf(1));
//                        priceProduct.setText(decimalFormat.format(1 * Long.valueOf(item.getPriceProduct())));
//                        listener.onChangeSoLuong(item);
//                    }
//                    return true;
//                }
//                return false;
//            }
//        });

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
                    if (Long.valueOf(item.getInventory()) > Long.valueOf(item.getQuantityProduct())) {
                        long new_quantity = Long.valueOf(item.getQuantityProduct()) + 1;
                        quantity.setText(String.valueOf(new_quantity));
                        long price = Long.valueOf(item.getPriceProduct()) * new_quantity;
                        priceProduct.setText(decimalFormat.format(price) + " đ");

                        listener.onClickThemSoLuong(item);
                    } else {
                        if (Long.valueOf(quantity.getText().toString()) > Long.valueOf(item.getInventory())) {
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
                    long new_quantity = Long.valueOf(quantity.getText().toString()) - 1;
                    if (new_quantity > 1) {
                        quantity.setText(String.valueOf(new_quantity));
                        long price = Long.valueOf(item.getPriceProduct()) * new_quantity;
                        priceProduct.setText(decimalFormat.format(price) + " đ");
                    } else {
                        quantity.setText(String.valueOf(1));
                        long price = Long.valueOf(item.getPriceProduct()) * 1;
                        priceProduct.setText(decimalFormat.format(price) + " đ");
                    }
                    listener.onClickGiamSoLuong(item);
                }
            }
        });
    }
}