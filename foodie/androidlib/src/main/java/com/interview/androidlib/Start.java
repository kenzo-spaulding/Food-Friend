package com.interview.androidlib;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseUser;

public class Start {
    public static void NewActivity(final AppCompatActivity app, Class c, FirebaseUser user){
        Intent intent = new Intent(app, c);
        Bundle bundle = new Bundle();
        bundle.putString("username", user.getDisplayName());
        bundle.putString("email", user.getEmail());
        bundle.putString("photo_url", user.getPhotoUrl().toString());
        bundle.putString("phone_number", user.getPhoneNumber());
        intent.putExtras(bundle);
        app.startActivity(intent);
    }
}
