package com.app.mit_prototype;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class QrCodeScanner extends AppCompatActivity implements ZXingScannerView.ResultHandler
{
    private ZXingScannerView scannerView;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent = new Intent(getApplicationContext(), Dashboard.class);
        startActivityForResult(intent, 0);
        return  true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //initialize the scanner view
        scannerView = new ZXingScannerView(this);
        //Set the scanner view as the content view
        setContentView(scannerView);

            Dexter.withContext(getApplicationContext())
                    .withPermission(Manifest.permission.CAMERA)
                    .withListener(new PermissionListener() {
                        @Override
                        public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                            scannerView.startCamera();
                        }

                        @Override
                        public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                            permissionToken.continuePermissionRequest();
                        }
                    }).check();

//        Toolbar actionBar = findViewById(R.id.toolbar_qr);
//        setSupportActionBar(actionBar);
//
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void handleResult(Result rawResult) {
        if(Patterns.WEB_URL.matcher(rawResult.getText()).matches()) {
            // Open URL
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(rawResult.getText()));
            startActivity(browserIntent);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();
        scannerView.setResultHandler(this);
        scannerView.startCamera();
    }

    public void flashON(View v) {
        scannerView.setFlash(true);
    }

    public void flashOFF(View v){
        scannerView.setFlash(false);
    }
}