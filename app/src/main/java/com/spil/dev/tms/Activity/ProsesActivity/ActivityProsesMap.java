package com.spil.dev.tms.Activity.ProsesActivity;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.PolyUtil;
import com.spil.dev.tms.Activity.ActivityUpload;
import com.spil.dev.tms.Activity.Adapter.ContainerAdapter;
import com.spil.dev.tms.Activity.BaseActivity;
import com.spil.dev.tms.Activity.Fragment.RejectFragment;
import com.spil.dev.tms.Activity.Maps.DirectionFinder;
import com.spil.dev.tms.Activity.Maps.DirectionFinderListener;
import com.spil.dev.tms.Activity.Maps.LocationBroadcaster;
import com.spil.dev.tms.Activity.Model.DriverModel;
import com.spil.dev.tms.Activity.Model.KendaraanModel;
import com.spil.dev.tms.Activity.Model.RealJob;
import com.spil.dev.tms.Activity.Model.RouteModel;
import com.spil.dev.tms.Activity.Model.SimpleJob;
import com.spil.dev.tms.Activity.Util.DistanceMatrix;
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
    public static final int REQ_FIRST = 1;
    public static final int REQ_SECOND = 2;
    public static final int REQ_THIRD = 3;
    public static final int REQ_OPT = 99;

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
    double nextTargetLat;
    double nextTargetLng;

    public static final double GEOFENCE_RADIUS = 500; // in meters, non retarded unit
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

    long lastTimeDistanceUpdate = 0;
    static final long TIME_DISTANCE_UPDATE_DELAY = 5L * 1000L;
    BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(LocationBroadcaster.LOCATION_BROADCAST_ACTION)) {
                setUpLocation((Location) intent.getParcelableExtra(LocationBroadcaster.LOCATION_DATA));
                if (realJob != null && System.currentTimeMillis() - lastTimeDistanceUpdate > TIME_DISTANCE_UPDATE_DELAY) {
                    // update the distance
                    updateTimeDistance();
                }
            }
        }
    };
    IntentFilter filter;
    List<Polyline> polylineList = new ArrayList<>();
    List<Marker> markerList = new ArrayList<>();
    List<Circle> circleList = new ArrayList<>();
    List<Polygon> polygonList = new ArrayList<>();

    Marker mockMarker;
    DatabaseReference root;
    DatabaseReference finishFirst;
    DatabaseReference finishSecond;
    DatabaseReference finishThird;
    DatabaseReference finishOpt;

    boolean isFinishFirst = false;
    boolean isFinishSecond = false;
    boolean isFinishThird = false;
    boolean isFinishOpt = false;

    boolean job123;

    public void updateTimeDistance() {
        DistanceMatrix.get(location.getLatitude(), location.getLongitude(), nextTargetLat, nextTargetLng, new DistanceMatrix.DistanceResult() {
            @Override
            public void onResult(String status, String distanceText, long distanceMeter, String durationText, long durationMin) {
                if (status.equals("OK")) {
                    tvDistance.setText(distanceText);
                    tvDuration.setText(durationText);
                }
            }
        });
    }

    public void setNextTarget(double lat, double lng) {
        this.nextTargetLat = lat;
        this.nextTargetLng = lng;
    }

    public void setNextTarget(String lat, String lng) {
        this.nextTargetLat = dd(lat);
        this.nextTargetLng = dd(lng);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_order);

        pref = new Pref(this);
        driver = pref.getDriverModel();
        kendaraan = pref.getKendaraan();
        pdLoading = new ProgressDialog(this);
        pdLoading.setMessage("Memuat...");
        colorActive = ContextCompat.getColor(this, R.color.colorPrimary);
        colorInactive = ContextCompat.getColor(this, R.color.md_blue_grey_100);
        ButterKnife.bind(this);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        if (getSupportActionBar() != null) getSupportActionBar().setTitle("Proses Job");
        filter = new IntentFilter(LocationBroadcaster.LOCATION_BROADCAST_ACTION);
        lm = ((LocationManager) getSystemService(Context.LOCATION_SERVICE));

        simpleJob = getIntent().getParcelableExtra(INTENT_DATA);
        root = FirebaseDatabase.getInstance().getReference("uploadstatus").child("job_" + simpleJob.getJobId());
        finishFirst = root.child("first");
        finishSecond = root.child("second");
        finishThird = root.child("third");
        finishOpt = root.child("opt");

        job123 = simpleJob.getJobType() >= 1 && simpleJob.getJobType() <= 3;
        if (job123) {
            isFinishFirst = true;
            isFinishSecond = true;
            isFinishThird = true;
            isFinishOpt = true;
        }
        finishFirst.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    isFinishFirst = dataSnapshot.getValue(Boolean.class);
                } catch (NullPointerException e) {
                    isFinishFirst = job123;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        finishSecond.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    isFinishSecond = dataSnapshot.getValue(Boolean.class);
                } catch (NullPointerException e) {
                    isFinishSecond = job123;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        finishThird.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    isFinishThird = dataSnapshot.getValue(Boolean.class);
                } catch (NullPointerException e) {
                    isFinishThird = job123;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        finishOpt.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    isFinishOpt = dataSnapshot.getValue(Boolean.class);
                } catch (NullPointerException e) {
                    isFinishOpt = job123;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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
//                    if (!job123) {
//                        ActivityUpload.start(ActivityProsesMap.this, simpleJob);
//                    }
                    finish();
                } else {
                    nextStep();
                }
            }
        });
        bTolak.setText("");

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ActivityReject.REQ_CODE && resultCode == Activity.RESULT_OK) {
            finish();
        }
        if (requestCode == REQ_FIRST) {
            if (resultCode == Activity.RESULT_OK) {
                finishFirst.setValue(true);
            } else {
                showUploadPrompt(REQ_FIRST);
            }
        }
        if (requestCode == REQ_SECOND) {
            if (resultCode == Activity.RESULT_OK) {
                finishSecond.setValue(true);
            } else {
                showUploadPrompt(REQ_SECOND);
            }
        }
        if (requestCode == REQ_THIRD) {
            if (resultCode == Activity.RESULT_OK) {
                finishThird.setValue(true);
            } else {
                showUploadPrompt(REQ_THIRD);
            }
        }
        if (requestCode == REQ_OPT) {
            if (resultCode == Activity.RESULT_OK) {
                finishOpt.setValue(true);
            } else {
                showUploadPrompt(REQ_OPT);
            }
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
                Log.i("KONTAINER", "1");
            } else {
                Log.i("KONTAINER", "2");
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

    ProgressDialog pdLoading;

    public void fetchJob() {
        uiRunner(new Runnable() {
            @Override
            public void run() {
                enableGeofence = false;
                pdLoading.show();
                new Netter(ActivityProsesMap.this).webService(Request.Method.POST, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject obj = null;
                        try {
                            obj = new JSONObject(response);
                            realJob = new Gson().fromJson(obj.getString("job-" + simpleJob.getJobId()), RealJob.class);
                            if (realJob == null) {
                                finish();
                            }
                            setUpAll();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, Netter.getDefaultErrorListener(ActivityProsesMap.this, new Runnable() {
                    @Override
                    public void run() {

                    }
                }), Netter.Webservice.DETAILPICKUP, new StringHashMap().putMore("id", Integer.toString(simpleJob.getJobId())));

            }
        });
    }

    boolean isJob89;
    boolean isJobTujuanMoreThanOne;
    boolean isSingleBox;
    boolean isSudahAdaYangTerkirim;
    boolean isJob12;

    @SuppressLint("MissingPermission")
    public void setUpAll() {
        clearMapElement();


        if (realJob.getJobDeliverStatus() > 6) {
            bTolak.setText("Lapor");
            bTolak.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityLapor.start(ActivityProsesMap.this);
                }
            });
        } else {
            bTolak.setText("Tolak");
            bTolak.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityReject.start(ActivityProsesMap.this, simpleJob);
                }
            });
        }

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
        isJobTujuanMoreThanOne = realJob.fTotalDest() == 2;
        isSingleBox = realJob.fTotalBox() <= 1;
        isSudahAdaYangTerkirim = (isJobTujuanMoreThanOne && !isSingleBox) && realJob.getDetailkontainer().get(0).getJobStatus() == 10;
        isJob12 = realJob.getJobType() == 1 || realJob.getJobType() == 2;
        if (getSupportActionBar() != null)
            getSupportActionBar().setSubtitle(realJob.getStringJobTypeName()/* + "("+realJob.getJobType()+")"*/);

        tvOrderId.setText(realJob.getOrderId());
        tvFirst.setText(realJob.getJobPickupName());
        if (realJob.getDetailkontainer().size() > 0 && !isJob89) {
            tvSecond.setText(realJob.getDetailkontainer().get(0).getDestinationName());
            tvThird.setVisibility(View.GONE);
            if (realJob.getDetailkontainer().size() > 1 && realJob.getJumlahtujuan() == 2) {
                tvThird.setText(realJob.getDetailkontainer().get(1).getDestinationName());
                tvThird.setVisibility(View.VISIBLE);
            }
        } else if (isJob89) {
//            tvFirst.setText(realJob.getJobPickupName());
            if (realJob.getDetailkontainer().size() > 0) {
                tvSecond.setVisibility(View.VISIBLE);
                tvThird.setVisibility(View.VISIBLE);
            } else {
                tvSecond.setVisibility(View.GONE);
                tvThird.setVisibility(View.GONE);

            }
            tvSecond.setText(realJob.getJobDeliverAddress());
            tvThird.setText(realJob.getJobBalikAddress());
        } else {
//            tvFirst.setText(realJob.getJobPickupName());
            tvSecond.setText(realJob.getJobDeliverAddress());
            tvThird.setVisibility(View.GONE);
        }
        // setup container adapter
        lvContainer.setAdapter(new ContainerAdapter(this, R.layout.list_container, realJob.getDetailkontainer()));
        UIUtils.setListViewHeightBasedOnItems(lvContainer);
        promptUpload(realJob);
        // draw route from start to end
        if (gmap != null) {
            LocationServices.getFusedLocationProviderClient(this).getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    setUpLocation(location);
                    prepareButton();
                    MapColor mc = MapColor.BLUE;
                    String target = realJob.getJobPickupAddress();
                    if (isJob89) {
                        if (realJob.getJobDeliverStatus() > 7) {
                            target = realJob.getJobDeliverAddress();
                            mc = MapColor.GREEN;
                        } else if (realJob.getJobDeliverStatus() > 11) {
                            target = realJob.getJobBalikAddress();
                            mc = MapColor.MAGENTA;
                        }
                    } else {
                        if (realJob.getJobDeliverStatus() > 7) {
                            mc = MapColor.GREEN;
                            if (realJob.fTotalBox() == 1) {
                                target = realJob.getDetailkontainer().get(0).getDestinationName();
                            } else if (realJob.fTotalBox() == 2) {
                                if (realJob.statusKontainerStatus() == 0) {
                                    target = realJob.getDetailkontainer().get(0).getDestinationName();
                                } else if (realJob.statusKontainerStatus() == 1 || realJob.statusKontainerStatus() == 2) {
                                    target = realJob.getDetailkontainer().get(1).getDestinationName();
                                }
                            }
                        }
                    }
                    // drawing lines
                    drawRoute(location.getLatitude(), location.getLongitude(), nextTargetLat, nextTargetLng, mc, target, polyFence);
                    pdLoading.dismiss();
                }
            });
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
                setGeofenceTarget(realJob.getJobPickupLatitude(), realJob.getJobPickupLongitude());
                setGeofenceTarget(realJob.getGeofence_asal());
                setNextTarget(realJob.getJobPickupLatitude(), realJob.getJobPickupLongitude());
                break;
            }
            // status pickup
            case 4: {
                whatFunc = Netter.Webservice.JOB_READYJOB;
                enableGeofence = true;
                setGeofenceTarget(realJob.getJobPickupLatitude(), realJob.getJobPickupLongitude());
                setGeofenceTarget(realJob.getGeofence_asal());
                setNextTarget(realJob.getJobPickupLatitude(), realJob.getJobPickupLongitude());
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
                setNextTarget(realJob.getJobPickupLatitude(), realJob.getJobPickupLongitude());
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
                setNextTarget(realJob.getJobPickupLatitude(), realJob.getJobPickupLongitude());
                if (isJob12) {
                    vDestinationList.setVisibility(View.VISIBLE);
                    lvContainer.setVisibility(View.VISIBLE);
                }
                break;
            }
            case 7: {
                // deliver
                whatFunc = Netter.Webservice.JOB_DELIVERJOB;
                enableGeofence = false;
                enableContainerCheck = true;
                setGeofenceTarget(realJob.getJobPickupLatitude(), realJob.getJobPickupLongitude());
                setGeofenceTarget(realJob.getGeofence_asal());
                setNextTarget(realJob.getJobPickupLatitude(), realJob.getJobPickupLongitude());
                if (isJob12) {
                    vDestinationList.setVisibility(View.GONE);
//                    lvContainer.setVisibility(View.GONE);
                }

//                if (!isFinishFirst) {
//                    showUploadPrompt(REQ_FIRST);
//                }
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
                        setNextTarget(realJob.getDetailkontainer().get(0).getDestinationLat(), realJob.getDetailkontainer().get(0).getDestinationLng());
                    } else {
                        setGeofenceTarget(realJob.getDetailkontainer().get(1).getDestinationLat(), realJob.getDetailkontainer().get(1).getDestinationLng());
                        setGeofenceTarget(realJob.getDetailkontainer().get(1).getGeofence_tujuan());
                        setNextTarget(realJob.getDetailkontainer().get(1).getDestinationLat(), realJob.getDetailkontainer().get(1).getDestinationLng());
                    }
                } else {
                    setGeofenceTarget(realJob.getJobDeliverLatitude(), realJob.getJobDeliverLongitude());
                    setGeofenceTarget(realJob.getGeofence_tujuan());
                    setNextTarget(realJob.getJobDeliverLatitude(), realJob.getJobDeliverLongitude());
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
                        setNextTarget(realJob.getDetailkontainer().get(0).getDestinationLat(), realJob.getDetailkontainer().get(0).getDestinationLng());
                    } else {
                        setGeofenceTarget(realJob.getDetailkontainer().get(1).getDestinationLat(), realJob.getDetailkontainer().get(1).getDestinationLng());
                        setGeofenceTarget(realJob.getDetailkontainer().get(1).getGeofence_tujuan());
                        setNextTarget(realJob.getDetailkontainer().get(1).getDestinationLat(), realJob.getDetailkontainer().get(1).getDestinationLng());
                    }
                } else {
                    setGeofenceTarget(realJob.getJobDeliverLatitude(), realJob.getJobDeliverLongitude());
                    setGeofenceTarget(realJob.getGeofence_tujuan());
                    setNextTarget(realJob.getJobDeliverLatitude(), realJob.getJobDeliverLongitude());
                }

                break;
            }
            case 10: {
                whatFunc = Netter.Webservice.JOB_FINISHJOBARRIVAL;
               /* if (isJob89) {
                    // finish load
                    whatFunc = Netter.Webservice.JOB_FINISHJOBARRIVAL;
                } else {
                    // finish job
                    whatFunc = Netter.Webservice.JOB_FINISHJOB;
                }*/

                enableGeofence = true;
                enableContainerCheck = true;
                if (!isSingleBox && isJobTujuanMoreThanOne && !isJob89) {
                    if (realJob.statusKontainer() == 0) {
                        setGeofenceTarget(realJob.getDetailkontainer().get(0).getDestinationLat(), realJob.getDetailkontainer().get(0).getDestinationLng());
                        setGeofenceTarget(realJob.getDetailkontainer().get(0).getGeofence_tujuan());
                        setNextTarget(realJob.getDetailkontainer().get(0).getDestinationLat(), realJob.getDetailkontainer().get(0).getDestinationLng());
//                        if (!isFinishSecond) {
//                            showUploadPrompt(REQ_SECOND);
//                        }
                    } else {
                        setGeofenceTarget(realJob.getDetailkontainer().get(1).getDestinationLat(), realJob.getDetailkontainer().get(1).getDestinationLng());
                        setGeofenceTarget(realJob.getDetailkontainer().get(1).getGeofence_tujuan());
                        setNextTarget(realJob.getDetailkontainer().get(1).getDestinationLat(), realJob.getDetailkontainer().get(1).getDestinationLng());
//                        if (!isFinishThird) {
//                            showUploadPrompt(REQ_THIRD);
//                        }
                    }
                } else {
                    setGeofenceTarget(realJob.getJobDeliverLatitude(), realJob.getJobDeliverLongitude());
                    setGeofenceTarget(realJob.getGeofence_tujuan());
                    setNextTarget(realJob.getJobDeliverLatitude(), realJob.getJobDeliverLongitude());
//                    if (!isFinishSecond) {
//                        showUploadPrompt(REQ_SECOND);
//                    }
                }

                break;
            }
            case 11: {
                if (isJob89) {
                    whatFunc = Netter.Webservice.JOB_GOEMPTYDEPO;
                } else {
                    // finish job
                    whatFunc = Netter.Webservice.JOB_FINISHJOB;
                }
                break;
            }
            case 12: {
                if (isJob89) {
                    whatFunc = Netter.Webservice.JOB_UPLOADEMPTYDEPO;
                    enableGeofence = true;
                    setGeofenceTarget(realJob.getJobBalikLatitude(), realJob.getJobBalikLongitude());
                    setGeofenceTarget(realJob.getGeofence_balik());
                    setNextTarget(realJob.getJobBalikLatitude(), realJob.getJobBalikLongitude());
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
                    setNextTarget(realJob.getJobBalikLatitude(), realJob.getJobBalikLongitude());

//                    if (!isFinishOpt) {
//                        showUploadPrompt(REQ_OPT);
//                    }
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
        buttonSwitch();
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
//
//        gmap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
//            @Override
//            public void onMapLongClick(LatLng latLng) {
//                mockLocationTest(latLng);
//                setUpAll();
//            }
//        });

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
        if (geoFence == null) {
            CircleOptions circleOptions = new CircleOptions().center(latLng).fillColor(color.getRgbTransparent(0.2f)).strokeColor(Color.TRANSPARENT).radius(GEOFENCE_RADIUS);
            Circle circle = gmap.addCircle(circleOptions);
            circleList.add(circle);
        } else {
            PolygonOptions polygonOptions = new PolygonOptions().addAll(geoFence).fillColor(color.getRgbTransparent(0.5f)).strokeColor(Color.TRANSPARENT);
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
        if (!job123) {
            getMenuInflater().inflate(R.menu.menu_view_photo, menu);
        }
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
                if (location != null)
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
        if (debugGeofence) return;
        if (realJob == null) return;
        if (polyFence != null) {
            boolean isInEdge = PolyUtil.isLocationOnEdge(new LatLng(location.getLatitude(), location.getLongitude()), polyFence, true, GEOFENCE_RADIUS);
            boolean isInside = PolyUtil.containsLocation(new LatLng(location.getLatitude(), location.getLongitude()), polyFence, true);

            if (location.getProvider().equals("network")) return;
            if ((enableGeofence && !(isInside || isInEdge)) || (enableContainerCheck && !realJob.isContainerValid())) {
//            if (enableGeofence && (!(isInside || isInEdge) || (enableContainerCheck && !realJob.isContainerValid()))) {
                // disable button
                bTerima.setEnabled(false);
                bTerima.setBackgroundColor(colorInactive);
            } else {
                bTerima.setEnabled(true);
                bTerima.setBackgroundColor(colorActive);
            }
        } else {
            double distance = Haversine.calculate(geofenceLat, geofenceLng, location.getLatitude(), location.getLongitude());
            if (location.getProvider().equals("network")) return;
//            if ((enableGeofence && (distance > GEOFENCE_RADIUS || (enableContainerCheck && !realJob.isContainerValid())))) {
            if ((enableGeofence && distance > GEOFENCE_RADIUS) || (enableContainerCheck && !realJob.isContainerValid())) {
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

    private void showUploadPrompt(final int code) {
        new AlertDialog.Builder(this).setTitle("Upload foto")
                .setMessage("Silahkan unggah foto untuk melanjutkan")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityUpload.start(ActivityProsesMap.this, simpleJob, code);
                    }
                }).setCancelable(false)
                .show();
    }

    private void promptUpload(RealJob realJob) {
        int status = realJob.getJobDeliverStatus();
        switch (realJob.getJobType()) {
            case 4: {
                if (status == 7 && !isFinishFirst) showUploadPrompt(REQ_FIRST);
                if (status == 10 && !isFinishSecond) showUploadPrompt(REQ_SECOND);
                break;
            }
            case 5: {
                if (status == 7 && !isFinishFirst) showUploadPrompt(REQ_FIRST);
//                if (status == 10 && !isFinishSecond) showUploadPrompt(REQ_SECOND);
                break;
            }
            case 6: {
                if (status == 7 && !isFinishFirst) showUploadPrompt(REQ_FIRST);
//                if (status == 10 && !isFinishSecond) showUploadPrompt(REQ_SECOND);
                break;
            }
            case 8: {
                if (status == 7 && !isFinishFirst) showUploadPrompt(REQ_FIRST);
                if (status == 10 && !isFinishSecond) showUploadPrompt(REQ_SECOND);
                break;
            }
            case 9: {
                if (status == 9 && !isFinishSecond) showUploadPrompt(REQ_FIRST);
                if (status == 11 && !isFinishSecond) showUploadPrompt(REQ_SECOND);
                break;
            }
        }
    }
}
