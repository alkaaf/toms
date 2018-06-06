package com.andresual.dev.tms.Activity.ProsesActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.andresual.dev.tms.Activity.ActivityUpload;
import com.andresual.dev.tms.Activity.BaseActivity;
import com.andresual.dev.tms.Activity.Model.DriverModel;
import com.andresual.dev.tms.Activity.Model.RealJob;
import com.andresual.dev.tms.Activity.Model.SimpleJob;
import com.andresual.dev.tms.Activity.Model.KendaraanModel;
import com.andresual.dev.tms.Activity.Util.Netter;
import com.andresual.dev.tms.Activity.Util.Pref;
import com.andresual.dev.tms.Activity.Util.StringHashMap;
import com.andresual.dev.tms.R;
import com.android.volley.Request;
import com.android.volley.Response;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityProses1And2 extends BaseActivity {
    @BindView(R.id.tvOrderId)
    TextView tvOrderId;
    @BindView(R.id.tvJobPickupName)
    TextView tvJobPickupName;
    @BindView(R.id.jobDesc)
    TextView tvJobDesc;
    @BindView(R.id.tvTanggal)
    TextView tvTanggal;
    @BindView(R.id.tvEstimasiJarak)
    TextView tvEstimasiJarak;
    @BindView(R.id.tvEstimasiWaktu)
    TextView tvEstimasiWaktu;
    @BindView(R.id.btn_terima)
    Button bTerima;

    public static final String INTENT_DATA = "datajob1-2";
    public static final String PREVIEW_ONLY = "justpreview";

    Pref pref;
    DriverModel driverModel;
    KendaraanModel kendaraanModel;
    SimpleJob job;
    RealJob realJob;
    SupportMapFragment mapFragment;
    GoogleMap gmap;
    ProgressDialog pd;
    Context context;
    ProgressDialog pdAccept;
    boolean isPreview;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proses_1_2);
        ButterKnife.bind(this);
        pref = new Pref(this);
        driverModel = pref.getDriverModel();
        kendaraanModel = pref.getKendaraan();
        job = getIntent().getParcelableExtra(INTENT_DATA);
        pd = new ProgressDialog(this);
        pdAccept = new ProgressDialog(this);
        pdAccept.setMessage("Menerima job");
        pd.setMessage("Memuat data");
        context = this;
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @SuppressLint("MissingPermission")
            @Override
            public void onMapReady(GoogleMap googleMap) {
                gmap = googleMap;
                gmap.setMyLocationEnabled(true);
                gmap.getUiSettings().setCompassEnabled(true);
                gmap.getUiSettings().setZoomControlsEnabled(true);
            }
        });

        isPreview = getIntent().getBooleanExtra(PREVIEW_ONLY, false);
        if (job.getJobDeliverStatus() >= 3 && !isPreview) {
            ActivityProsesMap.start(this, job);
            finish();
        }
        setContent();
        findViewById(R.id.llBottom).setVisibility(isPreview ? View.GONE : View.VISIBLE);
        bTerima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(ActivityProses1And2.this)
                        .setTitle("Terima Job")
                        .setMessage("Apakah anda ingin menerima job ini?")
                        .setPositiveButton("YA", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                accept();
                            }
                        })
                        .setNegativeButton("TIDAK", null)
                        .show();
            }
        });
    }

    @SuppressLint("MissingPermission")
    public void accept() {
        LocationServices.getFusedLocationProviderClient(this).getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                pdAccept.show();
                new Netter(context).webService(Request.Method.POST, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                pdAccept.dismiss();
                                try {
                                    JSONObject obj = new JSONObject(response);
                                    toast(obj.getString("message"));
                                    if (obj.getInt("status") == 200) {
                                        ActivityProsesMap.start(context, job);
                                        finish();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, Netter.getDefaultErrorListener(context, new Runnable() {
                            @Override
                            public void run() {
                                pdAccept.dismiss();
                            }
                        }), Netter.Webservice.JOB_ACCEPT, new StringHashMap()
                                .putMore("idjob", Integer.toString(job.getJobId()))
                                .putMore("email", driverModel.getEmail())
                                .putMore("lat", Double.toString(location.getLatitude()))
                                .putMore("lng", Double.toString(location.getLongitude()))
                );
            }
        });
    }

    public void setContent() {
        pd.show();
        new Netter(this).webService(Request.Method.POST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                try {
                    JSONObject obj = new JSONObject(response);
                    realJob = new Gson().fromJson(obj.getString("job-" + job.getJobId()), RealJob.class);
                    tvJobDesc.setText(realJob.getJobDescription());
                    tvJobPickupName.setText(realJob.getJobPickupAddress());
                    tvEstimasiJarak.setText(realJob.getJobDeliverDistancetext());
                    tvEstimasiWaktu.setText(realJob.getJobDeliverEstimatetimetext());
                    tvOrderId.setText(realJob.getOrderId());
                    tvTanggal.setText(realJob.getJobPickupDatetime());
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(ActivityProses1And2.this, "Gagal memuat data", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }, Netter.getDefaultErrorListener(this, new Runnable() {
            @Override
            public void run() {
                pd.dismiss();
                Toast.makeText(ActivityProses1And2.this, "Gagal memuat data", Toast.LENGTH_SHORT).show();
                finish();
            }
        }), Netter.Webservice.DETAILPICKUP, new StringHashMap().putMore("id", Integer.toString(job.getJobId())));
    }

    public static void startProses(Context context, SimpleJob simpleJob) {
        Intent intent = new Intent(context, ActivityProses1And2.class);
        intent.putExtra(INTENT_DATA, simpleJob);
        intent.putExtra(PREVIEW_ONLY, false);
        context.startActivity(intent);
    }

    public static void startPreview(Context context, SimpleJob simpleJob) {
        Intent intent = new Intent(context, ActivityProses1And2.class);
        intent.putExtra(INTENT_DATA, simpleJob);
        intent.putExtra(PREVIEW_ONLY, true);
        context.startActivity(intent);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_view_photo, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_view_photo && isPreview ) {
            ActivityUpload.start(this, job);
        }
        return super.onOptionsItemSelected(item);
    }
}
