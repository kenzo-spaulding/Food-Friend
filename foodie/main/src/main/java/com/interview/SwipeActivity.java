package com.interview;

import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableReference;
import com.google.firebase.functions.HttpsCallableResult;
import com.interview.androidlib.GPS;
import com.interview.lib.DateTime;
import com.interview.lib.Json;
import com.interview.login.LoginActivity;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private BottomNavigationView bottomNavigationView;
    private ProgressBar progressBar_Swipe;


    //////////  Backend Variables   ////////////////////////////////////////
    int i;

    private GPS gps;
    Date currentTime;
    int timeOfDay;

    public HttpsCallableReference callable;
    private SwipeFlingAdapterView flingContainer;

    private ArrayAdapter<String> arrayAdapterImg;
    private ArrayList<String> str;
    ArrayList<JSONObject> jsonList;
    ArrayList<String> losers = new ArrayList<>();
    JSONArray losersJson = new JSONArray();

    private boolean loading = true;
    private FirebaseAuth auth;


    //////////  Functions   ////////////////////////////////////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe);
        FirebaseApp.initializeApp(this);
        auth = FirebaseAuth.getInstance();

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

        progressBar_Swipe = (ProgressBar) findViewById(R.id.progressBar_Swipe);

        ///////////////////////////////////////////////////////////////////

        jsonList = new ArrayList<>();
        str = new ArrayList<>(Arrays.asList("Loading Data"));
        arrayAdapterImg = new ArrayAdapter<>(this, R.layout.item_card, R.id.textView_card, str);

        gps = new GPS(this);
        currentTime = Calendar.getInstance().getTime();
        timeOfDay = 0;

        flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);
        flingContainer.setAdapter(arrayAdapterImg);
        flingContainer.setFlingListener(this);
        flingContainer.setOnItemClickListener(this);


        bottomNavigationView = findViewById(R.id.nav_view);
        bottomNavigationView.setSelectedItemId(R.id.navigation_notifications);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.navigation_home:
                        onClick_logout();
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.navigation_dashboard:
                        if (!loading){
                            startActivity(new Intent(getApplicationContext(), RecyclerViewActivity.class));
                            overridePendingTransition(0, 0);
                        }
                        else
                        {
                            String message = "Wait until the list is done loading.";
                            Toast.makeText(SwipeActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    case R.id.navigation_notifications:
                        return true;
                    default:
                        return false;
                }
            }
        });
        onCallable();
    }

    private void onClick_logout(){
        auth.signOut();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void onCallable(){
        /**
         * This is a runnable multi-threaded overriden function.
         * If you run something outside this, its not guaranteed
         * you're signed in until its completed. If you must
         * be signed in FIRST before continuing on, place the
         * next line of code inside the "onComplete" method
         */
        progressBar_Swipe.setVisibility(View.VISIBLE);
        disableAllInputs();
        this.callable = FirebaseFunctions.getInstance().getHttpsCallable("recommendations");
        Map<String, Object> day = new HashMap<>();
        day.put("timeOfDay", DateTime.timeOfDayInt());
        day.put("training", true);
        Task<HttpsCallableResult> firebaseCall = this.callable.call(day);

        firebaseCall.addOnCompleteListener(this, new OnCompleteListener<HttpsCallableResult>() {
            @Override
            public void onComplete(@NonNull Task<HttpsCallableResult> task) {
                if (task.isSuccessful()){
                    HttpsCallableResult result = task.getResult();
                    textView_ItemDescription.setText(result.getData().toString());
                    try {
                        List v = ((List) result.getData());
                        for (int i = 0; i < v.size(); i++) {
                            JSONObject item = new JSONObject((Map) v.get(i));
                            jsonList.add(item);
                            try {
                                str.add(item.getString("headQuery"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        progressBar_Swipe.setVisibility(View.INVISIBLE);
                        i = str.size() + v.size();
                        arrayAdapterImg.notifyDataSetChanged();
                        flingContainer.getTopCardListener().selectLeft();
                        enableAllInputs();
                    }catch (Exception e){ enableAllInputs(); }
                }
                else{
                    textView_ItemDescription.setText("No more available training data.");
                    progressBar_Swipe.setVisibility(View.INVISIBLE);
                    enableAllInputs();
                }
            }
        });
    }

    private void disableAllInputs(){
        loading = true;
        bottomNavigationView.setEnabled(false);
        bottomNavigationView.setFocusable(false);
        bottomNavigationView.setFocusableInTouchMode(false);
        bottomNavigationView.setClickable(false);
        bottomNavigationView.setContextClickable(false);
    }

    private void enableAllInputs(){
        loading = false;
        bottomNavigationView.setEnabled(true);
        bottomNavigationView.setFocusable(true);
        bottomNavigationView.setFocusableInTouchMode(true);
        bottomNavigationView.setClickable(true);
        bottomNavigationView.setContextClickable(true);

        bottomNavigationView.setSelectedItemId(R.id.navigation_notifications);
    }

    @Override
    public void removeFirstObjectInAdapter() {
        // this is the simplest way to delete an object from the Adapter (/AdapterView)
        if (!loading) {
            jsonList.remove(0);
        }

        str.remove(0);
        arrayAdapterImg.notifyDataSetChanged();
    }

    @Override
    public void onLeftCardExit(Object o) {
        if (!loading)
            losers.add(str.get(i));
    }

    @Override
    public void onRightCardExit(Object o) {
        try {
            this.callable = FirebaseFunctions.getInstance().getHttpsCallable("updateUserPrefs");
            Map<String, Object> day = new HashMap<>();
            day.put("timeOfDay", DateTime.timeOfDayInt());
            day.put("winner", str.get(i));
            day.put("loser", (new JSONArray(losers)).toString());
            Task<HttpsCallableResult> firebaseCall = this.callable.call(day);

            losers.clear();
        }
        catch (Exception e) { e.printStackTrace(); }
    }

    @Override
    public void onAdapterAboutToEmpty(int i) {
        // Ask for more data here
        //str.add(new ImageContent("Food item: ".concat(String.valueOf(i)), "http://logo.clearbit.com/spotify.com?size=60"));
        //TODO: add additional items to the list to render before it ends
        str.add("Almost there...");
        if (!loading)
            onCallable();
        arrayAdapterImg.notifyDataSetChanged();
        i++;
    }

    @Override
    public void onScroll(float v) {
    }

    @Override
    public void onItemClicked(int i, Object o) {
    }

    public void onClick_Dislike(View view){
        if (flingContainer != null && flingContainer.getTopCardListener() != null && str != null && str.size() > 0)
                flingContainer.getTopCardListener().selectLeft();
    }

    public void onClick_Like(View view){
        if (flingContainer != null && flingContainer.getTopCardListener() != null && str != null && str.size() > 0)
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

