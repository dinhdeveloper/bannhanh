package qtc.project.banhangnhanh.sale.view.fragment.customer.filter;

import android.support.design.widget.BottomNavigationView;
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
import java.util.Arrays;

import b.laixuantam.myaarlibrary.base.BaseUiContainer;
import b.laixuantam.myaarlibrary.base.BaseView;
import b.laixuantam.myaarlibrary.helper.KeyboardUtils;
import qtc.project.banhangnhanh.R;
import qtc.project.banhangnhanh.activity.SaleHomeActivity;
import qtc.project.banhangnhanh.admin.model.CustomerModel;
import qtc.project.banhangnhanh.sale.adapter.customer.ListCustomerFHomeAdapter;

public class FragmentCustomerSaleFilterView extends BaseView<FragmentCustomerSaleFilterView.UIContainer> implements FragmentCustomerSaleFilterViewInterface {
    SaleHomeActivity activity;
    FragmentCustomerSaleFilterViewCallback callback;
    ArrayList<CustomerModel> arrayList = new ArrayList<>();
    ListCustomerFHomeAdapter infoAdapter;
    boolean enableLoadMore = true;
    @Override
    public void init(SaleHomeActivity activity, FragmentCustomerSaleFilterViewCallback callback) {
        this.activity = activity;
        this.callback = callback;

        KeyboardUtils.setupUI(getView(),activity);
        ui.btnExit.setOnClickListener(v -> {
            if (callback!=null)
                callback.onBackP();
        });

        ui.imageNavLeft.setOnClickListener(v -> {
            if (callback!=null)
                callback.onBackP();
        });
        initView();
        ui.title_header.setText("Danh sách khách hàng");

        onClick();
    }

    private void onClick() {
        ui.text_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH){
                    searchCustomer(ui.text_search.getText().toString());
                    return true;
                }
                return false;
            }
        });
        ui.image_close.setOnClickListener(v -> {
            ui.text_search.setText(null);
            if (callback!=null) {
                enableLoadMore = true;
                callback.getAllData();
            }
        });
    }

    private void searchCustomer(String search) {
        if (search!=null){
            ui.bottomnav.setVisibility(View.GONE);
            callback.searchCustomer(search);
        }
    }

    @Override
    public void initCustomer(CustomerModel[] list) {
        if (list == null || list.length == 0) {
            if (arrayList.size() == 0)
                showEmptyList();
            return;
        }
        arrayList.addAll(Arrays.asList(list));
        infoAdapter.notifyDataSetChanged();
        ui.tvTotal.setText("Có tất cả "+arrayList.size()+" khách hàng");
    }

    private void showEmptyList() {
        Toast.makeText(activity, "Không có khách hàng cần tìm", Toast.LENGTH_SHORT).show();
    }

    private void initView(){
        if (arrayList != null) {
            ui.tvTotal.setText("Có tất cả "+arrayList.size()+" khách hàng");
            ui.recycler_view_list_customer.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
            infoAdapter = new ListCustomerFHomeAdapter(activity, arrayList);
            ui.recycler_view_list_customer.setAdapter(infoAdapter);
            infoAdapter.notifyDataSetChanged();

            infoAdapter.setListener(model -> {
                ui.bottomnav.setVisibility(View.VISIBLE);
                ui.btnOk.setOnClickListener(v -> {
                    if (callback!=null)
                        callback.setCustomerToHome(model);
                });

                ui.btnExit.setOnClickListener(v -> {
                    if (callback!=null)
                        callback.onBackP();
                });
            });
        }
    }

    @Override
    public void setNoMoreLoading() {
        enableLoadMore = false;
    }

    @Override
    public void clearnData() {
        arrayList.clear();
        infoAdapter.notifyDataSetChanged();
    }

    @Override
    public BaseUiContainer getUiContainer() {
        return new FragmentCustomerSaleFilterView.UIContainer();
    }

    @Override
    public int getViewId() {
        return R.layout.layout_fragment_customer_sale_filter;
    }


    public class UIContainer extends BaseUiContainer {
        @UiElement(R.id.imageNavLeft)
        public ImageView imageNavLeft;

        @UiElement(R.id.title_header)
        public TextView title_header;

        @UiElement(R.id.tvTotal)
        public TextView tvTotal;

        @UiElement(R.id.imvHome)
        public ImageView imvHome;

        @UiElement(R.id.text_search)
        public EditText text_search;

        @UiElement(R.id.image_close)
        public ImageView image_close;

        @UiElement(R.id.recycler_view_list_customer)
        public RecyclerView recycler_view_list_customer;

        @UiElement(R.id.btnExit)
        public LinearLayout btnExit;

        @UiElement(R.id.btnOk)
        public LinearLayout btnOk;

        @UiElement(R.id.bottomnav)
        public BottomNavigationView bottomnav;



    }
}