package com.andresual.dev.tms.Activity;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.andresual.dev.tms.Activity.Manager.SessionKendaraan;
import com.andresual.dev.tms.Activity.Manager.SessionManager;
import com.andresual.dev.tms.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FinishStuffPickupActivity extends FragmentActivity {

    Integer jobId;
    String pickLat, pickLong;
    SessionManager sessionManager;
    SessionKendaraan sessionKendaraan;
    String idDriver, email, idKendaraan;
    String delivLat, delivLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_stuff_pickup);

        Button btnFinishStuff = findViewById(R.id.btn_finish_to_stuff_pickup);
        Button btnTolak = findViewById(R.id.btn_tolak);

        sessionManager = new SessionManager(getApplicationContext());
        sessionKendaraan = new SessionKendaraan(getApplicationContext());

        sessionKendaraan = new SessionKendaraan(this);
        HashMap<String, String> data = sessionKendaraan.getKendaraanDetails();
        idKendaraan = data.get(SessionKendaraan.ID_KENDARAAN);

        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetails();
        idDriver = user.get(SessionManager.ID_DRIVER);
        email = user.get(SessionManager.EMAIL_DRIVER);

        jobId = this.getIntent().getIntExtra("jobId", 0);
        pickLat = this.getIntent().getStringExtra("latitude");
        pickLong = this.getIntent().getStringExtra("longitude");
        delivLat = this.getIntent().getStringExtra("delivLat");
        delivLng = this.getIntent().getStringExtra("delivLng");

        btnFinishStuff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finishLoading();
            }
        });
    }

    public void finishLoading() {
        final Map<String, String> params = new HashMap<>();
        params.put("f", "deliverjob");
        params.put("email", email);
        params.put("idjob", jobId.toString());
        Log.i("jemputjob", params.toString());

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(Request.Method.POST, "http://manajemenkendaraan.com/tms/webservice.asp",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("response", response);
                        try {
                            JSONObject obj = new JSONObject(response);
                            Log.i("status", obj.getString("status"));
                            Log.i("messages", obj.getString("message"));

                            Intent intent = new Intent(FinishStuffPickupActivity.this, MengantarActivity.class);
                            intent.putExtra("jobId", jobId);
                            intent.putExtra("latitude", pickLat);
                            intent.putExtra("longitude", pickLong);
                            intent.putExtra("delivLat", delivLat);
                            intent.putExtra("delivLng", delivLng);
                            startActivity(intent);
                            finish();
                        } catch (Throwable t) {
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
