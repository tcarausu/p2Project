package com.example.myapplication.history_log;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.myapplication.R;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;


/** The views in the list are represented by view holder objects. These objects are instances of a class you define
 * by extending RecyclerView.ViewHolder. Each view holder is in charge of displaying a single item with a view.
 * For example, if your list shows music collection, each view holder might represent a single album.
 * The RecyclerView creates only as many view holders as are needed to display the on-screen portion of the dynamic content,
 * plus a few extra. As the user scrolls through the list, the RecyclerView takes the off-screen views and rebinds them to the
 * data which is scrolling onto the screen.
 *
 * The view holder objects are managed by an adapter, which you create by extending RecyclerView.Adapter.
 * The adapter creates view holders as needed. The adapter also binds the view holders to their data.
 * It does this by assigning the view holder to a position, and calling the adapter's onBindViewHolder() method.
 * That method uses the view holder's position to determine what the contents should be, based on its list position.*/


/**To feed all your data to the list, you must extend the RecyclerView.Adapter class. This object creates views for items,
 * and replaces the content of some of the views with new data items when the original item is no longer visible.*/
public class CustomRecyclerViewAdapter extends RecyclerView.Adapter<CustomRecyclerViewAdapter.CustomViewHolder> {

    private ArrayList<CardViewItem> mItemsList;
    private OnRecyclerItemClickListener mListener; // This listener is going to be in our main activity
    private static Context mContext;

    public interface OnRecyclerItemClickListener {
        void onRecyclerCardviewClicked(int position);
        void onMoreDotsClicked(int position);
    }

    // With this method we connect our main activity with the OnRecyclerItemClickListener interface
    public void setOnRecyclerItemClickListener(Context context, OnRecyclerItemClickListener listener){
        mContext = context;
        mListener = listener;
    }

    // Provide a reference to the views for each list item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a list item in a view holder
    public static class CustomViewHolder extends RecyclerView.ViewHolder{

        public RelativeLayout mBackgroundCardview;
        public CircularImageView mImageView;
        public TextView mTextView1;
        public TextView mTextView2;
        public TextView mTextView3;
        public ImageView mMoreDotsImage;

        public CustomViewHolder(@NonNull View itemView, final OnRecyclerItemClickListener listener) {
            super(itemView);

            mImageView = itemView.findViewById(R.id.imageViewID);
            mTextView1 = itemView.findViewById(R.id.textView1ID);
            mTextView2 = itemView.findViewById(R.id.textView2ID);
            mTextView3 = itemView.findViewById(R.id.textView3ID);
            mBackgroundCardview = itemView.findViewById(R.id.cardview_background);

            mMoreDotsImage = itemView.findViewById(R.id.imageViewDelete);


            // Passing the position of the item clicked
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) { // Checking if the position of the adapter item is valid
                            listener.onRecyclerCardviewClicked(position);
                        }
                    }
                }
            });

            mMoreDotsImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) { // Checking if the position of the adapter item is valid
                            listener.onMoreDotsClicked(position);
                        }
                    }
                }
            });
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    CustomRecyclerViewAdapter(ArrayList<CardViewItem> itemList){
        mItemsList = itemList;
    }


    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_item, parent, false);
        CustomViewHolder cvh = new CustomViewHolder(view, mListener);
        return cvh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder customViewHolder, int position) {
        // - get element from your dataset/list at this position
        // - replace the contents of the view with that element
        CardViewItem currentItem = mItemsList.get(position);

        customViewHolder.mImageView.setImageResource(currentItem.getImageResource());
        customViewHolder.mTextView1.setText(currentItem.getHeadLineText());
        customViewHolder.mTextView1.setTextColor(currentItem.getTextColor());
        customViewHolder.mTextView2.setText(currentItem.getSubLineText());
        customViewHolder.mTextView2.setTextColor(currentItem.getTextColor());
        customViewHolder.mTextView3.setText(currentItem.getMainText());
        customViewHolder.mTextView3.setTextColor(currentItem.getTextColor());
        customViewHolder.mBackgroundCardview.setSelected(currentItem.isHighlighted());

//        customViewHolder.itemView.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.zoom_in));

    }

    // Return the size of your dataset/list (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mItemsList.size();
    }

}