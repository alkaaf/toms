package com.spil.dev.tms.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.spil.dev.tms.Activity.Controller.NotificationController;
import com.spil.dev.tms.Activity.Model.SimpleJob;
import com.spil.dev.tms.Activity.Notification.JemputNotificationActivity;
import com.spil.dev.tms.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class OrderBaruNotificationActivity extends AppCompatActivity {

    ArrayList<SimpleJob> simpleJobArrayList;
    SimpleJob simpleJob;
    TextView tvOrderNo, tvPelanggan, tvTanggal, tvEstimasiJarak, tvEstimasiWaktu, tvOrigin, tvContSize, tvContType, tvComodity;
    String id;
    //////////////////////////////getter setter////////////////////////////////////////////////////


    public ArrayList<SimpleJob> getSimpleJobArrayList() {
        return simpleJobArrayList;
    }

    public void setSimpleJobArrayList(ArrayList<SimpleJob> simpleJobArrayList) {
        this.simpleJobArrayList = simpleJobArrayList;
    }

    public SimpleJob getSimpleJob() {
        return simpleJob;
    }

    public void setSimpleJob(SimpleJob simpleJob) {
        this.simpleJob = simpleJob;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_baru_notification);

        jobNotif();

        tvOrderNo = findViewById(R.id.tv_order_no1);
        tvPelanggan = findViewById(R.id.tv_pelanggan1);
        tvTanggal = findViewById(R.id.tv_waktu);
        tvEstimasiJarak = findViewById(R.id.tv_estimasi_jarak1);
        tvEstimasiWaktu = findViewById(R.id.tv_estimasi_waktu1);
        tvOrigin = findViewById(R.id.tv_origin);
        tvContSize = findViewById(R.id.tv_container_size);
        tvContType = findViewById(R.id.container_type);
        tvComodity = findViewById(R.id.tv_comodity);

        Button btnTerims = findViewById(R.id.btn_terima);
        btnTerims.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotificationController.getmInstance().acceptJob(OrderBaruNotificationActivity.this);
                Intent intent = new Intent(OrderBaruNotificationActivity.this, JemputNotificationActivity.class);
                startActivity(intent);
            }
        });
    }

    public void jobNotif() {

        final Map<String, String> params = new HashMap<>();
        params.put("f", "getDetailJobOrder");
        params.put("id", id);
        Log.i("job", params.toString());

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(Request.Method.POST, "http://manajemenkendaraan.com/tms/webservice.asp",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("RESPONSE", response);
                        try {
                            JSONObject obj = new JSONObject(response);
                            Iterator keys = obj.keys();
                            Log.i("test", keys.toString());
                            while (keys.hasNext()) {
                                String oke = (String) keys.next();
                                Log.i("oke", oke);

                                JSONObject data = obj.getJSONObject(oke);
                                Log.i("data", data.toString());
//                                jobOrderModelArrayList = new ArrayList<>();
                                for (int i = 0; i < data.length(); i++) {
                                    SimpleJob simpleJob = new SimpleJob();
                                    simpleJob.setOrderNo(data.getString("order_id"));
                                    tvOrderNo.setText(simpleJob.getOrderNo());
                                    simpleJob.setTanggal(data.getString("job_blast"));
                                    simpleJob.setContainerNo(data.getString("container_no"));
                                    tvContType.setText(simpleJob.getContainerNo());
                                    simpleJob.setContainerName(data.getString("container_name"));
                                    tvContType.setText(simpleJob.getContainerName());
                                    simpleJob.setComodity(data.getString("container_cargo_name"));
                                    tvComodity.setText(simpleJob.getComodity());
                                    simpleJob.setJobName(data.getString("job_pickup_name"));
                                    tvPelanggan.setText(simpleJob.getJobName());
                                    simpleJob.setOrigin(data.getString("job_pickup_address"));
                                    tvOrigin.setText(simpleJob.getOrigin());
                                    simpleJob.setDestination(data.getString("job_deliver_address"));
                                    simpleJob.setJobId(data.getInt("id"));
                                    Log.i("onResponse:", simpleJob.getJobId().toString());
//                                    simpleJobArrayList.add(simpleJob);
//                                    Log.i("array", simpleJobArrayList.toString());
//                                    Log.i("onResponse:", jobOrderModelArrayList.toString());
                                }
                            }

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
            protected Map<String, String> getParams() {
                return params;
            }
        };
        queue.add(sr);

    }
}
