package qtc.project.banhangnhanh.sale.view.fragment.level.detail;

import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import b.laixuantam.myaarlibrary.base.BaseUiContainer;
import b.laixuantam.myaarlibrary.base.BaseView;
import b.laixuantam.myaarlibrary.dependency.AppProvider;
import b.laixuantam.myaarlibrary.helper.KeyboardUtils;
import qtc.project.banhangnhanh.R;
import qtc.project.banhangnhanh.activity.SaleHomeActivity;
import qtc.project.banhangnhanh.admin.model.CustomerModel;
import qtc.project.banhangnhanh.admin.model.LevelCustomerModel;
import qtc.project.banhangnhanh.helper.Consts;
import qtc.project.banhangnhanh.sale.adapter.level.ListCustomerLevelApdater;

public class FragmentLevelSaleDetailView  extends BaseView<FragmentLevelSaleDetailView.UIContainer> implements FragmentLevelSaleDetailViewInterface{
    SaleHomeActivity activity;
    FragmentLevelSaleDetailViewCallback callback;

    public int tongkhach = 0;
    @Override
    public void init(SaleHomeActivity activity, FragmentLevelSaleDetailViewCallback callback) {
        this.activity = activity;
        this.callback = callback;
        KeyboardUtils.setupUI(getView(),activity);
        onClick();
    }

    private void onClick() {
        ui.imageNavLeft.setOnClickListener(v -> {
            if (callback!=null)
                callback.onBackP();
        });

        ui.btnExit.setOnClickListener(v -> {
            if (callback!=null)
                callback.goHome();
        });
    }

    @Override
    public void initLayout(LevelCustomerModel model) {
        if (model!=null){
            ui.title_header.setText(model.getName());
            AppProvider.getImageHelper().displayImage(Consts.HOST_API + model.getImage(), ui.imageLevel, null, R.drawable.no_image_full);
            ui.description.setText(model.getDescription());
            ui.nameLevel.setText("Số người nhận được cấp độ " + model.getName() + " : ");

            tongkhach = Integer.parseInt(model.getTotal_customer());

            ui.discount.setText("Có " + model.getTotal_customer() + " người hiển thị");

            //xem chi tiet
            ui.btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (callback != null) {
                        if (model.getTotal_customer().equals("0")) {
                            Toast.makeText(activity, "Không có khách hàng nào.", Toast.LENGTH_SHORT).show();
                        } else {
                            callback.callPopup(model.getName(),model);
                        }
                    }
                }
            });
        }
    }

    private void showDiaLog() {
        if (list!=null){
            LayoutInflater layoutInflater = activity.getLayoutInflater();
            View popupView = layoutInflater.inflate(R.layout.custom_popup_level_detail_sale, null);
            RecyclerView recycler_view_list_customer = popupView.findViewById(R.id.recycler_view_list_customer);
            tongkhachhang = popupView.findViewById(R.id.tongkhachhang);

            tongkhachhang.setText("Có tất cả " + String.valueOf(tongkhach) + " khách hàng");
            recycler_view_list_customer.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
            infoAdapter = new ListCustomerLevelApdater(activity, list);
            recycler_view_list_customer.setAdapter(infoAdapter);
            infoAdapter.notifyDataSetChanged();

            EditText edit_filter = popupView.findViewById(R.id.edit_filter);
            ImageView image_close_search = popupView.findViewById(R.id.image_close);

            alert = new AlertDialog.Builder(activity);
            alert.setView(popupView);
            dialog = alert.create();
            dialog.show();

            image_close_search.setOnClickListener(v -> {
                edit_filter.setText(null);
            });
            //search
            edit_filter.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (infoAdapter != null)
                        infoAdapter.getFilter().filter(s);
                }
            });
        }
    }

    ArrayList<CustomerModel> list = new ArrayList<>();
    AlertDialog.Builder alert;
    AlertDialog dialog;
    TextView tongkhachhang;
    ListCustomerLevelApdater infoAdapter;
    @Override
    public void showPopupListCustomer(ArrayList<CustomerModel> model) {
        if (dialog != null) {
            list.clear();
            list.addAll(model);
            infoAdapter.notifyDataSetChanged();
            infoAdapter.getListData().addAll(list);
            infoAdapter.getListDataBackup().addAll(list);
            tongkhach = list.size();
            tongkhachhang.setText("Có tất cả " + String.valueOf(tongkhach) + " khách hàng");
            dialog.show();
        }
    }

    @Override
    public BaseUiContainer getUiContainer() {
        return new FragmentLevelSaleDetailView.UIContainer();
    }

    @Override
    public int getViewId() {
        return R.layout.layout_fragment_level_sale_detail;
    }



    public class UIContainer extends BaseUiContainer {
        @UiElement(R.id.imageNavLeft)
        public ImageView imageNavLeft;

        @UiElement(R.id.title_header)
        public TextView title_header;

        @UiElement(R.id.imvHome)
        public ImageView imvHome;

        @UiElement(R.id.imageLevel)
        public ImageView imageLevel;

        @UiElement(R.id.description)
        public TextView description;

        @UiElement(R.id.btnSubmit)
        public LinearLayout btnSubmit;

        @UiElement(R.id.nameLevel)
        public TextView nameLevel;

        @UiElement(R.id.discount)
        public TextView discount;

        @UiElement(R.id.btnExit)
        public LinearLayout btnExit;


    }
}