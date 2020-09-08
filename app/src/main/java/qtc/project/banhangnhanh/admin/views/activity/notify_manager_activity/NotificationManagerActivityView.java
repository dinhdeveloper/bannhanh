package qtc.project.banhangnhanh.admin.views.activity.notify_manager_activity;

import android.widget.FrameLayout;
import android.widget.TextView;

import b.laixuantam.myaarlibrary.base.BaseUiContainer;
import b.laixuantam.myaarlibrary.base.BaseView;
import qtc.project.banhangnhanh.R;

public class NotificationManagerActivityView extends BaseView<NotificationManagerActivityView.UIContainer> implements NotificationManagerActivityViewInterface {

    @Override
    public void init(NotificationManagerActivityViewCallback callback) {
    }

    @Override
    public void setTitleHeader(String title) {
        ui.title_header.setText(title);
    }

    @Override
    public BaseUiContainer getUiContainer() {
        return new NotificationManagerActivityView.UIContainer();
    }

    @Override
    public int getViewId() {
        return R.layout.activity_notifycation_manager;
    }

    public static class UIContainer extends BaseUiContainer {

        @UiElement(R.id.title_header)
        public TextView title_header;

        @UiElement(R.id.LayoutBaseMainFragmentActivity)
        public FrameLayout LayoutBaseMainFragmentActivity;
    }
}
