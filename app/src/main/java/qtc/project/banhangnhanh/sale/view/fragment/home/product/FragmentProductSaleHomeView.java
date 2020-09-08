package qtc.project.banhangnhanh.sale.view.fragment.home.product;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import b.laixuantam.myaarlibrary.base.BaseUiContainer;
import b.laixuantam.myaarlibrary.base.BaseView;
import qtc.project.banhangnhanh.R;
import qtc.project.banhangnhanh.activity.SaleHomeActivity;
import qtc.project.banhangnhanh.sale.adapter.home.ProductHomeAdapter;
import qtc.project.banhangnhanh.sale.model.ProductModel;

public class FragmentProductSaleHomeView extends BaseView<FragmentProductSaleHomeView.UIContainer> implements FragmentProductSaleHomeViewInterface{
    SaleHomeActivity activity;
    FragmentProductSaleHomeViewCallback callback;

    ProductHomeAdapter productAdapter;
    ArrayList<ProductModel> arrayList = new ArrayList<>();

    boolean enableLoadMore = true;
    @Override
    public void init(SaleHomeActivity activity, FragmentProductSaleHomeViewCallback callback) {
        this.activity = activity;
        this.callback = callback;
        initRecycler();
        ui.title_header.setText("Danh sách sản phẩm");
        ui.imvHome.setOnClickListener(v -> {
            if (callback!=null)
                callback.onBackP();
        });

        onClick();
    }
    private void onClick() {
        ui.edit_filter.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH){
                    searchProduct(ui.edit_filter.getText().toString());
                }
                return false;
            }
        });

        ui.image_close.setOnClickListener(v -> {
            ui.edit_filter.setText(null);
            if (callback!=null)
            {
                callback.callAllData();
                callback.resetPage();
                enableLoadMore = true;
            }
        });
    }

    private void searchProduct(String search) {
        if (callback != null) {
            if (search != null) {
                callback.searchProduct(search);
            } else {
                callback.callAllData();
            }
        }
    }

    @Override
    public void setNoMoreLoading() {
        enableLoadMore = false;
    }

    @Override
    public void initRecyclerViewProduct(ProductModel[] list) {
        if (list == null || list.length == 0) {
            if (arrayList.size() == 0)
                showEmptyList();
            return;
        }
        for (ProductModel model : list) {
            if (Integer.valueOf(model.getTotal_stock()) > 0) {
                arrayList.add(model);
            }
        }
        if (arrayList.size() == 0) {
            callback.loadMore();
        }
        productAdapter.notifyDataSetChanged();
    }

    @Override
    public void clearListDataProduct() {
        arrayList.clear();
        productAdapter.notifyDataSetChanged();
    }

    public void initRecycler() {
        ui.recycler_view_list.getRecycledViewPool().clear();
        productAdapter = new ProductHomeAdapter(activity, arrayList);
        ui.recycler_view_list.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false));
        ui.recycler_view_list.setAdapter(productAdapter);
        productAdapter.notifyDataSetChanged();
        productAdapter.setListener(new ProductHomeAdapter.ProductHomeAdapterListener() {
            @Override
            public void onItemClick(ProductModel model) {
                if (callback!=null)
                    callback.goToDetail(model);
            }

            @Override
            public void onRequestLoadMoreProduct() {
                if (callback != null && enableLoadMore)
                    callback.loadMore();
            }
        });

    }

    private void showEmptyList() {
    }
    @Override
    public BaseUiContainer getUiContainer() {
        return new FragmentProductSaleHomeView.UIContainer();
    }

    @Override
    public int getViewId() {
        return R.layout.layout_fragment_product_sale;
    }



    public class UIContainer extends BaseUiContainer {
        @UiElement(R.id.imageNavLeft)
        public ImageView imageNavLeft;

        @UiElement(R.id.title_header)
        public TextView title_header;

        @UiElement(R.id.imvHome)
        public ImageView imvHome;

        @UiElement(R.id.recycler_view_list)
        public RecyclerView recycler_view_list;

        @UiElement(R.id.edit_filter)
        public EditText edit_filter;

        @UiElement(R.id.image_close)
        public ImageView image_close;
    }
}
