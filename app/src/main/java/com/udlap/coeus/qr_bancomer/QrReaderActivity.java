package com.udlap.coeus.qr_bancomer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONObject;

import java.util.HashMap;

public class QrReaderActivity extends AppCompatActivity {

    Button btnScan;
    Button btnGen;
    TextView readTxt;
    int i = 1;
    int acc;
    HashMap<String, String> hm;
    HashMap<String, String> dollars;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_reader);

        hm = new HashMap<>();
        dollars = new HashMap<>();

        generateHM();
        generatePrices();
        acc = 0;
        btnScan = (Button) findViewById(R.id.bScan);
        btnGen = (Button) findViewById(R.id.bGenQR);
        readTxt = (TextView) findViewById(R.id.txtContent);
        readTxt.setMovementMethod(new ScrollingMovementMethod());

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                IntentIntegrator integrator = new IntentIntegrator(QrReaderActivity.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrator.setCameraId(0);
                integrator.setBeepEnabled(true);
                integrator.setBarcodeImageEnabled(false);
                integrator.setOrientationLocked(true);
                integrator.initiateScan();
            }
        });

        btnGen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(QrReaderActivity.this, QrGenActivity.class).putExtra(Intent.EXTRA_TEXT, String.valueOf(acc));
                startActivity(intent);
            }
        });

    }

    private void generateHM(){
            hm.put("052548596200", "Vasos Desechables");
            hm.put("7501055352593", "Agua Fresa");
            hm.put("7501055300341", "Sprite");
            hm.put("7501055300075", "Coca Cola");
            hm.put("7501055339976", "Sidral Mundet");

    }

    private void generatePrices(){
        dollars.put("052548596200", "36");
        dollars.put("7501055352593", "13");
        dollars.put("7501055300341", "11");
        dollars.put("7501055300075", "8");
        dollars.put("7501055339976", "10");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Log.e("ScanError", "Cancelled scan");

            } else {
                Log.e("Scan", "Scanned");
                String res = result.getContents();
                String name = (hm.get(res) != null) ? hm.get(res) : res;
                int price = (dollars.get(res) != null) ? Integer.parseInt(dollars.get(res)) : 0;

                readTxt.append("\n" + i + ") "+ name + ": " + price);
                i++;

                    acc += price;

                Toast.makeText(this, "Scanned: " + name, Toast.LENGTH_LONG).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}
