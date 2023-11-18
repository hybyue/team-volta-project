package com.example.volta_lang.Login;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

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
import android.widget.TextView;
import android.widget.Toast;

import com.example.volta_lang.User.MainActivity;
import com.example.volta_lang.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
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

                if (TextUtils.isEmpty(location) || TextUtils.isEmpty(date)) {
                    Toast.makeText(VerificationActivity.this, "This filled is required", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(VerificationActivity.this, "Logged in", Toast.LENGTH_SHORT).show();

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
                // Create a Calendar instance and set it to the selected date
                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(year, month, day);

                // Format the selected date to be user-friendly
                SimpleDateFormat sdf = new SimpleDateFormat("MMMM d yyyy", Locale.getDefault());
                String formattedDate = sdf.format(selectedDate.getTime());

                // Showing the formatted date in the textView
                dateV.setText(formattedDate);
            }
        }, 2023, 01, 20);

        datePickerDialog.show();
    }
}