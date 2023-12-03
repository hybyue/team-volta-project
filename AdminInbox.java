package com.example.volta_lang.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.volta_lang.Login.LoginActivity;
import com.example.volta_lang.R;
import com.example.volta_lang.User.BookingActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class AdminInbox extends AppCompatActivity {

    Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_inbox);

        logout = findViewById(R.id.logout);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent logout = new Intent(AdminInbox.this, LoginActivity.class);
                startActivity(logout);
                finish();
            }
        });


        BottomNavigationView bottomNavigationView1 = findViewById(R.id.bottomNavigationView1);
        bottomNavigationView1.setSelectedItemId(R.id.inbox);
        bottomNavigationView1.setOnItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem items) {
                if (items.getItemId() == R.id.home) {
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                } else if (items.getItemId() == R.id.info) {
                    startActivity(new Intent(getApplicationContext(), UserInfoActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                } else if (items.getItemId() == R.id.inbox) {
                     return true;
                } else {
                    throw new IllegalStateException("Unexpected value: " + items.getItemId());
                }
            }
        });
    }
}