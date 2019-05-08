package com.example.afterweb.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.afterweb.MessagesActivity;
import com.example.afterweb.Model.Users;
import com.example.afterweb.R;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private Context mContext;
    private List<Users> mUsers;

    public UserAdapter(Context mContext, List<Users> mUsers) {
        this.mContext = mContext;
        this.mUsers = mUsers;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_items, viewGroup ,false);
        return new  UserAdapter.ViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder vireHolder, int i) {
        final Users users = mUsers.get(i);
        vireHolder.username.setText(users.getUsername());
        if (users.getImageURl() != null && !users.getImageURl().equals("default")){
            Glide.with(mContext).load(users.getImageURl()).into(vireHolder.profile_image);

        } else {
            vireHolder.profile_image.setImageResource(R.mipmap.ic_launcher);
        }
        vireHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(mContext,MessagesActivity.class);
                intent.putExtra("userId",users.getId());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView username;
        public ImageView profile_image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.txt_fragment_username);
            profile_image =itemView.findViewById(R.id.circle_profile);

        }
    }
}
