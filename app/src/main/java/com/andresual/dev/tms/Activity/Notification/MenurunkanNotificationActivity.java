package com.andresual.dev.tms.Activity.Notification;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.andresual.dev.tms.Activity.Controller.NotificationController;
import com.andresual.dev.tms.Activity.DashboardActivity;
import com.andresual.dev.tms.Activity.Fragment.BerandaFragment;
import com.andresual.dev.tms.Activity.Model.LocationModel;
import com.andresual.dev.tms.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import static android.support.v4.content.PermissionChecker.PERMISSION_GRANTED;

public class MenurunkanNotificationActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    private GoogleApiClient googleApiClient;
    private static final int LOCATION_REQUEST_CODE = 101;
    Double lat, lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menurunkan_notification);

        Button btnMenurunkan = findViewById(R.id.btn_menurunkan);
        btnMenurunkan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationModel locationModel = new LocationModel();
                locationModel.setLatitude(lat);
                locationModel.setLongitude(lng);
                NotificationController.getmInstance().setLocationActive(locationModel);
                NotificationController.getmInstance().menurunkanJob(MenurunkanNotificationActivity.this);
                Intent intent = new Intent(MenurunkanNotificationActivity.this, DashboardActivity.class);
                startActivity(intent);
            }
        });

        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        googleApiClient.disconnect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PERMISSION_GRANTED) {
                    checkLocationAndAddToMap();
                } else Toast.makeText(this, "Locatiion Permission Denied", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void checkLocationAndAddToMap() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            return;
        }

        Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).title("You are here");
        lat = location.getLatitude();
        lng = location.getLongitude();
        mMap.addMarker(markerOptions);
        Log.i("here", markerOptions.toString());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        checkLocationAndAddToMap();
    }
}
