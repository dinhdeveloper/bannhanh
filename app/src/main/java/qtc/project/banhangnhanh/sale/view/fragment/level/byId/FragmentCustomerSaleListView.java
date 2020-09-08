package qtc.project.banhangnhanh.sale.view.fragment.level.byId;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
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
import qtc.project.banhangnhanh.sale.adapter.level.CustomerLevelAdapter;

public class FragmentCustomerSaleListView extends BaseView<FragmentCustomerSaleListView.UIContainer> implements FragmentCustomerSaleListViewInterface{
    SaleHomeActivity activity;
    FragmentCustomerSaleListViewCallback callback;
    boolean enableLoadMore = true;
    CustomerLevelAdapter adapter;
    ArrayList<CustomerModel> arrayList = new ArrayList<>();
    String level_id;
    @Override
    public void init(SaleHomeActivity activity, FragmentCustomerSaleListViewCallback callback) {
        this.activity = activity;
        this.callback = callback;
        initRecycle();

        KeyboardUtils.setupUI(getView(),activity);
        ui.imageNavLeft.setOnClickListener(v -> {
            if (callback!=null)
                callback.onBackP();
        });

        onClick();
    }

    private void onClick() {
        ui.image_close.setOnClickListener(v -> {
            enableLoadMore = true;
            ui.edit_filter.setText(null);
            if (callback!=null){
                callback.getAllData();
            }
        });

        ui.edit_filter.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH){
                    searchData(ui.edit_filter.getText().toString());
                    return true;
                }
                return false;
            }
        });
    }

    private void searchData(String  search) {
        if (callback!=null)
            callback.seachCustomer(search,level_id);
    }

    private void initRecycle() {
        ui.recycler_view_list_customer.getRecycledViewPool().clear();
        adapter = new CustomerLevelAdapter(activity,arrayList);
        ui.recycler_view_list_customer.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false));
        ui.recycler_view_list_customer.setAdapter(adapter);
    }

    @Override
    public void initView(String name,CustomerModel[] list) {
        if (list == null || list.length == 0) {
            if (arrayList.size() == 0)
                showEmptyList();
            return;
        }
        if (name!=null){
            ui.title_header.setText(name);
        }
        arrayList.addAll(Arrays.asList(list));
        adapter.notifyDataSetChanged();

        if (arrayList!=null){
            for (CustomerModel model:arrayList){
                level_id = model.getLevel_id();
            }
        }
    }

    @Override
    public void resetData() {
        arrayList.clear();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void clearnData() {
        arrayList.clear();
        adapter.notifyDataSetChanged();
    }

    private void showEmptyList() {
        Toast.makeText(activity, "Không có khách hàng cần tìm", Toast.LENGTH_SHORT).show();
    }

    @Override
    public BaseUiContainer getUiContainer() {
        return new FragmentCustomerSaleListView.UIContainer();
    }

    @Override
    public int getViewId() {
        return R.layout.custom_popup_level_detail_sale;
    }



    public class UIContainer extends BaseUiContainer {
        @UiElement(R.id.imageNavLeft)
        public ImageView imageNavLeft;

        @UiElement(R.id.title_header)
        public TextView title_header;

        @UiElement(R.id.imvHome)
        public ImageView imvHome;

        @UiElement(R.id.edit_filter)
        public EditText edit_filter;

        @UiElement(R.id.image_close)
        public ImageView image_close;

        @UiElement(R.id.recycler_view_list_customer)
        public RecyclerView recycler_view_list_customer;


    }
}
