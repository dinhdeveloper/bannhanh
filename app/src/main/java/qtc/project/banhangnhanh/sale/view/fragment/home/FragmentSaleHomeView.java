package qtc.project.banhangnhanh.sale.view.fragment.home;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.Result;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

import b.laixuantam.myaarlibrary.base.BaseUiContainer;
import b.laixuantam.myaarlibrary.base.BaseView;
import b.laixuantam.myaarlibrary.helper.CurrencyFormater;
import b.laixuantam.myaarlibrary.helper.KeyboardUtils;
import b.laixuantam.myaarlibrary.widgets.currencyedittext.CurrencyEditText;
import b.laixuantam.myaarlibrary.widgets.ultils.ConvertDate;
import me.dm7.barcodescanner.zxing.ZXingScannerView;
import qtc.project.banhangnhanh.R;
import qtc.project.banhangnhanh.activity.SaleHomeActivity;
import qtc.project.banhangnhanh.admin.dependency.AppProvider;
import qtc.project.banhangnhanh.admin.model.CustomerModel;
import qtc.project.banhangnhanh.admin.model.EmployeeModel;
import qtc.project.banhangnhanh.admin.model.PackageInfoModel;
import qtc.project.banhangnhanh.sale.adapter.home.ListItemClickAdapter;
import qtc.project.banhangnhanh.sale.database.DatabaseProvider;
import qtc.project.banhangnhanh.sale.fragment.home.FragmentSaleHome;
import qtc.project.banhangnhanh.sale.model.ListOrderModel;
import qtc.project.banhangnhanh.sale.model.OrderModel;
import qtc.project.banhangnhanh.sale.model.ProductModel;

public class FragmentSaleHomeView extends BaseView<FragmentSaleHomeView.UIContainer> implements FragmentSaleHomeViewInterface, ZXingScannerView.ResultHandler {
    SaleHomeActivity activity;
    FragmentSaleHomeViewCallback callback;
    String discount = null;

    String id_customer = null;
    String tongtien = null;
    double tonkho = 0;
    private ZXingScannerView mScannerView;
    private Timer timer = new Timer();
    private final long DELAY = 1000; // in ms
    long tienthoilai = 0;
    long tienkhachdua;
    long tiengiamtructiep = 0;
    HashMap<String, ListOrderModel> hashMap = new HashMap<>();
    ArrayList<ListOrderModel> arList = new ArrayList<>();

    int TYLE_CODE_GEN = 2;
    ListItemClickAdapter clickAdapter;

    DatabaseProvider provider;
    PackageManager pm;

    @Override
    public void init(SaleHomeActivity activity, FragmentSaleHomeViewCallback callback) {
        this.activity = activity;
        this.callback = callback;
        KeyboardUtils.setupUI(getView(), activity);
        ui.title_header.setText("Trang chủ");
        ui.tvStore_end.setText("Hết hạn: " + ConvertDate.changeToNiceFormatDate(AppProvider.getPreferences().getUserModel().getStore_end()));
        ui.tvTitleEmpty.setVisibility(View.VISIBLE);
        ui.tvContentTitleEmpty.setVisibility(View.VISIBLE);
        ui.tvTitleEmpty.setText("Giỏ hàng trống");
        ui.tvContentTitleEmpty.setText("Chưa có sản phẩm trong giỏ hàng");
        ui.imvHome.setVisibility(View.INVISIBLE);
        ui.edit_filter.requestFocus();
        ui.layoutNone.setBackgroundColor(Color.parseColor("#FFFFFF"));
        provider = new DatabaseProvider(activity);
        KeyboardUtils.setupUI(getView(), activity);
        ui.edit_filter.requestFocus();
        if (hashMap.isEmpty()) {
            getDataSQLite();
        }
        onClick();
        pm = activity.getPackageManager();
    }


    private void getDataSQLite() {
        ArrayList<ListOrderModel> listOrderModels = provider.getNotes();
        if (listOrderModels.isEmpty()) {
            ui.layoutShow.setVisibility(View.GONE);
            ui.layoutNone.setVisibility(View.VISIBLE);
        } else {
            ui.layoutShow.setVisibility(View.VISIBLE);
            ui.layoutNone.setVisibility(View.GONE);
            //chon khach hang
            ui.layoutChooseCus.setOnClickListener(v -> {
                if (callback != null) {
                    callback.goToChooseCustomer();
                }
            });
            String tongTonKho = null;
            for (ListOrderModel orderModel : listOrderModels) {
                hashMap.put(orderModel.getId(), orderModel);
                tongTonKho = orderModel.getTotalStore();
            }
            arList.clear();
            for (Map.Entry<String, ListOrderModel> map : hashMap.entrySet()) {
                arList.add(map.getValue());
            }
            Collections.sort(arList, new Comparator<ListOrderModel>() {
                @Override
                public int compare(ListOrderModel o1, ListOrderModel o2) {
                    return o1.getPosition_item().compareTo(o2.getPosition_item());
                }
            });
            ui.recycler_view_list_order.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
            clickAdapter = new ListItemClickAdapter(activity, arList);
            ui.recycler_view_list_order.setAdapter(clickAdapter);

            String finalTongTonKho = tongTonKho;
            clickAdapter.setListener(new ListItemClickAdapter.ListItemClickAdapterListener() {
                @Override
                public void onItemClick(ListOrderModel model) {

                }

                @Override
                public void onClickThemSoLuong(ListOrderModel model) {
                    if (hashMap.get(model.getId()) != null) {
                        String old_quantity = hashMap.get(model.getId()).getQuantityProduct();
                        double new_quantity = Double.valueOf(old_quantity) + 1.0;
                        hashMap.get(model.getId()).setQuantityProduct(String.valueOf(new_quantity));
                        provider.updateRecord(hashMap.get(model.getId()));
                        tinhTong();
                    }
                }

                @Override
                public void onClickGiamSoLuong(ListOrderModel model) {
                    if (hashMap.get(model.getId()) != null) {
                        String old_quantity = hashMap.get(model.getId()).getQuantityProduct();
                        double new_quantity = Double.valueOf(old_quantity) - 1.0;
                        hashMap.get(model.getId()).setQuantityProduct(String.valueOf(new_quantity));
                        if (new_quantity <= 1.0) {
                            hashMap.get(model.getId()).setQuantityProduct(String.valueOf(1.0));
                        }
                        provider.updateRecord(hashMap.get(model.getId()));
                        tinhTong();
                    }
                }

                @Override
                public void onClickXoaItem(ListOrderModel items) {
                    if (hashMap.get(items.getId()) != null) {
                        provider.deleteRecord(hashMap.get(items.getId()));
                        hashMap.remove(items.getId());
                        clickAdapter.remove(items);
                        tinhTong();
                        if (hashMap.isEmpty()) {
                            ui.layoutNone.setVisibility(View.VISIBLE);
                            ui.layoutShow.setVisibility(View.GONE);
                        }
                    }
                }

                @Override
                public void onChangeSoLuong(ListOrderModel item) {
                    if (hashMap.get(item.getId()) != null) {
                        double new_quantity = Double.valueOf(item.getQuantityProduct());
                        hashMap.get(item.getId()).setQuantityProduct(String.valueOf(new_quantity));
                        provider.updateRecord(hashMap.get(item.getId()));
                        tinhTong();
                    }
                }
            });
            clickAdapter.notifyDataSetChanged();
            tinhTong();
            //tien giam truc tiep
            ui.tienGiamTT.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (timer != null)
                        timer.cancel();
                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if (!TextUtils.isEmpty(s)) {
                                        tiengiamtructiep = Long.valueOf(ui.tienGiamTT.getStringValue());
                                    } else {
                                        tiengiamtructiep = 0;
                                    }
                                    tinhTong();
                                }
                            });
                        }

                    }, DELAY);
                }
            });
            ui.tienGiamTT.setOnEditorActionListener((v, actionId, event) -> {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (!TextUtils.isEmpty(ui.tienGiamTT.getStringValue())) {
                        tiengiamtructiep = Long.valueOf(ui.tienGiamTT.getStringValue());
                    } else {
                        tiengiamtructiep = 0;
                    }
                    tinhTong();
                    return true;
                }
                return false;
            });


            //tinh tien
            ui.btnChoose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (hashMap.size() > 0) {
                        KeyboardUtils.setupUI(getView(), activity);
                        LayoutInflater layoutInflater = activity.getLayoutInflater();
                        View popupView = layoutInflater.inflate(R.layout.sale_custom_popup_tien_khach_dua, null);

                        LinearLayout btnExit = popupView.findViewById(R.id.btnExit);
                        LinearLayout btnOk = popupView.findViewById(R.id.btnOk);
                        CurrencyEditText priceItem = popupView.findViewById(R.id.priceItem);
                        TextView tien_thoi_lai_khach = popupView.findViewById(R.id.tien_thoi_lai_khach);
                        TextView tong_thanh_tien = popupView.findViewById(R.id.tong_thanh_tien);

                        String pattern = "###,###.###";
                        DecimalFormat decimalFormat = new DecimalFormat(pattern);
                        tong_thanh_tien.setText(decimalFormat.format(Long.valueOf(tongtien)));

                        priceItem.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {

                            }

                            @Override
                            public void afterTextChanged(Editable s) {
                                if (timer != null)
                                    timer.cancel();
                                timer = new Timer();
                                timer.schedule(new TimerTask() {
                                    @Override
                                    public void run() {
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                if (!TextUtils.isEmpty(s)) {
                                                    tienkhachdua = Long.valueOf(priceItem.getStringValue());
                                                    tienthoilai = tienkhachdua - Long.valueOf(tongtien);
                                                    tien_thoi_lai_khach.setText(decimalFormat.format(tienthoilai));
                                                }
                                            }
                                        });
                                    }

                                }, DELAY);
                            }
                        });

                        AlertDialog.Builder alert = new AlertDialog.Builder(activity);
                        alert.setView(popupView);
                        AlertDialog dialog = alert.create();
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.show();

                        btnExit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        btnOk.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                callback.connectBlutooth();
                                if (tienthoilai < 0) {
                                    priceItem.setText(null);
                                    tien_thoi_lai_khach.setText(null);
                                    Toast.makeText(activity, "Hãy nhập đúng tiền", Toast.LENGTH_SHORT).show();
                                } else {
                                    callback.tinhTien(arList, id_customer, tongtien, discount, tiengiamtructiep);
                                    dialog.dismiss();
                                }
                            }
                        });

                        priceItem.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                            @Override
                            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                                if (actionId == EditorInfo.IME_ACTION_DONE) {
                                    if (tienthoilai < 0) {
                                        priceItem.setText(null);
                                        tien_thoi_lai_khach.setText(null);
                                        Toast.makeText(activity, "Hãy nhập đúng tiền", Toast.LENGTH_SHORT).show();
                                    } else {
                                        callback.tinhTien(arList, id_customer, tongtien, discount, tiengiamtructiep);
                                        dialog.dismiss();
                                    }
                                }
                                return true;
                            }
                        });

                    } else {
                        Toast.makeText(activity, "Bạn chưa chọn sản phẩm", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Override
    public void setDataCustomer(CustomerModel model) {
        if (model != null) {
            ui.layoutShowCustomer.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(model.getId())) {
                id_customer = model.getId();
            }
            if (!TextUtils.isEmpty(model.getFull_name())) {
                ui.nameCustomers.setText(model.getFull_name());
            }
            if (!TextUtils.isEmpty(model.getLevel_name())) {
                ui.nameLevelCuss.setText(model.getLevel_name());
            }
            if (model.getLevel_discount() == null || model.getLevel_discount().equalsIgnoreCase("0")) {
                discount = String.valueOf("0");
            } else {
                discount = String.valueOf(model.getLevel_discount());
                tinhTong();
            }
            ui.imageClearn.setOnClickListener(v -> {
                try {
                    ui.layoutShowCustomer.setVisibility(View.GONE);
                    ui.priceSale.setText("0 đ");
                    ui.allPrice.setText(ui.idPriceTemp.getText().toString());
                    ui.nameCustomers.setText(null);
                    ui.nameLevelCuss.setText(null);
                    model.setId(null);
                    model.setFull_name(null);
                    model.setLevel_name(null);
                    discount = String.valueOf("0");
                    model.setLevel_discount("0");
                } catch (Exception e) {
                    Log.e("Exception Customer", e.getMessage());
                }
            });
        }
    }

    @Override
    public void initViewProduct(ArrayList<ProductModel> list) {
        if (list != null) {
            for (ProductModel model : list) {
                addItemCart(model, model.getListDataProduct().get(0));
            }
        } else {
            Toast.makeText(activity, "Sản phẩm không tồn tại", Toast.LENGTH_SHORT).show();
        }
    }

    //AlertDialog dialogss;

    @Override
    public void truyenIntent(ArrayList<ListOrderModel> arList, ArrayList<OrderModel> listHoanTat, long tiengiamtructiep) {
        if (tienkhachdua == 0) {
            tienkhachdua = Long.valueOf(tongtien);
        }
        if (id_customer != null) {
            callback.inBill(arList, tongtien, tienkhachdua, tienthoilai, listHoanTat, listHoanTat.get(0).getCustomer_id_code(), tiengiamtructiep);
        } else {
            callback.inBill(arList, tongtien, tienkhachdua, tienthoilai, listHoanTat, null, tiengiamtructiep);
        }
    }


    @Override
    public void initViewProductClick(ProductModel model) {
        addItemCart(model, model.getListDataProduct().get(0));
    }

    private void onClick() {
        //dong layout scanbar code
        ui.image_close_layout_scan.setOnClickListener(v -> {
            stopScan();
            ui.layout_scanbar_code.setVisibility(View.GONE);
        });
        ui.imageNavLeft.setOnClickListener(v -> {
            if (callback != null) {
                callback.callNav();
            }
        });
        ui.image_close.setOnClickListener(v -> {
            ui.edit_filter.setText(null);
        });
        ui.edit_filter.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    callback.goToProductList(ui.edit_filter.getText().toString());
                }
                return false;
            }
        });

        ui.layoutChooseCus.setOnClickListener(v -> {
            if (callback != null)
                callback.goToChooseCustomer();
        });


        ui.quetMV.setOnClickListener(v -> {
            TYLE_CODE_GEN = 1;
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.CAMERA}, 1101);
            } else {
                ui.layout_scanbar_code.setVisibility(View.VISIBLE);
                startScan();
            }
        });
    }

    @Override
    public BaseUiContainer getUiContainer() {
        return new FragmentSaleHomeView.UIContainer();
    }

    @Override
    public void handleResult(Result rawResult) {
        if (rawResult != null) {
            Vibrator v = (Vibrator) activity.getSystemService(Context.VIBRATOR_SERVICE);
            // SET RUNG 400 MILLISECONDS
            v.vibrate(400);
            generateCode(rawResult.getText());
            mScannerView.stopCamera();
            mScannerView.resumeCameraPreview(this);

            startScan();
            //ui.layout_scanbar_code.setVisibility(View.GONE);
        }
    }

    public void startScan() {
        if (mScannerView != null) {
            mScannerView.setResultHandler(this);
            mScannerView.startCamera();
            rescan();
        } else {
            mScannerView = new ZXingScannerView(activity);
            mScannerView.setAutoFocus(true);
            mScannerView.setResultHandler(this);
            ui.content_frame.addView(mScannerView);
            mScannerView.startCamera();
        }
    }

    public void stopScan() {
        if (mScannerView != null) {
            mScannerView.stopCameraPreview();
            mScannerView.stopCamera();
        }
    }

    public void rescan() {
        if (mScannerView != null) {
            if (pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)) {
                mScannerView.startCamera();
            } else {
                mScannerView.startCamera();
                mScannerView.startCamera(1);
            }
            mScannerView.resumeCameraPreview(this);
        }
    }

    private void generateCode(String resultCode) {
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix;
            switch (TYLE_CODE_GEN) {
                case 1:
                    bitMatrix = multiFormatWriter.encode(resultCode, BarcodeFormat.CODE_128, 300, 150);
                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                    Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                    //lam gi o day khi co bitmap
                    ui.edit_filter.setText(resultCode);
                    addCart(resultCode);
                    break;

                case 2:
                    bitMatrix = multiFormatWriter.encode(resultCode, BarcodeFormat.QR_CODE, 250, 250);
                    BarcodeEncoder barcodeQr = new BarcodeEncoder();
                    Bitmap bitmapQr = barcodeQr.createBitmap(bitMatrix);
                    //lam gi o day khi co bitmap
//                    ui.imv_qrcode.setImageBitmap(bitmapQr);
//                    ui.qrcode.setText(resultCode);
                    ui.edit_filter.setText(resultCode);
                    addCart(resultCode);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addCart(String id_code) {
        if (callback != null) {
            if (id_code != null) {
                callback.callDataSearchProduct(id_code);
                ui.edit_filter.setText(null);
                ui.edit_filter.requestFocus();
            }
        }
    }

    static int count = 0;
    AlertDialog dialogTienKhach;

    private void addItemCart(ProductModel model, PackageInfoModel infoModel) {

        if (hashMap.get(infoModel.getPack_id()) != null) {
            tonkho = Double.valueOf(model.getTotal_stock());
            String old_quantity = hashMap.get(infoModel.getPack_id()).getQuantityProduct();
            double new_quantity = Double.valueOf(old_quantity) + 1.0;
            if (new_quantity <= tonkho) {
                hashMap.get(infoModel.getPack_id()).setQuantityProduct(String.valueOf(new_quantity));
                provider.updateRecord(hashMap.get(infoModel.getPack_id()));
            } else {
                Toast.makeText(activity, "Tồn kho không đủ.", Toast.LENGTH_SHORT).show();
            }
        } else {
            ListOrderModel orderModel = new ListOrderModel();
            orderModel.setId(infoModel.getPack_id());
            orderModel.setNameProduct(model.getName());
            orderModel.setQuantityProduct(String.valueOf(1));
            orderModel.setPriceProduct(model.getSale_price());
            orderModel.setInventory(model.getTotal_stock());
            count++;
            orderModel.setPosition_item(String.valueOf(count));
            hashMap.put(infoModel.getPack_id(), orderModel);
            //add moi record sqlite
            provider.addNotes(orderModel);
        }
        arList.clear();
        for (Map.Entry<String, ListOrderModel> map : hashMap.entrySet()) {
            arList.add(map.getValue());
            ui.layoutShow.setVisibility(View.VISIBLE);
            ui.layoutNone.setVisibility(View.GONE);
        }
        Collections.sort(arList, new Comparator<ListOrderModel>() {
            @Override
            public int compare(ListOrderModel o1, ListOrderModel o2) {
                return o1.getPosition_item().compareTo(o2.getPosition_item());
            }
        });

        ui.layoutShow.setVisibility(View.VISIBLE);
        ui.layoutNone.setVisibility(View.GONE);
        ui.recycler_view_list_order.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        clickAdapter = new ListItemClickAdapter(activity, arList);
        ui.recycler_view_list_order.setAdapter(clickAdapter);

        clickAdapter.setListener(new ListItemClickAdapter.ListItemClickAdapterListener() {
            @Override
            public void onItemClick(ListOrderModel model) {

            }

            @Override
            public void onClickThemSoLuong(ListOrderModel model) {
                if (hashMap.get(model.getId()) != null) {
                    String old_quantity = hashMap.get(model.getId()).getQuantityProduct();
                    double new_quantity = Double.valueOf(old_quantity) + 1.0;
                    hashMap.get(model.getId()).setQuantityProduct(String.valueOf(new_quantity));
                    provider.updateRecord(hashMap.get(model.getId()));
                    tinhTong();
                }
            }

            @Override
            public void onClickGiamSoLuong(ListOrderModel model) {
                if (hashMap.get(model.getId()) != null) {
                    String old_quantity = hashMap.get(model.getId()).getQuantityProduct();
                    double new_quantity = Double.valueOf(old_quantity) - 1.0;
                    hashMap.get(model.getId()).setQuantityProduct(String.valueOf(new_quantity));
                    if (new_quantity <= 1.0) {
                        hashMap.get(model.getId()).setQuantityProduct(String.valueOf(1.0));
                    }
                    provider.updateRecord(hashMap.get(model.getId()));
                    tinhTong();
                }
            }

            @Override
            public void onClickXoaItem(ListOrderModel items) {
                if (hashMap.get(items.getId()) != null) {
                    provider.deleteRecord(hashMap.get(items.getId()));
                    hashMap.remove(items.getId());
                    clickAdapter.remove(items);
                    tinhTong();
                    if (hashMap.isEmpty()) {
                        ui.layoutNone.setVisibility(View.VISIBLE);
                        ui.layoutShow.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onChangeSoLuong(ListOrderModel item) {
                if (hashMap.get(item.getId()) != null) {
                    double new_quantity = Double.valueOf(item.getQuantityProduct());
                    hashMap.get(item.getId()).setQuantityProduct(String.valueOf(new_quantity));
                    provider.updateRecord(hashMap.get(item.getId()));
                    tinhTong();
                }
            }
        });
        clickAdapter.notifyDataSetChanged();
        tinhTong();
        //tien giam truc tiep
        ui.tienGiamTT.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (timer != null)
                    timer.cancel();
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (!TextUtils.isEmpty(s)) {
                                    tiengiamtructiep = Long.valueOf(ui.tienGiamTT.getStringValue());
                                } else {
                                    tiengiamtructiep = 0;
                                }
                                tinhTong();
                            }
                        });
                    }

                }, DELAY);
            }
        });
        //tinh tien
        ui.btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hashMap.size() > 0) {
                    KeyboardUtils.setupUI(getView(), activity);
                    LayoutInflater layoutInflater = activity.getLayoutInflater();
                    View popupView = layoutInflater.inflate(R.layout.sale_custom_popup_tien_khach_dua, null);

                    LinearLayout btnExit = popupView.findViewById(R.id.btnExit);
                    LinearLayout btnOk = popupView.findViewById(R.id.btnOk);
                    CurrencyEditText priceItem = popupView.findViewById(R.id.priceItem);
                    TextView tien_thoi_lai_khach = popupView.findViewById(R.id.tien_thoi_lai_khach);
                    TextView tong_thanh_tien = popupView.findViewById(R.id.tong_thanh_tien);
                    ImageView imageNavLeft = popupView.findViewById(R.id.imageNavLeft);
                    TextView title_header = popupView.findViewById(R.id.title_header);

                    title_header.setText("Tính tiền");

                    String pattern = "###,###.###";
                    DecimalFormat decimalFormat = new DecimalFormat(pattern);
                    tong_thanh_tien.setText(decimalFormat.format(Long.valueOf(tongtien)));

                    priceItem.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            if (timer != null)
                                timer.cancel();
                            timer = new Timer();
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (!TextUtils.isEmpty(s)) {
                                                tienkhachdua = Long.valueOf(priceItem.getStringValue());
                                                tienthoilai = tienkhachdua - Long.valueOf(tongtien);
                                                tien_thoi_lai_khach.setText(decimalFormat.format(tienthoilai));
                                            }
                                        }
                                    });
                                }

                            }, DELAY);
                        }
                    });

                    AlertDialog.Builder alert = new AlertDialog.Builder(activity);
                    alert.setView(popupView);
                    dialogTienKhach = alert.create();
                    dialogTienKhach.setCanceledOnTouchOutside(false);
                    dialogTienKhach.show();

                    imageNavLeft.setOnClickListener(v -> {
                        dialogTienKhach.dismiss();
                    });

                    btnExit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogTienKhach.dismiss();
                        }
                    });
                    btnOk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (callback != null) {
                                if (tienthoilai < 0) {
                                    priceItem.setText(null);
                                    tien_thoi_lai_khach.setText(null);
                                    Toast.makeText(activity, "Hãy nhập đúng tiền", Toast.LENGTH_SHORT).show();
                                } else {
                                    //show popup hoi in bill hay k?
                                    callback.tinhTien(arList, id_customer, tongtien, discount, tiengiamtructiep);
                                    dialogTienKhach.dismiss();
                                }
                            }
                        }
                    });

                    priceItem.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                        @Override
                        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                            if (actionId == EditorInfo.IME_ACTION_DONE) {
                                if (tienthoilai < 0) {
                                    priceItem.setText(null);
                                    tien_thoi_lai_khach.setText(null);
                                    Toast.makeText(activity, "Hãy nhập đúng tiền", Toast.LENGTH_SHORT).show();
                                } else {
                                    callback.tinhTien(arList, id_customer, tongtien, discount, tiengiamtructiep);
                                    dialogTienKhach.dismiss();
                                }
                            }
                            return true;
                        }
                    });

                } else {
                    Toast.makeText(activity, "Bạn chưa chọn sản phẩm", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void tinhTong() {
        try {
            //tong tien tren 1 item
            long priceTemp = 0;
            //tong tien
            long total = 0;
            String pattern = "###,###.###";
            DecimalFormat decimalFormat = new DecimalFormat(pattern);
            Set<String> keySet = hashMap.keySet();
            for (String key : keySet) {
                //tong tien 1 item.
                priceTemp = (long) (Double.valueOf(hashMap.get(key).getQuantityProduct()) * Long.valueOf(hashMap.get(key).getPriceProduct()));
                total += priceTemp;
            }
            ui.idPriceTemp.setText(decimalFormat.format(total) + " đ");
            if (discount != null) {
                long tiengiam = (long) ((total * Double.valueOf(discount)) / 100);
                tongtien = String.valueOf(total - tiengiam - tiengiamtructiep);
                ui.priceSale.setText(decimalFormat.format(Long.valueOf(tiengiam)) + " đ");
                ui.allPrice.setText(decimalFormat.format(Long.valueOf(tongtien)) + " đ");
            }

            //khach vang lai
            else if (discount == null) {
                discount = "0";
                ui.priceSale.setText(decimalFormat.format(Double.valueOf(discount)) + " đ");
                long tiengiam = (long) ((total * Double.valueOf(discount)) / 100);
                tongtien = String.valueOf(total - tiengiam - tiengiamtructiep);
                ui.allPrice.setText(decimalFormat.format(Long.valueOf(tongtien)) + " đ");
            }
        } catch (Exception e) {
            Log.e("Ex", e.getMessage());
        }
    }

    @Override
    public void hideRootView() {
        setGone(ui.fragmentHome);
    }

    @Override
    public void showRootView() {
        setVisible(ui.fragmentHome);
    }

    @Override
    public int getViewId() {
        return R.layout.layout_fragment_home_sale;
    }

    public class UIContainer extends BaseUiContainer {
        @UiElement(R.id.fragmentHome)
        public View fragmentHome;

        @UiElement(R.id.imageNavLeft)
        public ImageView imageNavLeft;


        @UiElement(R.id.title_header)
        public TextView title_header;

        @UiElement(R.id.imvHome)
        public ImageView imvHome;

        //
        @UiElement(R.id.layoutNone)
        public LinearLayout layoutNone;

        @UiElement(R.id.layoutShow)
        public LinearLayout layoutShow;

        @UiElement(R.id.tvTitleEmpty)
        public TextView tvTitleEmpty;

        @UiElement(R.id.tvContentTitleEmpty)
        public TextView tvContentTitleEmpty;

        @UiElement(R.id.edit_filter)
        public EditText edit_filter;

        @UiElement(R.id.image_close)
        public ImageView image_close;

        @UiElement(R.id.layoutMain)
        public LinearLayout layoutMain;

        @UiElement(R.id.layoutChooseCus)
        public LinearLayout layoutChooseCus;

        @UiElement(R.id.nameCustomers)
        public TextView nameCustomers;

        @UiElement(R.id.nameLevelCuss)
        public TextView nameLevelCuss;

        @UiElement(R.id.recycler_view_list_order)
        public RecyclerView recycler_view_list_order;

        @UiElement(R.id.tvStore_end)
        public TextView tvStore_end;

        @UiElement(R.id.idPriceTemp)
        public TextView idPriceTemp;


        @UiElement(R.id.priceSale)
        public TextView priceSale;

        @UiElement(R.id.allPrice)
        public TextView allPrice;

        @UiElement(R.id.tienGiamTT)
        public CurrencyEditText tienGiamTT;

        @UiElement(R.id.btnChoose)
        public LinearLayout btnChoose;

        @UiElement(R.id.imageClose)
        public ImageView imageClearn;

        @UiElement(R.id.quetMV)
        public ImageView quetMV;

        @UiElement(R.id.content_frame)
        public FrameLayout content_frame;

        @UiElement(R.id.layout_scanbar_code)
        public RelativeLayout layout_scanbar_code;

        @UiElement(R.id.image_close_layout_scan)
        public ImageView image_close_layout_scan;

        @UiElement(R.id.layoutShowCustomer)
        public LinearLayout layoutShowCustomer;

    }
}