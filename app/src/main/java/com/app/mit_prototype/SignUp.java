package com.app.mit_prototype;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private EditText usernameInput, emailInput, phoneInput, passwordInput, cPasswordInput;
    private RadioGroup gendergroup;
    private RadioButton selectedRadioButton;
    private TextView switchAuthScreen;
    private Button registerButton;

    String email, password, cPassword, gender, username, phone;

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_screen);

        Log.d("register", "started register");

        usernameInput = findViewById(R.id.signup_username);
        emailInput = findViewById(R.id.signup_email);
        phoneInput = findViewById(R.id.signup_phone);
        passwordInput = findViewById(R.id.signup_password);
        cPasswordInput = findViewById(R.id.signup_cpassword);
        switchAuthScreen = findViewById(R.id.switch_login);
        registerButton = findViewById(R.id.signup_button);

        gendergroup = findViewById(R.id.radio_gender);
        int selectedID = gendergroup.getCheckedRadioButtonId();

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = firebaseAuth.getInstance();


        selectedRadioButton = findViewById(selectedID);
        gender = selectedRadioButton.getText().toString().trim();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = emailInput.getText().toString().trim();
                phone = phoneInput.getText().toString().trim();
                password = passwordInput.getText().toString().trim();
                cPassword = cPasswordInput.getText().toString().trim();
                username = usernameInput.getText().toString().trim();

                if(validateEmail(email) && validatePhone(phone) && validatePassword(password, cPassword)
                        && !username.isEmpty()){

                    try {
                        Log.d("register", "email " + email);
                        Log.d("register", "password " + password);
                        firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Log.d("register", "create sucess");

                                    String uid = task.getResult().getUser().getUid();
                                    Map<String, Object> user = new HashMap<>();
                                    user.put("username", username); user.put("email", email);
                                    user.put("phone", phone); user.put("gender", gender);

                                    firebaseFirestore.collection("users")
                                        .document(uid).set(user)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    intent = new Intent(SignUp.this, MainActivity.class);
                                                    startActivity(intent);
                                                    Log.d("register", "success");
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.w("register", "failed");
                                                }
                                            });
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("register", "onFailure: failed create account");
                            }
                        });
                    }catch (Exception e){
                        Log.d("register", "onClick: exeption" + e.toString());
                    }
                }
                Log.d("register", "register clicked. out if statement");
            }
        });

        //validation starts here

        switchAuthScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(SignUp.this, SignIn.class);
                startActivity(intent);
            }
        });
    }

    protected boolean validateEmail(String email){
        if(!email.isEmpty()){
            return true;
        }
        return  false;
    }

    protected boolean validatePassword(String password, String cPassword){
        if(password.length() > 5){
            if(password.equals(cPassword)){
                return true;
            }
        }
        return  false;
    }

    protected boolean validatePhone(String phone){

        if(!phone.isEmpty()){
            return true;
        }
        return false;
    }
}