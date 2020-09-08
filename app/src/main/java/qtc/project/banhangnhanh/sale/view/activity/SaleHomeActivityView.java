package qtc.project.banhangnhanh.sale.view.activity;

import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import b.laixuantam.myaarlibrary.base.BaseUiContainer;
import b.laixuantam.myaarlibrary.base.BaseView;
import b.laixuantam.myaarlibrary.helper.AppUtils;
import qtc.project.banhangnhanh.R;
import qtc.project.banhangnhanh.activity.SaleHomeActivity;
import qtc.project.banhangnhanh.sale.fragment.customer.FragmentCustomerSale;
import qtc.project.banhangnhanh.sale.fragment.home.FragmentSaleHome;
import qtc.project.banhangnhanh.sale.fragment.level.FragmentLevelSaleCustomer;
import qtc.project.banhangnhanh.sale.fragment.order.FragmentOrderSale;
import qtc.project.banhangnhanh.sale.fragment.product.FragmentProductSale;

public class SaleHomeActivityView  extends BaseView<SaleHomeActivityView.UIContainer> implements SaleHomeActivityViewInterface {

    SaleHomeActivity activity;
    SaleHomeActivityViewCallback callback;
    Fragment fragment;


    @Override
    public void init(SaleHomeActivity activity, SaleHomeActivityViewCallback callback) {
        this.callback = callback;
        this.activity = activity;

        addEventDragLayout();
        addEventsHeaderNavigationLeft();
    }

    @Override
    public void toggleNav() {
        if (isDrawerOpen()){
            closeDrawer();
        }else{
            openDrawer();
        }
    }

    @Override
    public void openDrawer() {
        AppUtils.hideKeyBoard(getView());
        ui.drawer_layout.openDrawer(GravityCompat.START);
    }

    @Override
    public void closeDrawer() {
        if (isDrawerOpen()) {
            ui.drawer_layout.closeDrawer(GravityCompat.END);
        }
    }

    @Override
    public boolean isDrawerOpen() {
        return ui.drawer_layout.isDrawerOpen(GravityCompat.START);
    }


    private void addEventDragLayout() {
        ui.imageNavLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ui.drawer_layout.openDrawer(GravityCompat.START);
            }
        });
    }

    private void addEventsHeaderNavigationLeft() {
        View headerview = ui.nav_view.getHeaderView(0);
        LinearLayout layoutHome = headerview.findViewById(R.id.layoutHome);
        LinearLayout layoutHistory = headerview.findViewById(R.id.layoutHistory);
        LinearLayout layoutProduct = headerview.findViewById(R.id.layoutProduct);
        LinearLayout layoutCustomer = headerview.findViewById(R.id.layoutCustomer);
        LinearLayout layoutLevelCus = headerview.findViewById(R.id.layoutLevelCus);
        LinearLayout layoutLogout = headerview.findViewById(R.id.layoutLogout);

        layoutHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onClickItemNav(new FragmentSaleHome());
                ui.drawer_layout.closeDrawer(GravityCompat.START);
            }
        });
        layoutHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onClickItemNav(new FragmentOrderSale());
                ui.drawer_layout.closeDrawer(GravityCompat.START);
            }
        });
        layoutProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onClickItemNav(new FragmentProductSale());
                ui.drawer_layout.closeDrawer(GravityCompat.START);
            }
        });
        layoutCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onClickItemNav(new FragmentCustomerSale());
                ui.drawer_layout.closeDrawer(GravityCompat.START);
            }
        });
//
        layoutLevelCus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onClickItemNav(new FragmentLevelSaleCustomer());
                ui.drawer_layout.closeDrawer(GravityCompat.START);
            }
        });

        layoutLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater layoutInflater = activity.getLayoutInflater();
                View popupView = layoutInflater.inflate(R.layout.sale_alert_dialog_dang_xuat, null);
                TextView title_text = popupView.findViewById(R.id.title_text);
                TextView content_text = popupView.findViewById(R.id.content_text);
                Button cancel_button = popupView.findViewById(R.id.cancel_button);
                Button custom_confirm_button = popupView.findViewById(R.id.custom_confirm_button);

                title_text.setText("Cảnh báo");
                content_text.setText("Bạn có muốn đăng xuất tài khoản này không?");

                AlertDialog.Builder alert = new AlertDialog.Builder(activity);
                alert.setView(popupView);
                AlertDialog dialog = alert.create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();


                cancel_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                custom_confirm_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (callback!=null){
                            callback.logOut();
                        }
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    @Override
    public BaseUiContainer getUiContainer() {
        return new SaleHomeActivityView.UIContainer();
    }

    @Override
    public int getViewId() {
        return R.layout.activity_home_sale;
    }

    public static class UIContainer extends BaseUiContainer {
        @UiElement(R.id.imageNavLeft)
        public ImageView imageNavLeft;

        @UiElement(R.id.drawer_layout)
        public DrawerLayout drawer_layout;

        @UiElement(R.id.nav_view)
        public NavigationView nav_view;

    }
}
