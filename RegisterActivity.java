package com.example.volta_lang.Login;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.volta_lang.User.MainActivity;
import com.example.volta_lang.User.Profile;
import com.example.volta_lang.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class RegisterActivity extends AppCompatActivity {
     TextView GoLogin;
     EditText signName;
     EditText signNumber;
     EditText signEmail;
     EditText signPassword;
     EditText signConfirm, signLocation, signDay ;
     Button registerButton;
     FirebaseAuth registerAuth;
     ProgressBar progressBar;
     FirebaseFirestore registerStore;
     ImageView date;
    private String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        signName= findViewById(R.id.editName);
        signNumber = findViewById(R.id.editNumber);
        signLocation = findViewById(R.id.editLocation);
        signDay = findViewById(R.id.editDay);
        date = findViewById(R.id.date);
        signEmail = findViewById(R.id.signEmail);
        signPassword = findViewById(R.id.signPassword);
        signConfirm = findViewById(R.id.signConfirmPassword);
        registerButton= findViewById(R.id.registerButton);
        progressBar = findViewById(R.id.progressBar);
         registerAuth = FirebaseAuth.getInstance();
         registerStore = FirebaseFirestore.getInstance();

         TextInputLayout textName = findViewById(R.id.textName);
        TextInputLayout textNumber = findViewById(R.id.textNumber);
        TextInputLayout textLocate = findViewById(R.id.textLocate);
        TextInputLayout textBirth = findViewById(R.id.textBirth);
        TextInputLayout textEmail = findViewById(R.id.textEmail);
        TextInputLayout textPassword = findViewById(R.id.textPassword);
        TextInputLayout textCpassword = findViewById(R.id.textCpassword);




        GoLogin=(TextView) findViewById(R.id.goLogin);

        if (registerAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), Profile.class));
            finish();
        }


        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker();
            }
        });

        signDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = signEmail.getText().toString().trim();
                String password = signPassword.getText().toString().trim();
                String ConfirmPassword = signConfirm.getText().toString().trim();
                String name = signName.getText().toString().trim();
                String phone = signNumber.getText().toString().trim();
                String location = signLocation.getText().toString().trim();
                String dates = signDay.getText().toString().trim();

                boolean hasErrors = false;

                if (TextUtils.isEmpty(name)) {
                    textName.setError("Name cannot be empty");
                    hasErrors = true;
                } else {
                    textName.setError(null);
                }

                if (TextUtils.isEmpty(phone)) {
                    textNumber.setError("Phone number cannot be empty");
                    hasErrors = true;
                } else if (!isValidPhilippinePhoneNumber(phone)) {
                    textNumber.setError("Invalid phone number format");
                    hasErrors = true;
                } else {
                    textNumber.setError(null);
                }

                if (TextUtils.isEmpty(location)) {
                    textLocate.setError("Location cannot be empty");
                    hasErrors = true;
                } else {
                    textLocate.setError(null);
                }

                if (TextUtils.isEmpty(signDay.getText().toString())) {
                    textBirth.setError("Birthday cannot be empty");
                    hasErrors = true;
                } else {
                    textBirth.setError(null);
                }

                if (TextUtils.isEmpty(email)) {
                    textEmail.setError("Email Required");
                    hasErrors = true;
                } else if (!isValidEmail(email)){
                    textEmail.setError("use valid email");
                    hasErrors = true;
                }else {
                    textEmail.setError(null);
                }



                if (TextUtils.isEmpty(password) || password.length() < 8) {
                    textPassword.setError("Password must be at least 8 characters");
                    hasErrors = true;
                } else {
                    textPassword.setError(null);
                }

                if (TextUtils.isEmpty(ConfirmPassword)) {
                    textCpassword.setError("Confirm Password cannot be empty");
                    hasErrors = true;
                } else {
                    textCpassword.setError(null);
                }

                if (!password.equals(ConfirmPassword)) {
                    textCpassword.setError("Passwords don't match");
                    hasErrors = true;
                } else {
                    textCpassword.setError(null);
                }

                if (hasErrors) {
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);


                registerAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                     if (task.isSuccessful()){
                         Toast.makeText(RegisterActivity.this, "Register complete", Toast.LENGTH_SHORT).show();
                         userID = registerAuth.getCurrentUser().getUid();

                         DocumentReference documentReference = registerStore.collection("users").document(userID);
                         Map<String, Object> user = new HashMap<>();
                         user.put("name",name);
                         user.put("email", email);
                         user.put("phone", phone);
                         user.put("isUser", "1");
                         user.put("location", location);
                         user.put("birthday", dates);

                         documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                             @Override
                             public void onSuccess(Void unused) {

                                 Log.d(TAG,"onSuccess: created for user" +userID);

                             }
                         });

                         startActivity(new Intent(getApplicationContext(), MainActivity.class));
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

    private boolean isValidEmail(@NonNull String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return email.matches(emailPattern);
    }
    private boolean isValidPhilippinePhoneNumber(String phoneNumber) {
        // Check if the phone number starts with "09" or "+63" and has 11 digits
        return phoneNumber.matches("^(09|\\+639)\\d{9}$");
    }

    private void openDatePicker(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(year, month, day);

                SimpleDateFormat sdf = new SimpleDateFormat("MMMM d yyyy", Locale.getDefault());
                String formattedDate = sdf.format(selectedDate.getTime());

                signDay.setText(formattedDate);
            }
        }, 2005, 0, 20);

        datePickerDialog.show();
    }
}