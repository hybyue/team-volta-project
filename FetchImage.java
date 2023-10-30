package com.example.volta_lang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class FetchImage extends AppCompatActivity {

    private ImageSlider imageSlider;
    FirebaseFirestore data= FirebaseFirestore.getInstance();
    StorageReference storage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fetch_image);

        imageSlider = findViewById(R.id.imageSlider);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.location1);
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.home1) {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                } else if (item.getItemId() == R.id.location1) {
                    return true;
                } else if (item.getItemId() == R.id.booking1) {
                    startActivity(new Intent(getApplicationContext(), BookingActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                } else if (item.getItemId() == R.id.inbox1) {
                    startActivity(new Intent(getApplicationContext(), InboxActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                } else {
                    throw new IllegalStateException("Unexpected value: " + item.getItemId());
                }
            }
        });


        ArrayList<SlideModel> slideModels= new ArrayList<>();

//        slideModels.add(new SlideModel("https://img.freepik.com/free-photo/glowing-lines-human-heart-3d-shape-dark-background-generative-ai_191095-1435.jpg", ScaleTypes.FIT));
//        slideModels.add(new SlideModel("https://images4.alphacoders.com/130/1308321.jpeg", ScaleTypes.FIT));
//        slideModels.add(new SlideModel("https://c4.wallpaperflare.com/wallpaper/469/881/258/arsenixc-artifacts-city-clouds-wallpaper-preview.jpg", ScaleTypes.FIT));

        data.collection("venue").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot queryDocumentSnapshot:task.getResult()){
                        slideModels.add(new SlideModel(queryDocumentSnapshot.getString("url"), ScaleTypes.FIT));

                        imageSlider.setImageList(slideModels, ScaleTypes.FIT);
                    }
                }  else{
                    Toast.makeText(FetchImage.this, "cant Load image", Toast.LENGTH_LONG).show();
                }
            }

        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(FetchImage.this, "cant Load image", Toast.LENGTH_LONG).show();

                    }
                });




    }
}