package com.interview;

/**
 * @author Mauricio Lomeli
 * @version Feburary, 2020
 *
 * This activity is generates the list of the recommendation results.
 * It makes use of the RecyclerView to display results without pagination.
 * It displays the images of logos from an API and the results from the index.
 */

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableReference;
import com.google.firebase.functions.HttpsCallableResult;
import com.interview.lib.DateTime;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class RecyclerViewActivity extends AppCompatActivity implements RecyclerViewAdapter.OnItemClickListener {


    //////////  LAYOUT VARIABLES  //////////////////////////////////////////
    private RecyclerView recyclerView_Frame;

    //////////  Backend Variables   ////////////////////////////////////////
    ArrayList<JSONObject> jsonList = new ArrayList<>();

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
                    HttpsCallableResult result = task.getResult();
                    List v = ((List) result.getData());
                    for (int i = 0; i < v.size(); i++)
                        jsonList.add(new JSONObject((Map) v.get(i)));
                    startListView();
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
        Intent intent = new Intent(this, MapsActivity.class);
        try {
            Bundle bundle = new Bundle();
            bundle.putString("name", jsonList.get(i).getString("name"));
            bundle.putString("image_url", jsonList.get(i).getString("image_url"));
            bundle.putString("review_count", jsonList.get(i).getString("review_count"));
            bundle.putString("rating", jsonList.get(i).getString("rating"));
            bundle.putString("price", jsonList.get(i).getString("price"));
            bundle.putString("distance", jsonList.get(i).getString("distance"));
            bundle.putString("review_count", jsonList.get(i).getString("review_count"));
            bundle.putString("headQuery", jsonList.get(i).getString("headQuery"));

            JSONObject jsn = new JSONObject ((Map) jsonList.get(i).get("location"));
            bundle.putString("latitude", jsn.getString("latitude"));
            bundle.putString("longitude", jsn.getString("longitude"));

            intent.putExtras(bundle);

        }catch (Exception e) {}
        startActivity(intent);
    }
}
