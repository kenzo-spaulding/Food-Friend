package com.innerfoodsearch.login;
/**
 * @author Mauricio Lomeli
 * @version March, 2020
 *
 */
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.Serializable;

public class Authorization implements Serializable {
    public static FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    public static FirebaseAuth.AuthStateListener authStateListener = null;
    public static AuthCredential credential = null;
    public static FirebaseUser user = null;
    public static String username = null;
    public static String password = null;

    public Authorization(){}

    public void nullify(){
        authStateListener = null;
        credential = null;
        user = null;
        username = null;
        password = null;

    }

    public static FirebaseAuth getmFirebaseAuth() {
        return mFirebaseAuth;
    }

    public static FirebaseAuth.AuthStateListener getAuthStateListener() {
        return authStateListener;
    }

    public static AuthCredential getCredential() {
        return credential;
    }

    public static FirebaseUser getUser() {
        return user;
    }

    public static String getUsername() {
        return username;
    }

    public static String getPassword() {
        return password;
    }
}
