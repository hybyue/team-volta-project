package com.example.volta_lang.BookingAdapterData;



import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.volta_lang.Login.LoginActivity;
import com.example.volta_lang.R;
import com.example.volta_lang.User.Profile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.MyViewHolder>{

    Context context;
    ArrayList<BookOfUser> bookOfUserArrayList;

    FirebaseAuth fAuth ;

    FirebaseFirestore firestore ;
    View parentView;



    public BookAdapter(Context context, ArrayList<BookOfUser> bookOfUserArrayList, View parentView) {
        this.context = context;
        this.bookOfUserArrayList = bookOfUserArrayList;
        this.fAuth = FirebaseAuth.getInstance();
        this.firestore = FirebaseFirestore.getInstance();
        this.parentView = parentView;
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


        Glide.with(context)
                .load(book.imageUrl)
                .into(holder.venuePic);


            if (holder.venueName != null) {
                holder.venueName.setText(book.getName());
            }
            if (holder.dateset != null) {
                holder.dateset.setText(book.getDateSet());
            }
            if (holder.prices != null) {
                holder.prices.setText(book.getTotalPrice());
            }
            if (holder.timesets != null) {
                holder.timesets.setText(book.getTime());
            }
        }
        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = LayoutInflater.from(context).inflate(R.layout.alert_dialog, null);
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setView(view);

                Button yes = view.findViewById(R.id.yesButton);
                Button no = view.findViewById(R.id.noButton);
                TextView title = view.findViewById(R.id.logoutText);
                TextView description = view.findViewById(R.id.textDescrip);


                title.setText("Cancel");
                description.setText("Are you sure you want to cancel?");

                AlertDialog alertDialog = alert.create();
                alertDialog.setCancelable(true);


                // Set click listener for the "Yes" button
                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // User clicked "Yes," log out
                        deleteBook(book.getName(), holder.getAdapterPosition());

                        alertDialog.dismiss();
                    }
                });

                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                alertDialog.show();
            }
        });


    }




    @Override
    public int getItemCount() {
        return bookOfUserArrayList.size();
    }



    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView venueName, dateset, prices, timesets;
        ImageView venuePic;
        Button cancel;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);


        venueName = itemView.findViewById(R.id.venueName1);
        dateset = itemView.findViewById(R.id.dateset);
        prices = itemView.findViewById(R.id.totalPrice1);
        venuePic = itemView.findViewById(R.id.venuePic);
        cancel = itemView.findViewById(R.id.cancelBook);
        timesets = itemView.findViewById(R.id.timeSets);

    }
}
    private void deleteBook(String name, int position) {
        String currentUserUid = fAuth.getCurrentUser().getUid();

        firestore.collection("BookOfUser")
                .whereEqualTo("userUid", currentUserUid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                document.getReference().delete()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    bookOfUserArrayList.remove(position);
                                                    notifyDataSetChanged();
                                                    if (bookOfUserArrayList.isEmpty()) {
                                                        parentView.findViewById(R.id.imageView).setVisibility(View.VISIBLE);
                                                        parentView.findViewById(R.id.emptyBook).setVisibility(View.VISIBLE);
                                                    } else {
                                                        parentView.findViewById(R.id.imageView).setVisibility(View.INVISIBLE);
                                                        parentView.findViewById(R.id.emptyBook).setVisibility(View.INVISIBLE);
                                                    }
                                                } else {
                                                    Log.e("BookAdapter", "Error deleting book: " + task.getException().getMessage());
                                                }
                                            }
                                        });
                            }
                        } else {
                            Log.e("BookAdapter", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

}
