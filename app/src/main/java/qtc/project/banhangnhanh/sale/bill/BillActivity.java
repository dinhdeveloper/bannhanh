package qtc.project.banhangnhanh.sale.bill;

import android.app.Activity;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;

import b.laixuantam.myaarlibrary.helper.AccentRemove;
import qtc.project.banhangnhanh.R;
import qtc.project.banhangnhanh.activity.LoginActivity;
import qtc.project.banhangnhanh.activity.SaleHomeActivity;
import qtc.project.banhangnhanh.admin.dependency.AppProvider;
import qtc.project.banhangnhanh.sale.model.ListOrderModel;

public class BillActivity extends Activity {
    private String TAG = "Main Activity";
    EditText message;
    Button btnPrint, btnBill, btnDonate;
    //AdView viewAdmob;

    byte FONT_TYPE;
    private static BluetoothSocket btsocket;
    private static OutputStream outputStream;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_sale);
        message = (EditText) findViewById(R.id.txtMessage);
        btnPrint = (Button) findViewById(R.id.btnPrint);
        btnBill = (Button) findViewById(R.id.btnBill);
        btnDonate = (Button) findViewById(R.id.btnDonate);
        //viewAdmob = (AdView)findViewById(R.id.view_admob);
        FullScreencall();

        if (outputStream == null) {
            btnBill.setText("Kết nối máy in");
        }

        btnPrint.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                printDemo();
            }
        });
        btnBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printBill();
            }
        });

        btnDonate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BillActivity.this, LoginActivity.class));
            }
        });


    }

    protected void printBill() {
        if (btsocket == null) {
            Intent BTIntent = new Intent(getApplicationContext(), BTDeviceList.class);
            this.startActivityForResult(BTIntent, BTDeviceList.REQUEST_CONNECT_BT);
        } else {
            OutputStream opstream = null;
            try {
                opstream = btsocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            outputStream = opstream;

            if (outputStream != null) {
                btnPrint.setText("In hóa đơn");
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
                printCustom("QTC TEK", 1, 1);
                printNewLine();
                printCustom(AccentRemove.removeAccent("Binh Thanh, TP. HCM"), 0, 1);
                printCustom("Hot Line: +8490909009", 0, 1);
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

                printCustom("Ngay: " + day + "/" + month + "/" + year + " " + dateTime[1], 1, 0);
                printNewLine();
                printCustom("Nhan Vien: " + AccentRemove.removeAccent(AppProvider.getPreferences().getUserModel().getFull_name()), 1, 0);
                printCustom("-----------------------", 0, 1);
                if (getIntent().getExtras() != null) {
                    List<ListOrderModel> list = (List<ListOrderModel>) getIntent().getExtras().get("model");

                    long tien_khach_dua = (long) getIntent().getExtras().get("tien_khach_dua");
                    long tien_thoi_lai = (long) getIntent().getExtras().get("tien_thoi_lai");
                    long tongtienphaitra = (long) getIntent().getExtras().get("tong_tien");
                    String pattern = "###,###,###";
                    DecimalFormat decimalFormat = new DecimalFormat(pattern);

                    printNewLine();

                    long temp = 0;
                    long thanhtien = 0;
                    for (int i = 0; i < list.size(); i++) {

                        String name = AccentRemove.removeAccent(list.get(i).getNameProduct());
                        String price = decimalFormat.format(Long.valueOf(list.get(i).getPriceProduct()));
                        String quantity = list.get(i).getQuantityProduct();

                        printCustom((i + 1) + ". " + name, 0, 0);
                        printNewLine();
                        printCustom("x" + quantity,0,0);
                        printCustom(price, 0, 2);

                        printNewLine();
                        temp = Long.valueOf(list.get(i).getPriceProduct()) * Integer.valueOf(list.get(i).getQuantityProduct());
                        thanhtien += temp;
                    }
                    printCustom("-----------------------", 0, 1);

                    //long tien_giam = 100 - (((tien_khach_dua - tien_thoi_lai) * 100) / thanhtien);

                    long phantram_phaitra = (Long.valueOf(tongtienphaitra) * 100) / thanhtien;
                    long phantram_giamgia = 100 - phantram_phaitra;

                    long tien_giam = ((phantram_giamgia*thanhtien)/100);

                    printCustom("Tong: " + decimalFormat.format(thanhtien), 1, 0);
                    printNewLine();
                    printCustom("Giam Gia: " + decimalFormat.format(tien_giam), 1, 0);
                    printNewLine();
                    printCustom("-----------------------", 0, 1);
                    printNewLine();
                    printCustom("Thanh Toan: " + decimalFormat.format(tongtienphaitra), 1, 0);
                    printNewLine();
                    printCustom("Tien Mat: " + decimalFormat.format(tien_khach_dua), 1, 0);
                    printNewLine();
                    printCustom("Tien Thoi Lai: " + decimalFormat.format(tien_thoi_lai), 1, 0);

                    printCustom("-----------------------", 0, 1);
                    printNewLine();

                    printCustom("Xin Cam On Quy Khach", 0, 1);
                    printCustom("Hen Gap Lai", 0, 1);
                    printCustom("",0,1);
                    printCustom("",0,1);
                    printCustom("",0,1);
                }
                outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    protected void printDemo() {
        if (btsocket == null) {
            Intent BTIntent = new Intent(getApplicationContext(), BTDeviceList.class);
            this.startActivityForResult(BTIntent, BTDeviceList.REQUEST_CONNECT_BT);
        } else {
            OutputStream opstream = null;
            try {
                opstream = btsocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            outputStream = opstream;

            //print command
            try {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                outputStream = btsocket.getOutputStream();

                byte[] printformat = {0x1B, 0 * 21, FONT_TYPE};
                //outputStream.write(printformat);

                //print title
                printUnicode();
                //print normal text
                printCustom(message.getText().toString(), 0, 0);
                //printPhoto(R.drawable.company);
                printNewLine();
                printText("     >>>>   Thank you  <<<<     "); // total 32 char in a single line
                //resetPrint(); //reset printer
                printUnicode();
                printNewLine();
                printNewLine();

                outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
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

    //print unicode
    public void printUnicode() {
        try {
            outputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
            printText(Utils.UNICODE_TEXT);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
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

    public static void resetPrint() {
        try {
            outputStream.write(PrinterCommands.ESC_FONT_COLOR_DEFAULT);
            outputStream.write(PrinterCommands.FS_FONT_ALIGN);
            outputStream.write(PrinterCommands.ESC_ALIGN_LEFT);
            outputStream.write(PrinterCommands.ESC_CANCEL_BOLD);
            outputStream.write(PrinterCommands.LF);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //print text
    private void printText(String msg) {
        try {
            // Print normal text
            outputStream.write(msg.getBytes());
        } catch (IOException e) {
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


    private String leftRightAlign(String str1, String str2) {
        String ans = str1 + str2;
        if (ans.length() < 50) {
            int n = (50 - str1.length() + str2.length());
            ans = str1 + new String(new char[n]).replace("\0", " ") + str2;
        }
        return ans;
    }


    private String[] getDateTime() {
        final Calendar c = Calendar.getInstance();
        String dateTime[] = new String[2];
        dateTime[0] = c.get(Calendar.DAY_OF_MONTH) + "/" + c.get(Calendar.MONTH) + "/" + c.get(Calendar.YEAR);
        dateTime[1] = c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE);
        return dateTime;
    }

    @Override
    protected void onDestroy() {
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            btsocket = BTDeviceList.getSocket();
            if (btsocket != null) {
                printText(message.getText().toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void FullScreencall() {
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, SaleHomeActivity.class));
        finish();
    }
}