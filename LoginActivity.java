package com.example.volta_lang;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText signEmail, signPass;
    private Button login;
    private ImageView googleL;
    private ImageView facebookL, imageView7;
    private FirebaseAuth loginAuth;
     private ProgressBar progress;
     private TextView RegisterPage;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signEmail = findViewById(R.id.signEmail);
        signPass = findViewById(R.id.signPassword);
        login = findViewById(R.id.loginButton);
        progress = findViewById(R.id.progBar);
        imageView7 = findViewById(R.id.imageView7);

        loginAuth = FirebaseAuth.getInstance();

        RegisterPage=(TextView) findViewById(R.id.registerPage);


        imageView7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, Profile.class ));
            }
        });

//        googleL.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(LoginActivity.this, ContinueGoogleActivity.class));
//            }
//        });


        if (loginAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

      login.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              String email = signEmail.getText().toString().trim();
              String password = signPass.getText().toString().trim();

              if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                  // Display an error message if either email or password is empty
                  Toast.makeText(LoginActivity.this, "Email and password are required.", Toast.LENGTH_SHORT).show();
                  return;
              } else if (!isValidEmail(email)) {
                  // Display an error message if the email format is invalid
                  signEmail.setError("Invalid Email");

              } else if (password.length() < 6) {
                  // Display an error message if the password is too short
                  signPass.setError("Wrong password");
                  return;
              }
              progress.setVisibility(View.VISIBLE);

              loginAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                  @Override
                  public void onComplete(@NonNull Task<AuthResult> task) {
                      if (task.isSuccessful()){
                          Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                          startActivity(new Intent(getApplicationContext(), MainActivity.class));
                      }else{
                          Toast.makeText(LoginActivity.this, "Error Login" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                          progress.setVisibility(View.GONE);
                      }
                  }
              });
          }
      });



        RegisterPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));

            }
        });

    }

    private boolean isValidEmail(@NonNull String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return email.matches(emailPattern);
    }


}