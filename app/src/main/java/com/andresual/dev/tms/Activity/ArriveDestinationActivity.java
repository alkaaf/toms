package com.andresual.dev.tms.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.andresual.dev.tms.Activity.Adapter.SebelumnyaListAdapter;
import com.andresual.dev.tms.Activity.Manager.SessionKendaraan;
import com.andresual.dev.tms.Activity.Manager.SessionManager;
import com.andresual.dev.tms.Activity.Model.JobOrder2Model;
import com.andresual.dev.tms.Activity.Model.PassingLocationModel;
import com.andresual.dev.tms.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArriveDestinationActivity extends FragmentActivity implements OnMapReadyCallback {

    Integer jobId;
    String pickLat, pickLong;
    SessionManager sessionManager;
    SessionKendaraan sessionKendaraan;
    String idDriver, email, idKendaraan;
    String delivLat, delivLng;

    private GoogleMap mMap;
    private GoogleApiClient googleApiClient;
    private static final int LOCATION_REQUEST_CODE = 101;
    private static final int LOCATION_REQUEST = 500;
    ArrayList<LatLng> listPoints;
    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();
    private ProgressDialog progressDialog;
    PassingLocationModel passingLocationModel;
    public static String lat;
    public static String lng;
    private Button btnFindPath;
    JobOrder2Model modelData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arrive_destination);
        modelData = getIntent().getParcelableExtra(SebelumnyaListAdapter.INTENT_DATA);
        Button btnStartDischarge = findViewById(R.id.btn_start_discharge);
        Button btnTolak = findViewById(R.id.btn_tolak);

        sessionManager = new SessionManager(getApplicationContext());
        sessionKendaraan = new SessionKendaraan(getApplicationContext());

        sessionKendaraan = new SessionKendaraan(this);
        HashMap<String, String> data = sessionKendaraan.getKendaraanDetails();
        idKendaraan = data.get(SessionKendaraan.ID_KENDARAAN);

        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetails();
        idDriver = user.get(SessionManager.ID_DRIVER);
        email = user.get(SessionManager.EMAIL_DRIVER);

//        jobId = this.getIntent().getIntExtra("jobId", 0);
//        pickLat = this.getIntent().getStringExtra("latitude");
//        pickLong = this.getIntent().getStringExtra("longitude");
//        delivLat = this.getIntent().getStringExtra("delivLat");
//        delivLng = this.getIntent().getStringExtra("delivLng");
        jobId = modelData.getJobId();
        pickLat = modelData.getJobPickupLatitude();
        pickLong =modelData.getJobPickupLongitude();
        delivLat = modelData.getJobDeliverLatitude();
        delivLng = modelData.getJobDeliverLongitude();
        btnStartDischarge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arriveDestination();
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
//        sendRequest();

        mMap.getUiSettings().setZoomControlsEnabled(true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST);
            return;
        }
        mMap.setMyLocationEnabled(true);

        if (mMap != null) {
            mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
                @Override
                public void onMyLocationChange(Location location) {
                    lat = String.valueOf(location.getLatitude());
                    lng = String.valueOf(location.getLongitude());
                    Log.i("onMyLocationChange: ", lat);
                }
            });
//            sendRequest();
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mMap.setMyLocationEnabled(true);
                }
                break;
        }
    }

    public void arriveDestination() {
        final Map<String, String> params = new HashMap<>();
        params.put("f", "startjobarrival");
        params.put("email", email);
        params.put("idjob", jobId.toString());
        params.put("latitude", lat);
        params.put("longitude", lng);
        Log.i("jemputjob", params.toString());

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(Request.Method.POST, "http://manajemenkendaraan.com/tms/webservice.asp",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("response", response);
                        try {
                            JSONObject obj = new JSONObject(response);
                            Log.i("status", obj.getString("status"));
                            Log.i("messages", obj.getString("message"));

                            Intent intent = new Intent(ArriveDestinationActivity.this, StartDischargeActivity.class);
                            intent.putExtra(SebelumnyaListAdapter.INTENT_DATA, modelData);
//                            intent.putExtra("jobId", jobId);
//                            intent.putExtra("latitude", pickLat);
//                            intent.putExtra("longitude", pickLong);
//                            intent.putExtra("delivLat", delivLat);
//                            intent.putExtra("delivLng", delivLng);
                            startActivity(intent);
                            finish();
                        } catch (Throwable t) {
                            Log.i("tms", "Could not parse malformed JSON: \"" + response + "\"");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String,String> getParams(){
                return params;
            }
        };
        queue.add(sr);
    }
}
