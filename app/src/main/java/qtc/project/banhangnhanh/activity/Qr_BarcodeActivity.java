package qtc.project.banhangnhanh.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.Result;
import com.google.zxing.Writer;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.Hashtable;

import b.laixuantam.myaarlibrary.helper.MyLog;
import me.dm7.barcodescanner.zxing.ZXingScannerView;
import qtc.project.banhangnhanh.R;

public class Qr_BarcodeActivity extends Activity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView mScannerView;

    private static final int PERMISSION_REQUEST_CODE = 50;
    private EditText editTextProductId;
    private Button buttonGenerate, buttonScan;
    private ImageView imageViewResult;
    String productId;
    String barcodeResult;

    ViewGroup contentFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr__barcode);

        initView();
    }


    private void initView() {

        contentFrame = (ViewGroup) findViewById(R.id.content_frame);
        mScannerView = new ZXingScannerView(this);
        contentFrame.addView(mScannerView);
        mScannerView.setResultHandler(this);

        editTextProductId = findViewById(R.id.editTextProductId);
        imageViewResult = findViewById(R.id.imageViewResult);
        buttonGenerate = findViewById(R.id.buttonGenerate);
        buttonGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonGenerate_onClick();
            }
        });
        buttonScan = findViewById(R.id.buttonScan);
        buttonScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(Qr_BarcodeActivity.this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Qr_BarcodeActivity.this,
                            new String[]{Manifest.permission.CAMERA}, 1101);
                } else {
                    mScannerView.startCamera();
                }
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    private void buttonGenerate_onClick() {
        try {
            String productId = editTextProductId.getText().toString();
            Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<EncodeHintType, ErrorCorrectionLevel>();
            hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
            Writer codeWriter;
            codeWriter = new Code128Writer();
            BitMatrix byteMatrix = codeWriter.encode(productId, BarcodeFormat.CODE_128, 400, 200, hintMap);
            int width = byteMatrix.getWidth();
            int height = byteMatrix.getHeight();
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    bitmap.setPixel(i, j, byteMatrix.get(i, j) ? Color.BLACK : Color.WHITE);
                }
            }
            imageViewResult.setImageBitmap(bitmap);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void buttonScan_onClick(View view) {
    }


    @Override
    public void handleResult(Result rawResult) {
        MyLog.e("Qr_BarcodeActivity", rawResult.getText()); // Prints scan results
        MyLog.e("Qr_BarcodeActivity", rawResult.getBarcodeFormat().toString());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,  String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1101:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mScannerView.startCamera();
                } else {
                    Toast.makeText(this, "Please grant camera permission to use the QR Scanner", Toast.LENGTH_SHORT).show();
                    openAndroidPermissionsMenu();
                }
                return;
        }
    }
    private void openAndroidPermissionsMenu() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + getApplicationContext().getPackageName()));
            startActivity(intent);
        }
    }
}