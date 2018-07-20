package com.spil.dev.tms.Activity.ProsesActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.spil.dev.tms.Activity.BaseActivity;
import com.spil.dev.tms.Activity.ListDepoActivity;
import com.spil.dev.tms.Activity.ListDermagaActivity;
import com.spil.dev.tms.Activity.Maps.LocationBroadcaster;
import com.spil.dev.tms.Activity.Model.DepoModel;
import com.spil.dev.tms.Activity.Model.DermagaModel;
import com.spil.dev.tms.Activity.Model.SimpleJob;
import com.spil.dev.tms.Activity.Util.Netter;
import com.spil.dev.tms.Activity.Util.Pref;
import com.spil.dev.tms.Activity.Util.StringHashMap;
import com.spil.dev.tms.R;

import org.json.JSONException;
import org.json.JSONObject;

public class ActivityLapor extends BaseActivity {
    RadioGroup rg1;
    RadioGroup rg2;
    RadioButton rbDepo;
    RadioButton rbDermaga;
    EditText etAlasan;
    Button btnKirimLaporan;
    TextView tvTarget;
    private static final int LOCATION_REQUEST_CODE = 101;
    StringHashMap map = new StringHashMap();
    public static final int REQ_CODE = 10;
    public static final int REQ_DEPO = 23;
    public static final int REQ_DERMAGA = 64;
    ProgressDialog pd;
    IntentFilter filter;
    Location loc;
    LocationManager lm;
    BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(LocationBroadcaster.LOCATION_BROADCAST_ACTION)) {
                loc = intent.getParcelableExtra(LocationBroadcaster.LOCATION_DATA);
            }
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        registerReceiver(br, filter);
    }

    @Override
    public void onStop() {
        super.onStop();
        unregisterReceiver(br);
    }

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_reject);
        filter = new IntentFilter(LocationBroadcaster.LOCATION_BROADCAST_ACTION);
        pd = new ProgressDialog(this);
        pd.setMessage("Mengirim laporan");
        getSupportActionBar().setTitle("Laporkan keadaan");

        rg1 = findViewById(R.id.rg1);
        rg2 = findViewById(R.id.rg2);
        rbDepo = findViewById(R.id.rb_depo);
        rbDermaga = findViewById(R.id.rb_dermaga);
        tvTarget = findViewById(R.id.tv_target);
        etAlasan = findViewById(R.id.et_alasan);
        btnKirimLaporan = findViewById(R.id.btn_kirim_laporan);
        tvTarget.setVisibility(View.GONE);
        lm = ((LocationManager) getSystemService(Context.LOCATION_SERVICE));
        lm.requestSingleUpdate(LocationManager.GPS_PROVIDER, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                ActivityLapor.this.loc = location;
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        }, null);
//        rg1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                if (checkedId == R.id.rb_depo) {
//                    startActivityForResult(new Intent(getContext(), ListDepoActivity.class), REQ_DEPO);
//
//                } else if (checkedId == R.id.rb_dermaga) {
//                    startActivityForResult(new Intent(getContext(), ListDermagaActivity.class), REQ_DERMAGA);
//                }
//            }
//        });
        rbDepo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(ActivityLapor.this, ListDepoActivity.class), REQ_DEPO);
            }
        });
        rbDermaga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(ActivityLapor.this, ListDermagaActivity.class), REQ_DERMAGA);
            }
        });

        rg2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb6) {
                    etAlasan.setVisibility(View.VISIBLE);
                } else {
                    etAlasan.setVisibility(View.GONE);
                }
            }
        });

        btnKirimLaporan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kirimLaporan();
            }
        });
        map.putMore("email", new Pref(ActivityLapor.this).getDriverModel().getEmail());

        LocationServices.getFusedLocationProviderClient(ActivityLapor.this).getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                loc = location;
            }
        });
        resetUI();
    }

    public void resetUI() {
        rg1.clearCheck();
        rg2.clearCheck();
        map.remove("tipelokasi");
        map.remove("idlokasi");
        tvTarget.setText("");
        tvTarget.setVisibility(View.GONE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_DEPO && resultCode == Activity.RESULT_OK) {
            DepoModel depoModel = data.getParcelableExtra(ListDepoActivity.INTENT_DATA);
            tvTarget.setVisibility(View.VISIBLE);
            tvTarget.setText(depoModel.getNama());
            map.putMore("tipelokasi", "1");
            map.putMore("idlokasi", depoModel.getId());
        }

        if (requestCode == REQ_DERMAGA && resultCode == Activity.RESULT_OK) {
            DermagaModel dermagaModel = data.getParcelableExtra(ListDermagaActivity.INTENT_DATA);
            tvTarget.setVisibility(View.VISIBLE);
            tvTarget.setText(dermagaModel.getNama());
            map.putMore("tipelokasi", "2");
            map.putMore("idlokasi", dermagaModel.getId());
        }
    }

    public void kirimLaporan() {
        if (loc != null) {
            map.putMore("latitude", Double.toString(loc.getLatitude()));
            map.putMore("longitude", Double.toString(loc.getLongitude()));
        } else {
            Toast.makeText(this, "Lokasi anda belum ditemukan, tunggu beberapa saat lagi", Toast.LENGTH_SHORT).show();
            return;
        }
        if (map.get("idlokasi") == null) {
            Toast.makeText(this, "Belum ada Depo/Dermaga yang ditentukan", Toast.LENGTH_SHORT).show();
            return;
        }
        if (rg2.getCheckedRadioButtonId() == R.id.rb6) {
            map.putMore("report_note", etAlasan.getText().toString());
        } else {
            map.putMore("report_note", ((RadioButton) findViewById(rg2.getCheckedRadioButtonId())).getText().toString());
        }
        pd.show();
        new Netter(this).webService(Request.Method.POST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                try {
                    JSONObject obj = new JSONObject(response);
                    Toast.makeText(ActivityLapor.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                    resetUI();
                    if(obj.getInt("status") == 200){
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, Netter.getDefaultErrorListener(ActivityLapor.this, new Runnable() {
            @Override
            public void run() {
                pd.dismiss();
            }
        }), Netter.Webservice.REPORT, map);
    }
    public static void start(Activity context) {
        Intent intent = new Intent(context, ActivityLapor.class);
//        intent.putExtra(INTENT_DATA, simpleJob);
        context.startActivityForResult(intent, REQ_CODE);
    }
}
