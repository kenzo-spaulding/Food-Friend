package com.example.foodie_friend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;

import com.example.foodie_friend.frontend.dependencies.SleepTimer;

public class LoginActivity extends AppCompatActivity{

    private static final String hidden_username = "anteater";
    private static final String hidden_password = "zotzot";

    private EditText username;
    private EditText password;

    private TextView indicator;
    private Button login;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText) findViewById(R.id.EditText_Username);
        password = (EditText) findViewById(R.id.EditText_Password);
        indicator = (TextView) findViewById(R.id.TextView_Indicator);
        login = (Button) findViewById(R.id.Button_Sign_In);

        username.setText(hidden_username);
        password.setText(hidden_password);


        Intent intent = new Intent(this, RecyclerViewActivity.class);
        Pair<LoginActivity, Intent> pair = new Pair<>(this, intent);
        SleepTimer.delay(5, pair);
    }


    private void authenticate(String username, String password) {
        if (this.username.getText().toString().equals(hidden_username) &&
                this.password.getText().toString().equals(hidden_password)) {
            indicator.setText("Successful!");
            goToAnotherActivity();
        }
        else
        {
            indicator.setText("Invalid username or password.\nPleast try again.");
        }
    }

    public void goToAnotherActivity(){
        Intent intent = new Intent(this, SwipingActivity.class);
        startActivity(intent);
    }

    public void onClick(View view){
        authenticate(this.username.getText().toString(), this.password.getText().toString());
    }


}
