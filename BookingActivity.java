package com.example.volta_lang.User;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.volta_lang.BookingAdapterData.BookAdapter;
import com.example.volta_lang.BookingAdapterData.BookOfUser;
import com.example.volta_lang.MyAdapter;
import com.example.volta_lang.R;
import com.example.volta_lang.VenueData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class BookingActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    BookAdapter bookAdapter;
    ArrayList<BookOfUser> bookOfUsers;

    FirebaseFirestore firestore;

    FirebaseAuth fAuth;

    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        firestore = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.recyclerBook);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        fAuth = FirebaseAuth.getInstance();

        bookOfUsers = new ArrayList<BookOfUser>();
        bookAdapter = new BookAdapter(BookingActivity.this, bookOfUsers);

        recyclerView.setAdapter(bookAdapter);

        VenueListener();

        userID = fAuth.getCurrentUser().getUid();


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.booking1);
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.home1) {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                } else if (item.getItemId() == R.id.location1) {
                    startActivity(new Intent(getApplicationContext(), LocationActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                } else if (item.getItemId() == R.id.booking1) {
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
    }

    private void VenueListener() {
        firestore.collection("BookOfUser").document(fAuth.getCurrentUser().getUid()).collection("CurrentUser").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (DocumentSnapshot  documentSnapshot : task.getResult().getDocuments()){
                        BookOfUser booked = documentSnapshot.toObject(BookOfUser.class);
                        bookOfUsers.add(booked);

                    }
                    bookAdapter.notifyDataSetChanged();
                    if (bookOfUsers.isEmpty()) {
                        findViewById(R.id.imageView).setVisibility(View.VISIBLE);
                        findViewById(R.id.emptyBook).setVisibility(View.VISIBLE);

                    } else {
                        findViewById(R.id.imageView).setVisibility(View.INVISIBLE);
                        findViewById(R.id.emptyBook).setVisibility(View.INVISIBLE);

                    }
                }
            }
        });
    }
}