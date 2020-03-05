package com.example.foodie_friend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;

public class LoginActivity extends AppCompatActivity{

    private static final String hidden_username = "anteater";
    private static final String hidden_password = "zotzot";

    private EditText username;
    private EditText password;

    private TextView indicator;
    private Button login;

    CheckBox showPassword;
    CheckBox keepMeSignedIn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText) findViewById(R.id.EditText_Username);
        password = (EditText) findViewById(R.id.EditText_Password);
        indicator = (TextView) findViewById(R.id.textView_Indicator);
        login = (Button) findViewById(R.id.Button_Sign_In);
        showPassword = (CheckBox) findViewById(R.id.checkBox_Show_Password);
        keepMeSignedIn = (CheckBox) findViewById(R.id.checkBox_Keep_Signed_In);


        username.setText(hidden_username);
        password.setText(hidden_password);

        //Intent intent = new Intent(this, RecyclerViewActivity.class);
        //Pair<LoginActivity, Intent> pair = new Pair<>(this, intent);
        //SleepTimer.delay(5, pair);
    }


    private void authenticate(String username, String password) {
        if (this.username.getText().toString().equals(hidden_username) &&
                this.password.getText().toString().equals(hidden_password)) {
            indicator.setText("Successful!");
            goToAnotherActivity();
        }
        else
        {
            indicator.setText("Invalid username or password. Please try again.");
        }
    }

    public void goToAnotherActivity(){
        Intent intent = new Intent(this, SwipingActivity.class);
        startActivity(intent);
    }

    public void onClick_SignIn(View view){
        authenticate(this.username.getText().toString(), this.password.getText().toString());
    }

    public void onClick_CreateNewAccount(View view){

    }


    public void onClick_ShowPassword(View view){

    }

    public void onClick_SetKeepMeSignedIn(View view){

    }

}
