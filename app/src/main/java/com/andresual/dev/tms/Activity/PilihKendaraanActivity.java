package com.andresual.dev.tms.Activity;

import android.app.ProgressDialog;
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
import com.andresual.dev.tms.Activity.Model.KendaraanModel;
import com.andresual.dev.tms.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PilihKendaraanActivity extends AppCompatActivity {

    RecyclerView rvKendaraan;
    List<KendaraanModel> kendaraanModelList;
    PilihKendaraanAdapter mAdapter;
    ArrayList<KendaraanModel> kendaraanModelArrayList = new ArrayList<>();
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pilih_kendaraan);

        fetchKendaraan(this);

        rvKendaraan = findViewById(R.id.rv_pilih_kendaran);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvKendaraan.setLayoutManager(mLayoutManager);
        rvKendaraan.setHasFixedSize(true);

//        kendaraanModelArrayList = new ArrayList<>();
//        mAdapter = new PilihKendaraanAdapter(PilihKendaraanActivity.this, kendaraanModelList);
//        rvKendaraan.setItemAnimator(new DefaultItemAnimator());
//        rvKendaraan.setAdapter(mAdapter);
//        mAdapter.notifyDataSetChanged();

//        KendaraanController.getmInstance().fetchKendaraan(this);
    }

    public void fetchKendaraan(PilihKendaraanActivity pilihKendaraanActivity) {

        final ProgressDialog progressDialog = new ProgressDialog(pilihKendaraanActivity);
        progressDialog.setMessage("Loading data...");
        progressDialog.show();

        sessionManager = new SessionManager(this.getApplicationContext());
        HashMap<String, String> user = sessionManager.getUserDetails();
        String id = user.get(SessionManager.ID_DRIVER);

        final HashMap<String, String> params = new HashMap<>();
        params.put("f", "getListKendaraanDriver");
        params.put("iddriver", id);
        Log.i("fetchKendaraan", params.toString());

        RequestQueue queue = Volley.newRequestQueue(pilihKendaraanActivity);
        StringRequest sr = new StringRequest(Request.Method.POST, "http://manajemenkendaraan.com/tms/webservice.asp",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("RESPONSE", response);
                        progressDialog.dismiss();
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray kendaraanArray = obj.getJSONArray("data");
                            kendaraanModelArrayList = new ArrayList<>();
                            for (int i= 0;i < kendaraanArray.length(); i++) {
                                JSONObject hasil = kendaraanArray.getJSONObject(i);
                                Log.i("haha", hasil.toString());
                                KendaraanModel kendaraanModel = new KendaraanModel();
//                                kendaraanModel.setIdDriver(hasil.getString("id_driver"));
                                kendaraanModel.setIdNopol(hasil.getString("nopol"));
                                kendaraanModel.setIdKendaraan(hasil.getString("idkendaraan"));
                                kendaraanModelArrayList.add(kendaraanModel);
                                Log.i("idkendaraann", kendaraanModel.getIdKendaraan());

                                mAdapter = new PilihKendaraanAdapter(PilihKendaraanActivity.this, kendaraanModelArrayList);
                                rvKendaraan.setAdapter(mAdapter);
                                mAdapter.notifyDataSetChanged();
                            }
                        } catch (Throwable t){
                            Log.i("tms", "Could not parse malformed JSON: \"" + response + "\"");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String,String> getParams(){
                return params;
            }
        };
        queue.add(sr);
    }
}
