package com.example.foodie_friend.ui.login;
/**
 * ONLY CHANGE function updateUiWithUser
 */

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodie_friend.R;
import com.example.foodie_friend.RecyclerViewActivity;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableReference;
import com.google.firebase.functions.HttpsCallableResult;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class SignInActivity extends AppCompatActivity {
    public static final int RC_SIGN_IN = 1;

    private LoginViewModel loginViewModel;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private Button createAccount;
    private Button googleButton;
    private ProgressBar loadingProgressBar;
    private TextView indicator;
    private CheckBox showPassword;
    private CheckBox keepMeSignedIn;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    public HttpsCallableReference callable;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mFirebaseAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

            }
        };

        usernameEditText = (EditText) findViewById(R.id.username);
        passwordEditText = (EditText) findViewById(R.id.password);

        //TODO: remove the next line of code when ready to deploy
        usernameEditText.setText("joe@uci.edu");
        passwordEditText.setText("zotzot");

        loginButton = (Button) findViewById(R.id.login);
        createAccount = (Button) findViewById(R.id.button_Create_Account);
        googleButton = (Button) findViewById(R.id.button_Google);
        loadingProgressBar = (ProgressBar) findViewById(R.id.loading);
        indicator = (TextView) findViewById(R.id.textView_Indicator);
        showPassword = (CheckBox) findViewById(R.id.checkBox_Show_Password);
        keepMeSignedIn = (CheckBox) findViewById(R.id.checkBox_Keep_Signed_In);

        loginViewModel = ViewModelProviders.of(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString());
                }
                return false;
            }
        });

        loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                passwordEditText.getText().toString());
    }

    private void updateUiWithUser(Task<AuthResult> authResultTask) {
        authResultTask.addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    FirebaseUser user = mFirebaseAuth.getCurrentUser();
                    updateUI(user);
                } else {
                    // If sign in fails, display a message to the user.
                    updateUI(null);
                }
            }
        });
    }

    private void showLoginFailed(final TextView indicator) {
        indicator.setText("username or password is incorrect");
    }

    public void onClick_SignIn(View view){
        /**
         * This is a runnable multi-threaded overriden function.
         * If you run something outside this, its not guaranteed
         * you're signed in until its completed. If you must
         * be signed in FIRST before continuing on, place the
         * next line of code inside the "onComplete" method
         */
        //final Intent intent = new Intent(this, RecyclerViewActivity.class);
        loadingProgressBar.setVisibility(View.VISIBLE);
        @ColorInt final int color = Color.rgb(124, 124, 135);

        disableAllInputs();
        mFirebaseAuth.signInWithEmailAndPassword(usernameEditText.getText().toString(),
                passwordEditText.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            indicator.setText("Successfully logged in.");
                            enableAllInputs(color);
                            loadingProgressBar.setVisibility(View.INVISIBLE);
                            //TODO: success sign in. Must go to the next activity here or call updateUI
                            updateUI(mFirebaseAuth.getCurrentUser());
                            //startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            showLoginFailed(indicator);
                            enableAllInputs(color);
                            loadingProgressBar.setVisibility(View.INVISIBLE);
                            //TODO: failed signing in. What do you do after?
                        }
                    }
                });
    }


    private void createAccount(String username, String password){
        mFirebaseAuth.createUserWithEmailAndPassword(usernameEditText.getText().toString(),
                passwordEditText.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    FirebaseUser user = mFirebaseAuth.getCurrentUser();
                    indicator.setText("Successfully created the account.");
                } else {
                    // If sign in fails, display a message to the user.
                    showLoginFailed(indicator);
                }
            }
        });
    }

    public void onClick_CreateNewAccount(View view){
        //TODO: Create an account form and Link a create account button
        createAccount(usernameEditText.getText().toString(), passwordEditText.getText().toString());
    }

    public void onClick_ShowPassword(View view){
        if (showPassword.isChecked())
            passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT);
        else {
            passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            passwordEditText.setTypeface(Typeface.DEFAULT);
        }
    }

    public void onClick_GoogleButton(View view){
        // This works
        if (mFirebaseAuth.getCurrentUser() != null) {
            updateUI(mFirebaseAuth.getCurrentUser());
        }
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(Arrays.asList(
                                new AuthUI.IdpConfig.GoogleBuilder().build()))
                        .build(),
                RC_SIGN_IN);
    }

    public void onClick_KeepMeSignedIn(View view){
        //TODO: set sign in options to stay signed in
    }

    public void updateUI(FirebaseUser user){
        if (user != null) {
            String welcome = getString(R.string.welcome) + user.getDisplayName();

            /////////////////////////////////////////
            // TODO : initiate the next intent here
            /////////////////////////////////////////

            Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
            this.callable = FirebaseFunctions.getInstance().getHttpsCallable("recommendations");
            onCallable();
        }
    }

    private void disableAllInputs(){
        usernameEditText.setEnabled(false);
        passwordEditText.setEnabled(false);
        loginButton.setEnabled(false);
        createAccount.setEnabled(false);
        googleButton.setEnabled(false);
        indicator.setEnabled(false);
        showPassword.setEnabled(false);
        keepMeSignedIn.setEnabled(false);
    }
    private void enableAllInputs(@ColorInt int c){
        usernameEditText.setEnabled(true);
        passwordEditText.setEnabled(true);

        loginButton.setBackgroundColor(c);
        loginButton.setEnabled(true);
        createAccount.setBackgroundColor(c);
        createAccount.setEnabled(true);

        googleButton.setEnabled(true);
        indicator.setEnabled(true);
        showPassword.setEnabled(true);
        keepMeSignedIn.setEnabled(true);
    }

    public void onCallable(){
        /**
         * This is a runnable multi-threaded overriden function.
         * If you run something outside this, its not guaranteed
         * you're signed in until its completed. If you must
         * be signed in FIRST before continuing on, place the
         * next line of code inside the "onComplete" method
         */

        Map<Object, Integer> day = new HashMap<>();
        day.put("timeOfDay", 0);
        Task<HttpsCallableResult> firebaseCall = this.callable.call(day);
        /*
        Task<HttpsCallableResult> firebaseCall = this.callable.call(day).continueWith(new Continuation<HttpsCallableResult, HttpsCallableResult>() {
            @Override
            public HttpsCallableResult then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                return task.getResult();
            }
        });

        indicator.setText(firebaseCall.getResult().getData().toString());
        */
        firebaseCall.addOnCompleteListener(this, new OnCompleteListener<HttpsCallableResult>() {
            @Override
            public void onComplete(@NonNull Task<HttpsCallableResult> task) {
                if (task.isSuccessful()){
                    String data = task.getResult().getData().toString();
                    indicator.setText(data);
                }
                else{
                    indicator.setText("Failed");
                }
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mFirebaseAuth != null && mFirebaseAuth.getCurrentUser() != null)
            mFirebaseAuth.signOut();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mFirebaseAuth.removeAuthStateListener(authStateListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(authStateListener);
    }
}
