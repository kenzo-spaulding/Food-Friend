package com.interview.androidlib;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.widget.Toast;

import java.io.IOException;
import java.util.Locale;

public class GPS {
    public static final int COARSE_LOCATION_PERMISSION_REQUEST = 1332;
    public static final int FINE_LOCATION_PERMISSION_REQUEST = 1333;
    public static final int CONTACTS_PERMISSION_REQUEST = 1334;
    public static final int CAMERA_PERMISSION_REQUEST = 1335;
    public static final int INTERNAL_STORAGE_PERMISSION_REQUEST = 1336;
    public static final int EXTERNAL_STORAGE_PERMISSION_REQUEST = 1337;
    public static final int INTERNET_PERMISSION_REQUEST = 1338;
    public static final int WIFI_PERMISSION_REQUEST = 1339;

    private LocationManager locationManager;
    protected LocationListener locationListener;
    private Location location;
    private Address address;
    private AppCompatActivity app;
    private FragmentActivity frag;

    public GPS(AppCompatActivity app){
        if (app instanceof LocationListener) {
            this.app = app;
            locationListener = (LocationListener) app;
            locationManager = (LocationManager) app.getSystemService(Context.LOCATION_SERVICE);
            location = null;
            address = null;
            frag = null;
            startGPSTracking();
        }
    }

    public GPS(FragmentActivity frag){
        if (frag instanceof LocationListener) {
            this.frag = frag;
            locationListener = (LocationListener) frag;
            locationManager = (LocationManager) frag.getSystemService(Context.LOCATION_SERVICE);
            location = null;
            address = null;
            app = null;
            startGPSTracking();
        }
    }

    public Location getLastKnownLocation() {
        return location;
    }
    public Address getLastKnownAddress() {
        return address;
    }

    public void onPause() {
        if (app != null && (ContextCompat.checkSelfPermission(app,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED)) {
            startGPSTracking();
        }
        else if (frag != null && (ContextCompat.checkSelfPermission(frag,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED)) {
            startGPSTracking();
        }
    }

    public void onResume() {
        if (app != null && (ContextCompat.checkSelfPermission(app,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED)) {
            stopGPSTracking();
        }
        if (frag != null && (ContextCompat.checkSelfPermission(frag,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED)){
            stopGPSTracking();
        }

    }

    public void startGPSTracking(){
        // Check if the GPS permission is available
        if (app != null) {
            if (app.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            else {
                if (app.shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) || app.shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION))
                    Toast.makeText(app, "GPS permission is needed to view the map.", Toast.LENGTH_SHORT).show();

                //Request the user for permission to get access to the GPS
                app.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_PERMISSION_REQUEST);
            }
        }
        if (frag != null) {
            if (frag.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            else {
                if (frag.shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) || frag.shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION))
                    Toast.makeText(frag, "GPS permission is needed to view the map.", Toast.LENGTH_SHORT).show();

                //Request the user for permission to get access to the GPS
                frag.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_PERMISSION_REQUEST);
            }
        }
    }

    public void stopGPSTracking(){
        locationManager.removeUpdates(locationListener);
    }

    public void onLocationChanged(Location location) {
        this.location = location;
        Geocoder geo = null;
        if (app != null)
            geo = new Geocoder(app.getBaseContext(), Locale.getDefault());
        if (frag != null)
            geo = new Geocoder(frag.getBaseContext(), Locale.getDefault());
        try{
            this.address = geo.getFromLocation(location.getLatitude(), location.getLongitude(), 1).get(0);
        }catch (IOException e){
            e.printStackTrace();
        }
        if (locationManager != null && locationListener != null)
            locationManager.removeUpdates(locationListener);
    }

    public static float distance(Address address1, Address address2){
        Location a = new Location("");
        Location b = new Location("");

        a.setLongitude(address1.getLongitude());
        a.setLatitude(address1.getLatitude());

        b.setLongitude(address2.getLongitude());
        b.setLatitude(address2.getLatitude());

        return a.distanceTo(b);
    }
}
