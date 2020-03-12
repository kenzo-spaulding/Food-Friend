package com.interview.profile;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class DataViewHolder extends RecyclerView.ViewHolder {
    protected ImageView imageView_LikesDislikes;
    protected TextView textView_Category;

    // We also create a constructor that accepts the entire item_swipe row
    // and does the view lookups to find each subview
    public DataViewHolder(View itemView) {
        // Stores the itemView in a public final member variable that can be used
        // to access the context from any ViewHolder instance.
        super(itemView);

        imageView_LikesDislikes = (ImageView) itemView.findViewById(R.id.imageView_LikesDislikes);
        textView_Category = (TextView) itemView.findViewById(R.id.textView_Category);
    }
}