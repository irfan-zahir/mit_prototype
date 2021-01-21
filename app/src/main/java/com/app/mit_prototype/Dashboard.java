package com.app.mit_prototype;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;

public class Dashboard extends AppCompatActivity {

    ImageView signOutButton, qrButton, speechButton;
    private FirebaseAuth firebaseAuth;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        firebaseAuth = firebaseAuth.getInstance();

        signOutButton = findViewById(R.id.sign_out);
        qrButton = findViewById(R.id.function_qr);
        speechButton = findViewById(R.id.function_stt);

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                intent = new Intent(Dashboard.this, MainActivity.class);
                startActivity(intent);
            }
        });

        qrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(Dashboard.this, QrCodeScanner.class);
                startActivity(intent);
            }
        });

        speechButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(Dashboard.this, SpeechToText.class);
                startActivity(intent);
            }
        });
    }
}