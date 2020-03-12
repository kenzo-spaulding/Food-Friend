package com.interview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.interview.androidlib.Data;

import java.util.List;

public class DataViewAdapter extends
        RecyclerView.Adapter<DataViewHolder> {

    // Provide a direct reference to each of the views within a data item_swipe
    // Used to cache the views within the item_swipe layout for fast access

    // Store a member variable for the contacts
    private List<Data> data;


    // Pass in the contact array into the constructor
    public DataViewAdapter(List<Data> data) {
        this.data = data;
    }

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public DataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.list_data, parent, false);

        // Return a new holder instance
        DataViewHolder viewHolder = new DataViewHolder(contactView);
        return viewHolder;
    }

    // Involves populating data into the item_swipe through holder
    @Override
    public void onBindViewHolder(DataViewHolder viewHolder, int position) {
        // Get the data model based on position
        Data d = this.data.get(position);

        // Set item_swipe views based on your views and data model
        viewHolder.textView_Category.setText(d.getCategory());
        ContextCompat.getDrawable(viewHolder.imageView_LikesDislikes.getContext(), R.drawable.ic_profile_dislikes);
        ContextCompat.getDrawable(viewHolder.imageView_LikesDislikes.getContext(), R.drawable.ic_profile_likes);


        //new DownloadImage((ImageView) viewHolder.imageView_LikesDislikes).execute("https://logo.clearbit.com/" + logos.getUrl());

        //Button button = viewHolder.messageButton;f
        //button.setText(contact.isOnline() ? "Visit" : "Closed");
        //button.setEnabled(contact.isOnline());
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return data.size();
    }
}