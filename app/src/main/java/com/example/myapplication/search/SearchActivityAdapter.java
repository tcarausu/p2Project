package com.example.myapplication.search;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.models.User;

import java.util.List;

/**
 * File created by tcarau18
 **/
public class SearchActivityAdapter extends RecyclerView.Adapter<SearchActivityAdapter.ViewHolder> {
    private static final String TAG = "SearchActivityAdapter";
    private Context context;
    private List<User> userList;
    private int lastPosition = -1;

    public SearchActivityAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public SearchActivityAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_search_item, viewGroup, false);

        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final SearchActivityAdapter.ViewHolder viewHolder, int position) {
        Log.d(TAG, "onBindViewHolder: Called");

        final User currentUser = userList.get(position);

        if (currentUser != null) {
            Glide.with(context)
                    .load(currentUser.getProfile_photo())
                    .fitCenter()
                    .centerCrop()
                    .into(viewHolder.profile_photo);
            viewHolder.username.setText(currentUser.getUsername());


        } else {
            Glide.with(context)
                    .load(R.drawable.my_avatar)
                    .fitCenter()
                    .centerCrop()
                    .into(viewHolder.profile_photo);
            viewHolder.username.setText(R.string.loading);

        }
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView username;
        ImageView profile_photo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            profile_photo = itemView.findViewById(R.id.userProfilePicID);
            username = itemView.findViewById(R.id.userNameID);

        }
    }

}
