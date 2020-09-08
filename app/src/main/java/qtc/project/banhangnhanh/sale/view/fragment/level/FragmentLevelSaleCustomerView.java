package qtc.project.banhangnhanh.sale.view.fragment.level;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import b.laixuantam.myaarlibrary.base.BaseUiContainer;
import b.laixuantam.myaarlibrary.base.BaseView;
import b.laixuantam.myaarlibrary.helper.KeyboardUtils;
import qtc.project.banhangnhanh.R;
import qtc.project.banhangnhanh.activity.SaleHomeActivity;
import qtc.project.banhangnhanh.admin.model.LevelCustomerModel;
import qtc.project.banhangnhanh.sale.adapter.level.LevelCustomerSaleAdapter;

public class FragmentLevelSaleCustomerView extends BaseView<FragmentLevelSaleCustomerView.UIContainer> implements FragmentLevelSaleCustomerViewInterface{
    SaleHomeActivity activity;
    FragmentLevelSaleCustomerViewCallback callback;
    @Override
    public void init(SaleHomeActivity activity, FragmentLevelSaleCustomerViewCallback callback) {
        this.activity = activity;
        this.callback = callback;
        ui.title_header.setText("Cấp độ khách hàng");

        KeyboardUtils.setupUI(getView(),activity);
        ui.imvHome.setOnClickListener(v -> {
            if (callback!=null)
                callback.goHome();
        });
        ui.imageNavLeft.setOnClickListener(v -> {
            if (callback!=null){
                callback.callNav();
            }
        });
    }

    @Override
    public void initRecyclerView(ArrayList<LevelCustomerModel> list) {
        if (list!=null){
            ui.recycler_view_list.setVisibility(View.VISIBLE);
            ui.layoutNone.setVisibility(View.GONE);
            LevelCustomerSaleAdapter adapter = new LevelCustomerSaleAdapter(activity, list);
            ui.recycler_view_list.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false));
            ui.recycler_view_list.setAdapter(adapter);
            adapter.notifyDataSetChanged();

            adapter.setListener(model -> {
                if (callback!=null)
                    callback.goToLevelDetail(model);
            });
        }else {
            ui.recycler_view_list.setVisibility(View.GONE);
            ui.layoutNone.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public BaseUiContainer getUiContainer() {
        return new FragmentLevelSaleCustomerView.UIContainer();
    }

    @Override
    public int getViewId() {
        return R.layout.layout_fragment_level_customer_sale;
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

        //
    }
}