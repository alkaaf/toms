package com.spil.dev.tms.Activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.spil.dev.tms.Activity.Adapter.BerandaListAdapter;
import com.spil.dev.tms.Activity.Adapter.PilihKendaraanAdapter;
import com.spil.dev.tms.Activity.Controller.AuthController;
import com.spil.dev.tms.Activity.Controller.KendaraanController;
import com.spil.dev.tms.Activity.Manager.SessionManager;
import com.spil.dev.tms.Activity.Model.DriverModel;
import com.spil.dev.tms.Activity.Model.KendaraanModel;
import com.spil.dev.tms.Activity.Util.Netter;
import com.spil.dev.tms.Activity.Util.Pref;
import com.spil.dev.tms.Activity.Util.StringHashMap;
import com.spil.dev.tms.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PilihKendaraanActivity extends BaseActivity {

    RecyclerView rvKendaraan;
    SwipeRefreshLayout swipe;

    PilihKendaraanAdapter mAdapter;
    ArrayList<KendaraanModel> kendaraanList = new ArrayList<>();
    SessionManager sessionManager;

    DriverModel driverModel;
    Pref pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pilih_kendaraan);

        mAdapter = new PilihKendaraanAdapter(this, kendaraanList);
        rvKendaraan = findViewById(R.id.rv_pilih_kendaran);
        swipe = findViewById(R.id.swipe);
        rvKendaraan.setLayoutManager(new LinearLayoutManager(this));
        rvKendaraan.setAdapter(mAdapter);
        rvKendaraan.setHasFixedSize(true);

        pref = new Pref(this);


        driverModel = pref.getDriverModel();
        if(driverModel!=null) {
            fetchKendaraan();
        }
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(driverModel!=null) {
                    fetchKendaraan();
                }
            }
        });

        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    public void fetchKendaraan() {
        swipe.setRefreshing(true);
        new Netter(this).webService(Request.Method.POST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                swipe.setRefreshing(false);
                try {
                    JSONObject obj = new JSONObject(response);
                    int status = obj.getInt("status");
                    String message = obj.getString("message");
                    if (status == 200) {
                        ArrayList<KendaraanModel> temp = new Gson().fromJson(obj.getString("data"), new TypeToken<ArrayList<KendaraanModel>>(){}.getType());
                        kendaraanList.clear();
                        kendaraanList.addAll(temp);
                        mAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, Netter.getDefaultErrorListener(this, new Runnable() {
            @Override
            public void run() {
                swipe.setRefreshing(false);
            }
        }), Netter.Webservice.GETLISTKENDARAANDRIVER, new StringHashMap().putMore("id_driver", driverModel.getIdDriver()));
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (pref.checkKendaraan()) {
            startActivity(new Intent(this, DashboardActivity.class));
            finish();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_logout, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_logout){
            new AlertDialog.Builder(this)
                    .setTitle("Keluar aplikasi")
                    .setMessage("Apakah anda yakin ingin keluar aplikasi?")
                    .setPositiveButton("YA", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            logOut();
                        }
                    })
                    .setNegativeButton("TIDAK", null).show();
        }
        return super.onOptionsItemSelected(item);
    }

    public void logOut() {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.show();
        StringHashMap shm = new StringHashMap()
                .putMore("id_kendaraan", "0")
                .putMore("id_driver", driverModel.getIdDriver())
                .putMore("email", driverModel.getEmail())                ;
        new Netter(this).byAmik(Request.Method.POST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                try {
                    JSONObject obj = new JSONObject(response);
                    int status = obj.getInt("status");
                    String message = obj.getString("message");
                    Toast.makeText(PilihKendaraanActivity.this, message, Toast.LENGTH_SHORT).show();
                    if(status == 200){
                        pref.clearDriver();
                        pref.clearKendaraan();
                        startActivity(new Intent(PilihKendaraanActivity.this,MainActivity.class));
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, Netter.getDefaultErrorListener(PilihKendaraanActivity.this, new Runnable() {
            @Override
            public void run() {
                pd.dismiss();
            }
        }), Netter.Byamik.GETLOGOUT, shm);
    }

}