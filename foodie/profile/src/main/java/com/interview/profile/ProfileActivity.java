package com.interview.profile;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableReference;
import com.google.firebase.functions.HttpsCallableResult;
import com.interview.androidlib.Data;
import com.interview.androidlib.DownloadImage;
import com.interview.androidlib.Firebase;
import com.interview.androidlib.Goals;
import com.interview.androidlib.Profile;
import com.interview.lib.DateTime;
import com.interview.lib.Json;
import com.interview.lib.Logo;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {
    private static final String DEFAULT_IMAGE = "https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.thepeakid.com%2Fpeak-coeur-dalene%2Fpersonal-training%2Fdefault-profile-picture%2F&psig=AOvVaw3hq40C9crA1m2VzcA-QhVK&ust=1584104941790000&source=images&cd=vfe&ved=0CAIQjRxqFwoTCLDL2PmAlegCFQAAAAAdAAAAABAD";


    //////////  LAYOUT VARIABLES  //////////////////////////////////////////
    ImageView imageView_ProfileImage;

    TextView textView_UserName;
    TextView textView_TotalLikes;
    TextView textView_TotalDislikes;

    TabLayout tabLayout_Layout;

    TabItem tabItem_ProfileTab;
    TabItem tabItem_GoalsTab;
    TabItem tabItem_DataTab;

        //// List Variables: DataTab
    ImageView imageView_LikeDislike;
    TextView textView_Category;

        //// List Variables: GoalsTab
    CheckBox checkBox_Goal;
    TextView textView_Goal;

        //// List Variables: ProfileTab
    TextView textView_AddressTitle;
    TextView textView_Address;

    RecyclerView recyclerView_Frame;
    ProfileViewAdapter profileAdapter;
    GoalsViewAdapter goalsAdapter;
    DataViewAdapter dataAdapter;


    //////////  Backend Variables   ////////////////////////////////////////
    ArrayList<Profile> profile;
    ArrayList<Goals> goals;
    ArrayList<Data> data;

    String name;
    String profile_image;


    private HttpsCallableReference callable;
    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        FirebaseApp.initializeApp(this);

        //////  Layout Variables Assigned    //////////////////////////////
        imageView_ProfileImage = (ImageView) findViewById(R.id.imageView_ProfileImage);

        textView_UserName = (TextView) findViewById(R.id.textView_UserName);
        textView_TotalLikes = (TextView) findViewById(R.id.textView_TotalDislikes);
        textView_TotalDislikes = (TextView) findViewById(R.id.textView_TotalLikes);

        tabLayout_Layout = (TabLayout) findViewById(R.id.tabLayout_Layout);

        tabItem_ProfileTab = (TabItem) findViewById(R.id.tabItem_ProfileTab);
        tabItem_GoalsTab = (TabItem) findViewById(R.id.tabItem_GoalsTab);
        tabItem_DataTab = (TabItem) findViewById(R.id.tabItem_DataTab);

            //// List Variables: DataTab
        imageView_LikeDislike = (ImageView) findViewById(R.id.imageView_LikesDislikes);
        textView_Category = (TextView) findViewById(R.id.textView_Category);

            //// List Variables: GoalsTab
        checkBox_Goal = (CheckBox) findViewById(R.id.checkBox_Goals);
        textView_Goal = (TextView) findViewById(R.id.textView_Goals);

            //// List Variables: ProfileTab
        textView_AddressTitle = (TextView) findViewById(R.id.textView_AddressTitle);
        textView_Address =(TextView) findViewById(R.id.textView_Address);

        recyclerView_Frame = (RecyclerView) findViewById(R.id.recyclerViewFrame);



        // Initialize Recycler View Components
        profile = new ArrayList<>();
        data = new ArrayList<>();
        goals = new ArrayList<>();

        goals.add(new Goals(true, "Driving"));
        goals.add(new Goals(false, "Walking"));

        setProfileData();
        startListView();
        setTabListener();
    }

    private void setProfileData(){
        //TODO: Set the profiles & Data
        profile.add(new Profile("jlkfasdjl", "kfdjas"));
        profile.add(new Profile("jlkfasdjl", "kfdjas"));
        profile.add(new Profile("jlkfasdjl", "kfdjas"));
        profile.add(new Profile("jlkfasdjl", "kfdjas"));
        profile.add(new Profile("jlkfasdjl", "kfdjas"));
        data.add(new Data("kfdja", true));
    }

    public void onAuth_SetName(){
        try {
            textView_UserName.setText(mFirebaseAuth.getCurrentUser().getDisplayName());
        } catch (Exception e){ textView_UserName.setText(""); }
    }

    public void onAuth_SetImageURL(){
        try {
            new DownloadImage(imageView_ProfileImage).execute(mFirebaseAuth.getCurrentUser().getPhotoUrl().toString());
        } catch (Exception e){ new DownloadImage(imageView_ProfileImage).execute(DEFAULT_IMAGE); }
    }

    private void startListView(){

        // Categorize the adapters
        profileAdapter = new ProfileViewAdapter(profile);
        goalsAdapter = new GoalsViewAdapter(goals);
        dataAdapter = new DataViewAdapter(data);

        // assign the adapter to the frame
        recyclerView_Frame.setAdapter(profileAdapter);

        // layout manager populates elements
        recyclerView_Frame.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setTabListener(){
        tabLayout_Layout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    recyclerView_Frame.setAdapter(profileAdapter);
                }
                else if (tab.getPosition() == 1) {
                    recyclerView_Frame.setAdapter(goalsAdapter);
                }
                else if (tab.getPosition() == 2) {
                    recyclerView_Frame.setAdapter(dataAdapter);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }

            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });
    }
}
