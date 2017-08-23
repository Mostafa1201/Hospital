package com.example.ospidalia2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class location_page extends FragmentActivity implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener{

    private GoogleMap mMap;
    private GoogleApiClient client;
    private LocationRequest locRequest;
    private Location lastLocation;
    private Marker currentLocationMarker;
    public static final int REQ_LOC_CODE = 99;
    int PROXIMITY_RADIUS = 1000;
    double longitude;
    double latitude;


    private String getUrl(double latitude, double longitude, String nearbyPlace) {

        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("location=" + latitude + "," + longitude);
        googlePlacesUrl.append("&radius=" + PROXIMITY_RADIUS);
        googlePlacesUrl.append("&type=" + nearbyPlace);
        googlePlacesUrl.append("&sensor=true");
        googlePlacesUrl.append("&key=" + "AIzaSyAio6DyBV8neUYQFCipOn04s-J1jQPuAlA");
        // "content_copy\n");
        // Log.d("getUrl", googlePlacesUrl.toString());
        return (googlePlacesUrl.toString());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_page);
        TextView Home = (TextView) findViewById(R.id.home_button1);
        TextView doctor = (TextView) findViewById(R.id.doctor_button1);
        TextView search = (TextView) findViewById(R.id.search_button1);


        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Homeintent = new Intent(location_page.this, home_page.class);
                startActivity(Homeintent);
            }
        });


        doctor.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent DoctorIntent = new Intent(location_page.this, doctor_page.class);
                startActivity(DoctorIntent);
            }
        });


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent SearchIntent = new Intent(location_page.this, search_page.class);
                startActivity(SearchIntent);
            }

        });
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            CheckLocationPermission();
        }
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment.                                                                                                                                                                                                                                                    This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);//
        //  if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        // {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            buildGoodleApiClient();
            mMap.setMyLocationEnabled(true);
        }
        // }
    }

    protected synchronized void buildGoodleApiClient()
    {
        client = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        client.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locRequest = new LocationRequest();
        locRequest.setInterval(1000);
        locRequest.setFastestInterval(1000);
        locRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(client, locRequest, this);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        //Log.d("onLocationChanged", "entered");//
        lastLocation = location;
        if(currentLocationMarker != null)
        {
            currentLocationMarker.remove();
        }
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        LatLng latlng = new LatLng(latitude , longitude);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latlng);
        markerOptions.title("Current Location");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        currentLocationMarker = mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
        Object dataTransfer[] = new Object[2];
        NearByPlacesData data = new NearByPlacesData();
        mMap.clear();
        String url = getUrl(latitude,longitude,"hospital");
        dataTransfer[0] = mMap;
        dataTransfer[1] = url;
        data.execute(dataTransfer);
        Toast.makeText(location_page.this , "Showing NearBy Hospitals ;)" , Toast.LENGTH_LONG).show();
        //Toast.makeText(MapsActivity.this,"Your Current Location", Toast.LENGTH_LONG).show();//
        // Log.d("onLocationChanged", String.format("latitude:%.3f longitude:%.3f",latitude,longitude));//
        if(client != null)
        {
            LocationServices.FusedLocationApi.removeLocationUpdates(client,this);
            //  Log.d("onLocationChanged", "Removing Location Updates");//
        }
        // Log.d("onLocationChanged", "Exit");//
    }

    public boolean CheckLocationPermission()
    {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION))
            {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION} , REQ_LOC_CODE);
            }
            else
            {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION} , REQ_LOC_CODE);
            }
            return false;
        }
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode)
        {
            case REQ_LOC_CODE:
                if(grantResults.length > 0 && grantResults[0] ==  PackageManager.PERMISSION_GRANTED) {
                    //permission is granted
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                    {
                        if(client == null)
                        {
                            buildGoodleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                }
                else
                {
                    Toast.makeText(this,"Permission Denied " , Toast.LENGTH_LONG).show();
                    //this is to display a msg-->permission is denied
                }
                return;
        }

    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}