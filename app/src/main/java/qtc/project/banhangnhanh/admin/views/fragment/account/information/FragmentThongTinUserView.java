package qtc.project.banhangnhanh.admin.views.fragment.account.information;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import b.laixuantam.myaarlibrary.base.BaseUiContainer;
import b.laixuantam.myaarlibrary.base.BaseView;
import qtc.project.banhangnhanh.R;
import qtc.project.banhangnhanh.activity.HomeActivity;
import qtc.project.banhangnhanh.admin.model.EmployeeModel;
import qtc.project.banhangnhanh.admin.dependency.AppProvider;

public class FragmentThongTinUserView extends BaseView<FragmentThongTinUserView.UIContainer> implements FragmentThongTinUserViewInterface {

    HomeActivity activity;
    FragmentThongTinUserViewCallback callback;

    @Override
    public void init(HomeActivity activity, FragmentThongTinUserViewCallback callback) {
        this.activity = activity;
        this.callback = callback;
        getDataUser();
        onClick();
    }

    private void onClick() {
        ui.imageNavLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callback!=null)
                    callback.onBackProgress();
            }
        });
    }

    private void getDataUser() {
        EmployeeModel employeeModel = AppProvider.getPreferences().getUserModel();
        ui.full_name.setText(employeeModel.getFull_name());
        ui.user_name.setText(employeeModel.getId_code());
        if (employeeModel.getLevel().equals("2")){
            ui.level.setText("Admin");
        }
        else if (employeeModel.getLevel().equals("1")){
            ui.level.setText("Nhân Viên");
        }
    }

    @Override
    public BaseUiContainer getUiContainer() {
        return new FragmentThongTinUserView.UIContainer();
    }

    @Override
    public int getViewId() {
        return R.layout.layout_fragment_thong_tin_user;
    }


    public class UIContainer extends BaseUiContainer {
        @UiElement(R.id.user_name)
        public TextView user_name;

        @UiElement(R.id.full_name)
        public TextView full_name;

        @UiElement(R.id.level)
        public TextView level;

        @UiElement(R.id.imageNavLeft)
        public ImageView imageNavLeft;


    }
}
