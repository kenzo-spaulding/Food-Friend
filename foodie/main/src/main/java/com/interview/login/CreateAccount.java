package com.interview.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.interview.R;

public class CreateAccount extends AppCompatActivity {

    FirebaseAuth mFirebaseAuth;
    TextView indicator;
    EditText editText_Username;
    EditText editText_Password;
    Button button_Login;
    private LoginViewModel loginViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        indicator = (TextView) findViewById(R.id.textView_indicator2);
        editText_Username = (EditText) findViewById(R.id.editText_Email);
        editText_Password = (EditText) findViewById(R.id.editText_Password);
        button_Login = (Button) findViewById(R.id.button_CreateAccount);


        mFirebaseAuth = FirebaseAuth.getInstance();


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


    public void onClick_CreateAccount(View view){
        if (editText_Username.getText().length() < 6){
            indicator.setText("Password must be > 5 characters");
            return;
        }
        final Intent intent = new Intent(this, LoginActivity.class);
        mFirebaseAuth.createUserWithEmailAndPassword(editText_Username.getText().toString(),
                editText_Password.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    FirebaseUser user = mFirebaseAuth.getCurrentUser();
                    indicator.setText("Worked and created");
                    Bundle bundle = new Bundle();
                    bundle.putString("message", "Please authenticate by re-entering your email & password");
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                } else {
                    // If sign in fails, display a message to the user.
                    indicator.setText("Failed creating your account.\nEmail is taken or password isn't the correct format.");
                }
            }
        });
    }
}
