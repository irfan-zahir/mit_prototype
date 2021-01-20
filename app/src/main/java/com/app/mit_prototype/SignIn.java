package com.app.mit_prototype;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class SignIn extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private EditText usernameInput, passwordInput;
    private Button signInButton;
    private TextView switchAuthScreen;

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin_screen);

        signInButton = findViewById(R.id.signin_button);
        usernameInput = findViewById(R.id.signin_username);
        passwordInput = findViewById(R.id.signin_password);
        switchAuthScreen = findViewById(R.id.switch_register);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = firebaseAuth.getInstance();

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = passwordInput.getText().toString().trim();
                String username = usernameInput.getText().toString().trim();

                signInButton.setEnabled(false);

                firebaseFirestore.collection("users")
                        .whereEqualTo("username", username).limit(1).get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        String email;
                        if(task.isSuccessful()){
                            if(task.getResult().isEmpty()){
                                //no account found
                                Log.w("login", "no account found");
                            }else{
                                List<DocumentSnapshot> documents = task.getResult().getDocuments();
                                email = documents.get(0).get("email").toString().trim();
                                firebaseAuth.signInWithEmailAndPassword(email, password)
                                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if(task.isSuccessful()){
                                            FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                                            intent = new Intent(SignIn.this, MainActivity.class);
                                            intent.putExtra("currentUser", currentUser);
                                            startActivity(intent);
                                            Log.w("login", "success login");
                                        }
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w("login", "failed login");
                                    }
                                });
                            }

                        }
                    }
                });
            }
        });

        switchAuthScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(SignIn.this, SignUp.class);
                startActivity(intent);
            }
        });
    }
}