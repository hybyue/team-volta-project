package com.example.volta_lang;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;
import static androidx.core.content.ContentProviderCompat.requireContext;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.volta_lang.databinding.ActivityMainBinding;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firestore.v1.Document;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements RecyclerDetails {

    ImageView profileView,  settings;
    FirebaseAuth fAuth;
    TextView textView;
    FirebaseFirestore fStore;


    String userID;

    RecyclerView recyclerView;
    ArrayList<VenueData> venueData;
    MyAdapter myAdapter;
    ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading..");
        progressDialog.show();


        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        textView = findViewById(R.id.textView8);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        venueData = new ArrayList<VenueData>();
        myAdapter = new MyAdapter(MainActivity.this, venueData, this);

        recyclerView.setAdapter(myAdapter);

        VenueListener();

        userID = fAuth.getCurrentUser().getUid();
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.home1);
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.home1) {
                    return true;
                } else if (item.getItemId() == R.id.location1) {
                    startActivity(new Intent(getApplicationContext(), FetchImage.class));
                    overridePendingTransition(0, 0);
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

        DocumentReference documentReference = fStore.collection("users").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {

                textView.setText(documentSnapshot.getString("name"));


            }
        });




    }

    private void VenueListener() {
        fStore.collection("VenueData").orderBy("url", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if (error != null){
                            if (progressDialog.isShowing())
                                progressDialog.dismiss();
                            Log.e("error firebase", error.getMessage());
                            return;
                        }
                        for (DocumentChange dc : value.getDocumentChanges()){
                            if (dc.getType() == DocumentChange.Type.ADDED){
                                venueData.add(dc.getDocument().toObject(VenueData.class));
                            }
                            myAdapter.notifyDataSetChanged();
                            if (progressDialog.isShowing())
                                progressDialog.dismiss();
                        }
                    }
                });
    }


    public void profileView(View view) {
        Intent intent = new Intent(MainActivity.this, Profile.class);
        startActivity(intent);

    }

    @Override
    public void itemOnClicked(int position) {

        Intent display = new Intent(MainActivity.this, ViewDetails.class);

        display.putExtra("Venue", venueData.get(position).getVenue());
        display.putExtra("Location", venueData.get(position).getLocation());
        display.putExtra("price", venueData.get(position).getPrice());
        display.putExtra("Description", venueData.get(position).getDescription());
        display.putExtra("Image", venueData.get(position).getUrl());
        display.putExtra("Image1", venueData.get(position).getUrl1());
        display.putExtra("Image2", venueData.get(position).getUrl2());

        startActivity(display);

    }
}