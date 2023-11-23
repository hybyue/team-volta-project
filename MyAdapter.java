package com.example.volta_lang;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context context;
    ArrayList<VenueData> venueArrayList;
     final RecyclerDetails recyclerDetails;

    public MyAdapter(Context context, ArrayList<VenueData> venueArrayList, RecyclerDetails recyclerDetails) {
        this.context = context;
        this.venueArrayList = venueArrayList;
        this.recyclerDetails =recyclerDetails;
    }

    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.item_venue, parent, false);
        return new MyViewHolder(v, recyclerDetails);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder, int position) {

        VenueData venue = venueArrayList.get(position);


        Glide.with(context)
                .load(venue.url)
                .into(holder.imageSlider);
        holder.venueName.setText(venue.venue);
        holder.venueLocation.setText(venue.location);




    }

    @Override
    public int getItemCount() {
        return venueArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView venueName, venueLocation;
        ImageView imageSlider;
        public MyViewHolder(@NonNull View itemView, RecyclerDetails recyclerDetails) {
            super(itemView);

            venueName = itemView.findViewById(R.id.venueName1);
            venueLocation = itemView.findViewById(R.id.venueLocation);
            imageSlider = itemView.findViewById(R.id.imageSlider);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition(); // Get the clicked item position

                    if (pos != RecyclerView.NO_POSITION) {
                        if (recyclerDetails != null) {
                            recyclerDetails.itemOnClicked(pos);
                        }
                    }
                }
            });

        }
    }
}
