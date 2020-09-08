package qtc.project.banhangnhanh.sale.fragment.product;

import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;

import b.laixuantam.myaarlibrary.api.ApiRequest;
import b.laixuantam.myaarlibrary.api.ErrorApiResponse;
import b.laixuantam.myaarlibrary.base.BaseFragment;
import b.laixuantam.myaarlibrary.base.BaseParameters;
import b.laixuantam.myaarlibrary.helper.AccentRemove;
import b.laixuantam.myaarlibrary.widgets.dialog.alert.KAlertDialog;
import qtc.project.banhangnhanh.activity.BarCodeActivity;
import qtc.project.banhangnhanh.activity.SaleHomeActivity;
import qtc.project.banhangnhanh.admin.dependency.AppProvider;
import qtc.project.banhangnhanh.admin.event.ConnectedBTEvent;
import qtc.project.banhangnhanh.admin.model.BaseResponseModel;
import qtc.project.banhangnhanh.sale.api.ProductAdminRequest;
import qtc.project.banhangnhanh.sale.bill.BTDevice;
import qtc.project.banhangnhanh.sale.bill.BTDeviceList;
import qtc.project.banhangnhanh.sale.bill.PrinterCommands;
import qtc.project.banhangnhanh.sale.bill.Utils;
import qtc.project.banhangnhanh.sale.event.BackShowRootViewEvent;
import qtc.project.banhangnhanh.sale.event.UpdateListProductEvent;
import qtc.project.banhangnhanh.sale.model.ProductModel;
import qtc.project.banhangnhanh.sale.view.fragment.product.detail.FragmentProductSaleDetailView;
import qtc.project.banhangnhanh.sale.view.fragment.product.detail.FragmentProductSaleDetailViewCallback;
import qtc.project.banhangnhanh.sale.view.fragment.product.detail.FragmentProductSaleDetailViewInterface;

public class FragmentProductSaleDetail extends BaseFragment<FragmentProductSaleDetailViewInterface, BaseParameters> implements FragmentProductSaleDetailViewCallback {
    SaleHomeActivity activity;
    byte FONT_TYPE;
    private BluetoothSocket btsocket;
    private OutputStream outputStream;


    public  static  FragmentProductSaleDetail newInstance(ProductModel model){
        FragmentProductSaleDetail fm = new FragmentProductSaleDetail();
        Bundle bundle = new Bundle();
        bundle.putSerializable("model",model);
        fm.setArguments(bundle);

        return fm;
    }
    @Override
    protected void initialize() {
        activity = (SaleHomeActivity)getActivity();
        view.init(activity,this);

        getBundle();
    }

    private void getBundle() {
        Bundle bundle = getArguments();
        if (bundle!=null){
            ProductModel model = (ProductModel)bundle.get("model");
            view.initView(model);
        }

    }

    @Override
    protected FragmentProductSaleDetailViewInterface getViewInstance() {
        return new FragmentProductSaleDetailView();
    }

    @Override
    protected BaseParameters getParametersContainer() {
        return null;
    }

    @Override
    public void onBackP() {
        if (activity!=null){
            activity.checkBack();
            BackShowRootViewEvent.post();
        }
    }

    @Override
    public void updateProductDetail(ProductModel productModel) {
        if (productModel !=null){
            showProgress();
            ProductAdminRequest.ApiParams params = new ProductAdminRequest.ApiParams();
            params.id_business = AppProvider.getPreferences().getUserModel().getId_business();
            params.type_manager = "update_product";
            params.id_product = productModel.getId();
            params.name = productModel.getName();
            params.description = productModel.getDescription();
            params.quantity_safetystock = productModel.getQuantity_safetystock();

            AppProvider.getApiManagement().call(ProductAdminRequest.class, params, new ApiRequest.ApiCallback<BaseResponseModel<ProductModel>>() {
                @Override
                public void onSuccess(BaseResponseModel<ProductModel> body) {
                    dismissProgress();
                    if (!TextUtils.isEmpty(body.getSuccess()) && body.getSuccess().equals("true")) {
                        showAlert(body.getMessage(), KAlertDialog.SUCCESS_TYPE);
                        UpdateListProductEvent.post();
                    } else {
                        showAlert(body.getMessage(), KAlertDialog.ERROR_TYPE);
                    }
                }

                @Override
                public void onError(ErrorApiResponse error) {
                    dismissProgress();
                    showAlert("Không tải được dữ liệu", KAlertDialog.SUCCESS_TYPE);
                }

                @Override
                public void onFail(ApiRequest.RequestError error) {
                    dismissProgress();
                    showAlert("Không tải được dữ liệu", KAlertDialog.SUCCESS_TYPE);
                }
            });
        }
    }

    @SuppressWarnings("unused")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onConnectedBTEvent(ConnectedBTEvent event) {
        if (view != null) {
            setConnectedDeviceBT();
        }
    }

    private void setConnectedDeviceBT() {
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
                    doInBill(productNameLocal,barcodeLocal,statusLocal);
                    dismissProgress();
                }
            }
        }, 300);

    }
    String productNameLocal;
    String barcodeLocal;
    String statusLocal;
    @Override
    public void inBarCode(String product_name, String barcode, String status) {
//        if (activity!=null){
//            Intent intent = new Intent(activity, BarCodeActivity.class);
//            intent.putExtra("BARCODE",barcode);
//            intent.putExtra("status",status);
//            intent.putExtra("PRODUCT_NAME",product_name);
//            activity.startActivity(intent);
//        }
        this.productNameLocal = product_name;
        this.barcodeLocal = barcode;
        this.statusLocal = status;

        if (btsocket == null) {
            Intent BTIntent = new Intent(activity, BTDeviceList.class);
            this.startActivityForResult(BTIntent, BTDeviceList.REQUEST_CONNECT_BT);
        } else {
            doInBill(product_name,barcode,status);
        }
    }

    private void doInBill(String product_name, String barcode, String status) {
        OutputStream opstream = null;
        try {
            opstream = btsocket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        outputStream = opstream;

//        if (outputStream != null) view.showPopup();
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

            if (status.trim().equalsIgnoreCase("0")){
                genarateScanBarcode(barcode,product_name);
            }else if(status.trim().equalsIgnoreCase("1")){
                genarateScanQrcode(barcode,product_name);
            }
            printCustom(barcode,0,1);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void genarateScanQrcode(String resultCode,String product_name){
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix;
            bitMatrix = multiFormatWriter.encode(resultCode, BarcodeFormat.QR_CODE, 150, 150);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bmp = barcodeEncoder.createBitmap(bitMatrix);
            try {
                if (bmp != null) {
                    byte[] command = Utils.decodeBitmap(bmp);
                    outputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
                    String name = AccentRemove.removeAccent(product_name);
                    printCustom(name,0,1);
                    printText(command);
                    printNewLine();
                } else {
                    Log.e("Print Photo error", "the file isn't exists");
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("PrintTools", "the file isn't exists");
            }


//            ui.barCode.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    public void genarateScanBarcode(String resultCode,String product_name){
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix;
            bitMatrix = multiFormatWriter.encode(resultCode, BarcodeFormat.CODE_128, 260, 120);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bmp = barcodeEncoder.createBitmap(bitMatrix);
            try {
                if (bmp != null) {
                    byte[] command = Utils.decodeBitmap(bmp);
                    outputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
                    String name = AccentRemove.removeAccent(product_name);
                    printCustom(name,0,1);
                    printText(command);
                } else {
                    Log.e("Print Photo error", "the file isn't exists");
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("PrintTools", "the file isn't exists");
            }


//            ui.barCode.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
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