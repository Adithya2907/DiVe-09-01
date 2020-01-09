package com.example.dive1010;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import java.io.IOException;

public class QRScan extends AppCompatActivity {
    Button back, main_menu;
    SurfaceView camera;
    BarcodeDetector barcode;
    CameraSource cameraSource;
    SurfaceHolder holder;//holds the camera

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrscan);

        //activity which scans the QR and returns the value of the QR to the Scan activity
        back=findViewById(R.id.back);
        main_menu=findViewById(R.id.main_menu);
        camera=findViewById(R.id.camera);
        camera.setZOrderMediaOverlay(true);
        holder=camera.getHolder();
        barcode= new BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.QR_CODE).build();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (getApplicationContext(), Scan.class);
                startActivity(intent);
                finish();
            }
        });

        main_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //makes sure that the barcode scanner is operational
        if(!barcode.isOperational()){
            Toast.makeText(getApplicationContext(), "Couldn't set up the scanner!", Toast.LENGTH_SHORT).show();
            this.finish();
        }

        //creates the builder for the camerasource
        CameraSource.Builder builder = new CameraSource.Builder(getApplicationContext(), barcode)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedPreviewSize(1600, 1024)
                .setAutoFocusEnabled(true)
                .setRequestedFps(15.0f);
        cameraSource = builder.build();

        //places the camera in the camera holder
        camera.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try{
                    if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(camera.getHolder());//starts camera, so that the barcode can be scanned
                    }
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
            }
        });

        barcode.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
            }
            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes =detections.getDetectedItems();
                if(barcodes.size()>0){
                    Intent intent = new Intent();
                    intent.putExtra("barcode", barcodes.valueAt(0));
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
    }
}
