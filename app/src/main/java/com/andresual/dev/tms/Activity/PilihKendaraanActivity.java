package com.andresual.dev.tms.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.andresual.dev.tms.Activity.Adapter.BerandaListAdapter;
import com.andresual.dev.tms.Activity.Adapter.PilihKendaraanAdapter;
import com.andresual.dev.tms.Activity.Controller.AuthController;
import com.andresual.dev.tms.Activity.Controller.KendaraanController;
import com.andresual.dev.tms.Activity.Manager.SessionManager;
import com.andresual.dev.tms.Activity.Model.DriverModel;
import com.andresual.dev.tms.Activity.Model.KendaraanModel;
import com.andresual.dev.tms.Activity.Util.Netter;
import com.andresual.dev.tms.Activity.Util.Pref;
import com.andresual.dev.tms.Activity.Util.StringHashMap;
import com.andresual.dev.tms.R;
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

        if (pref.checkKendaraan()) {
            startActivity(new Intent(this, DashboardActivity.class));
            finish();
        }

        driverModel = pref.getDriverModel();
        fetchKendaraan();
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchKendaraan();
            }
        });
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
}