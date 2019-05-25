package com.example.myapplication.search;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.models.User;

import java.text.MessageFormat;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * File created by tcarau18
 * <p>
 * This class creates a Search View Adapter which takes all the users provided by,
 * the actual search in the Search Activity, actuality Fragment
 **/
public class SearchActivityAdapter extends RecyclerView.Adapter<SearchActivityAdapter.ViewHolder> {
    private static final String TAG = "SearchActivityAdapter";
    private Context context;
    private List<User> userList;
    private int lastPosition = -1;

    SearchActivityAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    void setUserList(List<User> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public SearchActivityAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_search_item, viewGroup, false);

        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final SearchActivityAdapter.ViewHolder viewHolder, @SuppressLint("RecyclerView") int index) {
        Log.d(TAG, "onBindViewHolder: Called");

        final User currentUser = userList.get(index);

        if (currentUser != null) {
            Glide.with(context)
                    .load(currentUser.getProfile_photo())
                    .fitCenter()
                    .centerCrop()
                    .into(viewHolder.profile_photo);
            viewHolder.username.setText(String.format("User name: %s", currentUser.getUsername()));
            viewHolder.nrOfPosts.setText(MessageFormat.format("Number of posts: {0}", currentUser.getNrOfPosts()));

        } else {
            Glide.with(context)
                    .load(R.drawable.my_avatar)
                    .fitCenter()
                    .centerCrop()
                    .into(viewHolder.profile_photo);
            viewHolder.username.setText(R.string.loading);
            viewHolder.nrOfPosts.setText(R.string.numberOfPosts);

        }

        viewHolder.profile_photo.setOnClickListener(v ->
                dialogChoice(currentUser.getUsername(), String.valueOf(currentUser.getNrOfPosts()), currentUser.getWebsite(), viewHolder.profile_photo.getDrawable())
        );

        Animation animation = AnimationUtils.loadAnimation(context, (index > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        viewHolder.itemView.startAnimation(animation);
        lastPosition = index;
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView username, nrOfPosts;
        CircleImageView profile_photo;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            profile_photo = itemView.findViewById(R.id.user_profile_Image_item_id);
            username = itemView.findViewById(R.id.user_name_on_search_list_user);
            nrOfPosts = itemView.findViewById(R.id.nr_of_posts_on_search_list_user);

        }
    }


    private void dialogChoice(CharSequence username, CharSequence nrOfPosts, CharSequence website, Drawable drawable) {
        nrOfPosts = "Number of Posts: " + nrOfPosts;
        website = "Website: " + website;

        final CharSequence[] options = {nrOfPosts, website, "Dismiss"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(username);
        builder.setIcon(drawable);

        CharSequence finalNrOfPosts = nrOfPosts;
        CharSequence finalWebsite = website;
        builder.setItems(options, (dialog, which) -> {

            if (options[which].equals(finalNrOfPosts)) {
                dialog.dismiss();
            } else if (options[which].equals(finalWebsite)) {
                dialog.dismiss();
            } else if (options[which].equals("Dismiss")) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
}
