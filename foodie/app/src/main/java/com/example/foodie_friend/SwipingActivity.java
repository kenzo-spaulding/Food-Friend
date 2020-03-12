package com.example.foodie_friend;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import org.json.*;

import android.app.Activity;

import com.example.foodie_friend.frontend.dependencies.JSON;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableReference;
import com.google.firebase.functions.HttpsCallableResult;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lorentzos.flingswipe.SwipeFlingAdapterView.*;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lorentzos.flingswipe.SwipeFlingAdapterView;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.foodie_friend.frontend.dependencies.SleepTimer;
import com.example.foodie_friend.frontend.dependencies.DownloadImage;

class ImageContent{
    private String name;
    private String url;
    public ImageContent(String name, String url){
        this.name = name;
        this.url = url;
    }
    void setImage(ImageView imageView){
        new DownloadImage((ImageView) imageView).execute(url);
    }
    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
}

class RowItem {
    private int imageId;
    private String title;
    private String desc;

    public RowItem(int imageId, String title, String desc) {
        this.imageId = imageId;
        this.title = title;
        this.desc = desc;
    }
    public int getImageId() {
        return imageId;
    }
    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    @Override
    public String toString() {
        return title + "\n" + desc;
    }
}

class ImageContentAdapter extends ArrayAdapter<RowItem>{
    Context context;

    public ImageContentAdapter(@NonNull Context context, int resource, @NonNull List<RowItem> objects) {
        super(context, resource, objects);
        this.context = context;
    }

    private class ViewHolder{
        ImageView imageView;
        TextView textView;
        TextView textDesc;
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        RowItem rowItem = getItem(position);
        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_swipe, null);
            holder = new ViewHolder();
            holder.textView = (TextView) convertView.findViewById(R.id.textView_card);
            holder.textView = (TextView) convertView.findViewById(R.id.textView_card);
            holder.imageView = (ImageView) convertView.findViewById(R.id.imageView_itemImage);

            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        holder.textView.setText(rowItem.getTitle());
        holder.textDesc.setText(rowItem.getTitle());
        holder.imageView.setImageResource(rowItem.getImageId());
        //holder.imageView.setImageURI(Uri.parse("https://logo.clearbit.com/mcdonalds.com"));
        //new DownloadImage(holder.imageView).execute("https://logo.clearbit.com/mcdonalds.com");

        return convertView;
    }
}




public class SwipingActivity extends AppCompatActivity implements onFlingListener, OnItemClickListener {

    private ArrayList<RowItem> imageContents;
    private ArrayAdapter<String> arrayAdapterImg;
    private int i;
    private SwipeFlingAdapterView flingContainer;
    private ArrayList<HashMap> item = new ArrayList<HashMap>();

    private ArrayList<String> str = new ArrayList<>(Arrays.asList("chicken", "beef", "salad", "soup"));

    public HttpsCallableReference callable;

    private TextView textView_ItemName;
    private TextView textView_ItemDescription;
    private TextView textView_Question;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swiping);

        onCallable();

        imageContents = new ArrayList<>();
        imageContents.add(new RowItem(R.drawable.ic_training_image_background, "FastFood", "https://upload.wikimedia.org/wikipedia/commons/thumb/3/36/McDonald%27s_Golden_Arches.svg/1200px-McDonald%27s_Golden_Arches.svg.png"));
        imageContents.add(new RowItem(R.drawable.ic_training_image_background, "Pizza", "https://upload.wikimedia.org/wikipedia/commons/thumb/3/36/McDonald%27s_Golden_Arches.svg/1200px-McDonald%27s_Golden_Arches.svg.png"));
        imageContents.add(new RowItem(R.drawable.ic_training_image_background, "Sandwiches", "https://upload.wikimedia.org/wikipedia/commons/thumb/3/36/McDonald%27s_Golden_Arches.svg/1200px-McDonald%27s_Golden_Arches.svg.png"));
        imageContents.add(new RowItem(R.drawable.ic_training_image_background, "Shipped","https://upload.wikimedia.org/wikipedia/commons/thumb/3/36/McDonald%27s_Golden_Arches.svg/1200px-McDonald%27s_Golden_Arches.svg.png"));
        imageContents.add(new RowItem(R.drawable.ic_training_image_background, "Seafood","https://upload.wikimedia.org/wikipedia/commons/thumb/3/36/McDonald%27s_Golden_Arches.svg/1200px-McDonald%27s_Golden_Arches.svg.png"));
        imageContents.add(new RowItem(R.drawable.ic_training_image_background, "Breakfast", "https://upload.wikimedia.org/wikipedia/commons/thumb/3/36/McDonald%27s_Golden_Arches.svg/1200px-McDonald%27s_Golden_Arches.svg.png"));

        //////////////////////////////////////////////////////

        textView_ItemName = findViewById(R.id.textView_itemName);
        textView_ItemDescription = findViewById(R.id.textView_itemDescription);
        textView_Question = findViewById(R.id.textView_question);

        arrayAdapterImg = new ArrayAdapter<>(this, R.layout.item_swipe, R.id.textView_card, str);

        flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);

        flingContainer.setAdapter(arrayAdapterImg);
        flingContainer.setFlingListener(this);
        flingContainer.setOnItemClickListener(this);

        //Remove when map is finished
        //Intent intent = new Intent(this, ProfileActivity.class);
        //Pair<SwipingActivity, Intent> pair = new Pair<>(this, intent);
        //SleepTimer.delay(5, pair);
    }

    @Override
    public void removeFirstObjectInAdapter() {
        // this is the simplest way to delete an object from the Adapter (/AdapterView)
        Log.d("LIST", "removed object!");
        str.remove(0);
        arrayAdapterImg.notifyDataSetChanged();
    }

    @Override
    public void onLeftCardExit(Object o) {
        //TODO: Log into Firebase the result
    }

    @Override
    public void onRightCardExit(Object o) {
        //TODO: Log into Firebase the result
    }

    @Override
    public void onAdapterAboutToEmpty(int i) {
        // Ask for more data here
        //str.add(new ImageContent("Food item: ".concat(String.valueOf(i)), "http://logo.clearbit.com/spotify.com?size=60"));
        str.add("ended");
        arrayAdapterImg.notifyDataSetChanged();
        Log.d("LIST", "notified");
        i++;
    }

    @Override
    public void onScroll(float v) {
        //View view = flingContainer.getSelectedView();
        //view.findViewById(R.id.item_swipe_right_indicator).setAlpha(scrollProgressPercent < 0 ? -scrollProgressPercent : 0);
        //view.findViewById(R.id.item_swipe_left_indicator).setAlpha(scrollProgressPercent > 0 ? scrollProgressPercent : 0);
    }

    @Override
    public void onItemClicked(int i, Object o) {
        //TODO: Do you need to change the image when clicked/tapped?
    }


    public void onClick_Dislike(View view){
        flingContainer.getTopCardListener().selectLeft();
    }

    public void onClick_Like(View view){
        flingContainer.getTopCardListener().selectRight();
    }

    public void onClick_Info(View view){
        String message = "Swipe image left to like, swipe right to dislike";
        Toast.makeText(SwipingActivity.this, message, Toast.LENGTH_SHORT).show();
    }


    public void onCallable(){
        /**
         * This is a runnable multi-threaded overriden function.
         * If you run something outside this, its not guaranteed
         * you're signed in until its completed. If you must
         * be signed in FIRST before continuing on, place the
         * next line of code inside the "onComplete" method
         */
        this.callable = FirebaseFunctions.getInstance().getHttpsCallable("recommendations");
        Map<String, Object> day = new HashMap<>();
        day.put("timeOfDay", 0); // TODO: remember 0 means breakfast
        day.put("training", true);
        Task<HttpsCallableResult> firebaseCall = this.callable.call(day);

        firebaseCall.addOnCompleteListener(this, new OnCompleteListener<HttpsCallableResult>() {
            @Override
            public void onComplete(@NonNull Task<HttpsCallableResult> task) {
                if (task.isSuccessful()){
                    HttpsCallableResult result = task.getResult();
                    Map data = (Map) ((List) result.getData()).get(0);
                    JSONObject json = null;
                    json = new JSONObject(data);

                    textView_ItemDescription.setText(json.toString());

                    //textView_ItemDescription.setText(description);
                    //textView_ItemName.setText(restaurantName);
                    //textView_Question.setText(question);
                    //textView_ItemDescription.setText(data.toString());
                }
                else{
                    textView_ItemDescription.setText("Failed");
                }
            }
        });
    }


}




/*

        arrayAdapter = new ArrayAdapter<>(this, R.layout.item_swipe, R.id.textView_card, textCards );

        SwipeFlingAdapterView flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);

        flingContainer.setAdapter(arrayAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                Log.d("LIST", "removed object!");

                imageContents.remove(0);
                textCards.remove(0);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                //Do something on the left!
                //You also have access to the original object.
                //If you want to use it just cast it (String) dataObject
                Toast.makeText(SwipingActivity.this, "Left!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                Toast.makeText(SwipingActivity.this, "Right!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                // Ask for more data here
                imageContents.add(new ImageContent("Food item: ".concat(String.valueOf(i)), "http://logo.clearbit.com/spotify.com?size=60"));
                arrayAdapter.notifyDataSetChanged();
                Log.d("LIST", "notified");
                i++;
            }

            @Override
            public void onScroll(float scrollProgressPercent) {
                //View view = flingContainer.getSelectedView();
                //view.findViewById(R.id.item_swipe_right_indicator).setAlpha(scrollProgressPercent < 0 ? -scrollProgressPercent : 0);
                //view.findViewById(R.id.item_swipe_left_indicator).setAlpha(scrollProgressPercent > 0 ? scrollProgressPercent : 0);
            }
        });


        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                Toast.makeText(SwipingActivity.this, "Clicked!", Toast.LENGTH_SHORT).show();
            }
        });

 */
