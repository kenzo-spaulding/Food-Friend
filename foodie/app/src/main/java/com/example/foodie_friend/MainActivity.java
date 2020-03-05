package com.example.foodie_friend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.example.foodie_friend.frontend.dependencies.FirebaseFunctions;
import com.example.foodie_friend.frontend.dependencies.GoogleFunctions;
import com.example.foodie_friend.ui.login.SignInActivity;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.format.TextStyle;
import java.util.Arrays;
import java.util.List;
import com.example.foodie_friend.frontend.dependencies.SleepTimer;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;


    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mConditionRef = mRootRef.child("test1");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //mFirebaseAuth = FirebaseAuth.getInstance();

        Intent intent = new Intent(this, SignInActivity.class);
        //Pair<MainActivity, Intent> pair = new Pair<>(this, intent);
        //SleepTimer.delay(3, pair);
        this.startActivity(intent);
    }


    @Override
    protected void onStart() {
        super.onStart();
        //FirebaseFunctions.addMConditionRefListeners(mConditionRef);
        //mAuthStateListener = GoogleFunctions.startLogin(this);


    }

    @Override
    protected void onPause() {
        super.onPause();
        //mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }


}
