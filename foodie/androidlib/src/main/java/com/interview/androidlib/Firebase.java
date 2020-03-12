package com.interview.androidlib;
/**
 * @author Mauricio Lomeli
 * @version Feburary, 2020
 *
 * This program holds various Firebase functions to help minimize the space
 *  taken up on the main activities.
 */

import android.app.Activity;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuth.AuthStateListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public abstract class Firebase implements ValueEventListener, AuthStateListener{

    private FirebaseAuth mFirebaseAuth;
    private AuthStateListener mAuthStateListener;
    private static DatabaseReference rootReference = FirebaseDatabase.getInstance().getReference();
    private HashMap<String, HashMap> treeRef;

    public Firebase(){
        mFirebaseAuth = FirebaseAuth.getInstance();
        mAuthStateListener = this;
    }


    public boolean isUserSignedIn(){
        return mFirebaseAuth.getCurrentUser() != null;
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        String text = dataSnapshot.getValue(String.class);
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }

    public void logout(){
        this.mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
    }

    public void pause(){
        this.mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
    }

    public void resume(){
        this.mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }
}
