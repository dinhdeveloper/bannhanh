package qtc.project.banhangnhanh.admin.views.fragment.levelcustomer.create;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import b.laixuantam.myaarlibrary.base.BaseUiContainer;
import b.laixuantam.myaarlibrary.base.BaseView;
import b.laixuantam.myaarlibrary.widgets.popupmenu.ActionItem;
import b.laixuantam.myaarlibrary.widgets.popupmenu.MyCustomPopupMenu;
import qtc.project.banhangnhanh.R;
import qtc.project.banhangnhanh.activity.HomeActivity;
import qtc.project.banhangnhanh.admin.model.LevelCustomerModel;
import qtc.project.banhangnhanh.admin.dependency.AppProvider;

public class FragmentCreateLevelCustomerView extends BaseView<FragmentCreateLevelCustomerView.UIContainer> implements FragmentCreateLevelCustomerViewInterface {

    HomeActivity activity;
    FragmentCreateLevelCustomerViewCallback callback;

    String image_level;

    @Override
    public void init(HomeActivity activity, FragmentCreateLevelCustomerViewCallback callback) {
        this.activity = activity;
        this.callback = callback;

        onClick();
    }

    @Override
    public void setDataProductImage(String filePath) {
        image_level = filePath;
        AppProvider.getImageHelper().displayImage(filePath, ui.image_level, null, R.drawable.imageloading, false);
    }

    private void onClick() {
        ui.imageNavLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (callback != null) {
                    callback.onBackProgress();
                }
            }
        });

        ui.choose_file_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(view);
            }
        });

        ui.layout_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LevelCustomerModel levelCustomerModel = new LevelCustomerModel();
                levelCustomerModel.setId_code(ui.id_level.getText().toString());
                levelCustomerModel.setName(ui.name_level_customer.getText().toString());
                levelCustomerModel.setDiscount(ui.discount_level.getText().toString());
                levelCustomerModel.setDescription(ui.description_level.getText().toString());
                levelCustomerModel.setImage(image_level);

                if (callback!=null)
                    callback.createLevelCustomer(levelCustomerModel);
            }
        });
    }

    private void showPopupMenu(View view) {
        ActionItem change_password = new ActionItem(1, "Chọn ảnh từ camera", null);
        ActionItem employee_tracking = new ActionItem(2, "Chọn hình từ thư viện", null);
//        int backgroundCustom = ContextCompat.getColor(getContext(), R.color.red);
//        int arrowColorCustom = ContextCompat.getColor(getContext(), R.color.red);

        MyCustomPopupMenu quickAction = new MyCustomPopupMenu(getContext()/*, backgroundCustom, arrowColorCustom*/);
        quickAction.addActionItem(change_password);
        quickAction.addActionItem(employee_tracking);

        quickAction.setOnActionItemClickListener(new MyCustomPopupMenu.OnActionItemClickListener() {
            @Override
            public void onItemClick(MyCustomPopupMenu source, int pos, int actionId) {
                switch (actionId) {
                    case 1:
                        if (callback != null)
                            callback.onClickOptionSelectImageFromCamera();
                        break;

                    case 2:
                        if (callback != null)
                            callback.onClickOptionSelectImageFromGallery();
                        break;
                }
            }
        });

        quickAction.show(view);
    }

    @Override
    public BaseUiContainer getUiContainer() {
        return new FragmentCreateLevelCustomerView.UIContainer();
    }

    @Override
    public int getViewId() {
        return R.layout.layout_fragment_create_level_customer;
    }

    public static class UIContainer extends BaseUiContainer {
        @UiElement(R.id.choose_file_image)
        public LinearLayout choose_file_image;

        @UiElement(R.id.image_level)
        public ImageView image_level;

        @UiElement(R.id.id_level)
        public EditText id_level;

        @UiElement(R.id.name_level_customer)
        public EditText name_level_customer;

        @UiElement(R.id.discount_level)
        public EditText discount_level;

        @UiElement(R.id.description_level)
        public EditText description_level;

        @UiElement(R.id.layout_create)
        public LinearLayout layout_create;

        @UiElement(R.id.imageNavLeft)
        public ImageView imageNavLeft;


    }
}
