package com.example.volta_lang.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.net.Uri;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.volta_lang.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class LocationActivity extends AppCompatActivity {

    private ImageSlider imageSlider;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        imageSlider = findViewById(R.id.imageSlider1);

        ArrayList<SlideModel> slideModels= new ArrayList<>();

        slideModels.add(new SlideModel("https://img.freepik.com/free-photo/glowing-lines-human-heart-3d-shape-dark-background-generative-ai_191095-1435.jpg", ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://images4.alphacoders.com/130/1308321.jpeg", ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://c4.wallpaperflare.com/wallpaper/469/881/258/arsenixc-artifacts-city-clouds-wallpaper-preview.jpg", ScaleTypes.FIT));

        imageSlider.setImageList(slideModels, ScaleTypes.FIT);

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


    }

    public void locate(View view) {
        String url = "https://www.google.com/maps/place/Rancho+Pobre+Resort/@16.0975611,120.5090849,14.58z/data=!4m21!1m14!4m13!1m5!1m1!1s0x33916bfa70d9ed91:0x6c07e4b77752e4bd!2m2!1d120.517998!2d16.0900286!1m6!1m2!1s0x33916bfa70d9ed91:0x6c07e4b77752e4bd!2sRancho+Pobre+Resort,+San+Jacinto,+Manaoag+-+Pozzorubio+Rd,+Pozorrubio,+Pangasinan!2m2!1d120.517998!2d16.0900286!3m5!1s0x33916bfa70d9ed91:0x6c07e4b77752e4bd!8m2!3d16.0900286!4d120.517998!16s%2Fg%2F11j4mgyg6w?entry=ttu";

        Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        mapIntent.setPackage("com.google.android.apps.maps");

        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        } else {
            Uri webpageUri = Uri.parse("https://www.google.com/maps/place/Rancho+Pobre+Resort/@16.0975611,120.5090849,14.58z/data=!4m21!1m14!4m13!1m5!1m1!1s0x33916bfa70d9ed91:0x6c07e4b77752e4bd!2m2!1d120.517998!2d16.0900286!1m6!1m2!1s0x33916bfa70d9ed91:0x6c07e4b77752e4bd!2sRancho+Pobre+Resort,+San+Jacinto,+Manaoag+-+Pozzorubio+Rd,+Pozorrubio,+Pangasinan!2m2!1d120.517998!2d16.0900286!3m5!1s0x33916bfa70d9ed91:0x6c07e4b77752e4bd!8m2!3d16.0900286!4d120.517998!16s%2Fg%2F11j4mgyg6w?entry=ttu");
            Intent webIntent = new Intent(Intent.ACTION_VIEW, webpageUri);
            startActivity(webIntent);
        }
    }
}