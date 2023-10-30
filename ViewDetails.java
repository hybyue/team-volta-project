package com.example.volta_lang;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;

import java.util.ArrayList;
import java.util.List;

public class ViewDetails extends AppCompatActivity {


    ImageSlider imageSlider1;
    TextView venueName, price, locateV, description;
    Button show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_details);


        String name = getIntent().getStringExtra("Venue");
        String location = getIntent().getStringExtra("Location");
        String image = getIntent().getStringExtra("Image");
        String image1 = getIntent().getStringExtra("Image1");
        String image2 = getIntent().getStringExtra("Image2");

        String prices = getIntent().getStringExtra("price");
        String desc = getIntent().getStringExtra("Description");






        imageSlider1 = findViewById(R.id.imageSlider1);
        venueName=  findViewById(R.id.venueName);
        price = findViewById(R.id.price);
        locateV = findViewById(R.id.locateV);
        description = findViewById(R.id.description);

      venueName.setText(name);
      price.setText(prices);
      locateV.setText(location);
      description.setText(desc);

        List<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel(image, ScaleTypes.FIT));
        slideModels.add(new SlideModel(image1, ScaleTypes.FIT));
        slideModels.add(new SlideModel(image2, ScaleTypes.FIT));
        // Set the image list for the ImageSlider
        imageSlider1.setImageList(slideModels, ScaleTypes.FIT);

        show=findViewById(R.id.show_all);
        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (show.getText().toString().equalsIgnoreCase("See more"))
                {
                    description.setMaxLines(Integer.MAX_VALUE);//your TextView
                    show.setText("See less");
                }
                else
                {
                    description.setMaxLines(1);//your TextView
                    show.setText("See more");
                }
            }
        });

    }
}