package qtc.project.banhangnhanh.admin.event;

import b.laixuantam.myaarlibrary.helper.BusHelper;

public class ConnectedBTEvent {

    public static void post() {
        BusHelper.post(new ConnectedBTEvent());
    }
}
