package com.example.myapplication.search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.models.User;

import java.util.List;

/**
 * File created by tcarau18
 **/
public class SearchActivityAdapter extends ArrayAdapter<User> {

    private Context context;
    private List<User> userList;
    private int lastPosition = -1;

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public SearchActivityAdapter(Context context, List<User> userList) {
        super(context, R.layout.list_search_item);
        this.context = context;
        this.userList = userList;
    }


    private static class ViewHolder {
        TextView username;
        ImageView profile_pic;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final User currentUser = userList.get(position);
        ViewHolder viewHolder;

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_search_item, parent, false);
            viewHolder.username = convertView.findViewById(R.id.user_name_on_search_list_id);
            viewHolder.profile_pic = convertView.findViewById(R.id.user_profile_Image_item_id);

            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(context, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        if (currentUser != null) {
            Glide.with(context)
                    .load(currentUser.getProfile_photo())
                    .fitCenter()
                    .centerCrop()
                    .into(viewHolder.profile_pic);
            viewHolder.username.setText(currentUser.getUsername());


        } else {
            Glide.with(context)
                    .load(R.drawable.my_avatar)
                    .fitCenter()
                    .centerCrop()
                    .into(viewHolder.profile_pic);
            viewHolder.username.setText(R.string.loading);

        }

        return convertView;
    }
}
