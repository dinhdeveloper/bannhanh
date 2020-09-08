package qtc.project.banhangnhanh.admin.views.fragment.home;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import b.laixuantam.myaarlibrary.base.BaseUiContainer;
import b.laixuantam.myaarlibrary.base.BaseView;
import qtc.project.banhangnhanh.R;
import qtc.project.banhangnhanh.activity.HomeActivity;
import qtc.project.banhangnhanh.admin.fragment.customer.FragmentCustomer;
import qtc.project.banhangnhanh.admin.fragment.employee.FragmentEmployeeManager;
import qtc.project.banhangnhanh.admin.fragment.levelcustomer.FragmentLevelCustomer;
import qtc.project.banhangnhanh.admin.fragment.order.FragmentOrderManager;
import qtc.project.banhangnhanh.admin.fragment.product.FragmentProduct;
import qtc.project.banhangnhanh.admin.fragment.report.FragmentReportManager;
import qtc.project.banhangnhanh.admin.fragment.supplier.FragmentSupplierManager;

public class FragmentHomeView  extends BaseView<FragmentHomeView.UIContainer> implements FragmentHomeViewInterface{

    HomeActivity activity;
    FragmentHomeViewCallback callback;

    @Override
    public void init(HomeActivity activity, FragmentHomeViewCallback callback) {
        this.activity = activity;
        this.callback = callback;

        onClickItem();
    }

    private void onClickItem() {
        ui.layoutQLSP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.addFragment(new FragmentProduct(),true,null);
            }
        });
        ui.layoutQLCDKH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.addFragment(new FragmentLevelCustomer(),true,null);
            }
        });

        ui.layoutQLKH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.addFragment(new FragmentCustomer(),true,null);
            }
        });

        ui.layoutQLNCU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.addFragment(new FragmentSupplierManager(),true,null);
            }
        });

        ui.layoutQLNV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.addFragment(new FragmentEmployeeManager(),true,null);
            }
        });

        ui.layoutQLDH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.addFragment(new FragmentOrderManager(),true,null);
            }
        });

        ui.layoutQLTK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.addFragment(new FragmentReportManager(),true,null);
            }
        });

        ui.imageNavLeft.setOnClickListener(v -> {
            activity.toggleNav();
        });
    }

    @Override
    public BaseUiContainer getUiContainer() {
        return new FragmentHomeView.UIContainer();
    }

    @Override
    public int getViewId() {
        return R.layout.layout_fragment_home;
    }

    public static class UIContainer extends BaseUiContainer {
        @UiElement(R.id.layoutQLSP)
        public LinearLayout layoutQLSP;
        @UiElement(R.id.layoutQLKH)
        public LinearLayout layoutQLKH;

        @UiElement(R.id.layoutQLDH)
        public LinearLayout layoutQLDH;

        @UiElement(R.id.layoutQLNV)
        public LinearLayout layoutQLNV;

        @UiElement(R.id.layoutQLCDKH)
        public LinearLayout layoutQLCDKH;

        @UiElement(R.id.layoutQLTK)
        public LinearLayout layoutQLTK;

        @UiElement(R.id.layoutQLNCU)
        public LinearLayout layoutQLNCU;

        @UiElement(R.id.imageNavLeft)
        public ImageView imageNavLeft;


    }
}
