package com.interview.signin.ui.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.interview.signin.R;

public class CreateAccount extends AppCompatActivity {

    FirebaseAuth mFirebaseAuth;
    TextView indicator;
    EditText email;
    EditText password;
    Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        indicator = (TextView) findViewById(R.id.textView_indicator2);
        email = (EditText) findViewById(R.id.editText_Email);
        password = (EditText) findViewById(R.id.editText_Password);
        button = (Button) findViewById(R.id.button_CreateAccount);


        mFirebaseAuth = FirebaseAuth.getInstance();
    }


    public void onClick_CreateAccount(View view){
        if (password.getText().length() < 6){
            indicator.setText("Password must be > 5 characters");
            return;
        }
        final Intent intent = new Intent(this, LoginActivity.class);
        mFirebaseAuth.createUserWithEmailAndPassword(email.getText().toString(),
                password.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
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
                    indicator.setText("Failed creating your account.");
                }
            }
        });
    }
}
