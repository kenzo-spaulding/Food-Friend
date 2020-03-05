package com.example.foodie_friend.frontend.dependencies;


import android.content.Intent;
import android.os.Handler;
import android.util.Pair;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import com.example.foodie_friend.MainActivity;
import com.example.foodie_friend.MapsActivity;
import com.example.foodie_friend.RecyclerViewActivity;
import com.example.foodie_friend.SwipingActivity;
import com.example.foodie_friend.ui.login.SignInActivity;

public class SleepTimer {

    public static void delay(int seconds, Object o){
        if (o instanceof TextView){
            delay(seconds, (TextView) o);
        }
        else if (o instanceof EditText){
            delay(seconds, (EditText) o);
        }
        else if (o instanceof AtomicInteger){
            delay(seconds, (AtomicInteger) o);
        }
        else if (o instanceof AtomicLong){
            delay(seconds, (AtomicLong) o);
        }
        else if (o instanceof AppCompatActivity) {
            Pair<Object, Intent> pair = (Pair)o;
            final Object activity = pair.first;
            final Intent intent = pair.second;
            Toast.makeText(((AppCompatActivity)activity), "Clicked!", Toast.LENGTH_SHORT).show();
        }
        else if (o instanceof Pair){
            Pair<Object, Intent> pair = (Pair)o;
            final Object activity = pair.first;
            final Intent intent = pair.second;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(activity, intent);
                }
            }, seconds * 1000);
        }
    }

    private static void startActivity(Object o, Intent intent) {
        if (o instanceof MainActivity)
            ((MainActivity) o).startActivity(intent);
        if (o instanceof SignInActivity)
            ((SignInActivity) o).startActivity(intent);
        else if (o instanceof RecyclerViewActivity)
            ((RecyclerViewActivity) o).startActivity(intent);
        else if (o instanceof MapsActivity)
            ((MapsActivity) o).startActivity(intent);
        else if (o instanceof SwipingActivity)
            ((SwipingActivity) o).startActivity(intent);
        else
            System.err.println("Class not found when switching from Activity");
    }

    public static void delay(final int seconds, final TextView text){
        int timer = 0;
        for (int i = seconds; i >= 0; i--){
            timer += 1000;
            final int counter = i;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    text.setText(Integer.toString(counter));
                }
            }, timer);
        }
    }

    public static void delay(final int seconds, final EditText text){
        int timer = 0;
        for (int i = seconds; i >= 0; i--){
            timer += 1000;
            final int counter = i;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    text.setText(Integer.toString(counter));
                }
            }, timer);
        }
    }

    public static void delay(final int seconds, final AtomicInteger number){
        int timer = 0;
        for (int i = seconds; i >= 0; i--){
            timer += 1000;
            final int counter = i;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    number.set(counter);
                }
            }, timer);
        }
    }

    public static void delay(final int seconds, final SignInActivity login, final RecyclerViewActivity list){
        int timer = 0;
        for (int i = seconds; i >= 0; i--){
            timer += 1000;
            final int counter = i;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(login, RecyclerViewActivity.class);
                    login.startActivity(intent);
                }
            }, timer);
        }
    }

    public static void delay(final int seconds, final AtomicLong atomicLong){
        int timer = 0;
        for (int i = seconds; i >= 0; i--){
            timer += 1000;
            final int counter = i;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    atomicLong.set(counter);
                }
            }, timer);
        }
    }
}
