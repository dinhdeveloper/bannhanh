package qtc.project.banhangnhanh.sale.view.fragment.order;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

import b.laixuantam.myaarlibrary.base.BaseUiContainer;
import b.laixuantam.myaarlibrary.base.BaseView;
import qtc.project.banhangnhanh.R;
import qtc.project.banhangnhanh.activity.SaleHomeActivity;
import qtc.project.banhangnhanh.sale.adapter.order.OrderAdapter;
import qtc.project.banhangnhanh.sale.model.OrderModel;

public class FragmentOrderSaleView extends BaseView<FragmentOrderSaleView.UIContainer> implements FragmentOrderSaleViewInterface {
    SaleHomeActivity activity;
    FragmentOrderSaleViewCallback callback;

    ArrayList<OrderModel> orderModels = new ArrayList<>();

    boolean enableLoadMore = true;
    OrderAdapter adapter;
    @Override
    public void init(SaleHomeActivity activity, FragmentOrderSaleViewCallback callback) {
        this.activity = activity;
        this.callback =callback;

        initRecycler();

        ui.title_header.setText("Đơn hàng");
        ui.imvHome.setOnClickListener(v -> {
            if (callback!=null)
                callback.goHome();
        });

        ui.imvFilter.setOnClickListener(v -> {
            orderModels.clear();
            adapter.notifyDataSetChanged();
            if (callback!=null)
                callback.filter();
        });

        ui.imageNavLeft.setOnClickListener(v -> {
            if (callback!=null){
                callback.callNav();
            }
        });

        onClick();
    }

    private void onClick() {
        //search customer
        ui.edit_filter.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        //ui.edit_filter.setInputType();
        ui.edit_filter.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (ui.edit_filter.getText().toString() != null) {
                        searchOrder(ui.edit_filter.getText().toString());
                        return true;
                    }
                }
                Toast.makeText(activity, "Không có kết quả tìm kiếm!", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        ui.edit_filter.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)){
                    if (ui.edit_filter.getText().toString() != null) {
                        searchOrder(ui.edit_filter.getText().toString());
                        return true;
                    }
                }
                return false;
            }
        });

        //xos search
        ui.image_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderModels.clear();
                adapter.notifyDataSetChanged();
                ui.edit_filter.setText(null);
                callback.reQuestData();
                enableLoadMore = true;
            }
        });
    }

    private void searchOrder(String search) {
        if (callback != null)
            callback.searchOrder(search);
    }

    @Override
    public void initRecyclerViewOrder(OrderModel[] list) {
        if (list == null || list.length == 0) {
            if (orderModels.size() == 0)
                showEmptyList();
            return;
        }
        orderModels.addAll(Arrays.asList(list));
        adapter.notifyDataSetChanged();
    }

    private void initRecycler() {
        ui.recycler_view_list.getRecycledViewPool().clear();
        ui.layoutNone.setVisibility(View.GONE);
        ui.recycler_view_list.setVisibility(View.VISIBLE);
        adapter = new OrderAdapter(activity,orderModels);
        ui.recycler_view_list.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false));
        ui.recycler_view_list.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.setListener(model -> {
            if (callback != null)
                enableLoadMore = true;
            callback.goToDetail(model);
        });
    }

    private void showEmptyList() {
    }

    @Override
    public void setNoMoreLoading() {
        enableLoadMore = false;
    }

    @Override
    public void clearnData() {
        orderModels.clear();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void setLayoutNull() {
        orderModels.clear();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void initRecyclerView(OrderModel[] list) {
        if (list == null || list.length == 0) {
            if (orderModels.size() == 0)
                showEmptyList();
            return;
        }
        orderModels.addAll(Arrays.asList(list));
        adapter.notifyDataSetChanged();
    }

    @Override
    public BaseUiContainer getUiContainer() {
        return new FragmentOrderSaleView.UIContainer();
    }

    @Override
    public int getViewId() {
        return R.layout.layout_fragment_order_sale;
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

        @UiElement(R.id.imvFilter)
        public ImageView imvFilter;

        @UiElement(R.id.edit_filter)
        public EditText edit_filter;

        @UiElement(R.id.image_close)
        public ImageView image_close;

        @UiElement(R.id.layoutDis)
        public RelativeLayout layoutDis;


    }
}