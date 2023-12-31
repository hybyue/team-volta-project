package com.example.volta_lang.Admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.volta_lang.BookingAdapterData.BookOfUser;
import com.example.volta_lang.BookingAdapterData.CurrentUser;
import com.example.volta_lang.BookingAdapterData.PendingAdapter;
import com.example.volta_lang.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class AcceptOrderActivity extends AppCompatActivity {

     private RecyclerView accept_orders;
     ArrayList<BookOfUser> bookOfUserArray = new ArrayList<>();
     PendingAdapter pendingAdapter;
     FirebaseFirestore fStore;
     FirebaseAuth fAuth;
     ImageView exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_order);

        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        accept_orders = findViewById(R.id.acceptOrders);
        accept_orders.setHasFixedSize(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        accept_orders.setLayoutManager(layoutManager);

        pendingAdapter = new PendingAdapter(AcceptOrderActivity.this, bookOfUserArray);

        accept_orders.setAdapter(pendingAdapter);

        BookOfTheUser();

        exit = findViewById(R.id.imageView8);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void BookOfTheUser() {
        fStore.collection("BookOfUser").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    bookOfUserArray.clear();
                    for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {
                        try {
                            // Assuming each document represents a user's booking
                            BookOfUser booked = documentSnapshot.toObject(BookOfUser.class);
                            bookOfUserArray.add(booked);
                        } catch (Exception e) {
                            Log.e("Firestore", "Error converting document: " + e.getMessage());
                        }
                    }
                    pendingAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(AcceptOrderActivity.this, "Error fetching bookings: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
