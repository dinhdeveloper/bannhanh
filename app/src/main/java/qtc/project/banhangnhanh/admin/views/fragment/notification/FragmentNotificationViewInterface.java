package qtc.project.banhangnhanh.admin.views.fragment.notification;


import b.laixuantam.myaarlibrary.base.BaseViewInterface;
import qtc.project.banhangnhanh.activity.SaleHomeActivity;
import qtc.project.banhangnhanh.admin.model.NotificationModel;
import qtc.project.banhangnhanh.admin.views.fragment.notification.FragmentNotificationViewCallback;

public interface FragmentNotificationViewInterface extends BaseViewInterface {

    void init(SaleHomeActivity activity, FragmentNotificationViewCallback callback);

    void setDataNotification(NotificationModel[] data);

    void setNoMoreLoading();

    void validateCheckSeenNotifySuccess();

    void hideRootView();

    void showRootView();
}
