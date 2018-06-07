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
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.andresual.dev.tms.Activity.ActivityUpload;
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
import com.andresual.dev.tms.Activity.Util.Haversine;
import com.andresual.dev.tms.Activity.Util.Netter;
import com.andresual.dev.tms.Activity.Util.Pref;
import com.andresual.dev.tms.Activity.Util.StringHashMap;
import com.andresual.dev.tms.R;
import com.android.volley.Request;
import com.android.volley.Response;
import com.github.aakira.expandablelayout.ExpandableLinearLayout;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
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

    int colorActive;
    int colorInactive;
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

    @BindView(R.id.expLl)
    ExpandableLinearLayout expLl;
    @BindView(R.id.tvFirst)
    TextView tvFirst;
    @BindView(R.id.tvSecond)
    TextView tvSecond;
    @BindView(R.id.tvThird)
    TextView tvThird;
    @BindView(R.id.bExpandInfo)
    View bExpandInfo;
    @BindView(R.id.btn_tolak)
    Button bTolak;

    boolean debugGeofence = false;
    boolean enableGeofence = false;
    double geofenceLat;
    double geofenceLng;
    public static final double GEOFENCE_RADIUS = 1000; // in meters, non retarded unit
    LocationManager lm;

    enum MapColor {

        BLUE(BitmapDescriptorFactory.HUE_BLUE, Color.BLUE),
        GREEN(BitmapDescriptorFactory.HUE_GREEN, Color.GREEN),
        RED(BitmapDescriptorFactory.HUE_RED, Color.RED),
        MAGENTA(BitmapDescriptorFactory.HUE_MAGENTA, Color.MAGENTA),
        CYAN(BitmapDescriptorFactory.HUE_CYAN, Color.CYAN),
        YELLOW(BitmapDescriptorFactory.HUE_YELLOW, Color.YELLOW);

        public float hsv;
        public int rgb;

        MapColor(float hsv, int rgb) {
            this.hsv = hsv;
            this.rgb = rgb;
        }

        public int getRgbTransparent(float intensity) {
            int newColor = ((int) (255 * intensity) << 24) | (rgb ^ 0xFF000000);
            return newColor;
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
    List<Circle> circleList = new ArrayList<>();

    Marker mockMarker;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_order);
        pref = new Pref(this);
        driver = pref.getDriverModel();
        kendaraan = pref.getKendaraan();
        colorActive = ContextCompat.getColor(this, R.color.colorPrimary);
        colorInactive = ContextCompat.getColor(this, R.color.md_blue_grey_100);
        ButterKnife.bind(this);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        if (getSupportActionBar() != null) getSupportActionBar().setTitle("Proses Job");
        filter = new IntentFilter(LocationBroadcaster.LOCATION_BROADCAST_ACTION);
        lm = ((LocationManager) getSystemService(Context.LOCATION_SERVICE));

        simpleJob = getIntent().getParcelableExtra(INTENT_DATA);
        bExpandInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expLl.toggle();
            }
        });

        checkTrack.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    findMe();
                }
            }
        });
        tvDistance.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(ActivityProsesMap.this, "Debug: Reset job to pickup", Toast.LENGTH_SHORT).show();
                resetJob();
                return false;
            }
        });
        tvDuration.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                debugGeofence = !debugGeofence;
                Toast.makeText(ActivityProsesMap.this, "Debug tombol "+debugGeofence, Toast.LENGTH_SHORT).show();
                buttonSwitch();
//                debugEnableMockLocation(debugGeofence, true);
                return false;
            }
        });

        bTerima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (whatFunc == null) {
                    ActivityUpload.start(ActivityProsesMap.this, simpleJob);
                    finish();
                } else {
                    nextStep();
                }
            }
        });
        bTolak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityReject.start(ActivityProsesMap.this, simpleJob);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ActivityReject.REQ_CODE && resultCode == Activity.RESULT_OK) {
            finish();
        }
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
            if (realJob.statusKontainer() < 1) {
                map.putMore("idjob_detail", realJob.getDetailkontainer().get(0).getIddetail());
            } else {
                map.putMore("idjob_detail", realJob.getDetailkontainer().get(1).getIddetail());
            }
        }
        Log.i("RequestJob", map.toString());
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
        enableGeofence = false;
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

    boolean isJob89;
    boolean isJobTujuanMoreThanOne;
    boolean isSingleBox;
    boolean isSudahAdaYangTerkirim;

    @SuppressLint("MissingPermission")
    public void setUpAll() {
        clearMapElement();
        Log.i("JOBA", "JobType " + realJob.getJobType());
        Log.i("JOBB", "JobStatus " + realJob.getJobDeliverStatus());
        tvDistance.setText(realJob.getJobDeliverDistancetext());
        tvStatus.setText(realJob.getStringDeliverStatus());
        tvDuration.setText(realJob.getJobDeliverEstimatetimetext());


        isJob89 = realJob.getJobType() == 8 || realJob.getJobType() == 9;
        isJobTujuanMoreThanOne = realJob.getJumlahtujuan() > 1;
        isSingleBox = Integer.parseInt(realJob.getJumlahbox()) == 1;
        isSudahAdaYangTerkirim = (isJobTujuanMoreThanOne && !isSingleBox) && realJob.getDetailkontainer().get(0).getJobStatus() == 10;

        if(getSupportActionBar()!=null)getSupportActionBar().setSubtitle(realJob.getJobTypeName() + "("+realJob.getJobType()+")");

        if (!isSingleBox && isJobTujuanMoreThanOne && !isJob89) {
            tvFirst.setText(realJob.getJobPickupName());
            tvSecond.setText(realJob.getDetailkontainer().get(0).getCustomerName());
            tvThird.setText(realJob.getDetailkontainer().get(1).getCustomerName());
        } else if (isJob89) {
            tvFirst.setText(realJob.getJobPickupName());
            tvSecond.setText(realJob.getJobDeliverAddress());
            tvThird.setText(realJob.getJobBalikAddress());
        } else {
            tvFirst.setText(realJob.getJobPickupName());
            tvSecond.setText(realJob.getJobDeliverAddress());
            tvThird.setVisibility(View.GONE);
        }

        // draw route from start to end
        if (gmap != null) {
            LocationServices.getFusedLocationProviderClient(this).getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    setUpLocation(location);
                    prepareButton();
                    drawRoute(location.getLatitude(), location.getLongitude(), dd(realJob.getJobPickupLatitude()), dd(realJob.getJobPickupLongitude()), MapColor.BLUE, realJob.getJobPickupName());
                    if (isJob89) {
                        drawRoute(location.getLatitude(), location.getLongitude(), dd(realJob.getJobPickupLatitude()), dd(realJob.getJobPickupLongitude()), MapColor.BLUE, realJob.getJobPickupName());
                        drawRoute(dd(realJob.getJobPickupLatitude()), dd(realJob.getJobPickupLongitude()), dd(realJob.getJobDeliverLatitude()), dd(realJob.getJobDeliverLongitude()), MapColor.GREEN, realJob.getJobDeliverAddress());
                        drawRoute(dd(realJob.getJobDeliverLatitude()), dd(realJob.getJobDeliverLongitude()), dd(realJob.getJobBalikLatitude()), dd(realJob.getJobBalikLongitude()), MapColor.MAGENTA, realJob.getJobBalikAddress());
                    } else {

                        if (isSingleBox) {
                            drawRoute(dd(realJob.getJobPickupLatitude()), dd(realJob.getJobPickupLongitude()), dd(realJob.getJobDeliverLatitude()), dd(realJob.getJobDeliverLongitude()), MapColor.GREEN, realJob.getJobDeliverAddress());
                        } else {
                            if (isJobTujuanMoreThanOne) {
                                drawRoute(dd(realJob.getJobPickupLatitude()), dd(realJob.getJobPickupLongitude()), realJob.getDetailkontainer().get(0).getDestinationLat(), realJob.getDetailkontainer().get(0).getDestinationLng(), MapColor.GREEN, realJob.getDetailkontainer().get(0).getDestinationName());
                                drawRoute(realJob.getDetailkontainer().get(0).getDestinationLat(), realJob.getDetailkontainer().get(0).getDestinationLng(), realJob.getDetailkontainer().get(1).getDestinationLat(), realJob.getDetailkontainer().get(1).getDestinationLng(), MapColor.MAGENTA, realJob.getDetailkontainer().get(1).getDestinationName());

                            } else {
                                drawRoute(dd(realJob.getJobPickupLatitude()), dd(realJob.getJobPickupLongitude()), dd(realJob.getJobDeliverLatitude()), dd(realJob.getJobDeliverLongitude()), MapColor.GREEN, realJob.getJobDeliverAddress());
                            }
                        }
                    }
                }
            });
        }

    }

    private void mockLocationTest(LatLng latLng) {
        if (mockMarker != null) mockMarker.remove();
        if (debugGeofence && latLng != null) {
            mockMarker = gmap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
            Location mockLocation = new Location(LocationManager.GPS_PROVIDER);
            mockLocation.setLatitude(latLng.latitude);
            mockLocation.setLongitude(latLng.longitude);
            mockLocation.setAltitude(location.getAltitude());
            mockLocation.setTime(System.currentTimeMillis());
            mockLocation.setAccuracy(1);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                mockLocation.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());
            }
            setUpLocation(location);
            lm.setTestProviderLocation(LocationManager.GPS_PROVIDER, mockLocation);
        }
    }

    private double dd(String s) {
        return Double.parseDouble(s);
    }

    private void prepareButton() {
        switch (realJob.getJobDeliverStatus()) {
            case 3: {
                whatFunc = Netter.Webservice.JOB_PICKUP;
                bTerima.setText("Pickup");

                enableGeofence = true;
                setGeofenceTarget(realJob.getJobPickupLatitude(), realJob.getJobPickupLongitude());

                break;
            }
            case 4: {
                whatFunc = Netter.Webservice.JOB_READYJOB;
                bTerima.setText("Ready To Stuff");

                enableGeofence = true;
                setGeofenceTarget(realJob.getJobPickupLatitude(), realJob.getJobPickupLongitude());

                break;
            }
            case 5: {
                whatFunc = Netter.Webservice.JOB_STARTJOBDEPARTURE;
                bTerima.setText("Start Stuff/Strip");

                enableGeofence = true;
                setGeofenceTarget(realJob.getJobPickupLatitude(), realJob.getJobPickupLongitude());

                break;
            }
            case 6: {
                whatFunc = Netter.Webservice.JOB_FINISHJOBDEPARTURE;
                bTerima.setText("Finish Stuff/Strip");

                enableGeofence = true;
                setGeofenceTarget(realJob.getJobPickupLatitude(), realJob.getJobPickupLongitude());

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

                enableGeofence = true;
                if (!isSingleBox && isJobTujuanMoreThanOne && !isJob89) {
                    if (realJob.statusKontainer() == 0) {
                        setGeofenceTarget(realJob.getDetailkontainer().get(0).getDestinationLat(), realJob.getDetailkontainer().get(0).getDestinationLng());
                    } else {
                        setGeofenceTarget(realJob.getDetailkontainer().get(1).getDestinationLat(), realJob.getDetailkontainer().get(1).getDestinationLng());
                    }
                } else {
                    setGeofenceTarget(realJob.getJobDeliverLatitude(), realJob.getJobDeliverLongitude());
                }

                break;
            }
            case 9: {
                whatFunc = Netter.Webservice.JOB_STARTJOBARRIVAL;
                bTerima.setText("Start Stuff/Strip");

                enableGeofence = true;
                if (!isSingleBox && isJobTujuanMoreThanOne && !isJob89) {
                    if (realJob.statusKontainer() == 0) {
                        setGeofenceTarget(realJob.getDetailkontainer().get(0).getDestinationLat(), realJob.getDetailkontainer().get(0).getDestinationLng());
                    } else {
                        setGeofenceTarget(realJob.getDetailkontainer().get(1).getDestinationLat(), realJob.getDetailkontainer().get(1).getDestinationLng());
                    }
                } else {
                    setGeofenceTarget(realJob.getJobDeliverLatitude(), realJob.getJobDeliverLongitude());
                }

                break;
            }
            case 10: {
                if (isJob89) {
                    whatFunc = Netter.Webservice.JOB_FINISHJOBARRIVAL;
                    bTerima.setText("Finish Stuff/Strip");
                } else {
                    whatFunc = Netter.Webservice.JOB_FINISHJOB;
                    bTerima.setText("Finish Job");
                }

                enableGeofence = true;
                if (!isSingleBox && isJobTujuanMoreThanOne && !isJob89) {
                    if (realJob.statusKontainer() == 0) {
                        setGeofenceTarget(realJob.getDetailkontainer().get(0).getDestinationLat(), realJob.getDetailkontainer().get(0).getDestinationLng());
                    } else {
                        setGeofenceTarget(realJob.getDetailkontainer().get(1).getDestinationLat(), realJob.getDetailkontainer().get(1).getDestinationLng());
                    }
                } else {
                    setGeofenceTarget(realJob.getJobDeliverLatitude(), realJob.getJobDeliverLongitude());
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

                    enableGeofence = true;
                    setGeofenceTarget(realJob.getJobBalikLatitude(), realJob.getJobBalikLongitude());
                }
                break;
            }
            case 13: {
                if (isJob89) {
                    whatFunc = Netter.Webservice.JOB_FINISHJOB;
                    bTerima.setText("Finish Job");

                    enableGeofence = true;
                    setGeofenceTarget(realJob.getJobBalikLatitude(), realJob.getJobBalikLongitude());
                }
                break;
            }
            case 14: {
                if (realJob.statusKontainer() == 1) {
                    whatFunc = Netter.Webservice.JOB_DELIVERJOB;
                    bTerima.setText("Deliver second job");
                } else {
                    bTerima.setText("Photo upload :)");
                    whatFunc = null;
                }
                break;
            }
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
        debugEnableMockLocation(false, false);
        unregisterReceiver(br);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        gmap = googleMap;
        gmap.setMyLocationEnabled(true);

        gmap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                mockLocationTest(latLng);
                setUpAll();
            }
        });

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
        drawCircle(latLng, color);
    }

    public void drawCircle(LatLng latLng, MapColor color) {
        CircleOptions circleOptions = new CircleOptions().center(latLng).fillColor(color.getRgbTransparent(0.1f)).strokeColor(Color.TRANSPARENT).radius(GEOFENCE_RADIUS);
        Circle circle = gmap.addCircle(circleOptions);
        circleList.add(circle);
    }

    public void setUpLocation(Location location) {
        this.location = location;
        this.latLng = new LatLng(location.getLatitude(), location.getLongitude());
        buttonSwitch();
        findMe();
    }

    private void findMe() {
        if (checkTrack.isChecked()) {
            CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(latLng, 15);
            gmap.animateCamera(cu);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_view_photo, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_view_photo) {
            ActivityUpload.start(this, simpleJob);
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("MissingPermission")
    private void resetJob() {
        LocationServices.getFusedLocationProviderClient(this).getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                new Netter(ActivityProsesMap.this).webService(Request.Method.POST, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject obj = new JSONObject(response);
                                    toast(obj.getString("message"));
                                    if (obj.getInt("status") == 200) {
                                        fetchJob();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, Netter.getDefaultErrorListener(ActivityProsesMap.this, new Runnable() {
                            @Override
                            public void run() {
                            }
                        }), Netter.Webservice.JOB_ACCEPT, new StringHashMap()
                                .putMore("idjob", Integer.toString(simpleJob.getJobId()))
                                .putMore("email", driver.getEmail())
                                .putMore("lat", Double.toString(location.getLatitude()))
                                .putMore("lng", Double.toString(location.getLongitude()))
                );
            }
        });
    }

    public void clearMapElement() {
        for (int i = 0; i < markerList.size(); i++) {
            markerList.get(i).remove();
        }
        markerList.clear();
        for (int i = 0; i < polylineList.size(); i++) {
            polylineList.get(i).remove();
        }
        polylineList.clear();
        for (int i = 0; i < circleList.size(); i++) {
            circleList.get(i).remove();
        }
        circleList.clear();
    }

    public void buttonSwitch() {
        double distance = Haversine.calculate(geofenceLat, geofenceLng, location.getLatitude(), location.getLongitude());
        Log.i("KANA_NISHINO", distance + "m");

        if (!debugGeofence && enableGeofence && distance > GEOFENCE_RADIUS) {
            bTerima.setEnabled(false);
            bTerima.setBackgroundColor(colorInactive);
        } else {
            bTerima.setEnabled(true);
            bTerima.setBackgroundColor(colorActive);
        }
    }

    public void setGeofenceTarget(double lat, double lng) {
        this.geofenceLat = lat;
        this.geofenceLng = lng;
        buttonSwitch();
    }

    public void setGeofenceTarget(String lat, String lng) {
        this.geofenceLat = dd(lat);
        this.geofenceLng = dd(lng);
        buttonSwitch();
    }

    public void debugEnableMockLocation(boolean enable, boolean showToast) {
//        try {
//            if (enable) {
//                Toast.makeText(ActivityProsesMap.this, "Press and hold location and map to set location", Toast.LENGTH_LONG).show();
//                lm.addTestProvider(LocationManager.GPS_PROVIDER, false, false,
//                        false, false, true, true, true, 0, 5);
//                lm.setTestProviderEnabled(LocationManager.GPS_PROVIDER, true);
//            } else {
//                if (showToast) {
//                    Toast.makeText(ActivityProsesMap.this, "Mocklocation disabled. Wait untuk your gps is locked", Toast.LENGTH_SHORT).show();
//                }
//                lm.setTestProviderEnabled(LocationManager.GPS_PROVIDER, false);
//                lm.clearTestProviderLocation(LocationManager.GPS_PROVIDER);
//                lm.clearTestProviderEnabled(LocationManager.GPS_PROVIDER);
//                lm.removeTestProvider(LocationManager.GPS_PROVIDER);
//            }
//        } catch (IllegalArgumentException e) {
//            e.printStackTrace();
//        }
//        mockLocationTest(null);
    }
}
