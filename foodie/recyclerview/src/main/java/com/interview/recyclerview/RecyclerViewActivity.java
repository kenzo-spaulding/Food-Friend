package com.interview.recyclerview;

/**
 * @author Mauricio Lomeli
 * @version Feburary, 2020
 *
 * This activity is generates the list of the recommendation results.
 * It makes use of the RecyclerView to display results without pagination.
 * It displays the images of logos from an API and the results from the index.
 */

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableReference;
import com.google.firebase.functions.HttpsCallableResult;
import com.interview.androidlib.Firebase;
import com.interview.androidlib.Profile;
import com.interview.lib.DateTime;
import com.interview.lib.Json;
import com.interview.lib.Logo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class RecyclerViewActivity extends AppCompatActivity implements RecyclerViewAdapter.OnItemClickListener {


    //////////  LAYOUT VARIABLES  //////////////////////////////////////////
    private RecyclerView recyclerView_Frame;

    //////////  Backend Variables   ////////////////////////////////////////
    ArrayList<JSONObject> jsonList;

    private FirebaseDatabase database;
    private DatabaseReference myRef;
    public HttpsCallableReference callable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);
        FirebaseApp.initializeApp(this);

        //////  Layout Variables Assigned    //////////////////////////////
        recyclerView_Frame = (RecyclerView) findViewById(R.id.recyclerViewFrame);
        onCallable();
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
        day.put("timeOfDay", DateTime.timeOfDayInt()); // TODO: remember 0 means breakfast
        Task<HttpsCallableResult> firebaseCall = this.callable.call(day);

        firebaseCall.addOnCompleteListener(this, new OnCompleteListener<HttpsCallableResult>() {
            @Override
            public void onComplete(@NonNull Task<HttpsCallableResult> task) {
                if (task.isSuccessful()){
                    String str = task.getResult().getData().toString();
                    try {
                        JSONArray json = new JSONArray(str);
                        for (int i = 0; i < json.length(); i++)
                            jsonList.add(json.getJSONObject(i));
                        startListView();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else{

                }
            }
        });
    }

    private void startListView(){
        // Initialize contacts

        // Create adapter passing in the sample user data
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(jsonList, this);

        // Attach the adapter to the recyclerview to populate items
        recyclerView_Frame.setAdapter(adapter);

        // Set layout manager to position the items
        recyclerView_Frame.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void OnItemClick(int i) {
        //Intent intent = new Intent(this, MapsActivity.class);
        //logos.get(i);
        //startActivity(intent);
    }
}
