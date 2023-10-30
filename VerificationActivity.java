package com.example.volta_lang;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class VerificationActivity extends AppCompatActivity {


    EditText enterL;
    Button submit;
    ImageView back, chooseDate;
    TextView dateV;
    FirebaseAuth registerAuth;
    FirebaseFirestore registerStore;
    private String userID;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        enterL = findViewById(R.id.locationText);
        back = findViewById(R.id.back1);
        submit = findViewById(R.id.submit);
        chooseDate = findViewById(R.id.selectDate);
        registerAuth =FirebaseAuth.getInstance();
        registerStore =FirebaseFirestore.getInstance();
        dateV = findViewById(R.id.dateV);


        chooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDatePicker();
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String location = enterL.getText().toString().trim();
                String date = dateV.getText().toString().trim();

                if (TextUtils.isEmpty(location)) {
                    enterL.setError("Location is empty");
                } else {
                    // Initialize Firestore
                    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

                    // Get the current user ID
                    String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    // Create a reference to the user's document
                    DocumentReference userDocRef = firestore.collection("users").document(userID);

                    // Create a map to update the user's data
                    Map<String, Object> updates = new HashMap<>();
                    updates.put("location", location);
                    updates.put("date", date);

                    // Update the user's data in Firestore
                    userDocRef.update(updates).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d(TAG, "User data updated for user " + userID);
                            Toast.makeText(VerificationActivity.this, "Data updated successfully", Toast.LENGTH_SHORT).show();

                            // Redirect to the main activity or any other desired screen
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }
                    });
                }
            }
        });

    }
    private void openDatePicker(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                //Showing the picked value in the textView
                dateV.setText(String.valueOf(year)+ "."+String.valueOf(month)+ "."+String.valueOf(day));

            }
        }, 2023, 01, 20);

        datePickerDialog.show();
    }
}