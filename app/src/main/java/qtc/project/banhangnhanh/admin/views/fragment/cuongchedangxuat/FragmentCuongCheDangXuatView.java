package qtc.project.banhangnhanh.admin.views.fragment.cuongchedangxuat;

import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import b.laixuantam.myaarlibrary.base.BaseUiContainer;
import b.laixuantam.myaarlibrary.base.BaseView;
import b.laixuantam.myaarlibrary.widgets.roundview.RoundTextView;
import qtc.project.banhangnhanh.R;
import qtc.project.banhangnhanh.activity.HomeActivity;

public class FragmentCuongCheDangXuatView extends BaseView<FragmentCuongCheDangXuatView.UIContainer> implements FragmentCuongCheDangXuatViewInterface {
    HomeActivity activity;
    FragmentCuongCheDangXuatViewCallback callback;

    @Override
    public void init(HomeActivity activity, FragmentCuongCheDangXuatViewCallback callback) {
        this.activity = activity;
        this.callback = callback;

        ui.btnBackHeader.setOnClickListener(v -> {
            if (callback!=null)
                callback.onBackHeader();
        });

        ui.btnExitEmployee.setOnClickListener(v -> {
            LayoutInflater layoutInflater = activity.getLayoutInflater();
            View popupView = layoutInflater.inflate(R.layout.alert_dialog_waiting, null);
            TextView title_text = popupView.findViewById(R.id.title_text);
            TextView content_text = popupView.findViewById(R.id.content_text);
            Button cancel_button = popupView.findViewById(R.id.cancel_button);
            Button custom_confirm_button = popupView.findViewById(R.id.custom_confirm_button);

            title_text.setText("Cảnh báo");
            content_text.setText("Bạn có muốn cưỡng chế hết tài khoản Admin trong ứng dụng này không ?");

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
                    if (callback!=null)
                        callback.exitEmployee();
                    dialog.dismiss();
                }
            });
        });

        ui.btnExitCustomer.setOnClickListener(v -> {
            LayoutInflater layoutInflater = activity.getLayoutInflater();
            View popupView = layoutInflater.inflate(R.layout.alert_dialog_waiting, null);
            TextView title_text = popupView.findViewById(R.id.title_text);
            TextView content_text = popupView.findViewById(R.id.content_text);
            Button cancel_button = popupView.findViewById(R.id.cancel_button);
            Button custom_confirm_button = popupView.findViewById(R.id.custom_confirm_button);

            title_text.setText("Cảnh báo");
            content_text.setText("Bạn có muốn cưỡng chế hết tài khoản NV trong ứng dụng này không ?");

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
                    if (callback!=null)
                        callback.exitCustomer();
                    dialog.dismiss();
                }
            });
        });
    }


    @Override
    public BaseUiContainer getUiContainer() {
        return new FragmentCuongCheDangXuatView.UIContainer();
    }

    @Override
    public int getViewId() {
        return R.layout.layout_fragment_cuong_che_dang_xuat;
    }


    public class UIContainer extends BaseUiContainer {

        @UiElement(R.id.imageNavLeft)
        public View btnBackHeader;

        @UiElement(R.id.btnExitEmployee)
        public RoundTextView btnExitEmployee;

        @UiElement(R.id.btnExitCustomer)
        public RoundTextView btnExitCustomer;

    }
}
