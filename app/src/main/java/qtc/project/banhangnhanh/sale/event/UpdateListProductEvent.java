package qtc.project.banhangnhanh.sale.event;

import b.laixuantam.myaarlibrary.helper.BusHelper;

public class UpdateListProductEvent {

    public static void post() {
        BusHelper.post(new UpdateListProductEvent());
    }
}