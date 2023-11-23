package com.example.volta_lang.BookingAdapterData;



import android.content.Context;
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
import com.example.volta_lang.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.MyViewHolder>{

    Context context;
    ArrayList<BookOfUser> bookOfUserArrayList;

    FirebaseAuth fAuth ;

    FirebaseFirestore firestore ;


    public BookAdapter(Context context, ArrayList<BookOfUser> bookOfUserArrayList) {
        this.context = context;
        this.bookOfUserArrayList = bookOfUserArrayList;
        this.fAuth = FirebaseAuth.getInstance();
        this.firestore = FirebaseFirestore.getInstance();
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


        Glide.with(context)
                .load(book.imageUrl)
                .into(holder.venuePic);

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
        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteBook(book.getName(), holder.getAdapterPosition());
            }
        });


    }




    @Override
    public int getItemCount() {
        return bookOfUserArrayList.size();
    }



    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView venueName, dateset, prices;
        ImageView venuePic;
        Button cancel;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);


        venueName = itemView.findViewById(R.id.venueName1);
        dateset = itemView.findViewById(R.id.dateset);
        prices = itemView.findViewById(R.id.totalPrice1);
        venuePic = itemView.findViewById(R.id.venuePic);
        cancel = itemView.findViewById(R.id.cancelBook);

    }
}
    private void deleteBook(String name, int position) {
        firestore.collection("BookOfUser")
                .document(fAuth.getCurrentUser().getUid())
                .collection("CurrentUser")
                .whereEqualTo("name", name)
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
                                                    // Document deleted successfully
                                                    bookOfUserArrayList.remove(position);
                                                    notifyDataSetChanged();
                                                    // Reload the data in the current activity
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
