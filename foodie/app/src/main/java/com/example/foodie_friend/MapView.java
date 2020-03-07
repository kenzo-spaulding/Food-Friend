package com.example.foodie_friend;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.foodie_friend.ui.mapview.MapViewFragment;

public class MapView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_view_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, MapViewFragment.newInstance())
                    .commitNow();
        }
    }
}
