package com.example.volta_lang.Admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.volta_lang.MyAdapter;
import com.example.volta_lang.R;
import com.example.volta_lang.User.Profile;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder>{
    Context context;
    ArrayList<userData> userData;

    public UserAdapter(Context context, ArrayList<com.example.volta_lang.Admin.userData> userData) {
        this.context = context;
        this.userData = userData;
    }

    @NonNull
    @Override
    public UserAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.admin_use_info, parent, false);
        return new UserAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.MyViewHolder holder, int position) {

        userData user = userData.get(position);


        if (user != null){



            if (holder.profilePicUser != null) {
                if (user.profileImageUrl != null && !user.profileImageUrl.isEmpty()) {
                    // User has a profile picture, load it with Glide
                    Glide.with(context)
                            .load(user.profileImageUrl)
                            .into(holder.profilePicUser);
                } else {
                    // User doesn't have a profile picture, load a default image
                    Glide.with(context)
                            .load(R.drawable.user)  // Replace with your default image resource
                            .into(holder.profilePicUser);
                }
            }
            if (holder.userName != null) {
                holder.userName.setText(user.name);
            }
            if (holder.userLocation != null) {
                holder.userLocation.setText(user.location);
            }
            if (holder.userNumber != null) {
                holder.userNumber.setText(user.phone);
            }
            if (holder.userEmail != null) {
                holder.userEmail.setText(user.email);
            }
            if (holder.userBirth != null) {
                holder.userBirth.setText(user.birthday);
            }
        }

    }

    @Override
    public int getItemCount() {
      return  userData.size();
    }

    public  static  class MyViewHolder extends RecyclerView.ViewHolder{
        TextView userName, userNumber, userLocation, userEmail, userBirth;
        CircleImageView profilePicUser;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.userName);
            userLocation = itemView.findViewById(R.id.userLocation);
            userNumber = itemView.findViewById(R.id.userNumber);
            userEmail = itemView.findViewById(R.id.userEmail);
            userBirth = itemView.findViewById(R.id.userBirth);
            profilePicUser = itemView.findViewById(R.id.profilePicUser);


        }
    }
}
