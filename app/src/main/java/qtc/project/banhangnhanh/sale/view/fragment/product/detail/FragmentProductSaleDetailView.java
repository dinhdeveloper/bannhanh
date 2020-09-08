package qtc.project.banhangnhanh.sale.view.fragment.product.detail;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.UUID;

import b.laixuantam.myaarlibrary.base.BaseUiContainer;
import b.laixuantam.myaarlibrary.base.BaseView;
import b.laixuantam.myaarlibrary.helper.KeyboardUtils;
import qtc.project.banhangnhanh.R;
import qtc.project.banhangnhanh.activity.SaleHomeActivity;
import qtc.project.banhangnhanh.admin.dependency.AppProvider;
import qtc.project.banhangnhanh.helper.Consts;
import qtc.project.banhangnhanh.sale.model.ProductModel;

public class FragmentProductSaleDetailView extends BaseView<FragmentProductSaleDetailView.UIContainer> implements FragmentProductSaleDetailViewInterface {
    SaleHomeActivity activity;
    FragmentProductSaleDetailViewCallback callback;
    String BARCODE, QRCODE;
    String PRODUCT_NAME;

    @Override
    public void init(SaleHomeActivity activity, FragmentProductSaleDetailViewCallback callback) {
        this.activity = activity;
        this.callback = callback;
        KeyboardUtils.setupUI(getView(),activity);
        ui.imageNavLeft.setOnClickListener(v -> {
            if (callback != null)
                callback.onBackP();
        });
        ui.title_header.setMaxHeight(300);
        ui.imvEdit.setVisibility(View.VISIBLE);

        onClick();
    }

    private void onClick() {
        //BARCODE
        ui.imvInBarcode.setOnClickListener(v -> {
            if (callback != null) {
                LayoutInflater layoutInflater = activity.getLayoutInflater();
                View popupView = layoutInflater.inflate(R.layout.custom_popup_sale_in_barcode, null);

                LinearLayout item_detail = popupView.findViewById(R.id.item_detail);
                AlertDialog.Builder alert = new AlertDialog.Builder(activity);
                alert.setView(popupView);
                dialogss = alert.create();
                // dialog.setCanceledOnTouchOutside(false);
                dialogss.show();
                item_detail.setOnClickListener(v1 -> {
                    callback.inBarCode(PRODUCT_NAME, BARCODE, "0");
                });
            }
        });

        ui.imvInQrCode.setOnClickListener(v -> {
            if (callback != null) {
                LayoutInflater layoutInflater = activity.getLayoutInflater();
                View popupView = layoutInflater.inflate(R.layout.custom_popup_sale_in_barcode, null);

                LinearLayout item_detail = popupView.findViewById(R.id.item_detail);
                AlertDialog.Builder alert = new AlertDialog.Builder(activity);
                alert.setView(popupView);
                dialogss = alert.create();
                // dialog.setCanceledOnTouchOutside(false);
                dialogss.show();
                item_detail.setOnClickListener(v1 -> {
                    callback.inBarCode(PRODUCT_NAME, QRCODE, "1");
                });
            }
        });
    }

    @Override
    public void initView(ProductModel model) {
        if (model != null) {
            PRODUCT_NAME = model.getName();
            BARCODE = model.getBarcode();
            QRCODE = model.getQr_code();
            ui.title_header.setText(model.getName());
            AppProvider.getImageHelper().displayImage(Consts.HOST_API + model.getImage(), ui.imageProduct, null, R.drawable.no_image_full);
            ui.nameProduct.setText(model.getName());
            ui.idProduct.setText(model.getId_code());
            ui.priceProduct.setText(Consts.decimalFormat.format(Long.valueOf(model.getSale_price())));
            ui.tonKho.setText(model.getTotal_stock());
            ui.tonKhoAnToan.setText(model.getQuantity_safetystock());
            ui.description.setText(model.getDescription());
            ui.tvBarCode.setText(model.getBarcode());
            ui.tvQrcode.setText(model.getQr_code());
            try {
                genarateScanBarcode(model.getBarcode());
                genarateScanQrcode(model.getQr_code());
            } catch (Exception e) {
                Log.e("Ex", e.getMessage());
            }
            ui.imvEdit.setOnClickListener(v -> {
                ui.nameProduct.setEnabled(true);
                ui.description.setEnabled(true);
                ui.nameProduct.setBackgroundColor(Color.parseColor("#F2F2F2"));
                ui.description.setBackgroundColor(Color.parseColor("#F2F2F2"));
                ui.btnUpdate.setOnClickListener(v1 -> {
                    ProductModel productModel = new ProductModel();
                    productModel.setId(model.getId());
                    productModel.setName(ui.nameProduct.getText().toString());
                    productModel.setDescription(ui.description.getText().toString());
                    productModel.setQuantity_safetystock(ui.tonKhoAnToan.getText().toString());

                    if (callback != null) {
                        callback.updateProductDetail(productModel);
                    }
                });

            });
        }
    }

    @Override
    public void showUpdateSuccess() {
        LayoutInflater layoutInflater = activity.getLayoutInflater();
        View popupView = layoutInflater.inflate(R.layout.alert_dialog_success, null);
        TextView title_text = popupView.findViewById(R.id.title_text);
        TextView content_text = popupView.findViewById(R.id.content_text);
        Button custom_confirm_button = popupView.findViewById(R.id.custom_confirm_button);

        title_text.setText("Xác nhận");
        content_text.setText("Bạn đã cập nhật thành công!");

        AlertDialog.Builder alert = new AlertDialog.Builder(activity);
        alert.setView(popupView);
        AlertDialog dialog = alert.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        custom_confirm_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                ui.nameProduct.setEnabled(false);
                ui.description.setEnabled(false);
                ui.nameProduct.setBackgroundColor(Color.parseColor("#F3ECD3"));
                ui.description.setBackgroundColor(Color.parseColor("#F3ECD3"));

            }
        });
    }

    AlertDialog dialogss;

    @Override
    public void showPopup() {
        dialogss.dismiss();
    }

    public void genarateScanBarcode(String resultCode) {
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix;
            bitMatrix = multiFormatWriter.encode(resultCode, BarcodeFormat.CODE_128, 300, 150);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            ui.barCode.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    public void genarateScanQrcode(String resultCode) {
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix;
            bitMatrix = multiFormatWriter.encode(resultCode, BarcodeFormat.QR_CODE, 300, 300);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            ui.qrCode.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    @Override
    public BaseUiContainer getUiContainer() {
        return new FragmentProductSaleDetailView.UIContainer();
    }

    @Override
    public int getViewId() {
        return R.layout.layout_fragment_product_detail_sale;
    }


    public class UIContainer extends BaseUiContainer {
        @UiElement(R.id.imageNavLeft)
        public ImageView imageNavLeft;

        @UiElement(R.id.title_header)
        public TextView title_header;

        @UiElement(R.id.imvEdit)
        public ImageView imvEdit;

        @UiElement(R.id.imageProduct)
        public ImageView imageProduct;

        @UiElement(R.id.nameProduct)
        public EditText nameProduct;

        @UiElement(R.id.idProduct)
        public TextView idProduct;

        @UiElement(R.id.priceProduct)
        public TextView priceProduct;

        @UiElement(R.id.tonKho)
        public TextView tonKho;

        @UiElement(R.id.tonKhoAnToan)
        public EditText tonKhoAnToan;

        @UiElement(R.id.description)
        public EditText description;

        @UiElement(R.id.qrCode)
        public ImageView qrCode;

        @UiElement(R.id.barCode)
        public ImageView barCode;

        @UiElement(R.id.tvBarCode)
        public TextView tvBarCode;

        @UiElement(R.id.tvQrcode)
        public TextView tvQrcode;

        @UiElement(R.id.imvInBarcode)
        public ImageView imvInBarcode;

        @UiElement(R.id.imvInQrCode)
        public ImageView imvInQrCode;

        @UiElement(R.id.btnUpdate)
        public LinearLayout btnUpdate;


    }
}
