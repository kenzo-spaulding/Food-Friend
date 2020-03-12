package com.interview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.interview.androidlib.Profile;

import java.util.List;


// Create the basic adapter extending from RecyclerView.Adapter
// Note that we specify the custom ViewHolder which gives us access to our views
public class ProfileViewAdapter extends
        RecyclerView.Adapter<ProfilesViewHolder> {

    // Provide a direct reference to each of the views within a data item_swipe
    // Used to cache the views within the item_swipe layout for fast access

    // Store a member variable for the contacts
    private List<Profile> profiles;


    // Pass in the contact array into the constructor
    public ProfileViewAdapter(List<Profile> profiles) {
        this.profiles = profiles;
    }

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public ProfilesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.list_profile, parent, false);

        // Return a new holder instance
        ProfilesViewHolder viewHolder = new ProfilesViewHolder(contactView);
        return viewHolder;
    }

    // Involves populating data into the item_swipe through holder
    @Override
    public void onBindViewHolder(ProfilesViewHolder viewHolder, int position) {
        // Get the data model based on position
        Profile pro = this.profiles.get(position);

        // Set item_swipe views based on your views and data model
        viewHolder.textView_Address.setText(pro.getAddress());
        viewHolder.textView_AddressTitle.setText(pro.getAddressTitle());

        //new DownloadImage((ImageView) viewHolder.imageView_logo).execute("https://logo.clearbit.com/" + logos.getUrl());

        //Button button = viewHolder.messageButton;f
        //button.setText(contact.isOnline() ? "Visit" : "Closed");
        //button.setEnabled(contact.isOnline());
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return profiles.size();
    }
}