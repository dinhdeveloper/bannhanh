package qtc.project.banhangnhanh.admin.event;

import b.laixuantam.myaarlibrary.helper.BusHelper;

public class UpdateServiceItemEvent {
    public static void post()
    {
        BusHelper.post(new UpdateServiceItemEvent());
    }
}
