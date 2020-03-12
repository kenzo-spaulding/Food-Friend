package com.interview.signin.data;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;

/**
 * ONLY CHANGE logout() & login(username, password)
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    public Task<AuthResult> login(String username, String password) {
        // TODO: handle loggedInUser authentication by checking their username matches password
        return firebaseAuth.signInWithEmailAndPassword(username,password);
    }

    public void logout() {
        // TODO: revoke authentication
        // TODO: Remove all cached data here
        firebaseAuth.signOut();
    }
}

