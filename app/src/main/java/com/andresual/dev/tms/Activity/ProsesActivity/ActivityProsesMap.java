package com.andresual.dev.tms.Activity.ProsesActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.andresual.dev.tms.Activity.BaseActivity;
import com.andresual.dev.tms.Activity.DCP.ReadyToStuffPickupDCPActivity;
import com.andresual.dev.tms.Activity.Maps.DirectionFinder;
import com.andresual.dev.tms.Activity.Maps.DirectionFinderListener;
import com.andresual.dev.tms.Activity.Maps.LocationBroadcaster;
import com.andresual.dev.tms.Activity.Model.DriverModel;
import com.andresual.dev.tms.Activity.Model.KendaraanModel;
import com.andresual.dev.tms.Activity.Model.RealJob;
import com.andresual.dev.tms.Activity.Model.RouteModel;
import com.andresual.dev.tms.Activity.Model.SimpleJob;
import com.andresual.dev.tms.Activity.Util.Netter;
import com.andresual.dev.tms.Activity.Util.Pref;
import com.andresual.dev.tms.Activity.Util.StringHashMap;
import com.andresual.dev.tms.R;
import com.android.volley.Request;
import com.android.volley.Response;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
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
    @BindView(R.id.btn_terima)
    Button bTerima;
    SupportMapFragment mapFragment;
    GoogleMap gmap;
    Location location;
    LatLng latLng;

    SimpleJob simpleJob;
    RealJob realJob;

    Netter.Webservice whatFunc;
    StringHashMap map;
    KendaraanModel kendaraan;
    DriverModel driver;
    Pref pref;

    enum MapColor {

        BLUE(BitmapDescriptorFactory.HUE_BLUE, Color.BLUE),
        GREEN(BitmapDescriptorFactory.HUE_GREEN, Color.GREEN),
        RED(BitmapDescriptorFactory.HUE_RED, Color.RED),
        YELLOW(BitmapDescriptorFactory.HUE_YELLOW, Color.YELLOW);

        public float hsv;
        public int rgb;

        MapColor(float hsv, int rgb) {
            this.hsv = hsv;
            this.rgb = rgb;
        }
    }

    BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(LocationBroadcaster.LOCATION_BROADCAST_ACTION)) {
                setUpLocation((Location) intent.getParcelableExtra(LocationBroadcaster.LOCATION_DATA));

            }
        }
    };
    IntentFilter filter;
    List<Polyline> polylineList = new ArrayList<>();
    List<Marker> markerList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_order);
        pref = new Pref(this);
        driver = pref.getDriverModel();
        kendaraan = pref.getKendaraan();

        ButterKnife.bind(this);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        if (getSupportActionBar() != null) getSupportActionBar().hide();
        filter = new IntentFilter(LocationBroadcaster.LOCATION_BROADCAST_ACTION);

        simpleJob = getIntent().getParcelableExtra(INTENT_DATA);
        checkTrack.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    findMe();
                }
            }
        });

        bTerima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(realJob.getJobDeliverStatus() == 14){
                 new AlertDialog.Builder(ActivityProsesMap.this).setMessage("Hey ini akan diisi sama upload gambar :). Sek yo,..").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialog, int which) {
                         finish();
                     }
                 }).show();
                } else {
                    nextStep();
                }
            }
        });
    }

    public void nextStep() {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Menuju tahap selanjutnya...");
        map = new StringHashMap();
        map.putMore("idjob", Integer.toString(realJob.getId()))
                .putMore("email", driver.getEmail())
                .putMore("lat", location.getLatitude())
                .putMore("lng", location.getLongitude());
        if (realJob.getJobDeliverStatus() >= 7) {
            if(realJob.getJumlahterkirim() >= 1) {
                map.putMore("idjob_detail", realJob.getDetailkontainer().get(1).getIddetail());
            } else {
                map.putMore("idjob_detail", realJob.getDetailkontainer().get(0).getIddetail());
            }
        }

        pd.show();
        if (whatFunc != null) {
            new Netter(this).webService(Request.Method.POST, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pd.dismiss();
                    try {
                        JSONObject obj = new JSONObject(response);
                        toast(obj.getString("message"));
                        if (obj.getInt("status") == 200) {
                            whatFunc = null;
                            fetchJob();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, Netter.getDefaultErrorListener(this, new Runnable() {
                @Override
                public void run() {
                    pd.dismiss();

                }
            }), whatFunc, map);
        } else {
            Toast.makeText(this, "Sek onok sing error", Toast.LENGTH_SHORT).show();
        }
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
        Log.i("JOBA", "JobType "+realJob.getJobType());
        Log.i("JOBB", "JobStatus "+realJob.getJobDeliverStatus());
        tvDistance.setText(realJob.getJobDeliverDistancetext());
        tvStatus.setText(realJob.getStringDeliverStatus());
        tvDuration.setText(realJob.getJobDeliverEstimatetimetext());
        final boolean isJob89 = realJob.getJobType() == 8 || realJob.getJobType() == 9;
        final boolean isJobTujuanMoreThanOne = realJob.getJumlahtujuan() > 1;
        final boolean isSingleBox = Integer.parseInt(realJob.getJumlahbox()) == 1;
        final boolean isSudahAdaYangTerkirim = realJob.getJumlahterkirim() >= 1;
        Log.i("JOBC", "is89 "+isJob89);
        Log.i("JOBD", "isTujuanMoreThanone "+isJobTujuanMoreThanOne);
        Log.i("JOBE", "isSingleBox "+isSingleBox);
        Log.i("JOBF", "isSudahAdaYAngterkirim "+isSudahAdaYangTerkirim);
        // draw route from start to end
        if (gmap != null) {
            LocationServices.getFusedLocationProviderClient(this).getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    setUpLocation(location);
                    drawRoute(location.getLatitude(), location.getLongitude(), dd(realJob.getJobPickupLatitude()), dd(realJob.getJobPickupLongitude()), MapColor.BLUE, realJob.getJobPickupName());
                    if (isJob89) {
                        drawRoute(location.getLatitude(), location.getLongitude(), dd(realJob.getJobPickupLatitude()), dd(realJob.getJobPickupLongitude()), MapColor.BLUE, realJob.getJobPickupName());
                        drawRoute(dd(realJob.getJobPickupLatitude()), dd(realJob.getJobPickupLongitude()), dd(realJob.getJobDeliverLatitude()), dd(realJob.getJobDeliverLongitude()), MapColor.GREEN, realJob.getJobDeliverAddress());
                        drawRoute(dd(realJob.getJobDeliverLatitude()), dd(realJob.getJobDeliverLongitude()), dd(realJob.getJobBalikLatitude()), dd(realJob.getJobBalikLongitude()), MapColor.YELLOW, realJob.getJobBalikAddress());
                    } else {

                        if (isSingleBox) {
                            drawRoute(dd(realJob.getJobPickupLatitude()), dd(realJob.getJobPickupLongitude()), dd(realJob.getJobDeliverLatitude()), dd(realJob.getJobDeliverLongitude()), MapColor.GREEN, realJob.getJobDeliverAddress());
                        } else {
                            if (isJobTujuanMoreThanOne) {
                                drawRoute(dd(realJob.getJobPickupLatitude()), dd(realJob.getJobPickupLongitude()), realJob.getDetailkontainer().get(0).getDestinationLat(), realJob.getDetailkontainer().get(0).getDestinationLng(), MapColor.GREEN, realJob.getDetailkontainer().get(0).getDestinationName());
                                drawRoute(realJob.getDetailkontainer().get(0).getDestinationLat(), realJob.getDetailkontainer().get(0).getDestinationLng(), realJob.getDetailkontainer().get(1).getDestinationLat(), realJob.getDetailkontainer().get(1).getDestinationLng(), MapColor.YELLOW, realJob.getDetailkontainer().get(1).getDestinationName());

                            } else {
                                drawRoute(dd(realJob.getJobPickupLatitude()), dd(realJob.getJobPickupLongitude()), dd(realJob.getJobDeliverLatitude()), dd(realJob.getJobDeliverLongitude()), MapColor.GREEN, realJob.getJobDeliverAddress());
                            }
                        }
                    }
                }
            });
        }

        // setting parameter and ui for transport
        switch (realJob.getJobDeliverStatus()) {
            case 3: {
                whatFunc = Netter.Webservice.JOB_PICKUP;
                bTerima.setText("Pickup");
                break;
            }
            case 4: {
                whatFunc = Netter.Webservice.JOB_READYJOB;
                bTerima.setText("Ready To Stuff");
                break;
            }
            case 5: {
                whatFunc = Netter.Webservice.JOB_STARTJOBDEPARTURE;
                bTerima.setText("Start Stuff/Strip");
                break;
            }
            case 6: {
                whatFunc = Netter.Webservice.JOB_FINISHJOBDEPARTURE;
                bTerima.setText("Finish Stuff/Strip");
                break;
            }
            case 7: {
                whatFunc = Netter.Webservice.JOB_DELIVERJOB;
                bTerima.setText("Deliver");
                break;
            }
            case 8: {
                whatFunc = Netter.Webservice.JOB_ARRIVEDESTINATION;
                bTerima.setText("Arrive Destination");

                break;
            }
            case 9: {
                whatFunc = Netter.Webservice.JOB_STARTJOBARRIVAL;
                bTerima.setText("Start Stuff/Strip");

                break;
            }
            case 10: {
                if (isJob89) {
                    whatFunc = Netter.Webservice.JOB_FINISHJOBARRIVAL;
                    bTerima.setText("Finish Stuff/Strip");
                } else {
                    if(isJobTujuanMoreThanOne){
                        if (isSudahAdaYangTerkirim) {
                            whatFunc = Netter.Webservice.JOB_FINISHJOB;
                            bTerima.setText("Finish Job");
                        } else {
                            whatFunc = Netter.Webservice.JOB_DELIVERJOB;
                            bTerima.setText("Deliver second box");
                        }
                    } else {
                        whatFunc = Netter.Webservice.JOB_FINISHJOB;
                        bTerima.setText("Finish Job");
                    }
                }
                break;
            }
            case 11: {
                if (isJob89) {
                    whatFunc = Netter.Webservice.JOB_GOEMPTYDEPO;
                    bTerima.setText("Go Empty To Depo");
                }
                break;
            }
            case 12: {
                if (isJob89) {
                    whatFunc = Netter.Webservice.JOB_UPLOADEMPTYDEPO;
                    bTerima.setText("Upload Empty to Depo");
                }
                break;
            }
            case 13: {
                if (isJob89) {
                    whatFunc = Netter.Webservice.JOB_FINISHJOB;
                    bTerima.setText("Finish Job");
                }
                break;
            }
            case 14: {
                // upload gambar e
                bTerima.setText("Photo upload :)");
                break;
            }
        }

    }

    private double dd(String s) {
        return Double.parseDouble(s);
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

    public void drawRoute(double slat, double slng, double dlat, double dlng, final MapColor color, String targetName) {
        try {
            new DirectionFinder(new DirectionFinderListener() {
                @Override
                public void onDirectionFinderStart() {

                }

                @Override
                public void onDirectionFinderSuccess(List<RouteModel> route) {
                    PolylineOptions polylineOptions = new PolylineOptions().
                            geodesic(true).
                            color(color.rgb).
                            width(10);
                    for (int i = 0; i < route.size(); i++) {
                        for (int j = 0; j < route.get(i).points.size(); j++) {
                            polylineOptions.add(route.get(i).points.get(j));
                        }
                    }
                    Polyline polyline = gmap.addPolyline(polylineOptions);
                    polylineList.add(polyline);
                }
            }, slat, slng, dlat, dlng).execute();
            drawMarker(new LatLng(dlat, dlng), color, targetName);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void drawMarker(LatLng latLng, MapColor color, String title) {
        MarkerOptions markerOptions = new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker(color.hsv)).title(title);
        Marker marker = gmap.addMarker(markerOptions);
        markerList.add(marker);
    }

    public void setUpLocation(Location location) {
        this.location = location;
        this.latLng = new LatLng(location.getLatitude(), location.getLongitude());
        findMe();
    }

    private void findMe() {
        if (checkTrack.isChecked()) {
            CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(latLng, 15);
            gmap.animateCamera(cu);
        }
    }
}
