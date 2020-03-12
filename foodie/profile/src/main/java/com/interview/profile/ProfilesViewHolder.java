package com.interview.profile;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class ProfilesViewHolder extends RecyclerView.ViewHolder {
    protected TextView textView_AddressTitle;
    protected TextView textView_Address;

    // We also create a constructor that accepts the entire item_swipe row
    // and does the view lookups to find each subview
    public ProfilesViewHolder(View itemView) {
        // Stores the itemView in a public final member variable that can be used
        // to access the context from any ViewHolder instance.
        super(itemView);

        textView_AddressTitle = (TextView) itemView.findViewById(R.id.textView_AddressTitle);
        textView_Address = (TextView) itemView.findViewById(R.id.textView_Address);
    }
}