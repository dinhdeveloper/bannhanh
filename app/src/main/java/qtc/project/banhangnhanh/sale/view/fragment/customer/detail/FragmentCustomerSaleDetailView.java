package qtc.project.banhangnhanh.sale.view.fragment.customer.detail;

import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
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
import b.laixuantam.myaarlibrary.widgets.roundview.RoundTextView;
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
        KeyboardUtils.setupUI(getView(), activity);
        ui.imageNavLeft.setOnClickListener(v -> {
            if (callback != null) {
                callback.onBackP();
            }
        });
    }

    @Override
    public void setDataCustomerDetail(CustomerModel model) {
        if (model != null) {
            ui.title_header.setText("Chi tiết khách hàng");
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
                callback.updateCustomer(customerModel, model.getId());
            });
        } else {
            ui.title_header.setText("Tạo mới khách hàng");
            setGone(ui.layoutLevelCus);
            ui.btnUpdate.setText("Tạo mới");
            setVisible(ui.btnDelete);
            ui.idCustomer.setEnabled(true);
            ui.btnUpdate.setOnClickListener(v -> {
                if (checkInput()) {
                    CustomerModel customerModel = new CustomerModel();
                    customerModel.setId_code(ui.idCustomer.getText().toString());
                    customerModel.setFull_name(ui.nameCustomer.getText().toString());
                    customerModel.setAddress(ui.addressCustomer.getText().toString());
                    customerModel.setEmail(ui.emailCustomer.getText().toString());
                    customerModel.setPhone_number(ui.phoneCustomer.getText().toString());
                    if (callback != null)
                        callback.createCustomer(customerModel);
                }
            });
        }
    }

    private boolean checkInput() {
        if (TextUtils.isEmpty(ui.idCustomer.getText().toString())) {
            ui.idCustomer.setError("Không bỏ trống.");
            return false;
        }
        if (TextUtils.isEmpty(ui.nameCustomer.getText().toString())) {
            ui.nameCustomer.setError("Không bỏ trống.");
            return false;
        }
        if (TextUtils.isEmpty(ui.phoneCustomer.getText().toString())) {
            ui.phoneCustomer.setError("Không bỏ trống.");
            return false;
        }
        return true;
    }

    @Override
    public void resetView() {
        ui.nameCustomer.setText(null);
        ui.idCustomer.setText(null);
        ui.phoneCustomer.setText(null);
        ui.emailCustomer.setText(null);
        ui.addressCustomer.setText(null);
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
        public EditText idCustomer;

        @UiElement(R.id.phoneCustomer)
        public EditText phoneCustomer;

        @UiElement(R.id.addressCustomer)
        public EditText addressCustomer;

        @UiElement(R.id.emailCustomer)
        public EditText emailCustomer;

        @UiElement(R.id.nameLevelCuss)
        public TextView nameLevelCuss;

        @UiElement(R.id.btnUpdate)
        public RoundTextView btnUpdate;

        @UiElement(R.id.btnDelete)
        public RoundTextView btnDelete;

        @UiElement(R.id.layoutLevelCus)
        public LinearLayout layoutLevelCus;

    }
}