package qtc.project.banhangnhanh.admin.views.fragment.employee.lichsubanhang.detail;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;

import b.laixuantam.myaarlibrary.base.BaseUiContainer;
import b.laixuantam.myaarlibrary.base.BaseView;
import qtc.project.banhangnhanh.R;
import qtc.project.banhangnhanh.activity.HomeActivity;
import qtc.project.banhangnhanh.admin.adapter.history.ListOrderDetailAdapter;
import qtc.project.banhangnhanh.admin.model.OrderCustomerModel;
import qtc.project.banhangnhanh.admin.model.OrderDetailModel;

public class FragmentOrderDetailEmployeeView extends BaseView<FragmentOrderDetailEmployeeView.UIContainer> implements FragmentOrderDetailEmployeeViewInterface {
    HomeActivity activity;
    FragmentOrderDetailEmployeeViewCallback callback;

    @Override
    public void init(HomeActivity activity, FragmentOrderDetailEmployeeViewCallback callback) {
        this.activity = activity;
        this.callback = callback;

        onClick();
    }

    private void onClick() {
        ui.imageNavLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callback != null)
                    callback.onBackProgress();
            }
        });
    }

    @Override
    public void sentDataToView(OrderCustomerModel model) {
        //try{
        if (model != null) {
            ui.id_employee_order.setText(model.getEmployee_id());
            ui.name_employee.setText(model.getEmployee_fullname());
            ui.date_create.setText(model.getOrder_created_date());

            if (model.getOrder_status().equals("Y")) {
                ui.status_order.setText("Hoàn thành");
            } else if (model.getOrder_status().equals("N")) {
                ui.status_order.setText("Đã hủy");
            }
            ui.priceTotal.setText(model.getOrder_total());
            String pattern = "###,###.###";
            DecimalFormat decimalFormat = new DecimalFormat(pattern);

//            ui.priceGiam.setText(decimalFormat.format(Integer.parseInt(model.getCustomer_level_discount())) + " VNĐ");
            ui.priceTotal.setText(decimalFormat.format(Integer.parseInt(model.getOrder_total())) + " VNĐ");
            //int tientemp = Integer.parseInt(model.getOrder_total()) + Integer.parseInt(model.getCustomer_level_discount());
            //ui.priceTemp.setText(decimalFormat.format(tientemp) + " VNĐ");
            int temp =0;
            for (OrderDetailModel model1 : model.getListOrderDetailModel()){
                temp+=Long.valueOf(model1.getPrice()) * Integer.valueOf(model1.getQuantity());
            }
            ui.priceTemp.setText(String.valueOf(decimalFormat.format(temp))+" VNĐ");
            ui.priceGiam.setText(String.valueOf(temp-Integer.valueOf(model.getOrder_total()))+" VNĐ");

            ListOrderDetailAdapter adapter = new ListOrderDetailAdapter(activity, model.getListOrderDetailModel());
            ui.recycler_view_list.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
            ui.recycler_view_list.setAdapter(adapter);
        }
//        }catch (Exception e){
//            Log.e("Exception",e.getMessage());
//        }
    }

    @Override
    public BaseUiContainer getUiContainer() {
        return new FragmentOrderDetailEmployeeView.UIContainer();
    }

    @Override
    public int getViewId() {
        return R.layout.layout_fragment_employee_saler_detail;
    }


    public class UIContainer extends BaseUiContainer {
        @UiElement(R.id.id_employee_order)
        public TextView id_employee_order;

        @UiElement(R.id.name_employee)
        public TextView name_employee;

        @UiElement(R.id.date_create)
        public TextView date_create;

        @UiElement(R.id.recycler_view_list)
        public RecyclerView recycler_view_list;

        @UiElement(R.id.priceTemp)
        public TextView priceTemp;

        @UiElement(R.id.priceGiam)
        public TextView priceGiam;

        @UiElement(R.id.priceTotal)
        public TextView priceTotal;

        @UiElement(R.id.status_order)
        public TextView status_order;


        @UiElement(R.id.imageNavLeft)
        public ImageView imageNavLeft;


    }
}
