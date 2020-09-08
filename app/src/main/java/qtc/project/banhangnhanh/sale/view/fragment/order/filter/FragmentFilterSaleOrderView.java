package qtc.project.banhangnhanh.sale.view.fragment.order.filter;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import b.laixuantam.myaarlibrary.base.BaseUiContainer;
import b.laixuantam.myaarlibrary.base.BaseView;
import b.laixuantam.myaarlibrary.helper.KeyboardUtils;
import b.laixuantam.myaarlibrary.widgets.slidedatetimepicker.SlideDateTimeListener;
import b.laixuantam.myaarlibrary.widgets.slidedatetimepicker.SlideDateTimePicker;
import b.laixuantam.myaarlibrary.widgets.ultils.ConvertDate;
import qtc.project.banhangnhanh.R;
import qtc.project.banhangnhanh.activity.SaleHomeActivity;

public class FragmentFilterSaleOrderView extends BaseView<FragmentFilterSaleOrderView.UIContainer> implements FragmentFilterSaleOrderViewInterface {
    SaleHomeActivity activity;
    FragmentFilterSaleOrderViewCallback callback;

    DatePickerDialog picker;
    TimePickerDialog timePicker;
    String date = "";
    String time = "";
    private String daySelected;

    @Override
    public void init(SaleHomeActivity activity, FragmentFilterSaleOrderViewCallback callback) {
        this.activity = activity;
        this.callback = callback;

        KeyboardUtils.setupUI(getView(), activity);
        ui.title_header.setText("Filter");
        ui.imageNavLeft.setOnClickListener(v -> {
            if (callback != null)
                callback.goBackHeader();
        });

        onClick();
    }

    private void onClick() {
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);


        ui.imageCalendar.setOnClickListener(v -> {
            isSelectDate = true;
            new SlideDateTimePicker.Builder(activity.getSupportFragmentManager())
                    .setListener(listener)
                    .setInitialDate(new Date())
                    .setTypeShowDialog(1)
                    .build()
                    .show();
        });
        ui.hourFilter.setOnClickListener(v -> {
            isSelectDate = false;
            new SlideDateTimePicker.Builder(activity.getSupportFragmentManager())
                    .setListener(listener)
                    .setInitialDate(new Date())
                    .setIs24HourTime(true)
                    .setTypeShowDialog(2)
                    .build()
                    .show();
        });

        ui.btnSearch.setOnClickListener(view -> {
            if (callback != null) {
                callback.filterData(scheduleDateSelected, scheduleTimeSelected,
                        ui.idOrderFilter.getText().toString(), ui.maKhachHang.getText().toString());
            }
        });


        ui.btnExit.setOnClickListener(v -> {
            if (callback != null)
                callback.goBackHeader();
        });
    }

    private String scheduleDateSelected;
    private String scheduleTimeSelected;
    private boolean isSelectDate;
    private SlideDateTimeListener listener = new SlideDateTimeListener() {

        @Override
        public void onDateTimeSet(Date date) {

            final Calendar now = Calendar.getInstance();
            long timeToday = now.getTimeInMillis();
            long different = date.getTime() - timeToday;

            String dateToday = ConvertDate.getDateFromTimestamp(timeToday);

            if (isSelectDate) {
                DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                DateFormat sdfTimeServer = new SimpleDateFormat("yyyy-MM-dd");
                String niceFormatDate = sdf.format(date);

                if (dateToday.equalsIgnoreCase(niceFormatDate)) {
                    scheduleDateSelected = sdfTimeServer.format(date);
                    ui.calendarFilter.setText(niceFormatDate);
                }
//                else {
//                    if (different < 0) {
//                        Toast.makeText(getContext(), "Ngày hẹn phải lớn hơn ngày hiện tại.", Toast.LENGTH_LONG).show();
//                        return;
//                    }else{
//                        scheduleDateSelected = sdfTimeServer.format(date);
//                        ui.calendarFilter.setText(niceFormatDate);
//                    }
//                }

            } else {

                String niceFormatDate = ConvertDate.changeToNiceFormatDate(scheduleDateSelected);

//                if (dateToday.equalsIgnoreCase(niceFormatDate)) {
//                    if (different < 0) {
//                        Toast.makeText(getContext(), "Giờ hẹn phải lớn hơn giờ hiện tại.", Toast.LENGTH_LONG).show();
//                        return;
//                    }
//                }
                DateFormat timeFormat = new SimpleDateFormat("HH:mm");
                scheduleTimeSelected = timeFormat.format(date);
                ui.hourFilter.setText(scheduleTimeSelected);
            }
        }

        // Optional cancel listener
        @Override
        public void onDateTimeCancel() {
        }
    };

    @Override
    public BaseUiContainer getUiContainer() {
        return new FragmentFilterSaleOrderView.UIContainer();
    }

    @Override
    public int getViewId() {
        return R.layout.layout_fragment_order_sale_filter;
    }


    public class UIContainer extends BaseUiContainer {
        @UiElement(R.id.imageNavLeft)
        public ImageView imageNavLeft;

        @UiElement(R.id.title_header)
        public TextView title_header;

        @UiElement(R.id.imvHome)
        public ImageView imvHome;

        @UiElement(R.id.calendarFilter)
        public TextView calendarFilter;

        @UiElement(R.id.imageCalendar)
        public ImageView imageCalendar;

        @UiElement(R.id.hourFilter)
        public TextView hourFilter;

        @UiElement(R.id.idOrderFilter)
        public EditText idOrderFilter;

        @UiElement(R.id.btnExit)
        public LinearLayout btnExit;

        @UiElement(R.id.btnSearch)
        public LinearLayout btnSearch;

        @UiElement(R.id.maKhachHang)
        public EditText maKhachHang;

    }
}
