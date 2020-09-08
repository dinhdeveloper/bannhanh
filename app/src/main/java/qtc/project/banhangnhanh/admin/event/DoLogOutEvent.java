package qtc.project.banhangnhanh.admin.event;

import b.laixuantam.myaarlibrary.helper.BusHelper;

public class DoLogOutEvent {

    public static void post() {
        BusHelper.post(new DoLogOutEvent());
    }
}
