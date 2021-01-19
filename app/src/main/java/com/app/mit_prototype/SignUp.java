package com.app.mit_prototype;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class SignUp extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private EditText usernameInput, emailInput, phoneInput, passwordInput, cPasswordInput;
    private RadioGroup gendergroup;
    private RadioButton selectedRadioButton;
    private TextView switchAuthScreen;

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_screen);

        usernameInput = findViewById(R.id.signup_username);
        emailInput = findViewById(R.id.signup_email);
        phoneInput = findViewById(R.id.signup_phone);
        passwordInput = findViewById(R.id.signup_password);
        cPasswordInput = findViewById(R.id.signup_cpassword);
        gendergroup = findViewById(R.id.radio_gender);
        switchAuthScreen = findViewById(R.id.switch_login);

        //validation starts here

        switchAuthScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(SignUp.this, SignIn.class);
                startActivity(intent);
            }
        });
    }
}