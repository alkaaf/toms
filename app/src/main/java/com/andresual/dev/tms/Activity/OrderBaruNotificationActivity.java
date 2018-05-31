package com.andresual.dev.tms.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.andresual.dev.tms.Activity.Controller.NotificationController;
import com.andresual.dev.tms.Activity.Model.JobOrder2Model;
import com.andresual.dev.tms.Activity.Model.JobOrderModel;
import com.andresual.dev.tms.Activity.Notification.JemputNotificationActivity;
import com.andresual.dev.tms.Activity.Util.FcmMessagingService;
import com.andresual.dev.tms.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.messaging.FirebaseMessagingService;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.andresual.dev.tms.Activity.Util.FcmMessagingService.id;

public class OrderBaruNotificationActivity extends AppCompatActivity {

    ArrayList<JobOrder2Model> jobOrder2ModelArrayList;
    JobOrder2Model jobOrder2Model;
    TextView tvOrderNo, tvPelanggan, tvTanggal, tvEstimasiJarak, tvEstimasiWaktu, tvOrigin, tvContSize, tvContType, tvComodity;

    //////////////////////////////getter setter////////////////////////////////////////////////////


    public ArrayList<JobOrder2Model> getJobOrder2ModelArrayList() {
        return jobOrder2ModelArrayList;
    }

    public void setJobOrder2ModelArrayList(ArrayList<JobOrder2Model> jobOrder2ModelArrayList) {
        this.jobOrder2ModelArrayList = jobOrder2ModelArrayList;
    }

    public JobOrder2Model getJobOrder2Model() {
        return jobOrder2Model;
    }

    public void setJobOrder2Model(JobOrder2Model jobOrder2Model) {
        this.jobOrder2Model = jobOrder2Model;
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
                                    JobOrder2Model jobOrder2Model = new JobOrder2Model();
                                    jobOrder2Model.setOrderNo(data.getString("order_id"));
                                    tvOrderNo.setText(jobOrder2Model.getOrderNo());
                                    jobOrder2Model.setTanggal(data.getString("job_blast"));
                                    jobOrder2Model.setContainerNo(data.getString("container_no"));
                                    tvContType.setText(jobOrder2Model.getContainerNo());
                                    jobOrder2Model.setContainerName(data.getString("container_name"));
                                    tvContType.setText(jobOrder2Model.getContainerName());
                                    jobOrder2Model.setComodity(data.getString("container_cargo_name"));
                                    tvComodity.setText(jobOrder2Model.getComodity());
                                    jobOrder2Model.setJobName(data.getString("job_pickup_name"));
                                    tvPelanggan.setText(jobOrder2Model.getJobName());
                                    jobOrder2Model.setOrigin(data.getString("job_pickup_address"));
                                    tvOrigin.setText(jobOrder2Model.getOrigin());
                                    jobOrder2Model.setDestination(data.getString("job_deliver_address"));
                                    jobOrder2Model.setJobId(data.getInt("id"));
                                    Log.i("onResponse:", jobOrder2Model.getJobId().toString());
//                                    jobOrder2ModelArrayList.add(jobOrder2Model);
//                                    Log.i("array", jobOrder2ModelArrayList.toString());
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
