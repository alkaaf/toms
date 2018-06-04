package com.andresual.dev.tms.Activity.ProsesActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
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

    public static final String INTENT_DATA = "datajob1-2";
    Pref pref;
    DriverModel driverModel;
    KendaraanModel kendaraanModel;
    SimpleJob job;
    RealJob realJob;
    SupportMapFragment mapFragment;
    GoogleMap gmap;
    ProgressDialog pd;

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
        pd.setMessage("Memuat data");

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

        setContent();

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
                    tvEstimasiWaktu.setText(realJob.getJobDeliverFinishtime());
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
}
