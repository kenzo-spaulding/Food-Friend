package com.interview.profile;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class GoalsViewHolder extends RecyclerView.ViewHolder {
    protected ImageView imageView_LikesDislikes;
    protected TextView textView_Category;

    protected TextView textView_Goals;
    protected CheckBox checkBox_Goals;

    protected TextView textView_AddressTitle;
    protected TextView textView_Address;

    // We also create a constructor that accepts the entire item_swipe row
    // and does the view lookups to find each subview
    public GoalsViewHolder(View itemView) {
        // Stores the itemView in a public final member variable that can be used
        // to access the context from any ViewHolder instance.
        super(itemView);

        checkBox_Goals = (CheckBox) itemView.findViewById(R.id.checkBox_Goals);
        textView_Goals = (TextView) itemView.findViewById(R.id.textView_Goals);

        checkBox_Goals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Send to firebase the change
            }
        });
    }
}