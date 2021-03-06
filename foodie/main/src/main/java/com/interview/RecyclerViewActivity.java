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
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableReference;
import com.google.firebase.functions.HttpsCallableResult;
import com.interview.androidlib.GPS;
import com.interview.lib.DateTime;
import com.interview.login.LoginActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class RecyclerViewActivity extends AppCompatActivity implements RecyclerViewAdapter.OnItemClickListener {


    //////////  LAYOUT VARIABLES  //////////////////////////////////////////
    private RecyclerView recyclerView_Frame;
    private ProgressBar progressBar_Loading;
    private boolean loading = true;
    BottomNavigationView bottomNavigationView;

    //////////  Backend Variables   ////////////////////////////////////////
    ArrayList<JSONObject> jsonList = new ArrayList<>();
    private FirebaseAuth auth;

    private HttpsCallableReference callable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);
        FirebaseApp.initializeApp(this);
        auth = FirebaseAuth.getInstance();

        //////  Layout Variables Assigned    //////////////////////////////

        bottomNavigationView = findViewById(R.id.nav_view);
        bottomNavigationView.setSelectedItemId(R.id.navigation_dashboard);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.navigation_home:
                        onClick_logout();
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.navigation_dashboard:
                        return true;
                    case R.id.navigation_notifications:
                        if (!loading) {
                            startActivity(new Intent(getApplicationContext(), SwipeActivity.class));
                            overridePendingTransition(0, 0);
                        }
                        else
                        {
                            String message = "Wait until the list is done loading.";
                            Toast.makeText(RecyclerViewActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    default:
                        return false;
                }
            }
        });

        recyclerView_Frame = (RecyclerView) findViewById(R.id.recyclerViewFrame);
        progressBar_Loading = (ProgressBar) findViewById(R.id.loadingRecycler);

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

        progressBar_Loading.setVisibility(View.VISIBLE);
        disableAllInputs();
        this.callable = FirebaseFunctions.getInstance().getHttpsCallable("recommendations");
        Map<String, Object> day = new HashMap<>();
        day.put("timeOfDay", DateTime.timeOfDayInt()); // Remember 0 means breakfast: {0: breakfast, 1: lunch, 2: dinner, 3: late snack}
        Task<HttpsCallableResult> firebaseCall = this.callable.call(day);

        firebaseCall.addOnCompleteListener(this, new OnCompleteListener<HttpsCallableResult>() {
            @Override
            public void onComplete(@NonNull Task<HttpsCallableResult> task) {
                if (task.isSuccessful()){
                    HttpsCallableResult result = task.getResult();
                    List v = ((List) result.getData());
                    for (int i = 0; i < v.size(); i++)
                        jsonList.add(new JSONObject((Map) v.get(i)));

                    sortJSONObjects(jsonList);
                    startListView();
                    progressBar_Loading.setVisibility(View.INVISIBLE);
                    enableAllInputs();
                }
                else{
                    progressBar_Loading.setVisibility(View.INVISIBLE);
                    enableAllInputs();
                }
            }
        });
    }


    // sorts the list into decending order
    private void sortJSONObjects(ArrayList<JSONObject> list){
        if (list.size() > 1){
            Collections.sort(list, new Comparator<JSONObject>() {
                @Override
                public int compare(JSONObject o1, JSONObject o2) {
                    try {
                        if (Double.parseDouble(o1.getString("rating"))
                                > Double.parseDouble(o2.getString("rating")))
                            return -1;
                        else
                            return 1;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return -1;
                }
            });
        }
    }

    // Starts the recycler view
    private void startListView(){
        // Create adapter passing in the user recommendations
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(jsonList, this);

        // Attach the adapter to the recyclerview to populate items
        recyclerView_Frame.setAdapter(adapter);

        // Set layout manager to position the items
        recyclerView_Frame.setLayoutManager(new LinearLayoutManager(this));
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

        bottomNavigationView.setSelectedItemId(R.id.navigation_dashboard);
    }

    private void onClick_logout(){
        auth.signOut();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void OnItemClick(int i) {
        Intent intent = new Intent(this, MapsActivity.class);
        Bundle bundle = new Bundle();
        try {
            bundle.putString("name", jsonList.get(i).getString("name"));
            bundle.putString("image_url", jsonList.get(i).getString("image_url"));
            bundle.putString("review_count", jsonList.get(i).getString("review_count"));
            bundle.putString("rating", jsonList.get(i).getString("rating"));
            bundle.putString("price", jsonList.get(i).getString("price"));
            bundle.putString("distance", jsonList.get(i).getString("distance"));
            bundle.putString("review_count", jsonList.get(i).getString("review_count"));
            bundle.putString("headQuery", jsonList.get(i).getString("headQuery"));

            JSONObject jsn = (JSONObject) jsonList.get(i).get("coordinates");
            bundle.putString("latitude", jsn.getString("latitude"));
            bundle.putString("longitude", jsn.getString("longitude"));

            JSONObject jsn2 = (JSONObject) jsonList.get(i).get("location");
            bundle.putString("address1", jsn2.getString("address1"));
            bundle.putString("address2", jsn2.getString("address2"));
            bundle.putString("city", jsn2.getString("city"));
            bundle.putString("zip_code", jsn2.getString("zip_code"));

        }catch (Exception e) {}
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
