package qtc.project.banhangnhanh.admin.event;

import b.laixuantam.myaarlibrary.helper.BusHelper;

public class AlertCheckRolePermissionEvent {

    public static void post() {
        BusHelper.post(new AlertCheckRolePermissionEvent());
    }
}
