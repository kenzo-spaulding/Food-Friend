package com.interview.login;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
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

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuth.AuthStateListener;
import com.interview.R;
import com.interview.RecyclerViewActivity;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {
    public static final int RC_SIGN_IN = 1;

    //////////  LAYOUT VARIABLES  //////////////////////////////////////////
    private EditText editText_Username;
    private EditText editText_Password;

    private CheckBox checkBox_ShowPassword;

    private Button button_Login;
    private Button button_CreateAccount;
    private Button button_GoogleSignIn;

    private ProgressBar progressBar_Loading;

    private TextView textView_Indicator;

    //////////  Backend Variables   ////////////////////////////////////////
    private LoginViewModel loginViewModel;
    private FirebaseAuth mFirebaseAuth;
    private AuthStateListener authStateListener;

    //////////  Functions   ////////////////////////////////////////////////
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //////  Assigned Layout Variables    //////////////////////////////
        assignLayoutVariables();

        //TODO: remove the next line of code when ready to deploy
        editText_Username.setText("mauricio@uci.edu");
        editText_Password.setText("zotzot");

        // If user created an account it displays the next instructions
        if (getIntent().getExtras() != null)
            textView_Indicator.setText(getIntent().getStringExtra("message"));

        setTextListenerRules();
    }

    // Assigns the layout variables
    private void assignLayoutVariables(){
        //////  Assigned Layout Variables    //////////////////////////////
        editText_Username = (EditText) findViewById(R.id.username);
        editText_Password = (EditText) findViewById(R.id.password);

        button_Login = (Button) findViewById(R.id.login);
        button_CreateAccount = (Button) findViewById(R.id.button_Create_Account);
        button_GoogleSignIn = (Button) findViewById(R.id.button_Google);

        progressBar_Loading = (ProgressBar) findViewById(R.id.loading);
        textView_Indicator = (TextView) findViewById(R.id.textView_Indicator);
        checkBox_ShowPassword = (CheckBox) findViewById(R.id.checkBox_Show_Password);


        //////  Assigned Backend Variables    //////////////////////////////
        FirebaseApp.initializeApp(this);
        mFirebaseAuth = FirebaseAuth.getInstance();
        authStateListener = new AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

            }
        };
    }

    // Sets the text listener for changes in text input during the login
    // Asserts the user if their login input is in the correct format
    private void setTextListenerRules(){
        loginViewModel = ViewModelProviders.of(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                button_Login.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    editText_Username.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    editText_Password.setError(getString(loginFormState.getPasswordError()));
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
                loginViewModel.loginDataChanged(editText_Username.getText().toString(),
                        editText_Password.getText().toString());
            }
        };

        // Add the listener to the text box so it displays an error message
        // if the input isn't in the right format
        editText_Username.addTextChangedListener(afterTextChangedListener);
        editText_Password.addTextChangedListener(afterTextChangedListener);


        editText_Password.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(editText_Username.getText().toString(),
                            editText_Password.getText().toString());
                }
                return false;
            }
        });

        loginViewModel.loginDataChanged(editText_Username.getText().toString(),
                editText_Password.getText().toString());
    }

    // Displays an error message if a login fails
    private void showLoginFailed(final TextView indicator) {
        indicator.setText("username or password is incorrect");
    }

    // Behavior when someone clicks the "Sign in" button
    public void onClick_SignIn(View view){
        /**
         * This is a runnable multi-threaded overriden function.
         * If you run something outside this, its not guaranteed
         * you're signed in until its completed. If you must
         * be signed in FIRST before continuing on, place the
         * next line of code inside the "onComplete" method
         */
        progressBar_Loading.setVisibility(View.VISIBLE);
        @ColorInt final int color = Color.rgb(124, 124, 135);
        final Intent intent = new Intent(this, RecyclerViewActivity.class);
        disableAllInputs();
        mFirebaseAuth.signInWithEmailAndPassword(editText_Username.getText().toString(),
                editText_Password.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            textView_Indicator.setText("Worked");
                            enableAllInputs(color);
                            progressBar_Loading.setVisibility(View.INVISIBLE);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            showLoginFailed(textView_Indicator);
                            enableAllInputs(color);
                            progressBar_Loading.setVisibility(View.INVISIBLE);
                        }
                    }
                });
    }

    // starts the create account activity
    public void onClick_CreateNewAccount(View view){
        Intent intent = new Intent(this, CreateAccount.class);
        startActivity(intent);
        finish();
    }

    // Enables the view of the password text edit to visible and disables it
    public void onClick_ShowPassword(View view){
        if (checkBox_ShowPassword.isChecked())
            editText_Password.setInputType(InputType.TYPE_CLASS_TEXT);
        else {
            editText_Password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            editText_Password.setTypeface(Typeface.DEFAULT);
        }
    }

    // Signs in with google's sigin in
    public void onClick_GoogleButton(View view){
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(Arrays.asList(
                                new AuthUI.IdpConfig.GoogleBuilder().build()))
                        .build(),
                RC_SIGN_IN);
    }

    // Disables inputs while loading
    private void disableAllInputs(){
        editText_Username.setEnabled(false);
        editText_Password.setEnabled(false);

        button_Login.setEnabled(false);
        button_CreateAccount.setEnabled(false);
        button_GoogleSignIn.setEnabled(false);

        textView_Indicator.setEnabled(false);
        checkBox_ShowPassword.setEnabled(false);
    }

    // Enables inputs after finished loading
    private void enableAllInputs(@ColorInt int c){
        editText_Username.setEnabled(true);
        editText_Password.setEnabled(true);

        button_Login.setBackgroundColor(c);
        button_Login.setEnabled(true);
        button_CreateAccount.setBackgroundColor(c);
        button_CreateAccount.setEnabled(true);

        button_GoogleSignIn.setEnabled(true);
        textView_Indicator.setEnabled(true);
        checkBox_ShowPassword.setEnabled(true);
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
