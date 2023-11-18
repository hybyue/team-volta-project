package com.example.volta_lang.BookingAdapterData;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.volta_lang.R;

import java.util.ArrayList;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.MyViewHolder>  {

    Context context;
    ArrayList<BookOfUser> bookOfUserArrayList;


    public BookAdapter(Context context, ArrayList<BookOfUser> bookOfUserArrayList) {
        this.context = context;
        this.bookOfUserArrayList = bookOfUserArrayList;
    }


    @NonNull
    @Override
    public BookAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.booked_view, parent, false);
        return new BookAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BookAdapter.MyViewHolder holder, int position) {
            BookOfUser book = bookOfUserArrayList.get(position);

        if (book != null) {
            if (holder.venueName != null) {
                holder.venueName.setText(book.getName());
            }
            if (holder.dateset != null) {
                holder.dateset.setText(book.getDateSet());
            }
            if (holder.prices != null) {
                holder.prices.setText(book.getPrice());
            }
        }
    }


    @Override
    public int getItemCount() {
        return bookOfUserArrayList.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView venueName, dateset, prices;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);


        venueName = itemView.findViewById(R.id.venueName1);
        dateset = itemView.findViewById(R.id.dateset);
        prices = itemView.findViewById(R.id.totalPrice1);

    }
}
}
