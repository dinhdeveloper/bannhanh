package qtc.project.banhangnhanh.sale.view.fragment.customer.detail;

import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import b.laixuantam.myaarlibrary.base.BaseUiContainer;
import b.laixuantam.myaarlibrary.base.BaseView;
import b.laixuantam.myaarlibrary.helper.KeyboardUtils;
import qtc.project.banhangnhanh.R;
import qtc.project.banhangnhanh.activity.SaleHomeActivity;
import qtc.project.banhangnhanh.admin.model.CustomerModel;
import qtc.project.banhangnhanh.sale.view.activity.SaleHomeActivityViewCallback;

public class FragmentCustomerSaleDetailView extends BaseView<FragmentCustomerSaleDetailView.UIContainer> implements FragmentCustomerSaleDetailViewInterface {
    SaleHomeActivity activity;
    FragmentCustomerSaleDetailViewCallback callback;

    @Override
    public BaseUiContainer getUiContainer() {
        return new FragmentCustomerSaleDetailView.UIContainer();
    }

    @Override
    public void init(SaleHomeActivity activity, FragmentCustomerSaleDetailViewCallback callback) {
        this.activity = activity;
        this.callback = callback;
        KeyboardUtils.setupUI(getView(),activity);
        ui.title_header.setText("Chi tiết khách hàng");
        ui.imageNavLeft.setOnClickListener(v -> {
            if (callback!=null){
                callback.onBackP();
            }
        });
    }

    @Override
    public void initLayout(CustomerModel model) {
        if (model!=null){
            ui.nameCustomer.setText(model.getFull_name());
            ui.idCustomer.setText(model.getId_code());
            ui.phoneCustomer.setText(model.getPhone_number());
            ui.emailCustomer.setText(model.getEmail());
            ui.nameLevelCuss.setText(model.getLevel_name());
            ui.addressCustomer.setText(model.getAddress());

            ui.btnUpdate.setOnClickListener(v -> {
                CustomerModel customerModel = new CustomerModel();
                customerModel.setId_code(ui.idCustomer.getText().toString());
                customerModel.setFull_name(ui.nameCustomer.getText().toString());
                customerModel.setAddress(ui.addressCustomer.getText().toString());
                customerModel.setEmail(ui.emailCustomer.getText().toString());
                customerModel.setPhone_number(ui.phoneCustomer.getText().toString());

                callback.updateCustomer(customerModel,model.getId());
            });
        }
    }

    @Override
    public void showPopUpSuccess() {
        LayoutInflater layoutInflater = activity.getLayoutInflater();
        View popupView = layoutInflater.inflate(R.layout.alert_dialog_success, null);
        TextView title_text = popupView.findViewById(R.id.title_text);
        TextView content_text = popupView.findViewById(R.id.content_text);
        Button custom_confirm_button = popupView.findViewById(R.id.custom_confirm_button);

        title_text.setText("Xác nhận");
        content_text.setText("Bạn đã cập nhật thành công!");

        AlertDialog.Builder alert = new AlertDialog.Builder(activity);
        alert.setView(popupView);
        AlertDialog dialog = alert.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        custom_confirm_button.setOnClickListener(v -> {
            dialog.dismiss();
        });
    }

    @Override
    public int getViewId() {
        return R.layout.layout_fragment_customer_sale_detail;
    }


    public class UIContainer extends BaseUiContainer {
        @UiElement(R.id.imageNavLeft)
        public ImageView imageNavLeft;

        @UiElement(R.id.title_header)
        public TextView title_header;

        @UiElement(R.id.imvHome)
        public ImageView imvHome;

        @UiElement(R.id.nameCustomer)
        public EditText nameCustomer;

        @UiElement(R.id.idCustomer)
        public TextView idCustomer;

        @UiElement(R.id.phoneCustomer)
        public EditText phoneCustomer;

        @UiElement(R.id.addressCustomer)
        public EditText addressCustomer;

        @UiElement(R.id.emailCustomer)
        public EditText emailCustomer;

        @UiElement(R.id.nameLevelCuss)
        public TextView nameLevelCuss;

        @UiElement(R.id.btnUpdate)
        public LinearLayout btnUpdate;


    }
}