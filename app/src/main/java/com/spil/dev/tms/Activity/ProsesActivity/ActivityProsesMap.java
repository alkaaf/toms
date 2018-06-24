package com.spil.dev.tms.Activity.ProsesActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.maps.android.PolyUtil;
import com.spil.dev.tms.Activity.ActivityUpload;
import com.spil.dev.tms.Activity.Adapter.ContainerAdapter;
import com.spil.dev.tms.Activity.BaseActivity;
import com.spil.dev.tms.Activity.Maps.DirectionFinder;
import com.spil.dev.tms.Activity.Maps.DirectionFinderListener;
import com.spil.dev.tms.Activity.Maps.LocationBroadcaster;
import com.spil.dev.tms.Activity.Model.DriverModel;
import com.spil.dev.tms.Activity.Model.KendaraanModel;
import com.spil.dev.tms.Activity.Model.RealJob;
import com.spil.dev.tms.Activity.Model.RouteModel;
import com.spil.dev.tms.Activity.Model.SimpleJob;
import com.spil.dev.tms.Activity.Util.Haversine;
import com.spil.dev.tms.Activity.Util.Netter;
import com.spil.dev.tms.Activity.Util.Pref;
import com.spil.dev.tms.Activity.Util.StringHashMap;
import com.spil.dev.tms.Activity.Util.UIUtils;
import com.spil.dev.tms.R;
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

    static ActivityProsesMap instance;

    public ActivityProsesMap() {
        instance = this;
    }

    public static ActivityProsesMap getInstance() {
        return instance;
    }

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

    @BindView(R.id.tvOrderId)
    TextView tvOrderId;
    @BindView(R.id.expLl)
    View expLl;
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
    @BindView(R.id.tvJobPickupName)
    TextView tvJobPickupName;
    @BindView(R.id.tvJobDesc)
    TextView tvJobDesc;
    @BindView(R.id.tvTanggal)
    TextView tvTanggal;
    @BindView(R.id.vDestinationList)
    View vDestinationList;
    @BindView(R.id.lvContainer)
    ListView lvContainer;
    @BindView(R.id.tvMuatanKosong)
    View tvMuatanKosong;

    boolean debugGeofence = false;
    boolean enableGeofence = false;
    boolean enableContainerCheck = false;
    double geofenceLat;
    double geofenceLng;
    List<LatLng> polyFence;
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
    List<Polygon> polygonList = new ArrayList<>();

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
                if (expLl.getVisibility() == View.GONE) {
                    expLl.setVisibility(View.VISIBLE);
                } else {
                    expLl.setVisibility(View.GONE);
                }
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
                Toast.makeText(ActivityProsesMap.this, "Debug tombol " + debugGeofence, Toast.LENGTH_SHORT).show();
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
        if (requestCode == ActivityUpload.REQ_CODE && resultCode == Activity.RESULT_OK) {

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
    boolean isJob12;

    @SuppressLint("MissingPermission")
    public void setUpAll() {
        clearMapElement();

        // tvMuatanKosong
        tvMuatanKosong.setVisibility((realJob.getDetailkontainer() != null && realJob.getDetailkontainer().isEmpty()) ? View.VISIBLE : View.GONE);

        Log.i("JOBID", "JOBID " + realJob.getId());
        Log.i("JOBB", "JobStatus " + realJob.getJobDeliverStatus());
        tvDistance.setText(realJob.getJobDeliverDistancetext());
        tvStatus.setText(realJob.getStringDeliverStatus());
        tvDuration.setText(realJob.getJobDeliverEstimatetimetext());
        tvOrderId.setText(realJob.getOrderId());
        tvJobDesc.setText(realJob.getJobDescription());
        tvJobPickupName.setText(realJob.getJobPickupName());
        tvTanggal.setText(realJob.parsedPickupDate());

        isJob89 = realJob.getJobType() == 8 || realJob.getJobType() == 9;
        isJobTujuanMoreThanOne = realJob.getJumlahtujuan() > 1;
        isSingleBox = Integer.parseInt(realJob.getJumlahbox()) == 1;
        isSudahAdaYangTerkirim = (isJobTujuanMoreThanOne && !isSingleBox) && realJob.getDetailkontainer().get(0).getJobStatus() == 10;
        isJob12 = realJob.getJobType() == 1 || realJob.getJobType() == 2;
        if (getSupportActionBar() != null)
            getSupportActionBar().setSubtitle(realJob.getStringJobTypeName()/* + "("+realJob.getJobType()+")"*/);

        tvOrderId.setText(realJob.getOrderId());
        if (!isSingleBox && isJobTujuanMoreThanOne && !isJob89) {
            tvFirst.setText(realJob.getJobPickupName());
            tvSecond.setText(realJob.getDetailkontainer().get(0).getDestinationName());
            tvThird.setText(realJob.getDetailkontainer().get(1).getDestinationName());
        } else if (isJob89) {
            tvFirst.setText(realJob.getJobPickupName());
            tvSecond.setText(realJob.getJobDeliverAddress());
            tvThird.setText(realJob.getJobBalikAddress());
        } else {
            tvFirst.setText(realJob.getJobPickupName());
            tvSecond.setText(realJob.getJobDeliverAddress());
            tvThird.setVisibility(View.GONE);
        }

        // setup container adapter
        lvContainer.setAdapter(new ContainerAdapter(this, R.layout.list_container, realJob.getDetailkontainer()));
        UIUtils.setListViewHeightBasedOnItems(lvContainer);
        // draw route from start to end
        if (gmap != null) {
            LocationServices.getFusedLocationProviderClient(this).getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    setUpLocation(location);
                    prepareButton();
                    drawRoute(location.getLatitude(), location.getLongitude(), dd(realJob.getJobPickupLatitude()), dd(realJob.getJobPickupLongitude()), MapColor.BLUE, realJob.getJobPickupName(), realJob.getGeofence_asal());
                    if (isJob89) {
                        drawRoute(location.getLatitude(), location.getLongitude(), dd(realJob.getJobPickupLatitude()), dd(realJob.getJobPickupLongitude()), MapColor.BLUE, realJob.getJobPickupName(), realJob.getGeofence_asal());
                        drawRoute(dd(realJob.getJobPickupLatitude()), dd(realJob.getJobPickupLongitude()), dd(realJob.getJobDeliverLatitude()), dd(realJob.getJobDeliverLongitude()), MapColor.GREEN, realJob.getJobDeliverAddress(), realJob.getGeofence_tujuan());
                        drawRoute(dd(realJob.getJobDeliverLatitude()), dd(realJob.getJobDeliverLongitude()), dd(realJob.getJobBalikLatitude()), dd(realJob.getJobBalikLongitude()), MapColor.MAGENTA, realJob.getJobBalikAddress(), realJob.getGeofence_balik());
                    } else {
                        if (isSingleBox) {
                            drawRoute(dd(realJob.getJobPickupLatitude()), dd(realJob.getJobPickupLongitude()), dd(realJob.getJobDeliverLatitude()), dd(realJob.getJobDeliverLongitude()), MapColor.GREEN, realJob.getJobDeliverAddress(), realJob.getGeofence_asal());
                        } else {
                            if (isJobTujuanMoreThanOne) {
                                drawRoute(dd(realJob.getJobPickupLatitude()), dd(realJob.getJobPickupLongitude()), realJob.getDetailkontainer().get(0).getDestinationLat(), realJob.getDetailkontainer().get(0).getDestinationLng(), MapColor.GREEN, realJob.getDetailkontainer().get(0).getDestinationName(), realJob.getDetailkontainer().get(0).getGeofence_tujuan());
                                drawRoute(realJob.getDetailkontainer().get(0).getDestinationLat(), realJob.getDetailkontainer().get(0).getDestinationLng(), realJob.getDetailkontainer().get(1).getDestinationLat(), realJob.getDetailkontainer().get(1).getDestinationLng(), MapColor.MAGENTA, realJob.getDetailkontainer().get(1).getDestinationName(), realJob.getDetailkontainer().get(0).getGeofence_tujuan());
                            } else {
                                drawRoute(dd(realJob.getJobPickupLatitude()), dd(realJob.getJobPickupLongitude()), dd(realJob.getJobDeliverLatitude()), dd(realJob.getJobDeliverLongitude()), MapColor.GREEN, realJob.getJobDeliverAddress(), realJob.getGeofence_tujuan());
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
        bTerima.setText(realJob.getButtonLabel());
        switch (realJob.getJobDeliverStatus()) {
            case 3: {
                whatFunc = Netter.Webservice.JOB_PICKUP;
                if (isJob12) {
                    vDestinationList.setVisibility(View.GONE);
                    lvContainer.setVisibility(View.GONE);
                }
                break;
            }
            // status pickup
            case 4: {
                whatFunc = Netter.Webservice.JOB_READYJOB;
                enableGeofence = true;
                setGeofenceTarget(realJob.getJobPickupLatitude(), realJob.getJobPickupLongitude());
                setGeofenceTarget(realJob.getGeofence_asal());
                if (isJob12) {
                    vDestinationList.setVisibility(View.GONE);
                    lvContainer.setVisibility(View.GONE);
                }
                break;
            }
            case 5: {
                whatFunc = Netter.Webservice.JOB_STARTJOBDEPARTURE;
                enableGeofence = true;
//                enableContainerCheck = true;
                if ((realJob.getDetailkontainer() == null || realJob.getDetailkontainer().isEmpty()) && !isFinishing()) {
                    new AlertDialog.Builder(this).setTitle("Kontainer kosong").setMessage("Belum ada kontainer dimuat").setPositiveButton("Ok", null).show();
                }
                setGeofenceTarget(realJob.getJobPickupLatitude(), realJob.getJobPickupLongitude());
                setGeofenceTarget(realJob.getGeofence_asal());
                if (isJob12) {
                    vDestinationList.setVisibility(View.VISIBLE);
                    lvContainer.setVisibility(View.VISIBLE);
                }
                break;
            }
            case 6: {
                whatFunc = Netter.Webservice.JOB_FINISHJOBDEPARTURE;
                enableGeofence = true;
                enableContainerCheck = true;
                if ((realJob.getDetailkontainer() == null || realJob.getDetailkontainer().isEmpty()) && !isFinishing()) {
                    new AlertDialog.Builder(this).setTitle("Kontainer kosong").setMessage("Belum ada kontainer dimuat").setPositiveButton("Ok", null).show();
                }
                setGeofenceTarget(realJob.getJobPickupLatitude(), realJob.getJobPickupLongitude());
                setGeofenceTarget(realJob.getGeofence_asal());
                if (isJob12) {
                    vDestinationList.setVisibility(View.VISIBLE);
                    lvContainer.setVisibility(View.VISIBLE);
                }
                break;
            }
            case 7: {
                // deliver
                whatFunc = Netter.Webservice.JOB_DELIVERJOB;
                enableGeofence = true;
                enableContainerCheck = true;
                setGeofenceTarget(realJob.getJobPickupLatitude(), realJob.getJobPickupLongitude());
                setGeofenceTarget(realJob.getGeofence_asal());
                if (isJob12) {
                    vDestinationList.setVisibility(View.GONE);
//                    lvContainer.setVisibility(View.GONE);
                }
                break;
            }
            case 8: {//deliver
                whatFunc = Netter.Webservice.JOB_ARRIVEDESTINATION;
                enableGeofence = true;
                enableContainerCheck = true;
                if (!isSingleBox && isJobTujuanMoreThanOne && !isJob89) {
                    if (realJob.statusKontainer() == 0) {
                        setGeofenceTarget(realJob.getDetailkontainer().get(0).getDestinationLat(), realJob.getDetailkontainer().get(0).getDestinationLng());
                        setGeofenceTarget(realJob.getDetailkontainer().get(0).getGeofence_tujuan());
                    } else {
                        setGeofenceTarget(realJob.getDetailkontainer().get(1).getDestinationLat(), realJob.getDetailkontainer().get(1).getDestinationLng());
                        setGeofenceTarget(realJob.getDetailkontainer().get(1).getGeofence_tujuan());
                    }
                } else {
                    setGeofenceTarget(realJob.getJobDeliverLatitude(), realJob.getJobDeliverLongitude());
                    setGeofenceTarget(realJob.getGeofence_tujuan());
                }
                if (isJob12) {
                    vDestinationList.setVisibility(View.VISIBLE);
                    lvContainer.setVisibility(View.VISIBLE);
                }

                break;
            }
            case 9: {
                whatFunc = Netter.Webservice.JOB_STARTJOBARRIVAL;
                enableGeofence = true;
                enableContainerCheck = true;
                if (!isSingleBox && isJobTujuanMoreThanOne && !isJob89) {
                    if (realJob.statusKontainer() == 0) {
                        setGeofenceTarget(realJob.getDetailkontainer().get(0).getDestinationLat(), realJob.getDetailkontainer().get(0).getDestinationLng());
                        setGeofenceTarget(realJob.getDetailkontainer().get(0).getGeofence_tujuan());
                    } else {
                        setGeofenceTarget(realJob.getDetailkontainer().get(1).getDestinationLat(), realJob.getDetailkontainer().get(1).getDestinationLng());
                        setGeofenceTarget(realJob.getDetailkontainer().get(1).getGeofence_tujuan());
                    }
                } else {
                    setGeofenceTarget(realJob.getJobDeliverLatitude(), realJob.getJobDeliverLongitude());
                    setGeofenceTarget(realJob.getGeofence_tujuan());
                }

                break;
            }
            case 10: {
                if (isJob89) {
                    // finish load
                    whatFunc = Netter.Webservice.JOB_FINISHJOBARRIVAL;
                } else {
                    // finish job
                    whatFunc = Netter.Webservice.JOB_FINISHJOB;
                }

                enableGeofence = true;
                enableContainerCheck = true;
                if (!isSingleBox && isJobTujuanMoreThanOne && !isJob89) {
                    if (realJob.statusKontainer() == 0) {
                        setGeofenceTarget(realJob.getDetailkontainer().get(0).getDestinationLat(), realJob.getDetailkontainer().get(0).getDestinationLng());
                        setGeofenceTarget(realJob.getDetailkontainer().get(0).getGeofence_tujuan());
                    } else {
                        setGeofenceTarget(realJob.getDetailkontainer().get(1).getDestinationLat(), realJob.getDetailkontainer().get(1).getDestinationLng());
                        setGeofenceTarget(realJob.getDetailkontainer().get(1).getGeofence_tujuan());
                    }
                } else {
                    setGeofenceTarget(realJob.getJobDeliverLatitude(), realJob.getJobDeliverLongitude());
                    setGeofenceTarget(realJob.getGeofence_tujuan());
                }

                break;
            }
            case 11: {
                if (isJob89) {
                    whatFunc = Netter.Webservice.JOB_GOEMPTYDEPO;
                }
                break;
            }
            case 12: {
                if (isJob89) {
                    whatFunc = Netter.Webservice.JOB_UPLOADEMPTYDEPO;
                    enableGeofence = true;
                    setGeofenceTarget(realJob.getJobBalikLatitude(), realJob.getJobBalikLongitude());
                    setGeofenceTarget(realJob.getGeofence_balik());
                }
                break;
            }
            case 13: {
                if (isJob89) {
                    whatFunc = Netter.Webservice.JOB_FINISHJOB;
                    enableGeofence = true;
                    enableContainerCheck = true;
                    setGeofenceTarget(realJob.getJobBalikLatitude(), realJob.getJobBalikLongitude());
                    setGeofenceTarget(realJob.getGeofence_balik());
                }
                break;
            }
            case 14: {
                if (realJob.statusKontainer() == 1) {
                    whatFunc = Netter.Webservice.JOB_DELIVERJOB;
                } else {
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

        quickFindMe();
        fetchJob();
    }

    public static void start(Context context, SimpleJob simpleJob) {
        Intent intent = new Intent(context, ActivityProsesMap.class);
        intent.putExtra(INTENT_DATA, simpleJob);
        context.startActivity(intent);
    }

    public void drawRoute(double slat, double slng, double dlat, double dlng, final MapColor color, String targetName, List<LatLng> geoFence) {
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
            drawMarker(new LatLng(dlat, dlng), color, targetName, geoFence);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void drawMarker(LatLng latLng, MapColor color, String title, List<LatLng> geoFence) {
        MarkerOptions markerOptions = new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker(color.hsv)).title(title);
        Marker marker = gmap.addMarker(markerOptions);
        markerList.add(marker);
        drawFence(latLng, color, geoFence);
    }

    public void drawFence(LatLng latLng, MapColor color, List<LatLng> geoFence) {
        if(geoFence == null) {
            CircleOptions circleOptions = new CircleOptions().center(latLng).fillColor(color.getRgbTransparent(0.6f)).strokeColor(Color.TRANSPARENT).radius(GEOFENCE_RADIUS);
            Circle circle = gmap.addCircle(circleOptions);
            circleList.add(circle);
        } else {
            PolygonOptions polygonOptions = new PolygonOptions().addAll(geoFence).fillColor(color.getRgbTransparent(0.6f)).strokeColor(Color.TRANSPARENT);
            Polygon polygon = gmap.addPolygon(polygonOptions);
            polygonList.add(polygon);
        }
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
//            gmap.animateCamera(cu);
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

    @SuppressLint("MissingPermission")
    private void quickFindMe() {
        LocationServices.getFusedLocationProviderClient(this).getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 15f));
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

        for (int i = 0; i < polygonList.size(); i++) {
            polygonList.get(i).remove();
        }
        polygonList.clear();
    }

    public void buttonSwitch() {
        if(polyFence != null){
            boolean isInside = PolyUtil.containsLocation(new LatLng(location.getLatitude(),location.getLongitude()),polyFence,true);
            if (!location.getProvider().equals("gps")) return;
            if (!debugGeofence && ((!isInside) || (enableContainerCheck && realJob.getDetailkontainer().isEmpty()))) {
                // disable button
                bTerima.setEnabled(false);
                bTerima.setBackgroundColor(colorInactive);
            } else {
                bTerima.setEnabled(true);
                bTerima.setBackgroundColor(colorActive);
            }
        } else{
            double distance = Haversine.calculate(geofenceLat, geofenceLng, location.getLatitude(), location.getLongitude());
            Log.i("KANA_NISHINO", distance + "m " + location.getProvider());
            if (!location.getProvider().equals("gps")) return;
            if (!debugGeofence && ((enableGeofence && distance > GEOFENCE_RADIUS) || (enableContainerCheck && realJob.getDetailkontainer().isEmpty()))) {
                // disable button
                bTerima.setEnabled(false);
                bTerima.setBackgroundColor(colorInactive);
            } else {
                bTerima.setEnabled(true);
                bTerima.setBackgroundColor(colorActive);
            }
        }
    }

    public void setGeofenceTarget(double lat, double lng) {
        setGeofenceTarget(null);
        this.geofenceLat = lat;
        this.geofenceLng = lng;
        buttonSwitch();
    }

    public void setGeofenceTarget(String lat, String lng) {
        setGeofenceTarget(null);
        this.geofenceLat = dd(lat);
        this.geofenceLng = dd(lng);
        buttonSwitch();
    }

    public void setGeofenceTarget(List<LatLng> fence) {
        polyFence = fence;
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
