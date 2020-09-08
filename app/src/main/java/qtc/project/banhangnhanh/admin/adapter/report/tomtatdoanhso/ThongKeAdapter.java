package qtc.project.banhangnhanh.admin.adapter.report.tomtatdoanhso;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

import b.laixuantam.myaarlibrary.widgets.superadapter.SuperAdapter;
import b.laixuantam.myaarlibrary.widgets.superadapter.SuperViewHolder;
import qtc.project.banhangnhanh.R;
import qtc.project.banhangnhanh.admin.model.DataChartModel;

public class ThongKeAdapter extends SuperAdapter<DataChartModel> {


    public ThongKeAdapter(Context context, List<DataChartModel> items) {
        super(context, items, R.layout.custom_item_recycleview_thong_ke);
    }

    @Override
    public void onBind(SuperViewHolder holder, int viewType, int layoutPosition, DataChartModel item) {
        TextView text_title = holder.findViewById(R.id.text_title);
        TextView text_value = holder.findViewById(R.id.text_value);

        try {
            if (item != null) {
                String pattern = "###,###,###";
                DecimalFormat decimalFormat = new DecimalFormat(pattern);

                text_title.setText(item.getTitle());
                text_value.setText(decimalFormat.format(Long.valueOf(item.getValue())));
           }
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }
    }
}
