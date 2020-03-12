package com.interview.androidlib.interfaces;
import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationBuilderWithBuilderAccessor;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.app.AppCompatCallback;
import androidx.appcompat.app.ActionBar;
import androidx.loader.content.AsyncTaskLoader;

import java.util.ArrayList;
import java.util.List;

abstract class Listener{
    private static int ID = 0;
    private int id;
    public Listener(){
        id = ++ID;
    }

    protected void countdownNextActivity(final AppCompatActivity currentActivity, final AppCompatActivity nextActivity, final int milliseconds){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(milliseconds);
                    Intent intent = new Intent(currentActivity, nextActivity.getClass());
                    currentActivity.startActivity(intent);
                    currentActivity.finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    protected void startNextActivityAfterShutdown(final AppCompatActivity currentActivity, final AppCompatActivity nextActivity){
    }

    protected void addOnDestroyBeginActivity(final AppCompatActivity nextActivity){

    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof Listener)
            return this.id == ((Listener) obj).id;
        return false;
    }

    public boolean equals(@Nullable Listener listener) {
        return this.id == listener.id;
    }
}

public abstract class ActivityCompatibility extends Listener
{
    private List<Listener> listeners;
    private Thread thread;
    public ActivityCompatibility() {
        listeners = new ArrayList<>();
    }
}
