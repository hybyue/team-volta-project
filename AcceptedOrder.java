package com.example.volta_lang.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.volta_lang.BookingAdapterData.AcceptAdapter;
import com.example.volta_lang.BookingAdapterData.AcceptedBookings;
import com.example.volta_lang.BookingAdapterData.BookOfUser;
import com.example.volta_lang.BookingAdapterData.PendingAdapter;
import com.example.volta_lang.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseError;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class AcceptedOrder extends AppCompatActivity {
    private RecyclerView accepted_orders;
    ArrayList<AcceptedBookings> acceptedBookings = new ArrayList<>();
    AcceptAdapter acceptAdapter;
    FirebaseFirestore fStore;
    ImageView exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accepted_order);


        fStore = FirebaseFirestore.getInstance();

        accepted_orders = findViewById(R.id.acceptedOrders);
        accepted_orders.setHasFixedSize(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        accepted_orders.setLayoutManager(layoutManager);

        acceptAdapter = new AcceptAdapter(AcceptedOrder.this, acceptedBookings);

        accepted_orders.setAdapter(acceptAdapter);

        AcceptedViews();

        exit = findViewById(R.id.imageView8);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void AcceptedViews() {
        fStore.collection("BookOfUser").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    acceptedBookings.clear();
                    for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {
                        try {
                            // Assuming each document represents a user's booking
                            AcceptedBookings booked = documentSnapshot.toObject(AcceptedBookings.class);
                            acceptedBookings.add(booked);
                        } catch (Exception e) {
                            Log.e("Firestore", "Error converting document: " + e.getMessage());
                        }
                    }
                    acceptAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(AcceptedOrder.this, "Error fetching bookings: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}