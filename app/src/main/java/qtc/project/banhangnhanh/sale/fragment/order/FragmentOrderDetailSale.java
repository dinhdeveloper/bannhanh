package qtc.project.banhangnhanh.sale.fragment.order;

import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;

import b.laixuantam.myaarlibrary.api.ApiRequest;
import b.laixuantam.myaarlibrary.api.ErrorApiResponse;
import b.laixuantam.myaarlibrary.base.BaseFragment;
import b.laixuantam.myaarlibrary.base.BaseParameters;
import b.laixuantam.myaarlibrary.helper.AccentRemove;
import b.laixuantam.myaarlibrary.widgets.dialog.alert.KAlertDialog;
import b.laixuantam.myaarlibrary.widgets.ultils.ConvertDate;
import qtc.project.banhangnhanh.R;
import qtc.project.banhangnhanh.activity.SaleHomeActivity;
import qtc.project.banhangnhanh.admin.dependency.AppProvider;
import qtc.project.banhangnhanh.admin.event.ConnectedBTEvent;
import qtc.project.banhangnhanh.admin.model.BaseResponseModel;
import qtc.project.banhangnhanh.admin.model.EmployeeModel;
import qtc.project.banhangnhanh.admin.model.OrderDetailModel;
import qtc.project.banhangnhanh.helper.Consts;
import qtc.project.banhangnhanh.sale.api.CancelOrderRequest;
import qtc.project.banhangnhanh.sale.bill.BTDeviceList;
import qtc.project.banhangnhanh.sale.bill.PrinterCommands;
import qtc.project.banhangnhanh.sale.bill.Utils;
import qtc.project.banhangnhanh.sale.event.BackShowRootViewEvent;
import qtc.project.banhangnhanh.sale.event.UpdateListProductEvent;
import qtc.project.banhangnhanh.sale.fragment.home.FragmentSaleHome;
import qtc.project.banhangnhanh.sale.model.OrderModel;
import qtc.project.banhangnhanh.sale.view.fragment.order.detail.FragmentOrderDetailSaleView;
import qtc.project.banhangnhanh.sale.view.fragment.order.detail.FragmentOrderDetailSaleViewCallback;
import qtc.project.banhangnhanh.sale.view.fragment.order.detail.FragmentOrderDetailSaleViewInterface;

import static qtc.project.banhangnhanh.helper.Consts.decimalFormat;

public class FragmentOrderDetailSale extends BaseFragment<FragmentOrderDetailSaleViewInterface, BaseParameters> implements FragmentOrderDetailSaleViewCallback {
    SaleHomeActivity activity;
    private BluetoothSocket btsocket;
    private OutputStream outputStream;

    public static FragmentOrderDetailSale newInstance(OrderModel model) {
        FragmentOrderDetailSale fm = new FragmentOrderDetailSale();
        Bundle bundle = new Bundle();
        bundle.putSerializable("model", model);
        fm.setArguments(bundle);

        return fm;
    }

    @Override
    protected void initialize() {
        activity = (SaleHomeActivity) getActivity();
        view.init(activity, this);
        employeeModel = AppProvider.getPreferences().getUserModel();
        getBundle();
    }

    private void getBundle() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            OrderModel model = (OrderModel) bundle.get("model");
            view.initView(model);
        }
    }

    @Override
    protected FragmentOrderDetailSaleViewInterface getViewInstance() {
        return new FragmentOrderDetailSaleView();
    }

    @Override
    protected BaseParameters getParametersContainer() {
        return null;
    }

    @Override
    public void onBackP() {
        if (activity != null) {
            activity.checkBack();
            BackShowRootViewEvent.post();
        }
    }

    @Override
    public void cancelOrder(String id_order) {
        if (!AppProvider.getConnectivityHelper().hasInternetConnection()) {
            showAlert(getContext().getResources().getString(R.string.error_internet_connection), KAlertDialog.ERROR_TYPE);
            return;
        }
        showProgress();
        CancelOrderRequest.ApiParams params = new CancelOrderRequest.ApiParams();
        params.id_business = AppProvider.getPreferences().getUserModel().getId_business();
        if (id_order != null) {
            params.id_order = id_order;
        }
        AppProvider.getApiManagement().call(CancelOrderRequest.class, params, new ApiRequest.ApiCallback<BaseResponseModel>() {
            @Override
            public void onSuccess(BaseResponseModel body) {
                dismissProgress();
                if (!TextUtils.isEmpty(body.getSuccess()) && body.getSuccess().equalsIgnoreCase("true")) {
                    showAlert(body.getMessage(), KAlertDialog.SUCCESS_TYPE);
                    UpdateListProductEvent.post();
                } else {
                    showAlert(body.getMessage(), KAlertDialog.ERROR_TYPE);
                }
            }

            @Override
            public void onError(ErrorApiResponse error) {
                dismissProgress();
                showAlert("Không tải được dữ liệu", KAlertDialog.ERROR_TYPE);
            }

            @Override
            public void onFail(ApiRequest.RequestError error) {
                dismissProgress();
                showAlert("Không tải được dữ liệu", KAlertDialog.ERROR_TYPE);
            }
        });
    }

    @Override
    public void reQuestData() {
    }

    OrderModel modelLocal;
    ArrayList<OrderDetailModel> detailModelsLocal;
    float tiengiamLocal;
    EmployeeModel employeeModel;

    @Override
    public void inBill(OrderModel model, ArrayList<OrderDetailModel> detailModels, float tiengiam) {
        this.modelLocal = model;
        this.detailModelsLocal = detailModels;
        this.tiengiamLocal = tiengiam;
        if (btsocket == null) {
            Intent BTIntent = new Intent(activity, BTDeviceList.class);
            this.startActivityForResult(BTIntent, BTDeviceList.REQUEST_CONNECT_BT);
        } else {
            doInBill(model, detailModels, tiengiam);
        }
    }

    private void doInBill(OrderModel model, ArrayList<OrderDetailModel> detailModels, float tiengiam) {
        OutputStream opstream = null;
        try {
            opstream = btsocket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        outputStream = opstream;
//        if (outputStream != null) {
//            activity.replaceFragment(new FragmentSaleHome(), false);
//            //view.dismissDialog();
//        }
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
            printNewLine();
            String store = null;
            if (!TextUtils.isEmpty(employeeModel.getStore_name())) {
                store = AccentRemove.removeAccent(employeeModel.getStore_name());
            } else {
                store = "QTC TEK";
            }
            printCustom(store, 3, 1);
            printNewLine();
            printCustom(AccentRemove.removeAccent(employeeModel.getStore_address()), 0, 1);
            printCustom("Hot Line: " + employeeModel.getStore_phone(), 0, 1);
            printCustom("-~-~-~-~-~-~-~-~-~-~-", 0, 1);
            printNewLine();
            printCustom("PHIEU THANH TOAN", 1, 1);
            printNewLine();
            printCustom("-~-~-~-~-~-~-~-~-~-~-", 0, 1);
            printNewLine();

            //printCustom("Ngay: " + ConvertDate.changeToNiceFormatDate(model.getOrder_created_date()), 1, 0);
            if (!TextUtils.isEmpty(model.getOrder_created_date())) {
                String[] arrDateTimeCreated = model.getOrder_created_date().split(" ");
                String dateCreated = null;
                if (arrDateTimeCreated != null && arrDateTimeCreated.length > 0) {
                    dateCreated = ConvertDate.changeToNiceFormatDate(arrDateTimeCreated[0]);
                    printCustom(dateCreated + " " + arrDateTimeCreated[1], 1, 0);
                }
            }
            printCustom("Ma Don Hang :" + model.getOrder_id_code(), 1, 0);
            //printCustom("Nhan Vien: " + AccentRemove.removeAccent(AppProvider.getPreferences().getUserModel().getFull_name()), 1, 0);
            if (!TextUtils.isEmpty(model.getEmployee_fullname())) {
                printCustom("Nhan Vien: " + AccentRemove.removeAccent(model.getEmployee_fullname()), 1, 0);
            }
            if (!TextUtils.isEmpty(model.getCustomer_id())) {
                printCustom("Khach Hang: " + AccentRemove.removeAccent(model.getCustomer_id_code()), 1, 0);
            }
            printCustom("-----------------------", 0, 1);

            long allPrice = 0;
            int size = detailModels.size();
            for (int i = 0; i < size; i++) {
                String name = AccentRemove.removeAccent(detailModels.get(i).getName());
                String price = decimalFormat.format(Long.valueOf(detailModels.get(i).getPrice()));
                String quantity = detailModels.get(i).getQuantity();
                long total = (long) (Long.valueOf(detailModels.get(i).getPrice()) * Double.valueOf(detailModels.get(i).getQuantity()));
                allPrice += total;

                printCustom((i + 1) + ". " + name, 0, 0);
                printNewLine();
                printCustom("  " + price + " x " + quantity + " = " + decimalFormat.format(Long.valueOf(total)), 0, 2);
                //printCustom("-----------", 0, 2);
//                    printCustom(Consts.decimalFormat.format(Long.valueOf(temp)), 0, 2);
                printNewLine();
            }
            long phantram_phaitra = (Long.valueOf(model.getOrder_total()) * 100) / allPrice;
            //long phantram_giamgia = 100 - phantram_phaitra;

            long tien_giam_tt = Long.valueOf(model.getOrder_direct_discount());
            long tien_giam = allPrice - Long.valueOf(model.getOrder_direct_discount()) - Long.valueOf(model.getOrder_total());
            printCustom("\nTong Cong: " + decimalFormat.format(allPrice), 1, 0);
            printNewLine();
            printCustom("Giam Gia: " + decimalFormat.format(tien_giam), 1, 0);
            printNewLine();
            printCustom("Giam Truc Tiep: " + decimalFormat.format(tien_giam_tt), 1, 0);
            printNewLine();
            printCustom("-----------------------", 0, 1);
            printNewLine();
            printCustom("Thanh Toan: " + decimalFormat.format(Long.valueOf(model.getOrder_total())), 1, 0);
            printNewLine();
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
                    doInBill(modelLocal, detailModelsLocal, tiengiamLocal);
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

    private String leftRightAlign(String str1, String str2) {
        String ans = str1 + str2;
        if (ans.length() < 38) {
            int n = (38 - str1.length() + str2.length());
            ans = str1 + new String(new char[n]).replace("\0", " ") + str2;
        }
        return ans;
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            btsocket = BTDeviceList.getSocket();
            if (btsocket != null) {
                //printText(message.getText().toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
