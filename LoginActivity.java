package com.example.volta_lang.Login;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.example.volta_lang.Admin.AdminSite;
import com.example.volta_lang.User.MainActivity;
import com.example.volta_lang.User.Profile;
import com.example.volta_lang.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    private EditText signEmail, signPass;
    private Button login;
    private FirebaseAuth loginAuth;
     private ProgressBar progress;
     private TextView RegisterPage;

     FirebaseFirestore fStore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signEmail = findViewById(R.id.signEmail);
        signPass = findViewById(R.id.signPassword);
        login = findViewById(R.id.contactHost);
        progress = findViewById(R.id.progBar);

        fStore = FirebaseFirestore.getInstance();
        loginAuth = FirebaseAuth.getInstance();


        RegisterPage=(TextView) findViewById(R.id.registerPage);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        if (user != null) {
            // User is signed in
            // Check if the user is an admin
            DocumentReference userRef = FirebaseFirestore.getInstance().collection("users").document(user.getUid());
            userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {
                        if (documentSnapshot.getString("isAdmin") != null) {
                            // User is an admin
                            startActivity(new Intent(LoginActivity.this, AdminSite.class));
                            finish();
                        }
                    }
                }
            });
        }

        if (FirebaseAuth.getInstance().getCurrentUser() != null){
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
                                       loginAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                           @Override
                                           public void onSuccess(AuthResult authResult) {

                                               Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                               checkAdminListener(authResult.getUser().getUid());

                                           }

                                       }).addOnFailureListener(new OnFailureListener() {
                                           @Override
                                           public void onFailure(@NonNull Exception e) {
                                               Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                               progress.setVisibility(View.INVISIBLE);                                 }
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
    private void checkAdminListener(String uid) {
        DocumentReference df = fStore.collection("users").document(uid);

        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {


                Log.d("TAG", "onSuccess" +documentSnapshot.getData());

               if (documentSnapshot.getString("isAdmin") != null){
                   startActivity(new Intent(LoginActivity.this, AdminSite.class));
                  finish();
               }
               if (documentSnapshot.getString("isUser") != null){
                   startActivity(new Intent(getApplicationContext(), MainActivity.class));
                   finish();
               }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
