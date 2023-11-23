package com.example.volta_lang.User;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.example.volta_lang.MyAdapter;
import com.example.volta_lang.R;
import com.example.volta_lang.RecyclerDetails;
import com.example.volta_lang.VenueData;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements RecyclerDetails {

    private ImageView profileView,  settings;
    private FirebaseAuth fAuth;
    private TextView textView;
    private boolean isPopupShown = false;
    private FirebaseFirestore fStore;

    RelativeLayout relativeLayout;
    private String userID;

    private RecyclerView recyclerView;
    ArrayList<VenueData> venueData = new ArrayList<>();
    MyAdapter myAdapter;
    View popUp;
    ProgressDialog progressDialog;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading..");
        progressDialog.show();


        relativeLayout= findViewById(R.id.layout);

        createPopupWindow();

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        textView = findViewById(R.id.textView8);
        View includedLayout = findViewById(R.id.included);



        includedLayout.bringToFront();

        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        myAdapter = new MyAdapter(MainActivity.this, venueData, this);

        recyclerView.setAdapter(myAdapter);

        VenueListener();
        SnapHelper mSnapHelper =  new PagerSnapHelper();
        mSnapHelper.attachToRecyclerView(recyclerView);

        userID = fAuth.getCurrentUser().getUid();


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.home1);
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.home1) {
                    return true;
                } else if (item.getItemId() == R.id.location1) {
                    startActivity(new Intent(getApplicationContext(), LocationActivity.class));
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


        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Profile.class));
            }
        });



    }

    private void createPopupWindow() {

        if (isPopupAlreadyShown()) {
            return;
        }
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
         popUp = inflater.inflate(R.layout.popup_design, null);

        if (popUp == null) {
            Log.e("MainActivity", "Failed to inflate popup_design");
            return;
        }

        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;

        PopupWindow popupWindow = new PopupWindow(popUp, width, height, focusable);

        relativeLayout.post(new Runnable() {
            @Override
            public void run() {
                popupWindow.showAtLocation(relativeLayout, Gravity.CENTER, 0, 0);
            }
        });

        isPopupShown = true;
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("popupShown", true);
        editor.apply();

        popUp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
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
        display.putExtra("Description", venueData.get(position).getDescription());
        display.putExtra("Image", venueData.get(position).getUrl());
        display.putExtra("Image1", venueData.get(position).getUrl1());
        display.putExtra("Image2", venueData.get(position).getUrl2());
        display.putExtra("Price", venueData.get(position).getPrice());


        startActivity(display);

    }
    private boolean isPopupAlreadyShown() {
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        return sharedPreferences.getBoolean("popupShown", false);
    }
}