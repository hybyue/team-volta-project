package com.example.volta_lang.User;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.volta_lang.R;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class ViewDetails extends AppCompatActivity {


    ImageSlider imageSlider1;
    TextView venueName, locateV, description;
    Button show;

    TextView hostName, hostNum, hostNum1, hostGmail, prices;
    FirebaseFirestore fStore;
    Button setDate;

    ImageView back;
    boolean isExpanded = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_details);


        String name = getIntent().getStringExtra("Venue");
        String location = getIntent().getStringExtra("Location");
        String image = getIntent().getStringExtra("Image");
        String image1 = getIntent().getStringExtra("Image1");
        String image2 = getIntent().getStringExtra("Image2");
        String image3 = getIntent().getStringExtra("Image3");
        String image4 = getIntent().getStringExtra("Image4");
        String image5 = getIntent().getStringExtra("Image5");
        String image6 = getIntent().getStringExtra("Image6");
        String image7 = getIntent().getStringExtra("Image7");
        String image8 = getIntent().getStringExtra("Image8");
        String image9 = getIntent().getStringExtra("Image9");
        String image10 = getIntent().getStringExtra("Image10");
        String image11 = getIntent().getStringExtra("Image11");

        String desc = getIntent().getStringExtra("Description");
        String price = getIntent().getStringExtra("Price");

        setDate = findViewById(R.id.setDate);

        hostName = findViewById(R.id.hostName);
        hostNum = findViewById(R.id.hostNum);
        hostNum1 = findViewById(R.id.hostNum1);
        hostGmail = findViewById(R.id.hostGmail);
        fStore = FirebaseFirestore.getInstance();


        setDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent requestIntent = new Intent(ViewDetails.this, RequestBook.class);

                requestIntent.putExtra("Venue", venueName.getText().toString());
                requestIntent.putExtra("Location", locateV.getText().toString());
                requestIntent.putExtra("Image", image);
                requestIntent.putExtra("Price", prices.getText().toString());


                startActivity(requestIntent);
            }
        });

        DocumentReference documentReference = fStore.collection("users").document("FQ2LO8ggaDfWFQzJrc55T0AOVAJ2");
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {

                hostName.setText("Hosted by " + documentSnapshot.getString("name"));
                hostNum.setText("Events & Catering: " + documentSnapshot.getString("contact"));
                hostNum1.setText("Pool & Room Reservation: " + documentSnapshot.getString("contact1"));
                hostGmail.setText("" + documentSnapshot.getString("gmail"));


            }
        });

        imageSlider1 = findViewById(R.id.imageSlider1);
        venueName = findViewById(R.id.venueName1);
        locateV = findViewById(R.id.locateV);
        description = findViewById(R.id.description);
        prices = findViewById(R.id.prices);


        venueName.setText(name);
        locateV.setText(location);
        description.setText(desc);

        String formattedPrice = formatPrice(price);
        prices.setText(formattedPrice);
        
        
        List<SlideModel> slideModels = new ArrayList<>();

        if (image != null && !image.isEmpty()) {
            slideModels.add(new SlideModel(image, ScaleTypes.FIT));
        }
        if (image1 != null && !image1.isEmpty()) {
            slideModels.add(new SlideModel(image1, ScaleTypes.FIT));
        }
        if (image2 != null && !image2.isEmpty()) {
            slideModels.add(new SlideModel(image2, ScaleTypes.FIT));
        }
        if (image3 != null && !image3.isEmpty()) {
            slideModels.add(new SlideModel(image3, ScaleTypes.FIT));
        }

        if (image4 != null && !image4.isEmpty()) {
            slideModels.add(new SlideModel(image4, ScaleTypes.FIT));
        }
        if (image5 != null && !image5.isEmpty()) {
            slideModels.add(new SlideModel(image5, ScaleTypes.FIT));
        }
        if (image6 != null && !image6.isEmpty()) {
            slideModels.add(new SlideModel(image6, ScaleTypes.FIT));
        }
        if (image7 != null && !image7.isEmpty()) {
            slideModels.add(new SlideModel(image7, ScaleTypes.FIT));
        }
        if (image8 != null && !image8.isEmpty()) {
            slideModels.add(new SlideModel(image8, ScaleTypes.FIT));
        }
        if (image9 != null && !image9.isEmpty()) {
            slideModels.add(new SlideModel(image9, ScaleTypes.FIT));
        }
        if (image10 != null && !image10.isEmpty()) {
            slideModels.add(new SlideModel(image10, ScaleTypes.FIT));
        }
        if (image11 != null && !image11.isEmpty()) {
            slideModels.add(new SlideModel(image11, ScaleTypes.FIT));
        }


        // Set the image list for the ImageSlider
        imageSlider1.setImageList(slideModels, ScaleTypes.FIT);

        show = findViewById(R.id.show_all);
        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isExpanded) {
                    description.setMaxLines(2);
                    show.setText("See more");
                } else {
                    description.setMaxLines(Integer.MAX_VALUE);
                    show.setText("Hide");
                }
                isExpanded = !isExpanded; // Toggle the state
            }
        });

        back = findViewById(R.id.backTohome);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private String formatPrice(String price) {

        try {
            int intPrice = Integer.parseInt(price);
            return NumberFormat.getNumberInstance().format(intPrice);
        } catch (NumberFormatException e) {
            // Handle the case where the price is not a valid integer
            e.printStackTrace();
            return price;
        }
    }

}