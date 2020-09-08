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
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import b.laixuantam.myaarlibrary.base.BaseUiContainer;
import b.laixuantam.myaarlibrary.base.BaseView;
import b.laixuantam.myaarlibrary.helper.KeyboardUtils;
import b.laixuantam.myaarlibrary.widgets.currencyedittext.CurrencyEditText;
import me.dm7.barcodescanner.zxing.ZXingScannerView;
import qtc.project.banhangnhanh.R;
import qtc.project.banhangnhanh.activity.SaleHomeActivity;
import qtc.project.banhangnhanh.admin.model.CustomerModel;
import qtc.project.banhangnhanh.admin.model.PackageInfoModel;
import qtc.project.banhangnhanh.sale.adapter.home.ListItemClickAdapter;
import qtc.project.banhangnhanh.sale.adapter.product.ProductAdapter;
import qtc.project.banhangnhanh.sale.bill.BillActivity;
import qtc.project.banhangnhanh.sale.database.DatabaseProvider;
import qtc.project.banhangnhanh.sale.model.ListOrderModel;
import qtc.project.banhangnhanh.sale.model.ProductModel;

public class FragmentSaleHomeView extends BaseView<FragmentSaleHomeView.UIContainer> implements FragmentSaleHomeViewInterface, ZXingScannerView.ResultHandler{
    SaleHomeActivity activity;
    FragmentSaleHomeViewCallback callback;
    String name = "";
    String level = null;
    String discount = null;
    String gia = "";
    String id_customer = null;
    String tongtien = null;
    int tonkho = 0;
    private ZXingScannerView mScannerView;
    private Timer timer = new Timer();
    private final long DELAY = 1000; // in ms

    int size = 0;
    long tienthoilai = 0;
    long tienkhachdua;
    HashMap<String, ListOrderModel> hashMap = new HashMap<>();
    ArrayList<ListOrderModel> arList = new ArrayList<>();

    ArrayList<ProductModel> arrayList = new ArrayList<>();
    boolean enableLoadMore = true;
    ProductAdapter productAdapter;
    int TYLE_CODE_GEN = 2;
    ListItemClickAdapter clickAdapter;

    DatabaseProvider provider;
    @Override
    public void init(SaleHomeActivity activity, FragmentSaleHomeViewCallback callback) {
        this.activity = activity;
        this.callback = callback;
        KeyboardUtils.setupUI(getView(),activity);
        ui.title_header.setText("Home");
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
        getDataSQLite();
        onClick();
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
            ui.layoutChooseCus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (callback != null) {
                        callback.goToChooseCustomer();
                    }
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
                        //dem += 1;
                        String old_quantity = hashMap.get(model.getId()).getQuantityProduct();
                        int new_quantity = Integer.valueOf(old_quantity) + 1;
                        hashMap.get(model.getId()).setQuantityProduct(String.valueOf(new_quantity));
                        provider.updateRecord(hashMap.get(model.getId()));
                        tinhTong();
                    }
                }

                @Override
                public void onClickGiamSoLuong(ListOrderModel model) {
                    if (hashMap.get(model.getId()) != null) {
                        //dem -= 1;
                        String old_quantity = hashMap.get(model.getId()).getQuantityProduct();
                        int new_quantity = Integer.valueOf(old_quantity) - 1;
                        hashMap.get(model.getId()).setQuantityProduct(String.valueOf(new_quantity));
                        if (new_quantity <= 1) {
                            hashMap.get(model.getId()).setQuantityProduct(String.valueOf(1));
                        }
                        provider.updateRecord(hashMap.get(model.getId()));
                        tinhTong();
                    }
                }

                @Override
                public void onClickXoaItem(ListOrderModel items) {
                    if (hashMap.get(items.getId()) != null) {
                        provider.deleteRow(hashMap.get(items.getId()).getId());
                        hashMap.remove(items.getId());
                        clickAdapter.remove(items);

                        tinhTong();
                    }
                }

                @Override
                public void onChangeSoLuong(ListOrderModel item) {
                    if (hashMap.get(item.getId()) != null) {
                        hashMap.get(item.getId()).setQuantityProduct(item.getQuantityProduct());
                        if (finalTongTonKho != null) {
                            if (Integer.valueOf(hashMap.get(item.getId()).getInventory()) <= Integer.valueOf(finalTongTonKho)) {
                                tinhTong();
                                provider.updateRecord(hashMap.get(item.getId()));
                            } else {
                                Toast.makeText(activity, "Tồn kho không đủ", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            tinhTong();
                        }
                    }
                }
            });
            clickAdapter.notifyDataSetChanged();
            tinhTong();
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
                                if (tienthoilai < 0) {
                                    priceItem.setText(null);
                                    tien_thoi_lai_khach.setText(null);
                                    Toast.makeText(activity, "Hãy nhập đúng tiền", Toast.LENGTH_SHORT).show();
                                } else {
                                    callback.tinhTien(arList, id_customer, tongtien, discount);
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
                                        callback.tinhTien(arList, id_customer, tongtien, discount);
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
    public void initView(CustomerModel model) {
        if (model!=null){
            id_customer = model.getId();
            ui.nameCustomers.setText(model.getFull_name());
            ui.nameLevelCuss.setText(model.getLevel_name());
            discount = model.getLevel_discount();
            if (model.getLevel_discount() == null) {
                discount = String.valueOf("0");
            } else {
                discount = String.valueOf(model.getLevel_discount());
                tinhTong();
            }
            ui.imageClearn.setOnClickListener(v -> {
                ui.nameCustomers.setText(null);
                ui.nameLevelCuss.setText(null);
                model.setId(null);
                model.setFull_name(null);
                model.setLevel_name(null);
            });
        }
    }

    @Override
    public void initViewProduct(ArrayList<ProductModel> list) {
        if (list!=null){
            for (ProductModel model:list){
                addItemCart(model,model.getListDataProduct().get(0));
            }
        }
        else {
            Toast.makeText(activity, "Sản phẩm không tồn tại", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void truyenIntent(ArrayList<ListOrderModel> arList) {
        Intent intent = new Intent(activity, BillActivity.class);
        Bundle bundle = new Bundle();
        if (tienkhachdua == 0) {
            tienkhachdua = Long.valueOf(tongtien);
        }
        bundle.putLong("tong_tien", Long.valueOf(tongtien));
        bundle.putLong("tien_khach_dua", tienkhachdua);
        bundle.putLong("tien_thoi_lai", tienthoilai);
        bundle.putSerializable("model", arList);
        intent.putExtras(bundle);
        activity.startActivity(intent);
    }


    @Override
    public void initViewProductClick(ProductModel model) {
        addItemCart(model,model.getListDataProduct().get(0));
    }

    private void onClick() {
        //dong layout scanbar code
        ui.image_close_layout_scan.setOnClickListener(v -> {
            stopScan();
            ui.layout_scanbar_code.setVisibility(View.GONE);
        });
        ui.imageNavLeft.setOnClickListener(v -> {
            if (callback!=null){
                callback.callNav();
            }
        });
        ui.image_close.setOnClickListener(v -> {
            ui.edit_filter.setText(null);
        });
        ui.edit_filter.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId==EditorInfo.IME_ACTION_SEARCH){
                    callback.goToProductList(ui.edit_filter.getText().toString());
                }
                return false;
            }
        });

        ui.layoutChooseCus.setOnClickListener(v -> {
            if (callback!=null)
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

    private void initScanBarcode() {
        mScannerView = new ZXingScannerView(activity);
        mScannerView.setAutoFocus(true);
        mScannerView.setResultHandler(this);
        ui.content_frame.addView(mScannerView);
        mScannerView.startCamera();
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
        }else {
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
            mScannerView.startCamera();
            mScannerView.startCamera(1);
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

    private void editextSearch(String resultCode) {
        if (!resultCode.trim().isEmpty()){
            ui.edit_filter.addTextChangedListener(new TextWatcher() {
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
                                        addCart(ui.edit_filter.getText().toString());
                                    }
                                }
                            });
                        }

                    }, DELAY);
                }
            });
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
    private void addItemCart(ProductModel model, PackageInfoModel infoModel) {

        if (hashMap.get(infoModel.getPack_id()) != null) {
            tonkho = Integer.valueOf(model.getTotal_stock());
            String old_quantity = hashMap.get(infoModel.getPack_id()).getQuantityProduct();
            int new_quantity = Integer.valueOf(old_quantity) + 1;
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
        //save vao sharedPreferences
//        arList.clear();
//        Set<String> keySet = hashMap.keySet();
//        for (String key : keySet) {
//            arList.add(hashMap.get(key));
//            AppProvider.getPreferences().saveMap(key, hashMap.get(key));
//            AppProvider.getPreferences().saveKeyMap(keySet);
//        }
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
                    //dem += 1;
                    String old_quantity = hashMap.get(model.getId()).getQuantityProduct();
                    int new_quantity = Integer.valueOf(old_quantity) + 1;
                    hashMap.get(model.getId()).setQuantityProduct(String.valueOf(new_quantity));
                    provider.updateRecord(hashMap.get(model.getId()));
                    tinhTong();
                }
            }

            @Override
            public void onClickGiamSoLuong(ListOrderModel model) {
                if (hashMap.get(model.getId()) != null) {
                    //dem -= 1;
                    String old_quantity = hashMap.get(model.getId()).getQuantityProduct();
                    int new_quantity = Integer.valueOf(old_quantity) - 1;
                    hashMap.get(model.getId()).setQuantityProduct(String.valueOf(new_quantity));
                    if (new_quantity <= 1) {
                        hashMap.get(model.getId()).setQuantityProduct(String.valueOf(1));
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
                }
            }

            @Override
            public void onChangeSoLuong(ListOrderModel item) {
                if (hashMap.get(item.getId()) != null) {
                    hashMap.get(item.getId()).setQuantityProduct(item.getQuantityProduct());
                    if (Integer.valueOf(hashMap.get(item.getId()).getInventory()) <= Integer.valueOf(model.getTotal_stock())) {
                        tinhTong();
                    } else {
                        Toast.makeText(activity, "Tồn kho không đủ", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        clickAdapter.notifyDataSetChanged();
        tinhTong();
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
                    AlertDialog dialog = alert.create();
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();

                    imageNavLeft.setOnClickListener(v -> {
                        dialog.dismiss();
                    });

                    btnExit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
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
                                    callback.tinhTien(arList, id_customer, tongtien, discount);
                                    dialog.dismiss();
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
                                    callback.tinhTien(arList, id_customer, tongtien, discount);
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
                priceTemp = Long.valueOf(hashMap.get(key).getQuantityProduct()) * Long.valueOf(hashMap.get(key).getPriceProduct());
                total += priceTemp;
            }
            ui.idPriceTemp.setText(decimalFormat.format(total) + " đ");
            if (discount != null) {
                long tien_giam = (Long.valueOf(total)*Long.valueOf(discount))/100;
                ui.priceSale.setText(decimalFormat.format(Long.valueOf(tien_giam)) + " đ");

                long tiengiam = (total * Integer.parseInt(discount)) / 100;
                tongtien = String.valueOf(total - tiengiam);
                ui.allPrice.setText(decimalFormat.format(Long.valueOf(tongtien)) + " đ");
            }
            //khach vang lai
            else if (discount == null) {
                discount = "0";
                ui.priceSale.setText(decimalFormat.format(Integer.parseInt(discount)) + " %");
                long tiengiam = (total * Integer.parseInt(discount)) / 100;
                tongtien = String.valueOf(total - tiengiam);
                ui.allPrice.setText(decimalFormat.format(Long.valueOf(tongtien)) + " đ");
            }
        } catch (Exception e) {
            Log.e("Ex", e.getMessage());
        }
    }
    @Override
    public int getViewId() {
        return R.layout.layout_fragment_home_sale;
    }


    public class UIContainer extends BaseUiContainer {
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

        @UiElement(R.id.fragmentHome)
        public RelativeLayout fragmentHome;

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

        @UiElement(R.id.idPriceTemp)
        public TextView idPriceTemp;

        @UiElement(R.id.priceSale)
        public TextView priceSale;

        @UiElement(R.id.allPrice)
        public TextView allPrice;

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


    }
}