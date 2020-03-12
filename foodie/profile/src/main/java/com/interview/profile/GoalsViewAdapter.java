package com.interview.profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.interview.androidlib.Data;
import com.interview.androidlib.Goals;
import com.interview.androidlib.Profile;

import java.util.ArrayList;
import java.util.List;

public class GoalsViewAdapter extends
        RecyclerView.Adapter<GoalsViewHolder> {

    // Provide a direct reference to each of the views within a data item_swipe
    // Used to cache the views within the item_swipe layout for fast access

    // Store a member variable for the contacts
    private List<Goals> goals;


    // Pass in the contact array into the constructor
    public GoalsViewAdapter(List<Goals> goals) {
        this.goals = goals;
    }

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public GoalsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.list_goals, parent, false);

        // Return a new holder instance
        GoalsViewHolder viewHolder = new GoalsViewHolder(contactView);
        return viewHolder;
    }

    // Involves populating data into the item_swipe through holder
    @Override
    public void onBindViewHolder(GoalsViewHolder viewHolder, int position) {
        // Get the data model based on position
        Goals g = this.goals.get(position);

        // Set item_swipe views based on your views and data model
        viewHolder.checkBox_Goals.setChecked(g.isAGoal());
        viewHolder.textView_Goals.setText(g.getGoal());

        //new DownloadImage((ImageView) viewHolder.imageView_logo).execute("https://logo.clearbit.com/" + logos.getUrl());

        //Button button = viewHolder.messageButton;f
        //button.setText(contact.isOnline() ? "Visit" : "Closed");
        //button.setEnabled(contact.isOnline());
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return goals.size();
    }
}