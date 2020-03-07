package com.example.foodie_friend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;

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


public class SwipingActivity extends AppCompatActivity {

    private ArrayList<ImageContent> imageContents;
    private ArrayAdapter<ImageContent> arrayAdapter;
    private int i;
    private ImageView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swiping);

        imageContents = new ArrayList<>();
        imageContents.add(new ImageContent("Chicken", "https://logo.clearbit.com/mcdonalds.com"));
        imageContents.add(new ImageContent("Burgers", "https://logo.clearbit.com/mcdonalds.com"));
        imageContents.add(new ImageContent("Pizza", "https://logo.clearbit.com/mcdonalds.com"));
        imageContents.add(new ImageContent("Salads","https://logo.clearbit.com/mcdonalds.com"));
        imageContents.add(new ImageContent("Cajun","https://logo.clearbit.com/mcdonalds.com"));
        imageContents.add(new ImageContent("Pasta", "https://logo.clearbit.com/mcdonalds.com"));
        imageContents.add(new ImageContent("Seafood", "https://logo.clearbit.com/mcdonalds.com"));
        imageContents.add(new ImageContent("Vegan", "https://logo.clearbit.com/mcdonalds.com"));

        arrayAdapter = new ArrayAdapter<>(this, R.layout.item_swipe, R.id.textView_card, imageContents );

        SwipeFlingAdapterView flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);

        flingContainer.setAdapter(arrayAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                Log.d("LIST", "removed object!");
                imageContents.remove(0);
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
                imageContents.add(new ImageContent("Food item: ".concat(String.valueOf(i)), "https://logo.clearbit.com/mcdonalds.com"));
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



        //Remove when map is finished

        Intent intent = new Intent(this, MapsActivity.class);
        Pair<SwipingActivity, Intent> pair = new Pair<>(this, intent);
        SleepTimer.delay(5, pair);
    }
}

