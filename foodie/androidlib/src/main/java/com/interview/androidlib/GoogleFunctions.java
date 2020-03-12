package com.interview.androidlib;

/**
 * @author Mauricio Lomeli
 * @version Feburary, 2020
 *
 * This program holds various Google functions to help minimize the space
 * taken up on the main activities.
 */

import android.app.Activity;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.Arrays;


public class GoogleFunctions {
    public static final int RC_SIGN_IN = 1;

    public static FirebaseAuth.AuthStateListener startLogin(final Activity activity){
        return new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Toast.makeText(activity, "hi!", Toast.LENGTH_SHORT).show();
                } else {
                    activity.startActivityForResult(
                        AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(Arrays.asList(
                                new AuthUI.IdpConfig.GoogleBuilder().build(),
                                new AuthUI.IdpConfig.EmailBuilder().build()))
                            .build(),
                        RC_SIGN_IN);
                }
            }
        };
    }


}
