package qtc.project.banhangnhanh.helper.fcm;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;
import java.util.Objects;

import b.laixuantam.myaarlibrary.helper.MyLifecycleHandler;
import b.laixuantam.myaarlibrary.helper.MyLog;
import b.laixuantam.myaarlibrary.helper.MyNotification;
import qtc.project.banhangnhanh.R;
import qtc.project.banhangnhanh.activity.HomeActivity;
import qtc.project.banhangnhanh.admin.dependency.AppProvider;
import qtc.project.banhangnhanh.admin.event.AleartSignedEvent;
import qtc.project.banhangnhanh.admin.event.AlertCheckRolePermissionEvent;
import qtc.project.banhangnhanh.admin.event.AlertCheckTaskEvent;
import qtc.project.banhangnhanh.admin.event.AlertCheckUserRolePermissionEvent;
import qtc.project.banhangnhanh.admin.event.FroceSignoutEvent;
import qtc.project.banhangnhanh.admin.event.StatusEmployeeEvent;
import qtc.project.banhangnhanh.helper.notification.NotificationUtils;
import qtc.project.banhangnhanh.helper.notification.NotificationVO;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMessagingService";
    private static final String TITLE = "title";
    private static final String EMPTY = "";
    private static final String MESSAGE = "message";
    private static final String IMAGE = "image";
    private static final String ACTION = "action";
    private static final String DATA = "data";
    private static final String ACTION_DESTINATION = "action_destination";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if (!AppProvider.getPreferences().checkLoginStatus()) {
            return;
        }
//        if (remoteMessage.getNotification() != null) {
//            MyLog.e(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
//
//            RemoteMessage.Notification notification = remoteMessage.getNotification();
//            String title = notification.getTitle();
//            String message = notification.getBody();
//
//            BaseNotificationModel baseNotificationModel = new BaseNotificationModel();
//
//            baseNotificationModel.setTitle(title);
//            baseNotificationModel.setMessage(message);
//            if (MyLifecycleHandler.isApplicationInForeground()) {
//
//                if (notification != null) {
//
//                    MyNotification.getInstance().showDefaultNotification(getApplicationContext(), title, message, R.drawable.icon_customer);
//
//                }
//
//            }
//        }

        MyLog.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            MyLog.d(TAG, "Message data payload: " + remoteMessage.getData());
            Map<String, String> data = remoteMessage.getData();
            handleData(data);


        } else if (remoteMessage.getNotification() != null) {
            MyLog.d(TAG, "Message Notification getClickAction: " + remoteMessage.getNotification().getClickAction());

            String click_action = remoteMessage.getNotification().getClickAction();
//            handleNotification(remoteMessage.getNotification());

            if (MyLifecycleHandler.isApplicationInForeground()) {
                switch (click_action) {
                    case "check_auth":
                        AleartSignedEvent.post();
                        break;

                    case "update_role_permission":
                        AlertCheckRolePermissionEvent.post();
                        break;

                    case "update_role_permission_user":
                        AlertCheckUserRolePermissionEvent.post();
                        break;

                    case "check_task":
                        AlertCheckTaskEvent.post();
                        break;
                    case "employee_status_disable":
                        StatusEmployeeEvent.post();
                        break;
                      case  "check_sign_out":
                        FroceSignoutEvent.post();
                        break;

                    default:
                        String title = remoteMessage.getNotification().getTitle();
                        String message = remoteMessage.getNotification().getBody();
                        MyNotification.getInstance().showDefaultNotification(getApplicationContext(), title, message, R.mipmap.ic_launcher);
                        break;
                }

//                if (click_action.equalsIgnoreCase("check_auth")) {
//                    AleartSignedEvent.post();
//                } else if (click_action.equalsIgnoreCase("check_notify") || click_action.equalsIgnoreCase("check_order_admin") || click_action.equalsIgnoreCase("check_order_deposit_admin")) {
//                    String title = remoteMessage.getNotification().getTitle();
//                    String message = remoteMessage.getNotification().getBody();
//                    MyNotification.getInstance().showDefaultNotification(getApplicationContext(), title, message, R.drawable.logo_nino);
//                }
//                if (click_action.equalsIgnoreCase("update_role_permission")) {
//                    AlertCheckRolePermissionEvent.post();
//                } else if (click_action.equalsIgnoreCase("update_role_permission_user")) {
//                    AlertCheckUserRolePermissionEvent.post();
//                } else if (click_action.equalsIgnoreCase("check_task")) {
//                    AlertCheckTaskEvent.post();
//                }
            }

        }// Check if message contains a notification payload.


    }

    private void handleNotification(RemoteMessage.Notification RemoteMsgNotification) {
        String message = RemoteMsgNotification.getBody();
        String title = RemoteMsgNotification.getTitle();
        NotificationVO notificationVO = new NotificationVO();
        notificationVO.setTitle(title);
        notificationVO.setMessage(message);

        Intent resultIntent = new Intent(getApplicationContext(), HomeActivity.class);
        NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
        notificationUtils.displayNotification(notificationVO, resultIntent);
        notificationUtils.playNotificationSound();
    }

    private void handleData(Map<String, String> data) {
        String title = data.get(TITLE);
        String message = data.get(MESSAGE);
        String iconUrl = data.get(IMAGE);
        String action = data.get(ACTION);
        String actionDestination = data.get(ACTION_DESTINATION);
        NotificationVO notificationVO = new NotificationVO();
        notificationVO.setTitle(title);
        notificationVO.setMessage(message);
        notificationVO.setAction(action);
//        notificationVO.setActionDestination(actionDestination);
//        notificationVO.setIconUrl(iconUrl);

        if (MyLifecycleHandler.isApplicationInForeground()) {

            MyNotification.getInstance().showDefaultNotification(getApplicationContext(), title, message, R.mipmap.ic_launcher);

        } else {

            Intent resultIntent = new Intent(getApplicationContext(), HomeActivity.class);

            if (!TextUtils.isEmpty(action) && Objects.requireNonNull(action).equalsIgnoreCase("check_order")) {
                Bundle bundle = new Bundle();
                bundle.putString("action", action);
                resultIntent.putExtras(bundle);
            }

            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.displayNotification(notificationVO, resultIntent);
            notificationUtils.playNotificationSound();
        }

    }

}
