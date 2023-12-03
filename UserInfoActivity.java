package com.example.volta_lang.Admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.volta_lang.MyAdapter;
import com.example.volta_lang.R;
import com.example.volta_lang.User.BookingActivity;
import com.example.volta_lang.User.InboxActivity;
import com.example.volta_lang.User.MainActivity;
import com.example.volta_lang.VenueData;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class UserInfoActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    ArrayList<userData> user = new ArrayList<>();
    UserAdapter userAdapter;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        fStore = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        userAdapter = new UserAdapter(UserInfoActivity.this, user);

        recyclerView.setAdapter(userAdapter);

        UserListener();


        BottomNavigationView bottomNavigationView1 = findViewById(R.id.bottomNavigationView1);
        bottomNavigationView1.setSelectedItemId(R.id.info);
        bottomNavigationView1.setOnItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem items) {
                if (items.getItemId() == R.id.home) {
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                } else if (items.getItemId() == R.id.info) {
                    return true;
                }else if (items.getItemId() == R.id.inbox) {
                    startActivity(new Intent(getApplicationContext(), AdminInbox.class));
                    overridePendingTransition(0, 0);
                    return true;
                } else {
                    throw new IllegalStateException("Unexpected value: " + items.getItemId());
                }
            }
        });
    }

    private void UserListener() {
        fStore.collection("users").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("error firebase", error.getMessage());
                } else {
                    for (DocumentChange dc : value.getDocumentChanges()) {
                        if (dc.getType() == DocumentChange.Type.ADDED) {
                            user.add(dc.getDocument().toObject(userData.class));
                        }
                        userAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }
}