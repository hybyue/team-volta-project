package com.example.volta_lang.BookingAdapterData;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.volta_lang.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class PendingAdapter extends RecyclerView.Adapter<PendingAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<BookOfUser> bookOfUserArray;
    private FirebaseFirestore firestore;
    private FirebaseAuth fAuth;

    public PendingAdapter(Context context, ArrayList<BookOfUser> bookOfUserArrayList) {
        this.context = context;
        this.bookOfUserArray = bookOfUserArrayList;
        this.fAuth = FirebaseAuth.getInstance();
        this.firestore = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public PendingAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View views = LayoutInflater.from(context).inflate(R.layout.pending_design, parent, false);
        return new PendingAdapter.MyViewHolder(views);
    }

    @Override
    public void onBindViewHolder(@NonNull PendingAdapter.MyViewHolder holder, int position) {

        BookOfUser books = bookOfUserArray.get(position);


        Glide.with(context)
                .load(books.imageUrl)
                .into(holder.venuePic);

        if (books != null) {
            if (holder.userName != null){
                holder.userName.setText(books.getUsername());
            }
            if (holder.venueName != null) {
                holder.venueName.setText(books.getName());
            }

            if (holder.dateOfvenue != null) {

                holder.dateOfvenue.setText(books.getDateSet());
            }
            if (holder.price != null) {
                holder.price.setText(books.getTotalPrice());
            }
            if (holder.emailUser != null) {
                holder.emailUser.setText(books.getUserGmail());
            }
        }
        
        holder.decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteBook(books.getUserGmail(), holder.getAdapterPosition());
            }
        });


        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceptBooking(books.getUserGmail(), holder.getAdapterPosition());

            }
        });



    }



    @Override
    public int getItemCount() {
        return bookOfUserArray.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView venuePic;
        TextView userName, venueName, dateOfvenue, price, emailUser;

        Button decline, accept;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            venuePic = itemView.findViewById(R.id.venuePics);
            userName = itemView.findViewById(R.id.userNames);
            venueName = itemView.findViewById(R.id.venueNames);
            dateOfvenue = itemView.findViewById(R.id.dateSets);
            price = itemView.findViewById(R.id.priceBook);
            emailUser = itemView.findViewById(R.id.emailUser);

            decline = itemView.findViewById(R.id.declineBook);
            accept = itemView.findViewById(R.id.acceptBook);

        }

    }
    private void deleteBook(String name, int position) {

        firestore.collection("BookOfUser")
                .whereEqualTo("userGmail", name)
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
                                                    bookOfUserArray.remove(position);
                                                    notifyDataSetChanged();

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
    private void acceptBooking(String name, int position) {
        // Move the accepted booking to a different Firestore collection.
        firestore.collection("AcceptedBookings")
                .add(bookOfUserArray.get(position))
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        deleteBook(name, position);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("PendingAdapter", "Error adding accepted booking: " + e.getMessage());
                        // Handle failure, e.g., show a toast or log the error.
                    }
                });
    }
}
