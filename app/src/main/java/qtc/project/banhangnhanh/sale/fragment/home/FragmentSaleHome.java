package qtc.project.banhangnhanh.sale.fragment.home;

import android.Manifest;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import b.laixuantam.myaarlibrary.api.ApiRequest;
import b.laixuantam.myaarlibrary.api.ErrorApiResponse;
import b.laixuantam.myaarlibrary.base.BaseFragment;
import b.laixuantam.myaarlibrary.base.BaseParameters;
import b.laixuantam.myaarlibrary.helper.AccentRemove;
import b.laixuantam.myaarlibrary.helper.CurrencyFormater;
import b.laixuantam.myaarlibrary.widgets.dialog.alert.KAlertDialog;
import b.laixuantam.myaarlibrary.widgets.ultils.ConvertDate;
import qtc.project.banhangnhanh.activity.HomeActivity;
import qtc.project.banhangnhanh.activity.LoginActivity;
import qtc.project.banhangnhanh.activity.SaleHomeActivity;
import qtc.project.banhangnhanh.admin.dependency.AppProvider;
import qtc.project.banhangnhanh.admin.event.ConnectedBTEvent;
import qtc.project.banhangnhanh.admin.event.DoLogOutEvent;
import qtc.project.banhangnhanh.admin.model.BaseResponseModel;
import qtc.project.banhangnhanh.admin.model.CustomerModel;
import qtc.project.banhangnhanh.admin.model.EmployeeModel;
import qtc.project.banhangnhanh.helper.Consts;
import qtc.project.banhangnhanh.sale.api.CreateOrderRequest;
import qtc.project.banhangnhanh.sale.api.ProductRequest;
import qtc.project.banhangnhanh.sale.bill.BTDeviceList;
import qtc.project.banhangnhanh.sale.bill.PrinterCommands;
import qtc.project.banhangnhanh.sale.bill.Utils;
import qtc.project.banhangnhanh.sale.database.DatabaseProvider;
import qtc.project.banhangnhanh.sale.event.BackShowRootViewEvent;
import qtc.project.banhangnhanh.sale.fragment.customer.FragmentCustomerSaleFilter;
import qtc.project.banhangnhanh.sale.model.ListOrderModel;
import qtc.project.banhangnhanh.sale.model.OrderModel;
import qtc.project.banhangnhanh.sale.model.ProductModel;
import qtc.project.banhangnhanh.sale.view.fragment.home.FragmentSaleHomeView;
import qtc.project.banhangnhanh.sale.view.fragment.home.FragmentSaleHomeViewCallback;
import qtc.project.banhangnhanh.sale.view.fragment.home.FragmentSaleHomeViewInterface;

import static qtc.project.banhangnhanh.helper.Consts.decimalFormat;

public class FragmentSaleHome extends BaseFragment<FragmentSaleHomeViewInterface, BaseParameters> implements FragmentSaleHomeViewCallback {
    SaleHomeActivity activity;
    EmployeeModel model;
    public static final int REQUEST_PHONE_CALL = 101;

    @Override
    protected void initialize() {
        activity = (SaleHomeActivity) getActivity();
        view.init(activity, this);
        model = AppProvider.getPreferences().getUserModel();
    }

    @Override
    public void onStart() {
        super.onStart();
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String date1 = year + "-" + month + "-" + day;
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        // Định nghĩa 2 mốc thời gian ban đầu
        Date dateCurrrent = Date.valueOf(date1);
        Date dateEnd = Date.valueOf(model.getStore_end());
        cal1.setTime(dateCurrrent);
        cal2.setTime(dateEnd);
        if (dateCurrrent.compareTo(dateEnd) > 0) {
            String message = "Tài khoản của bạn đã hết hạn vui lòng liên hệ với nhà cung cấp để gia hạn hợp đồng";
            activity.showConfirmAlert("Liên hệ", message, "Liên hệ", null, new KAlertDialog.KAlertClickListener() {
                @Override
                public void onClick(KAlertDialog kAlertDialog) {
                    kAlertDialog.dismiss();
                    doLogOut();
                }
            }, null, -1);
        }
    }

    private void doLogOut() {
        EmployeeModel employeeModel = AppProvider.getPreferences().getUserModel();
        if (employeeModel != null) {
            if (employeeModel.getLevel().equalsIgnoreCase("2")) {
                FirebaseMessaging.getInstance().unsubscribeFromTopic("pos_notifycation_app_admin");
                FirebaseMessaging.getInstance().unsubscribeFromTopic("pos_notifycation_app_admin_" + employeeModel.getId_business());
            } else {
                FirebaseMessaging.getInstance().unsubscribeFromTopic("pos_notifycation_employee_" + employeeModel.getId() + "_" + employeeModel.getId_business());
                FirebaseMessaging.getInstance().unsubscribeFromTopic("pos_notifycation_employee_" + employeeModel.getId_business());
            }
        }
        AppProvider.getPreferences().clear();
        finish();
        startActivity(new Intent(activity, LoginActivity.class));
        DoLogOutEvent.post();
    }

    @Override
    protected FragmentSaleHomeViewInterface getViewInstance() {
        return new FragmentSaleHomeView();
    }

    @Override
    protected BaseParameters getParametersContainer() {
        return null;
    }

    @Override
    public void goToChooseCustomer() {
        if (activity != null) {
            activity.changToFragmentCustomerSaleFilter();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    view.hideRootView();
                }
            }, 500);
        }
    }

    @Override
    public void callDataSearchProduct(String search) {
        ProductRequest.ApiParams params = new ProductRequest.ApiParams();
        params.id_business = AppProvider.getPreferences().getUserModel().getId_business();
        if (search != null) {
            params.product = search;
        }
        showProgress();
        AppProvider.getApiManagement().call(ProductRequest.class, params, new ApiRequest.ApiCallback<BaseResponseModel<ProductModel>>() {
            @Override
            public void onSuccess(BaseResponseModel<ProductModel> body) {
                dismissProgress();
                if (body != null) {
                    ArrayList<ProductModel> list = new ArrayList<>();
                    list.addAll(Arrays.asList(body.getData()));
                    view.initViewProduct(list);
                }
            }

            @Override
            public void onError(ErrorApiResponse error) {
                dismissProgress();
                Log.e("onError", error.message);
            }

            @Override
            public void onFail(ApiRequest.RequestError error) {
                dismissProgress();
                Log.e("onFail", error.name());
            }
        });
    }

    @Override
    public void tinhTien(ArrayList<ListOrderModel> arList, String id_customer, String tongtien, String discount, long tiengiamtructiep) {
        showProgress();
        CreateOrderRequest.ApiParams params = new CreateOrderRequest.ApiParams();
        params.id_business = AppProvider.getPreferences().getUserModel().getId_business();
        StringBuilder id_product_pack = new StringBuilder();
        StringBuilder quantity_product_pack = new StringBuilder();
        StringBuilder price_product_pack = new StringBuilder();

        for (ListOrderModel orderModel : arList) {
            params.employee_id = AppProvider.getPreferences().getUserModel().getId();
            if (!TextUtils.isEmpty(id_customer)) {
                params.id_customer = id_customer;
            }
            id_product_pack.append(orderModel.getId() + ",");
            price_product_pack.append(orderModel.getPriceProduct() + ",");
            quantity_product_pack.append(orderModel.getQuantityProduct() + "|");
            // int tongtien = Integer.parseInt(orderModel.getPriceProduct()) * Integer.parseInt(orderModel.getQuantityProduct());
            params.total = tongtien;
        }
        params.id_product_pack = String.valueOf(id_product_pack);
        params.price_product_pack = String.valueOf(price_product_pack);
        params.quantity_product_pack = String.valueOf(quantity_product_pack);
        params.direct_discount = String.valueOf(tiengiamtructiep);

        AppProvider.getApiManagement().call(CreateOrderRequest.class, params, new ApiRequest.ApiCallback<BaseResponseModel<OrderModel>>() {
            @Override
            public void onSuccess(BaseResponseModel<OrderModel> body) {
                dismissProgress();
                if (!TextUtils.isEmpty(body.getSuccess()) && body.getSuccess().equalsIgnoreCase("true")) {
                    DatabaseProvider provider = new DatabaseProvider(activity);
                    provider.deleteDatabase();
                    //view.showBill(list);
                    ArrayList<OrderModel> listOrderModels = new ArrayList<>();
                    listOrderModels.addAll(Arrays.asList(body.getData()));
                    //view.truyenIntent(arList, listOrderModels);

                    String title = "In hóa đơn";
                    String message = "Bạn có muốn in hóa đơn không?";

                    activity.showConfirmAlert(title, message, "Đồng ý", "Từ chối", new KAlertDialog.KAlertClickListener() {
                        @Override
                        public void onClick(KAlertDialog kAlertDialog) {
                            kAlertDialog.dismiss();
                            view.truyenIntent(arList, listOrderModels,tiengiamtructiep);
                        }
                    }, new KAlertDialog.KAlertClickListener() {
                        @Override
                        public void onClick(KAlertDialog kAlertDialog) {
                            kAlertDialog.dismiss();
                            activity.replaceFragment(new FragmentSaleHome(), false);
                        }
                    }, KAlertDialog.WARNING_TYPE, KAlertDialog.WARNING_TYPE);
//                    activity.showConfirmAlert(title, message, new KAlertDialog.KAlertClickListener() {
//                        @Override
//                        public void onClick(KAlertDialog kAlertDialog) {
//                            //confirm
//                            kAlertDialog.dismiss();
//                            //request active or lock account
//                            view.truyenIntent(arList, listOrderModels);
//                        }
//                    }, new KAlertDialog.KAlertClickListener() {
//                        @Override
//                        public void onClick(KAlertDialog kAlertDialog) {
//                            //cancel
//                            activity.replaceFragment(new FragmentSaleHome(), false);
//                            kAlertDialog.dismiss();
//                        }
//                    }, KAlertDialog.WARNING_TYPE);

                } else if (body.getSuccess().equals("false")) {
                    Toast.makeText(getContext(), "" + body.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(ErrorApiResponse error) {
                dismissProgress();
                Log.e("onError", error.message);
            }

            @Override
            public void onFail(ApiRequest.RequestError error) {
                dismissProgress();
                Log.e("onFail", error.name());
            }
        });
    }

    @Override
    public void goToProductList(String search) {
        if (activity != null) {
            activity.changToFragmentProductSaleHome(search);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    view.hideRootView();
                }
            }, 500);
        }
    }

    @Override
    public void callNav() {
        if (activity != null)
            activity.toggleNav();
    }

    byte FONT_TYPE;
    public static BluetoothSocket btsocket;
    public static OutputStream outputStream;

    @Override
    public void connectBlutooth() {
        //printBill();
    }

    ArrayList<ListOrderModel> listOrderInbill;
    String tongtienOrderInbill;
    long tienkhachduaOrderInbill;
    long tienthoilaiOrderInbill;
    ArrayList<OrderModel> listHoanTatOrderInbill;
    String id_customerOrderInbill;
    long tiengiamtructiepInbill;

    @Override
    public void inBill(ArrayList<ListOrderModel> list, String tongtien, long tienkhachdua, long tienthoilai, ArrayList<OrderModel> listHoanTat, String id_customer,long tiengiamtructiep) {
        this.listOrderInbill = list;
        this.tongtienOrderInbill = tongtien;
        this.tienkhachduaOrderInbill = tienkhachdua;
        this.tienthoilaiOrderInbill = tienthoilai;
        this.listHoanTatOrderInbill = listHoanTat;
        this.id_customerOrderInbill = id_customer;
        this.tiengiamtructiepInbill = tiengiamtructiep;

        if (btsocket == null) {
            Intent BTIntent = new Intent(activity, BTDeviceList.class);
            this.startActivityForResult(BTIntent, BTDeviceList.REQUEST_CONNECT_BT);
        } else {
            doInBill(list, tongtien, tienkhachdua, tienthoilai, listHoanTat, id_customer,tiengiamtructiep);
        }
    }

    private void doInBill(ArrayList<ListOrderModel> list, String tongtien, long tienkhachdua, long tienthoilai, ArrayList<OrderModel> listHoanTat, String id_customer, long tiengiamtructiep) {
        OutputStream opstream = null;
        try {
            opstream = btsocket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        outputStream = opstream;

        if (outputStream != null) {
            activity.replaceFragment(new FragmentSaleHome(), false);
            // view.dismissDialog();
        }
        //print command
        try {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            outputStream = btsocket.getOutputStream();
            byte[] printformat = new byte[]{0x1B, 0x21, 0x03};
            outputStream.write(printformat);
            //printPhoto(R.drawable.company);
            printNewLine();
            String store = null;
            if (!TextUtils.isEmpty(model.getStore_name())) {
                store = AccentRemove.removeAccent(model.getStore_name());
            }else {
                store = "QTC TEK";
            }
            printCustom(store, 3, 1);
            printNewLine();
            printCustom(AccentRemove.removeAccent(model.getStore_address()), 0, 1);
            printCustom("Hot Line: " + model.getStore_phone(), 0, 1);
            printCustom("-~-~-~-~-~-~-~-~-~-~-", 0, 1);
            printNewLine();
            printCustom("PHIEU THANH TOAN", 1, 1);
            printNewLine();
            printCustom("-~-~-~-~-~-~-~-~-~-~-", 0, 1);
            printNewLine();
            String dateTime[] = getDateTime();
            Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);

            // Trả về giá trị từ 0 - 11
            int month = c.get(Calendar.MONTH) + 1;
            int day = c.get(Calendar.DAY_OF_MONTH);

            //2021-01-08 11:48:18
            String date_create = year+"-"+month+"-"+day+" "+dateTime[1];
            if (!TextUtils.isEmpty(date_create)) {
                String[] arrDateTimeCreated = date_create.split(" ");
                String dateCreated = null;
                if (arrDateTimeCreated != null && arrDateTimeCreated.length > 0) {
                    dateCreated = ConvertDate.changeToNiceFormatDate(arrDateTimeCreated[0]);
                    printCustom(dateCreated + " " + arrDateTimeCreated[1], 1, 0);
                }
            }

            printCustom("Ma Don Hang :" + listHoanTat.get(0).getOrder_id_code(), 1, 0);
            printCustom("Nhan Vien: " + AccentRemove.removeAccent(AppProvider.getPreferences().getUserModel().getFull_name()), 1, 0);
            if (id_customer != null) {
                printCustom("Khach Hang: " + AccentRemove.removeAccent(id_customer), 1, 0);
            }
            printCustom("-----------------------", 0, 1);
            String pattern = "###,###,###";
            DecimalFormat decimalFormat = new DecimalFormat(pattern);

            printNewLine();

            long temp = 0;
            long thanhtien = 0;
            for (int i = 0; i < list.size(); i++) {
//                    Log.e("AAAA", list.get(i).getNameProduct() + "- position: " + list.get(i).getPosition_item());
                String name = AccentRemove.removeAccent(list.get(i).getNameProduct());
                String price = decimalFormat.format(Long.valueOf(list.get(i).getPriceProduct()));
                String quantity = list.get(i).getQuantityProduct();
                temp = (long) (Long.valueOf(list.get(i).getPriceProduct()) * Double.valueOf(list.get(i).getQuantityProduct()));
                thanhtien += temp;

                printCustom((i + 1) + ". " + name, 0, 0);
                printNewLine();
                printCustom("  " + price + " x " + quantity + " = " + Consts.decimalFormat.format(Long.valueOf(temp)), 0, 2);
                //printCustom("-----------", 0, 2);
//                    printCustom(Consts.decimalFormat.format(Long.valueOf(temp)), 0, 2);
                printNewLine();
            }
            long phantram_giamgia = 0;
            if (!TextUtils.isEmpty(listHoanTat.get(0).getCustomer_level_discount())) {
                phantram_giamgia = Long.valueOf(listHoanTat.get(0).getCustomer_level_discount());
            }
            long tien_giam = ((phantram_giamgia * thanhtien) / 100);

            printCustom("\nTong Cong: " + decimalFormat.format(thanhtien), 1, 0);
            printNewLine();
            printCustom("Giam Gia: " + decimalFormat.format(tien_giam), 1, 0);
            printNewLine();
            printCustom("Giam Truc Tiep: " + decimalFormat.format(tiengiamtructiep), 1, 0);
            printNewLine();
            printCustom("-----------------------", 0, 1);
            printNewLine();
            printCustom("Thanh Toan: " + decimalFormat.format(Long.valueOf(tongtien)), 1, 0);
            printNewLine();
            printCustom("Tien Mat: " + decimalFormat.format(Long.valueOf(tienkhachdua)), 1, 0);
            printNewLine();
            printCustom("Tien Thua: " + decimalFormat.format(Long.valueOf(tienthoilai)), 1, 0);
//            printNewLine();
//            printCustom("Phi Giao Hang:....................VND", 1, 0);
//            printNewLine();

            printCustom("-----------------------", 0, 1);
            printNewLine();

            printCustom("Xin Cam On Quy Khach", 0, 1);
            printCustom("Hen Gap Lai", 0, 1);
            printNewLine();
            printCustom("-----------------------", 0, 1);
            printNewLine();
            printCustom("Copyright @ 2020 QTCTEK", 0, 1);
            printNewLine();
            printCustom("www.qtctek.com", 0, 1);
            printCustom("", 0, 1);
            printCustom("", 0, 1);
            printCustom("", 0, 1);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void setDataCustomer(CustomerModel model) {
        if (model != null) {
            view.setDataCustomer(model);
        }
    }

    public void setDataProduct(ProductModel model) {
        if (model != null)
            view.initViewProductClick(model);
    }

    //print photo
    public void printPhoto(int img) {
        try {
            Bitmap bmp = BitmapFactory.decodeResource(getResources(),
                    img);
            if (bmp != null) {
                byte[] command = Utils.decodeBitmap(bmp);
                outputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
                printText(command);
            } else {
                Log.e("Print Photo error", "the file isn't exists");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("PrintTools", "the file isn't exists");
        }
    }

    //print byte[]
    private void printText(byte[] msg) {
        try {
            // Print normal text
            outputStream.write(msg);
            printNewLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //print custom
    private void printCustom(String msg, int size, int align) {
        //Print config "mode"
        byte[] cc = new byte[]{0x1B, 0x21, 0x03};  // 0- normal size text
        //byte[] cc1 = new byte[]{0x1B,0x21,0x00};  // 0- normal size text
        byte[] bb = new byte[]{0x1B, 0x21, 0x08};  // 1- only bold text
        byte[] bb2 = new byte[]{0x1B, 0x21, 0x20}; // 2- bold with medium text
        byte[] bb3 = new byte[]{0x1B, 0x21, 0x10}; // 3- bold with large text
        try {
            switch (size) {
                case 0:
                    outputStream.write(cc);
                    break;
                case 1:
                    outputStream.write(bb);
                    break;
                case 2:
                    outputStream.write(bb2);
                    break;
                case 3:
                    outputStream.write(bb3);
                    break;
            }

            switch (align) {
                case 0:
                    //left align
                    outputStream.write(PrinterCommands.ESC_ALIGN_LEFT);
                    break;
                case 1:
                    //center align
                    outputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
                    break;
                case 2:
                    //right align
                    outputStream.write(PrinterCommands.ESC_ALIGN_RIGHT);
                    break;
                case 3:
                    outputStream.write(PrinterCommands.ESC_HORIZONTAL_CENTERS);
                    break;
            }
            outputStream.write(msg.getBytes());
            outputStream.write(PrinterCommands.LF);
            //outputStream.write(cc);
            //printNewLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //print new line
    private void printNewLine() {
        try {
            outputStream.write(PrinterCommands.FEED_LINE);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private String[] getDateTime() {
        final Calendar c = Calendar.getInstance();
        String dateTime[] = new String[2];
        dateTime[0] = c.get(Calendar.DAY_OF_MONTH) + "/" + c.get(Calendar.MONTH) + "/" + c.get(Calendar.YEAR);
        dateTime[1] = c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE);
        return dateTime;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            if (btsocket != null) {
                outputStream.close();
                btsocket.close();
                btsocket = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        Toast.makeText(activity, "ConnectedDeviceBT1", Toast.LENGTH_SHORT).show();
//        try {
//            btsocket = BTDeviceList.getSocket();
//            if (btsocket != null) {
//                Toast.makeText(activity, "ConnectedDeviceBT2", Toast.LENGTH_SHORT).show();
//                //printText(message.getText().toString());
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        //super.onActivityResult(requestCode, resultCode, data);
//    }

    public void setConnectedDeviceBT() {
//        Toast.makeText(activity, "ConnectedDeviceBT3", Toast.LENGTH_SHORT).show();
        showProgress();
        try {
            btsocket = BTDeviceList.getSocket();

        } catch (Exception e) {
            e.printStackTrace();
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (btsocket != null) {
                    doInBill(listOrderInbill, tongtienOrderInbill, tienkhachduaOrderInbill, tienthoilaiOrderInbill, listHoanTatOrderInbill, id_customerOrderInbill,tiengiamtructiepInbill);
                    dismissProgress();
                }
            }
        }, 300);

    }

    @SuppressWarnings("unused")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onConnectedBTEvent(ConnectedBTEvent event) {
        if (view != null) {
            setConnectedDeviceBT();
        }
    }

    @SuppressWarnings("unused")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBackShowRootViewEvent(BackShowRootViewEvent event) {
        if (view != null) {
            view.showRootView();
        }
    }

}