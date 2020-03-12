package com.interview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

// Create the basic adapter extending from RecyclerView.Adapter
// Note that we specify the custom ViewHolder which gives us access to our views
public class RecyclerViewAdapter extends
        RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private OnItemClickListener mListener;
    public interface OnItemClickListener{
        void OnItemClick(int i);
    }

    // Provide a direct reference to each of the views within a data item_swipe
    // Used to cache the views within the item_swipe layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row

        protected ImageView imageView_logo;
        protected TextView textView_FoodName;
        protected TextView textView_FoodSize;
        protected TextView textView_FoodMods;
        protected TextView textView_DrinkName;
        protected TextView textView_DrinkSize;
        protected TextView textView_DrinkMods;

        OnItemClickListener listener;

        // We also create a constructor that accepts the entire item_swipe row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView, OnItemClickListener listener) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            this.listener = listener;

            imageView_logo = (ImageView) itemView.findViewById(R.id.imageview_CompanyLogo);
            textView_FoodName = (TextView) itemView.findViewById(R.id.textView_FoodName);
            textView_FoodSize = (TextView) itemView.findViewById(R.id.textView_FoodSize);
            textView_FoodMods = (TextView) itemView.findViewById(R.id.textView_FoodMods);
            textView_DrinkName = (TextView) itemView.findViewById(R.id.textView_DrinkName);
            textView_DrinkSize = (TextView) itemView.findViewById(R.id.textView_DrinkSize);
            textView_DrinkMods = (TextView) itemView.findViewById(R.id.textView_FoodMods);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.OnItemClick(getAdapterPosition());
        }
    }

    // Store a member variable for the contacts
    private List<JSONObject> json;

    // Pass in the contact array into the constructor
    public RecyclerViewAdapter(List<JSONObject> json, OnItemClickListener listener) {
        this.json = json;
        mListener = listener;
    }

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View list_content = inflater.inflate(R.layout.list_content, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(list_content, mListener);
        return viewHolder;
    }

    // Involves populating data into the item_swipe through holder
    @Override
    public void onBindViewHolder(RecyclerViewAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        JSONObject j = this.json.get(position);

        // Set item_swipe views based on your views and data model
        try {
            viewHolder.textView_FoodName.setText(j.getString("name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            viewHolder.textView_DrinkName.setText(j.getString("price"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //new DownloadImage((ImageView) viewHolder.imageView_logo).execute("https://logo.clearbit.com/" + j.getUrl());
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return this.json.size();
    }
}