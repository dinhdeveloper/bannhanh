package qtc.project.banhangnhanh.sale.view.fragment.order.detail;

import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import b.laixuantam.myaarlibrary.base.BaseUiContainer;
import b.laixuantam.myaarlibrary.base.BaseView;
import qtc.project.banhangnhanh.R;
import qtc.project.banhangnhanh.activity.SaleHomeActivity;
import qtc.project.banhangnhanh.admin.model.OrderDetailModel;
import qtc.project.banhangnhanh.sale.adapter.order.OrderDetailAdapter;
import qtc.project.banhangnhanh.sale.model.OrderModel;

public class FragmentOrderDetailSaleView extends BaseView<FragmentOrderDetailSaleView.UIContainer> implements FragmentOrderDetailSaleViewInterface {
    SaleHomeActivity activity;
    FragmentOrderDetailSaleViewCallback callback;

    @Override
    public void init(SaleHomeActivity activity, FragmentOrderDetailSaleViewCallback callback) {
        this.activity = activity;
        this.callback = callback;
        ui.title_header.setText("Chi tiết đơn hàng");
        ui.imageNavLeft.setOnClickListener(v -> {
            if (callback != null)
                callback.onBackP();
        });
    }

    @Override
    public void initView(OrderModel model) {
        if (model != null) {
            ui.idCustomer.setText(model.getCustomer_id_code());
            ui.idEmployee.setText(model.getEmployee_code());
            ui.idOrder.setText(model.getOrder_id_code());

            ArrayList<OrderDetailModel> detailModels = new ArrayList<>();
            for (int i = 0; i < model.getListDataOrderDetail().size(); i++) {
                detailModels.add(model.getListDataOrderDetail().get(i));
            }
            OrderDetailAdapter detailAdapter = new OrderDetailAdapter(activity, detailModels);
            ui.recycler_view_order_detail.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
            ui.recycler_view_order_detail.setAdapter(detailAdapter);
            detailAdapter.notifyDataSetChanged();

            int size = detailModels.size();
            long allPrice = 0;

            for (int i = 0; i < size; i++) {
                long total = Long.valueOf(detailModels.get(i).getPrice()) * Long.valueOf(detailModels.get(i).getQuantity());
                allPrice += total;
            }
            String pattern = "###,###,###";
            DecimalFormat decimalFormat = new DecimalFormat(pattern);

            ui.idPriceDemo.setText(decimalFormat.format(allPrice) + " đ");
            float tiengiam = allPrice - Long.valueOf(model.getOrder_total());
            ui.allPrice.setText(decimalFormat.format(Long.valueOf(model.getOrder_total())) + " đ");
            // ui.allPrice.setText(decimalFormat.format(allPrice - Integer.parseInt(model.getCustomer_level_discount())));
            ui.priceSale.setText(decimalFormat.format(tiengiam) + " đ");
            if (model.getOrder_status().equalsIgnoreCase("Y")) {
                ui.btnExit.setVisibility(View.VISIBLE);
                ui.btnExit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        callback.cancelOrder(model.getId_order());
                    }
                });
            } else if (model.getOrder_status().equalsIgnoreCase("N")) {
                ui.btnExit.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public void showSuccess() {
        LayoutInflater layoutInflater = activity.getLayoutInflater();
        View popupView = layoutInflater.inflate(R.layout.alert_dialog_success, null);
        TextView title_text = popupView.findViewById(R.id.title_text);
        TextView content_text = popupView.findViewById(R.id.content_text);
        Button custom_confirm_button = popupView.findViewById(R.id.custom_confirm_button);

        title_text.setText("Xác nhận");
        content_text.setText("Bạn đã huỷ thành công!");

        AlertDialog.Builder alert = new AlertDialog.Builder(activity);
        alert.setView(popupView);
        AlertDialog dialog = alert.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        custom_confirm_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (callback != null)
                    callback.reQuestData();
            }
        });
    }

    @Override
    public BaseUiContainer getUiContainer() {
        return new FragmentOrderDetailSaleView.UIContainer();
    }

    @Override
    public int getViewId() {
        return R.layout.layout_fragment_order_detail_sale;
    }


    public class UIContainer extends BaseUiContainer {

        @UiElement(R.id.imageNavLeft)
        public ImageView imageNavLeft;

        @UiElement(R.id.title_header)
        public TextView title_header;

        @UiElement(R.id.imvHome)
        public ImageView imvHome;

        @UiElement(R.id.idCustomer)
        public TextView idCustomer;

        @UiElement(R.id.idEmployee)
        public TextView idEmployee;

        @UiElement(R.id.idOrder)
        public TextView idOrder;

        @UiElement(R.id.idPriceDemo)
        public TextView idPriceDemo;

        @UiElement(R.id.priceSale)
        public TextView priceSale;

        @UiElement(R.id.allPrice)
        public TextView allPrice;

        @UiElement(R.id.recycler_view_order_detail)
        public RecyclerView recycler_view_order_detail;

        @UiElement(R.id.btnExit)
        public LinearLayout btnExit;


    }
}