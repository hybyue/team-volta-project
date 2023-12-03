package com.example.volta_lang.BookingAdapterData;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.volta_lang.R;

import java.util.ArrayList;

public class AcceptAdapter extends RecyclerView.Adapter<AcceptAdapter.MyViewHolder> {
    Context context;
    ArrayList<AcceptedBookings> acceptedBookings;

    public AcceptAdapter(Context context, ArrayList<AcceptedBookings> acceptedBookings) {
        this.context = context;
        this.acceptedBookings = acceptedBookings;
    }

    @NonNull
    @Override
    public AcceptAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.accepted_adminsite, parent, false);
        return new AcceptAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AcceptAdapter.MyViewHolder holder, int position) {
        AcceptedBookings accept = acceptedBookings.get(position);


        Glide.with(context)
                .load(accept.imageUrl)
                .into(holder.venuePic);

        if (accept != null) {
            if (holder.userName != null){
                holder.userName.setText(accept.getUsername());
            }
            if (holder.venueName != null) {
                holder.venueName.setText(accept.getName());
            }

            if (holder.dateOfvenue != null) {

                holder.dateOfvenue.setText(accept.getDateSet());
            }
            if (holder.price != null) {
                holder.price.setText(accept.getTotalPrice());
            }
            if (holder.emailUser != null) {
                holder.emailUser.setText(accept.getUserGmail());
            }
        }

    }

    @Override
    public int getItemCount() {
        return acceptedBookings.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView venuePic;
        TextView userName, venueName, dateOfvenue, price, emailUser;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            venuePic = itemView.findViewById(R.id.venuePics);
            userName = itemView.findViewById(R.id.userNames);
            venueName = itemView.findViewById(R.id.venueNames);
            dateOfvenue = itemView.findViewById(R.id.dateSets);
            price = itemView.findViewById(R.id.priceBook);
            emailUser = itemView.findViewById(R.id.emailUser);
        }
    }
}
