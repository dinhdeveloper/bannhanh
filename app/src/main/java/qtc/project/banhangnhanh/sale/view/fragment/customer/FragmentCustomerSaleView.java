package qtc.project.banhangnhanh.sale.view.fragment.customer;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import b.laixuantam.myaarlibrary.base.BaseUiContainer;
import b.laixuantam.myaarlibrary.base.BaseView;
import qtc.project.banhangnhanh.R;
import qtc.project.banhangnhanh.activity.SaleHomeActivity;
import qtc.project.banhangnhanh.admin.model.CustomerModel;
import qtc.project.banhangnhanh.sale.adapter.customer.CustomerSaleAdapter;

public class FragmentCustomerSaleView  extends BaseView<FragmentCustomerSaleView.UIContainer> implements FragmentCustomerSaleViewInterface{

    SaleHomeActivity activity;
    FragmentCustomerSaleViewCallback callback;
    @Override
    public void init(SaleHomeActivity activity, FragmentCustomerSaleViewCallback callback) {
        this.activity = activity;
        this.callback = callback;
        ui.title_header.setText("Khách hàng");
        ui.imvHome.setOnClickListener(v -> {
            if (callback!=null)
                callback.goHome();
        });

        ui.addCustomer.setOnClickListener(v -> {
            if (callback!=null)
                callback.addCustomer();
        });
        ui.imageNavLeft.setOnClickListener(v -> {
            if (callback!=null){
                callback.callNav();
            }
        });

        onClickSearch();
    }

    private void onClickSearch() {
        ui.edit_filter.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchCustomer(ui.edit_filter.getText().toString());
                    return true;
                }
                Toast.makeText(activity, "Không có kết quả tìm kiếm!", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        ui.edit_filter.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    searchCustomer(ui.edit_filter.getText().toString());
                    return true;
                }
                return false;
            }
        });

        ui.image_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ui.edit_filter.setText(null);
                if (callback!=null)
                    callback.callAllDataCustomer();
            }
        });
    }

    private void searchCustomer(String toString) {
        if (callback != null){
            if (toString!=null){
                callback.callDataSearchCus(toString);
            }
            else {
                callback.callAllDataCustomer();
            }
        }
    }

    @Override
    public void initRecyclerViewCustomer(ArrayList<CustomerModel> list) {
        ui.recycler_view_list.setVisibility(View.VISIBLE);
        ui.layoutNone.setVisibility(View.GONE);
        CustomerSaleAdapter adapter = new CustomerSaleAdapter(activity, list);
        ui.recycler_view_list.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false));
        ui.recycler_view_list.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        adapter.setListener(model -> {
            if (callback!=null)
                callback.goDetail(model);
        });
    }

    @Override
    public BaseUiContainer getUiContainer() {
        return new FragmentCustomerSaleView.UIContainer();
    }

    @Override
    public int getViewId() {
        return R.layout.layout_fragment_customer_sale;
    }



    public class UIContainer extends BaseUiContainer {

        @UiElement(R.id.imageNavLeft)
        public ImageView imageNavLeft;

        @UiElement(R.id.title_header)
        public TextView title_header;

        @UiElement(R.id.imvHome)
        public ImageView imvHome;

        @UiElement(R.id.layoutNone)
        public LinearLayout layoutNone;

        @UiElement(R.id.recycler_view_list)
        public RecyclerView recycler_view_list;

        @UiElement(R.id.addCustomer)
        public ImageView addCustomer;

        @UiElement(R.id.edit_filter)
        public EditText edit_filter;

        @UiElement(R.id.image_close)
        public ImageView image_close;


    }
}
