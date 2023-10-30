package com.example.volta_lang;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;


public class RegisterActivity extends AppCompatActivity {
     TextView GoLogin;
     EditText signName;
     EditText signNumber;
     EditText signEmail;
     EditText signPassword;
     EditText signConfirm;
     Button registerButton;
     FirebaseAuth registerAuth;
     ProgressBar progressBar;
     FirebaseFirestore registerStore;
    private String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        signName= findViewById(R.id.signName);
        signNumber = findViewById(R.id.signNumber);
        signEmail = findViewById(R.id.signEmail);
        signPassword = findViewById(R.id.signPassword);
        signConfirm = findViewById(R.id.signConfirmPassword);
        registerButton= findViewById(R.id.registerButton);
        progressBar = findViewById(R.id.progressBar);
         registerAuth = FirebaseAuth.getInstance();
         registerStore = FirebaseFirestore.getInstance();

        GoLogin=(TextView) findViewById(R.id.goLogin);

        if (registerAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), Profile.class));
            finish();
        }


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = signEmail.getText().toString().trim();
                String password = signPassword.getText().toString().trim();
                String ConfirmPassword = signConfirm.getText().toString().trim();
                String name = signName.getText().toString().trim();
                String phone = signNumber.getText().toString().trim();

                if( TextUtils.isEmpty(name)) {
                    signName.setError("Fill in the blank");
                }if (TextUtils.isEmpty(phone)) {
                    signNumber.setError("Fill in the blank");
                } else {
                    // Validate the phone number format
                    if (!isValidPhilippinePhoneNumber(phone)) {
                        signNumber.setError("Invalid phone number format");
                    }
                }if(TextUtils.isEmpty(email)){
                    signEmail.setError("Email Required");

                }if (TextUtils.isEmpty(password) || password.length() < 6) {
                    signPassword.setError("Password is must be valid");
                }
                if(TextUtils.isEmpty(ConfirmPassword)) {
                    signConfirm.setError("fill in the blank");
                  return;
                }


                if(TextUtils.isEmpty(password) || password.length() < 6) {
                    signPassword.setError("Password is must be valid");

                }
                if (!password.equals(ConfirmPassword)){
                    signConfirm.setError("Password don't matched");
                    return;
                }{

                }

                progressBar.setVisibility(View.VISIBLE);


                registerAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                     if (task.isSuccessful()){
                         Toast.makeText(RegisterActivity.this, "Additional Registration", Toast.LENGTH_SHORT).show();
                         userID = registerAuth.getCurrentUser().getUid();
                         DocumentReference documentReference = registerStore.collection("users").document(userID);
                         Map<String, Object> user = new HashMap<>();
                         user.put("name",name);
                         user.put("email", email);
                         user.put("phone", phone);

                         documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                             @Override
                             public void onSuccess(Void unused) {

                                 Log.d(TAG,"onSuccess: created for user" +userID);

                             }
                         });

                         startActivity(new Intent(getApplicationContext(), VerificationActivity.class));
                     }else{
                         Toast.makeText(RegisterActivity.this, " " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                         progressBar.setVisibility(View.GONE);
                     }
                    }
                });
            }
        });




        GoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });
    }

    // Function to validate Philippine phone numbers
    private boolean isValidPhilippinePhoneNumber(String phoneNumber) {
        // Check if the phone number starts with "09" or "+63" and has 11 digits
        return phoneNumber.matches("^(09|\\+639)\\d{9}$");
    }
}