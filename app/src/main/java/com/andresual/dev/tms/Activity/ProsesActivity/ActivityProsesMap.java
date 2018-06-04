package com.andresual.dev.tms.Activity.ProsesActivity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.TextView;

import com.andresual.dev.tms.Activity.BaseActivity;
import com.andresual.dev.tms.Activity.Maps.DirectionFinder;
import com.andresual.dev.tms.Activity.Maps.DirectionFinderListener;
import com.andresual.dev.tms.Activity.Maps.LocationBroadcaster;
import com.andresual.dev.tms.Activity.Model.RealJob;
import com.andresual.dev.tms.Activity.Model.RouteModel;
import com.andresual.dev.tms.Activity.Model.SimpleJob;
import com.andresual.dev.tms.Activity.Util.Netter;
import com.andresual.dev.tms.Activity.Util.StringHashMap;
import com.andresual.dev.tms.R;
import com.android.volley.Request;
import com.android.volley.Response;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityProsesMap extends BaseActivity implements OnMapReadyCallback {
    public static final String INTENT_DATA = "data.job.hehe";

    @BindView(R.id.tvDistance)
    TextView tvDistance;
    @BindView(R.id.tvDuration)
    TextView tvDuration;
    @BindView(R.id.tv1)
    TextView tvStatus;
    @BindView(R.id.checkTrack)
    CheckBox checkTrack;
    SupportMapFragment mapFragment;
    GoogleMap gmap;
    Location location;

    SimpleJob simpleJob;
    RealJob realJob;

    BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(LocationBroadcaster.LOCATION_BROADCAST_ACTION)) {
                location = intent.getParcelableExtra(LocationBroadcaster.LOCATION_DATA);
                Log.i("PROSESMAP", location.toString());
            }
        }
    };
    IntentFilter filter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_order);
        ButterKnife.bind(this);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        if (getSupportActionBar() != null) getSupportActionBar().hide();
        filter = new IntentFilter(LocationBroadcaster.LOCATION_BROADCAST_ACTION);

        simpleJob = getIntent().getParcelableExtra(INTENT_DATA);

    }

    public void fetchJob() {
        new Netter(this).webService(Request.Method.POST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject obj = null;
                try {
                    obj = new JSONObject(response);
                    realJob = new Gson().fromJson(obj.getString("job-" + simpleJob.getJobId()), RealJob.class);
                    setUpAll();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, Netter.getDefaultErrorListener(this, new Runnable() {
            @Override
            public void run() {

            }
        }), Netter.Webservice.DETAILPICKUP, new StringHashMap().putMore("id", Integer.toString(simpleJob.getJobId())));
    }

    @SuppressLint("MissingPermission")
    public void setUpAll() {
        tvDistance.setText(realJob.getJobDeliverDistancetext());
        tvStatus.setText(realJob.getStringDeliverStatus());
        tvDuration.setText(realJob.getJobDeliverEstimatetimetext());

        // get direction
        if (gmap != null) {
            LocationServices.getFusedLocationProviderClient(this).getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    ActivityProsesMap.this.location = location;
                    Log.i("LocationService", location.toString());
                    try {
                        new DirectionFinder(new DirectionFinderListener() {
                            @Override
                            public void onDirectionFinderStart() {

                            }

                            @Override
                            public void onDirectionFinderSuccess(List<RouteModel> route) {
                                PolylineOptions polylineOptions = new PolylineOptions().
                                        geodesic(true).
                                        color(Color.BLUE).
                                        width(10);
                                for (int i = 0; i < route.size(); i++) {
                                    for (int j = 0; j < route.get(i).points.size(); j++) {
                                        polylineOptions.add(route.get(i).points.get(j));
                                    }
                                }
                                gmap.addPolyline(polylineOptions);
                            }
                        }, location.getLatitude(), location.getLongitude(), Double.parseDouble(realJob.getJobPickupLatitude()), Double.parseDouble(realJob.getJobPickupLongitude())).execute();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(br, filter);
        startService(new Intent(this, LocationBroadcaster.class));
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(br);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        gmap = googleMap;
        gmap.setMyLocationEnabled(true);
        UiSettings ui = gmap.getUiSettings();
        ui.setCompassEnabled(true);
        ui.setZoomControlsEnabled(true);
        ui.setZoomGesturesEnabled(true);
        fetchJob();
    }

    public static void start(Context context, SimpleJob simpleJob) {
        Intent intent = new Intent(context, ActivityProsesMap.class);
        intent.putExtra(INTENT_DATA, simpleJob);
        context.startActivity(intent);
    }
}
