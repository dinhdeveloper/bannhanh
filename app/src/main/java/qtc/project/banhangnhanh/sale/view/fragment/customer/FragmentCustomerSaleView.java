package qtc.project.banhangnhanh.sale.view.fragment.customer;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import java.util.Timer;
import java.util.TimerTask;

import b.laixuantam.myaarlibrary.base.BaseUiContainer;
import b.laixuantam.myaarlibrary.base.BaseView;
import b.laixuantam.myaarlibrary.helper.AppUtils;
import b.laixuantam.myaarlibrary.widgets.cptr.PtrClassicFrameLayout;
import b.laixuantam.myaarlibrary.widgets.cptr.PtrDefaultHandler;
import b.laixuantam.myaarlibrary.widgets.cptr.PtrFrameLayout;
import b.laixuantam.myaarlibrary.widgets.cptr.loadmore.OnLoadMoreListener;
import b.laixuantam.myaarlibrary.widgets.cptr.recyclerview.RecyclerAdapterWithHF;
import qtc.project.banhangnhanh.R;
import qtc.project.banhangnhanh.activity.SaleHomeActivity;
import qtc.project.banhangnhanh.admin.dialog.option.OptionModel;
import qtc.project.banhangnhanh.admin.model.BaseResponseModel;
import qtc.project.banhangnhanh.admin.model.CustomerModel;
import qtc.project.banhangnhanh.sale.adapter.customer.CustomerSaleAdapter;
import qtc.project.banhangnhanh.sale.view.fragment.list_base.FragmentListBaseViewCallback;
import qtc.project.banhangnhanh.sale.view.fragment.list_base.FragmentListBaseViewInterface;

public class FragmentCustomerSaleView  extends BaseView<FragmentCustomerSaleView.UIContainer> implements FragmentListBaseViewInterface {

    SaleHomeActivity activity;
    FragmentListBaseViewCallback callback;
    private CustomerSaleAdapter customerSaleAdapter;
    private RecyclerAdapterWithHF recyclerAdapterWithHF;
    private ArrayList<OptionModel> listDatas = new ArrayList<>();
    private Timer timer = new Timer();
    private final long DELAY = 1000; // in ms
    private boolean isRefreshList = false;

    @Override
    public void init(SaleHomeActivity activity, FragmentListBaseViewCallback callback) {
        this.activity = activity;
        this.callback = callback;
        ui.title_header.setText("Khách hàng");
        ui.edit_filter.setHint("Tên khách hàng");
        ui.imvHome.setOnClickListener(v -> {
            if (callback!=null)
                callback.goToFragmentDashBoard();
        });

        setVisible(ui.actionAdd);
        ui.actionAdd.setOnClickListener(v -> {
            if (callback!=null)
                callback.onClickAddItem();
        });

        ui.edit_filter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (timer != null)
                    timer.cancel();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() >= 1) {

                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            String key = s.toString().trim();
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    AppUtils.hideKeyBoard(getView());
                                    listDatas.clear();
                                    customerSaleAdapter.notifyDataSetChanged();
                                    ui.recycler_view_list_order.getRecycledViewPool().clear();
                                    callback.onRequestSearchWithFilter("", key);
                                }
                            });
                        }

                    }, DELAY);
                } else {
                    if (!isRefreshList) {
                        AppUtils.hideKeyBoard(getView());
                        listDatas.clear();
                        customerSaleAdapter.notifyDataSetChanged();
                        ui.recycler_view_list_order.getRecycledViewPool().clear();
                        callback.onRequestSearchWithFilter("", "");
                    }
                }
            }
        });

        setUpListData();
    }

    private void setUpListData() {
        ui.recycler_view_list_order.getRecycledViewPool().clear();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        ui.recycler_view_list_order.setLayoutManager(linearLayoutManager);

        //todo setup list with adapter

        customerSaleAdapter = new CustomerSaleAdapter(getContext(), listDatas);

        customerSaleAdapter.setListener(item -> {
            if (callback != null)
                callback.onItemListSelected(item);
        });

        recyclerAdapterWithHF = new RecyclerAdapterWithHF(customerSaleAdapter);

        ui.recycler_view_list_order.setAdapter(recyclerAdapterWithHF);

        ui.ptrClassicFrameLayout.setLoadMoreEnable(true);

        ui.ptrClassicFrameLayout.setPtrHandler(new PtrDefaultHandler(true) {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                AppUtils.hideKeyBoard(getView());
                isRefreshList = true;
                ui.edit_filter.setText("");
                listDatas.clear();
                customerSaleAdapter.notifyDataSetChanged();
                ui.recycler_view_list_order.getRecycledViewPool().clear();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ui.ptrClassicFrameLayout.refreshComplete();

                        if (callback != null) {
                            callback.refreshLoadingList();
                            isRefreshList = false;
                        }
                    }
                }, 100);


            }
        });

        ui.ptrClassicFrameLayout.setOnLoadMoreListener(new OnLoadMoreListener() {

            @Override
            public void loadMore() {
                handler.postDelayed(new Runnable() {

                    @Override
                    public void run() {

                        if (callback != null)
                            callback.onRequestLoadMoreList();

                    }
                }, 100);
            }
        });
    }

    @Override
    public void showEmptyList() {
        setVisible(ui.layoutEmptyList);
        setGone(ui.ptrClassicFrameLayout);
    }

    @Override
    public void hideEmptyList() {
        setGone(ui.layoutEmptyList);
        setVisible(ui.ptrClassicFrameLayout);
    }

    @Override
    public void setDataList(BaseResponseModel arrDatas) {
        ui.recycler_view_list_order.getRecycledViewPool().clear();

        if (arrDatas.getData() == null || arrDatas.getData().length == 0) {
            if (listDatas.size() == 0)
                showEmptyList();
            return;
        }

        hideEmptyList();

//        listDatas.addAll(Arrays.asList(arrDatas));
        CustomerModel[] arrOrder = (CustomerModel[]) arrDatas.getData();

        for (CustomerModel itemOrderModel : arrOrder) {
            OptionModel optionModel = new OptionModel();
            optionModel.setDtaCustom(itemOrderModel);
            listDatas.add(optionModel);
//            if (customerSaleAdapter!=null){
//                customerSaleAdapter.getListData().add(optionModel);
//                customerSaleAdapter.getListDataBackup().add(optionModel);
//            }
        }

        recyclerAdapterWithHF.notifyDataSetChanged();
        ui.ptrClassicFrameLayout.loadMoreComplete(true);
        ui.ptrClassicFrameLayout.setLoadMoreEnable(true);
    }

    @Override
    public void setNoMoreLoading() {
        ui.ptrClassicFrameLayout.loadMoreComplete(true);
        ui.ptrClassicFrameLayout.setLoadMoreEnable(false);
    }

    @Override
    public void resetListData() {
        listDatas.clear();
        customerSaleAdapter.notifyDataSetChanged();
        ui.recycler_view_list_order.getRecycledViewPool().clear();
    }

    @Override
    public void hideRootView() {
        setGone(ui.layoutRootView);
    }

    @Override
    public void showRootView() {
        setVisible(ui.layoutRootView);
    }

    @Override
    public void clearListData() {
        listDatas.clear();
        customerSaleAdapter.notifyDataSetChanged();
        ui.recycler_view_list_order.getRecycledViewPool().clear();
    }

    @Override
    public BaseUiContainer getUiContainer() {
        return new FragmentCustomerSaleView.UIContainer();
    }

    @Override
    public int getViewId() {
        return R.layout.layout_admin_fragment_list_base;
    }

    public class UIContainer extends BaseUiContainer {
        @UiElement(R.id.layoutRootView)
        public View layoutRootView;

        @UiElement(R.id.btnBackHeader)
        public View btnBackHeader;

        @UiElement(R.id.title_header)
        public TextView title_header;

        @UiElement(R.id.imvHome)
        public ImageView imvHome;

        @UiElement(R.id.edit_filter)
        public EditText edit_filter;

        @UiElement(R.id.btnAction1)
        public View actionAdd;

        @UiElement(R.id.btnAction2)
        public View actionFilter;

        @UiElement(R.id.ptrClassicFrameLayout)
        public PtrClassicFrameLayout ptrClassicFrameLayout;

        @UiElement(R.id.recycler_view_list)
        public RecyclerView recycler_view_list_order;

        @UiElement(R.id.layoutEmptyList)
        public View layoutEmptyList;
    }
}
