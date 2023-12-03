package com.example.volta_lang.User;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.volta_lang.Login.LoginActivity;
import com.example.volta_lang.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends AppCompatActivity {

    private TextView name, email, phone, location;
    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private String userID;
    private ImageView  backbutton, settingsBtn;

    private Button imageView, editInfo;
    EditText editName;
    EditText editPhone ;
    EditText editLocation;
    EditText editBirth ;
    CircleImageView edit;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri selectedImageUri;
    private   CircleImageView circleImg, circleImageView;
    private  ProgressBar progressBar;


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
        editInfo = findViewById(R.id.imageView);


        editInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEditProfileDialog();
            }


        });

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
            finish();
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              showAlertDialog();
            }

        });

        DocumentReference documentReference = fStore.collection("users").document(userID);
        documentReference.addSnapshotListener(Profile.this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                if (documentSnapshot != null && documentSnapshot.exists()) {
                    name.setText(documentSnapshot.getString("name"));
                    email.setText(documentSnapshot.getString("birthday"));
                    phone.setText(documentSnapshot.getString("phone"));
                    location.setText(documentSnapshot.getString("location"));

                    String imageUrl = documentSnapshot.getString("profileImageUrl");

                    if (imageUrl != null && !imageUrl.isEmpty()) {
                        Glide.with(Profile.this) // Use Profile.this instead of this
                                .load(imageUrl)
                                .into(edit);
                    }
                }
            }
        });


        edit = findViewById(R.id.profilePic);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editProfilePic();
            }
        });



    }

    private void editProfilePic() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.edit_profile_pic, null);
        builder.setView(view);

        Button saveButton = view.findViewById(R.id.saveBtn);
        Button cancelBtn = view.findViewById(R.id.cancelBtn);
         progressBar = view.findViewById(R.id.progBars);
         circleImg = view.findViewById(R.id.circleImg);


        

        AlertDialog dialog = builder.create();


        DocumentReference documentReference = fStore.collection("users").document(userID);
        documentReference.addSnapshotListener(Profile.this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                if (documentSnapshot != null && documentSnapshot.exists()) {
                    String imageUrl = documentSnapshot.getString("profileImageUrl");

                    if (imageUrl != null && !imageUrl.isEmpty()) {
                        Glide.with(Profile.this) // Use Profile.this instead of this
                                .load(imageUrl)
                                .into(circleImg);
                    }
                }
            }
        });
        
        circleImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectAPicture();
            }
        });
        
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedImageUri != null) {
                    progressBar.setVisibility(View.VISIBLE);
                    uploadImageToStorage(selectedImageUri);
                } else {
                    Toast.makeText(Profile.this, "Select an image first", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        dialog.show();

    }



    private void selectAPicture() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();

            if (selectedImageUri != null) {
                circleImg.setImageURI(selectedImageUri);
                circleImageView.setImageURI(selectedImageUri);
            }
        }
    }

    private void uploadImageToStorage(Uri imageUri) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("profile_images/" + userID);

        storageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Image uploaded successfully
                    // Get the download URL and update Firestore with the URL
                    storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        updateProfileImage(uri.toString());
                        // Dismiss the dialog or perform any other action
                        Glide.with(Profile.this)
                                .load(uri.toString())
                                .into(edit);
                    });
                })
                .addOnFailureListener(e -> {
                    // Handle the error
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(Profile.this, "Failed to upload image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void updateProfileImage(String imageUrl) {
        DocumentReference userRef = fStore.collection("users").document(userID);

        // Create a map with the updated image URL
        Map<String, Object> updatedData = new HashMap<>();
        updatedData.put("profileImageUrl", imageUrl);


        userRef.update(updatedData)
                .addOnSuccessListener(aVoid -> {
                    // Update successful, refresh the UI or show a message
                    progressBar.setVisibility(View.INVISIBLE);

                    Toast.makeText(Profile.this, "Profile image updated successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    // Handle the error
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(Profile.this, "Failed to update profile image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }


    private void showAlertDialog() {
        View view = LayoutInflater.from(Profile.this).inflate(R.layout.alert_dialog, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(Profile.this);
        alert.setView(view);

        Button yes = view.findViewById(R.id.yesButton);
        Button no = view.findViewById(R.id.noButton);

        AlertDialog alertDialog = alert.create();
        alertDialog.setCancelable(true);


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

        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        alertDialog.show();
    }
    private void openEditProfileDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.edit_info_user, null);
        builder.setView(view);

        // Initialize the editable fields in the dialog
         editName = view.findViewById(R.id.editName);
         editPhone = view.findViewById(R.id.editNumber);
         editLocation = view.findViewById(R.id.editLocation);
         editBirth = view.findViewById(R.id.editDay);

         circleImageView = view.findViewById(R.id.circleImgs);


        DocumentReference documentReference = fStore.collection("users").document(userID);
        documentReference.addSnapshotListener(Profile.this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                if (documentSnapshot != null && documentSnapshot.exists()) {
                    String imageUrl = documentSnapshot.getString("profileImageUrl");

                    if (imageUrl != null && !imageUrl.isEmpty()) {
                        Glide.with(Profile.this) // Use Profile.this instead of this
                                .load(imageUrl)
                                .into(circleImageView);
                    }
                }
            }
        });

        circleImageView.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 editProfilePic();
             }
         });



        // Add more fields as needed

        editName.setText(name.getText().toString());
        editPhone.setText(phone.getText().toString());
        editLocation.setText(location.getText().toString());
        editBirth.setText(email.getText().toString());

        editBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker();
            }
        });

        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle the update logic here
                updateUserData(
                        editName.getText().toString(),
                        editPhone.getText().toString(),
                        editLocation.getText().toString(),
                        editBirth.getText().toString()
                        // Add more parameters as needed
                );
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Cancel the edit operation
                dialog.dismiss();
            }
        });

        // Show the dialog
        builder.create().show();
    }

    private void updateUserData(String newName, String newPhone, String newLocation, String newBirthday) {
        // Get the document reference for the current user
        DocumentReference userRef = fStore.collection("users").document(userID);

        // Create a map with the updated data
        Map<String, Object> updatedData = new HashMap<>();
        updatedData.put("name", newName);
        updatedData.put("phone", newPhone);
        updatedData.put("location", newLocation);
        updatedData.put("birthday", newBirthday);
        // Add more fields as needed

        // Update the document in Firestore
        userRef.update(updatedData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Update successful, refresh the UI or show a message
                        Toast.makeText(Profile.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle the error
                        Toast.makeText(Profile.this, "Failed to update profile: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void openDatePicker(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(year, month, day);

                SimpleDateFormat sdf = new SimpleDateFormat("MMMM d yyyy", Locale.getDefault());
                String formattedDate = sdf.format(selectedDate.getTime());

                editBirth.setText(formattedDate);
            }
        }, 2005, 0, 20);

        datePickerDialog.show();
    }
}

