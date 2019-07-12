package com.example.myapplication.utility_classes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.models.Comment;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ListViewAdapter extends ArrayAdapter<Comment> {

    private List<Comment> commentList;
    private Context mContext;

    public ListViewAdapter(Context context, ArrayList<Comment> commentList) {
        super(context, R.layout.layout_post_comment_viewholder_item, commentList);
        this.commentList = commentList;

    }

    @Override
    public View getView(int position,View convertView,ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View viewHolder = inflater.inflate(R.layout.layout_post_comment_viewholder_item, parent,false);

        Comment currentComment = commentList.get(position) ;

        CircleImageView mProfileImg = viewHolder.findViewById(R.id.comment_profile_picture);
        TextView mUsername = viewHolder.findViewById(R.id.comment_username);
        TextView mComment = viewHolder.findViewById(R.id.comment);

        Glide.with(getContext())
                .load(currentComment.getUserProfilePhoto())
                .fitCenter()
                .centerCrop()
                .into(mProfileImg);

        mUsername.setText(currentComment.getUsername());
        mComment.setText(currentComment.getComment());

        return viewHolder;
    }
}
