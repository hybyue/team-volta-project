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
import com.example.volta_lang.Admin.HomeActivity;
import com.example.volta_lang.User.MainActivity;
import com.example.volta_lang.User.Profile;
import com.example.volta_lang.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    private EditText signEmail, signPass;
    private Button login;
    private FirebaseAuth loginAuth;
     private ProgressBar progress;
     private TextView RegisterPage, forgetPass;

     FirebaseFirestore fStore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signEmail = findViewById(R.id.signEmail);
        signPass = findViewById(R.id.signPassword);
        login = findViewById(R.id.contactHost);
        progress = findViewById(R.id.progBar);

        forgetPass = findViewById(R.id.forgetPass);

        forgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });
        fStore = FirebaseFirestore.getInstance();
        loginAuth = FirebaseAuth.getInstance();


        RegisterPage=(TextView) findViewById(R.id.registerPage);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        TextInputLayout textEmail = findViewById(R.id.textEmail);
        TextInputLayout textPassword = findViewById(R.id.textPassword);


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
                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
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

                boolean hasErrors = false;

                if (TextUtils.isEmpty(email)) {
                    textEmail.setError("Email Required");
                    hasErrors = true;
                } else if (!isValidEmail(email)) {
                    textEmail.setError("use valid email");
                    hasErrors = true;
                } else {
                    textEmail.setError(null);
                }


                if (TextUtils.isEmpty(password) || password.length() < 8) {
                    textPassword.setError("Password must be at least 8 characters");
                    hasErrors = true;
                } else {
                    textPassword.setError(null);
                }


                if (hasErrors) {
                    return;
                }

                progress.setVisibility(View.VISIBLE);
                loginAuth.signInWithEmailAndPassword(email, password)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                FirebaseUser user = authResult.getUser();
                                checkAdminListener(user.getUid());
                                progress.setVisibility(View.INVISIBLE);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                Log.e("LoginActivity", "Sign in failure", e);

                                // Extracting a more detailed error message
                                String errorMessage = "Login failed";
                                if (e instanceof FirebaseAuthException) {
                                    FirebaseAuthException authException = (FirebaseAuthException) e;
                                    errorMessage = authException.getErrorCode() + ": " + authException.getMessage();
                                }

                                Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                                progress.setVisibility(View.INVISIBLE);
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

            private void checkAdminListener(String uid) {
                DocumentReference df = fStore.collection("users").document(uid);

                df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {


                        Log.d("TAG", "onSuccess" + documentSnapshot.getData());

                        if (documentSnapshot.getString("isAdmin") != null) {
                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                            finish();
                        }
                        if (documentSnapshot.getString("isUser") != null) {
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
    private void resetPassword() {
        String email = signEmail.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            // Handle the case where the email is empty
            Toast.makeText(LoginActivity.this, "Enter your registered email", Toast.LENGTH_SHORT).show();
            return;
        }

        // Use the FirebaseAuth instance to send a password reset email
        loginAuth.sendPasswordResetEmail(email)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Password reset email sent successfully
                        Toast.makeText(LoginActivity.this, "Password reset email sent", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle the case where password reset email sending fails
                        Toast.makeText(LoginActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}


