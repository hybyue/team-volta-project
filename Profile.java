package com.example.volta_lang.User;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.volta_lang.Login.LoginActivity;
import com.example.volta_lang.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class Profile extends AppCompatActivity {

    private TextView name, email, phone, location;
    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private String userID;
    private ImageView  backbutton, settingsBtn;

    private Button imageView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        name = findViewById(R.id.profileName);
        email = findViewById(R.id.emailFetch);
        phone = findViewById(R.id.phoneFetch);
        location = findViewById(R.id.locate_fetch);
        imageView = findViewById(R.id.imageView7);
        backbutton =findViewById(R.id.backButton);
        settingsBtn = findViewById(R.id.settingButton);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userID = fAuth.getCurrentUser().getUid();


        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
            }
        });

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              showAlertDialog();
            }

        });

        DocumentReference documentReference = fStore.collection("users").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {

                if (error != null) {
                    Toast.makeText(Profile.this, "Error fetching data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (documentSnapshot != null && documentSnapshot.exists()) {
                    name.setText(documentSnapshot.getString("name"));
                    email.setText(documentSnapshot.getString("email"));
                    phone.setText(documentSnapshot.getString("phone"));
                    location.setText(documentSnapshot.getString("location"));
                } else {
                    Toast.makeText(Profile.this, "Document does not exist or is null", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


    private void showAlertDialog() {
        View view = LayoutInflater.from(Profile.this).inflate(R.layout.alert_dialog, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(Profile.this);
        alert.setView(view);

        Button yes = view.findViewById(R.id.yesButton);
        Button no = view.findViewById(R.id.noButton);

        AlertDialog alertDialog = alert.create();


        // Set click listener for the "Yes" button
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // User clicked "Yes," log out
                if (fAuth.getCurrentUser() != null) {
                    fAuth.getInstance().signOut();
                    Intent signout = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(signout);
                    finish();
                }
                alertDialog.dismiss();
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }
}

