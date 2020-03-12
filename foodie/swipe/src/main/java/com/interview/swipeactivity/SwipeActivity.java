package com.interview.swipeactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.icu.util.DateInterval;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.time.*;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableReference;
import com.google.firebase.functions.HttpsCallableResult;
import com.interview.androidlib.GPS;
import com.interview.lib.Json;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Calendar;

public class SwipeActivity extends AppCompatActivity implements SwipeFlingAdapterView.onFlingListener,
        SwipeFlingAdapterView.OnItemClickListener, LocationListener {

    //////////  LAYOUT VARIABLES  //////////////////////////////////////////
    private ImageView imageView_TrainingBackground;
    private ImageView imageView_TrainingImage;

    private CardView cardView_CardView;

    private TextView textView_ItemName;
    private TextView textView_Question;
    private TextView textView_ItemDescription;
    private TextView textView_CardText;

    private Button button_Info;
    private Button button_Like;
    private Button button_Dislike;

    //////////  Backend Variables   ////////////////////////////////////////
    int i;

    private GPS gps;
    Date currentTime;
    int timeOfDay;

    public HttpsCallableReference callable;
    private SwipeFlingAdapterView flingContainer;

    private ArrayAdapter<String> arrayAdapterImg;
    private ArrayList<String> str;


    //////////  Functions   ////////////////////////////////////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe);

        //////  Layout Variables Assigned    //////////////////////////////
        assignLayoutVariables();

    }

    private void assignLayoutVariables(){
        //////  Layout Variables Assigned    //////////////////////////////
        imageView_TrainingBackground = (ImageView) findViewById(R.id.imageView_TrainingBackground);
        imageView_TrainingImage = (ImageView) findViewById(R.id.imageView_TrainingImage);

        cardView_CardView = (CardView) findViewById(R.id.cardView_CardView);

        textView_ItemName = (TextView) findViewById(R.id.textView_itemName);
        textView_Question = (TextView) findViewById(R.id.textView_question);
        textView_ItemDescription = (TextView) findViewById(R.id.textView_itemDescription);
        textView_CardText = (TextView) findViewById(R.id.textView_card);

        button_Info = (Button) findViewById(R.id.button_Info);
        button_Like = (Button) findViewById(R.id.button_like);
        button_Dislike = (Button) findViewById(R.id.button_dislike);

        ///////////////////////////////////////////////////////////////////

        str = new ArrayList<>(Arrays.asList("chicken", "beef", "salad", "soup"));
        arrayAdapterImg = new ArrayAdapter<>(this, R.layout.item_card, R.id.textView_card, str);

        gps = new GPS(this);
        currentTime = Calendar.getInstance().getTime();
        timeOfDay = 0;

        flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);
        flingContainer.setAdapter(arrayAdapterImg);
        flingContainer.setFlingListener(this);
        flingContainer.setOnItemClickListener(this);
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
                    JSONObject jsonObject = Json.fromObject(result.getData());

                    textView_ItemDescription.setText(jsonObject.toString());

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

    @Override
    public void removeFirstObjectInAdapter() {
        // this is the simplest way to delete an object from the Adapter (/AdapterView)
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
        //TODO: add additional items to the list to render before it ends
        str.add("ended");
        arrayAdapterImg.notifyDataSetChanged();
        i++;
    }

    @Override
    public void onScroll(float v) {
    }

    @Override
    public void onItemClicked(int i, Object o) {
        //TODO: Do you need to change the image when clicked/tapped?
        textView_ItemDescription.setText(gps.getLastKnownAddress().toString());
    }

    public void onClick_Dislike(View view){
        if (str.size() > 0 &&flingContainer != null && flingContainer.getTopCardListener() != null)
                flingContainer.getTopCardListener().selectLeft();
    }

    public void onClick_Like(View view){
        if (str.size() > 0 && flingContainer != null && flingContainer.getTopCardListener() != null)
                flingContainer.getTopCardListener().selectRight();
    }

    public void onClick_Info(View view){
        String message = "Swipe image left to like, swipe right to dislike";
        Toast.makeText(SwipeActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationChanged(Location location) {
        gps.onLocationChanged(location);
        currentTime = Calendar.getInstance().getTime();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    protected void onPause() {
        super.onPause();
        gps.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        gps.onResume();
    }
}

