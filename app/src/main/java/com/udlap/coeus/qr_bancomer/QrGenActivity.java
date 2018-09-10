package com.udlap.coeus.qr_bancomer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import org.json.JSONException;
import org.json.JSONObject;

public class QrGenActivity extends AppCompatActivity {

    String EditTextValue ;
    ImageView imgView;
    Button btnGen;
    public final static int QRcodeWidth = 500 ;
    TextView readTxt;
    Bitmap bmap;
    JSONObject creds;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_gen);
        imgView = (ImageView) findViewById(R.id.imgQR);
        btnGen = (Button) findViewById(R.id.bCreateQr);
        readTxt = (TextView) findViewById(R.id.opDetails);
        Intent call = getIntent();
        if(call.hasExtra(Intent.EXTRA_TEXT)){
            EditTextValue = call.getStringExtra(Intent.EXTRA_TEXT);
            readTxt.setText("Total: " + EditTextValue + " pesos");
        }

        btnGen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(EditTextValue != null){
                    try {

                        //creds.put("op", "0001");
                        //creds.put("clave", "012180004585820842");
                        //creds.put("type", "CL");
                        //creds.put("refa", "Mauricio");
                        //creds.put("country", "MX");
                        //creds.put("amount", EditTextValue);
                        //creds.put("currency", "MXN");

                        String toQR = "0001" + "," + "012180004585820842" + "," + "CL" + "," + "Mauricio" + "," + "MXN" + "," + EditTextValue;


                        bmap = TextToImageEncode(toQR);

                        imgView.setImageBitmap(bmap);

                    } catch (WriterException e) {
                        e.printStackTrace();
                    }

                }
                else{
                    readTxt.requestFocus();
                    Toast.makeText(QrGenActivity.this, "Please Enter Your Scanned Test" , Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    Bitmap TextToImageEncode(String Value) throws WriterException {
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(
                    Value,
                    BarcodeFormat.DATA_MATRIX.QR_CODE,
                    QRcodeWidth, QRcodeWidth, null
            );

        } catch (IllegalArgumentException Illegalargumentexception) {

            return null;
        }
        int bitMatrixWidth = bitMatrix.getWidth();

        int bitMatrixHeight = bitMatrix.getHeight();

        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;

            for (int x = 0; x < bitMatrixWidth; x++) {

                pixels[offset + x] = bitMatrix.get(x, y) ?
                        getResources().getColor(R.color.QRCodeBlackColor):getResources().getColor(R.color.QRCodeWhiteColor);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);

        bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }
}
