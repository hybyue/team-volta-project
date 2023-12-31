package com.example.volta_lang.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.volta_lang.Login.LoginActivity;
import com.example.volta_lang.R;
import com.example.volta_lang.User.BookingActivity;
import com.example.volta_lang.User.InboxActivity;
import com.example.volta_lang.User.LocationActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {

    CardView cardView;
    CardView activeBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);



        cardView = findViewById(R.id.cardView3);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AcceptOrderActivity.class));
            }
        });

        activeBook = findViewById(R.id.activeBook);
        activeBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AcceptedOrder.class));
            }
        });

        BottomNavigationView bottomNavigationView1 = findViewById(R.id.bottomNavigationView1);
        bottomNavigationView1.setSelectedItemId(R.id.home);
        bottomNavigationView1.setOnItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem items) {
                if (items.getItemId() == R.id.home) {
                    return true;
                } else if (items.getItemId() == R.id.info) {
                    startActivity(new Intent(getApplicationContext(), UserInfoActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                } else if (items.getItemId() == R.id.inbox) {
                    startActivity(new Intent(getApplicationContext(), AdminInbox.class));
                    overridePendingTransition(0, 0);
                    return true;
                } else {
                    throw new IllegalStateException("Unexpected value: " + items.getItemId());
                }
            }
        });

    }

    public void Pending(View view) {
    }
}