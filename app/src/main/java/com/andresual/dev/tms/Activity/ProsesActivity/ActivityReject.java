package com.andresual.dev.tms.Activity.ProsesActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.andresual.dev.tms.Activity.BaseActivity;
import com.andresual.dev.tms.Activity.Model.DriverModel;
import com.andresual.dev.tms.Activity.Model.KendaraanModel;
import com.andresual.dev.tms.Activity.Model.SimpleJob;
import com.andresual.dev.tms.Activity.Util.Netter;
import com.andresual.dev.tms.Activity.Util.Pref;
import com.andresual.dev.tms.Activity.Util.StringHashMap;
import com.andresual.dev.tms.R;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityReject extends BaseActivity {
    StringHashMap map = new StringHashMap();
    @BindView(R.id.rg)
    RadioGroup rg;
    @BindView(R.id.btn_kirim_alasan)
    Button bKirim;

    DriverModel driver;
    KendaraanModel kendaraan;
    Pref pref;
    String alasan;
    SimpleJob simpleJob;

    public static final String INTENT_DATA = "data.job.reject";
    public static final int REQ_CODE = 19;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tolak_order);
        ButterKnife.bind(this);
        pref = new Pref(this);
        driver = pref.getDriverModel();
        kendaraan = pref.getKendaraan();
        simpleJob = getIntent().getParcelableExtra(INTENT_DATA);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                alasan = ((RadioButton) findViewById(checkedId)).getText().toString();
            }
        });
        pd = new ProgressDialog(this);
        pd.setMessage("Menolak pekerjaan");

        bKirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(ActivityReject.this).setMessage("Apakah anda ingin menolak pekerjaan ini dengan alasan " + alasan + "?").setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        reject();
                    }
                })
                        .setNegativeButton("TIDAK", null)
                        .show();
            }
        });

    }

    ProgressDialog pd;

    public void reject() {
        if (TextUtils.isEmpty(alasan)) {
            Toast.makeText(this, "Alasan belum dipilih", Toast.LENGTH_SHORT).show();
            return;
        }
        map.putMore("email", driver.getEmail()).putMore("idjob", simpleJob.getJobId()).putMore("rejectnote", alasan);
        pd.show();
        new Netter(this).webService(Request.Method.POST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                try {
                    JSONObject obj = new JSONObject(response);
                    toast(obj.getString("message"));
                    if (obj.getInt("status") == 200) {
                        setResult(Activity.RESULT_OK);
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
            }
        }, Netter.Webservice.REJECT, map);
    }

    public static void start(Activity context, SimpleJob simpleJob) {
        Intent intent = new Intent(context, ActivityReject.class);
        intent.putExtra(INTENT_DATA, simpleJob);
        context.startActivityForResult(intent, REQ_CODE);
    }
}