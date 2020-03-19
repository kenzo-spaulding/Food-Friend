package com.interview;
/**
 * @author Mauricio Lomeli
 * @version Feburary, 2020
 *
 * This activity begins the GoogleMaps activity. It will connect to
 * Google's maps and provides an interactive map for the user to view
 * the relative location of the recommendation.
 */

import android.content.Intent;
import android.location.Address;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.interview.androidlib.DownloadImage;
import com.interview.androidlib.GPS;
import com.interview.lib.DateTime;

import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    //////////  LAYOUT VARIABLES  //////////////////////////////////////////

    private TextView textView_Title;
    private TextView textView_Radius;
    private TextView textView_Calories;
    private TextView textView_Recommend;
    private TextView textView_Options;

    BottomNavigationView bottomNavigationView;


    //////////  Backend Variables   ////////////////////////////////////////
    private GoogleMap mMap;
    private GPS gps;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        bundle = getIntent().getExtras();

        //////  Layout Variables Assigned    //////////////////////////////
        textView_Title = (TextView) findViewById(R.id.textView_Title);
        textView_Radius = (TextView) findViewById(R.id.textView_Radius);
        textView_Calories = (TextView) findViewById(R.id.textView_Calories);
        textView_Recommend = (TextView) findViewById(R.id.textView_Recommend);
        textView_Options = (TextView) findViewById(R.id.textView_Options);

        //////////  Backend Variables Assigned  ////////////////////////////////////////
        this.gps = new GPS(this);


        bottomNavigationView = findViewById(R.id.nav_view);
        bottomNavigationView.setSelectedItemId(R.id.navigation_dashboard);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.navigation_home:
                        onClick_logout();
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.navigation_dashboard:
                        startActivity(new Intent(getApplicationContext(), RecyclerViewActivity.class));
                        overridePendingTransition(0, 0);
                    case R.id.navigation_notifications:
                        startActivity(new Intent(getApplicationContext(), SwipeActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    default:
                        return false;
                }
            }
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragmentmap);
        mapFragment.getMapAsync(this);
    }

    private void onClick_logout(){

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        LatLng gpsCenterCoord = new LatLng(40, -100);
        LatLng gpsBoundaryCoord = new LatLng(10, -154);
        LatLngBounds unitedStates = new LatLngBounds(gpsBoundaryCoord, gpsCenterCoord);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(unitedStates.getCenter(), 14));
    }

    public LatLng latlng(Address address){
        return new LatLng(address.getLatitude(), address.getLongitude());
    }

    public LatLng latlng(Location location){
        return new LatLng(location.getLatitude(), location.getLongitude());
    }

    public void moveCamera(Address address){
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latlng(address)));
    }

    public void moveCamera(Location location){
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latlng(location)));
    }

    public void addMarker(Address address, String title){
        mMap.addMarker(new MarkerOptions().position(latlng(address)).title(title));
    }

    public void addMarker(Location location, String title){
        mMap.addMarker(new MarkerOptions().position(latlng(location)).title(title));
    }

    public void addMarker(Address address, String title, String snippet){
        mMap.addMarker(new MarkerOptions().position(latlng(address)).title(title).snippet(snippet));
    }

    public void addMarker(Location location, String title, String snippet){
        mMap.addMarker(new MarkerOptions().position(latlng(location)).title(title).snippet(snippet));
    }

    public void clear(){
        mMap.clear();
    }

    @Override
    public void onLocationChanged(Location location) {
        gps.onLocationChanged(location);

        Address address = gps.getLastKnownAddress();
        String city = (address == null) ? "Current Location" : "Current Location in " + address.getLocality();
        if (bundle != null){
            try {
                Intent intent = getIntent();
                String longitude = intent.getStringExtra("longitude");
                String latitude = intent.getStringExtra("latitude");
                String name = intent.getStringExtra("name");
                String snippet = intent.getStringExtra("headQuery");
                String image_url = intent.getStringExtra("image_url");
                String distance = intent.getStringExtra("distance");
                String phone = intent.getStringExtra("phone");
                String ratings = intent.getStringExtra("rating");
                String address1 = intent.getStringExtra("address1");
                String address2 = intent.getStringExtra("address2");
                String address_city = intent.getStringExtra("city");
                String zip_code = intent.getStringExtra("zip_code");
                Address addr = new Address(Locale.getDefault());
                addr.setLongitude(Double.parseDouble(longitude));
                addr.setLatitude(Double.parseDouble(latitude));
                addMarker(addr, name, snippet);
                moveCamera(addr);
                textView_Title.setText(name);
                textView_Radius.setText(String.format("%.2f", Double.parseDouble(distance)) + " miles");
                textView_Recommend.setText(snippet + ((phone == null || phone.equals("")) ? "" : "," + phone));
                textView_Options.setText(DateTime.timeOfDayString());
                textView_Calories.setText(address1 + "\n" + address_city + ", " + zip_code);
                textView_Recommend.setText(snippet);

            } catch (Exception e) {e.printStackTrace();}
        }
    }

    public void onClick_Go(View view){
        Intent intent = new Intent(this, RecyclerViewActivity.class);
        startActivity(intent);
    }

    public void onClick_Train(View view){
        Intent intent = new Intent(this, SwipeActivity.class);
        startActivity(intent);
    }

    public void onClick_Refresh(View view){
        try{
            gps.stopGPSTracking();
            gps.startGPSTracking();
        } catch (Exception e){  }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        gps.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        gps.onResume();
    }
}
