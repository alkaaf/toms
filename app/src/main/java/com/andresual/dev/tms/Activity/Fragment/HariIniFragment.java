package com.andresual.dev.tms.Activity.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.andresual.dev.tms.Activity.Adapter.BerandaListAdapter;
import com.andresual.dev.tms.Activity.Adapter.HariIniListAdapter;
import com.andresual.dev.tms.Activity.Manager.SessionManager;
import com.andresual.dev.tms.Activity.MapsOrderActivity;
import com.andresual.dev.tms.Activity.Model.JobOrder2Model;
import com.andresual.dev.tms.Activity.OrderBaruActivity;
import com.andresual.dev.tms.Activity.RiwayatScheduleActivity;
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
import java.util.Map;
import java.util.concurrent.RecursiveTask;

public class HariIniFragment extends Fragment {

    ImageView ivCoba;
    SessionManager sessionManager;
    RecyclerView rvHariIni;
    HariIniListAdapter mAdapter;
    RelativeLayout clEmptyView;

    ArrayList<JobOrder2Model> jobOrder2ModelArrayList = new ArrayList<>();

    //GETTER SETTER//

    public ArrayList<JobOrder2Model> getJobOrder2ModelArrayList() {
        return jobOrder2ModelArrayList;
    }

    public void setJobOrder2ModelArrayList(ArrayList<JobOrder2Model> jobOrder2ModelArrayList) {
        this.jobOrder2ModelArrayList = jobOrder2ModelArrayList;
    }

    /////////////////

    public HariIniFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_hari_ini, container, false);

        fetchHariIni(this);

        clEmptyView = view.findViewById(R.id.cl2);
        rvHariIni = view.findViewById(R.id.rv1);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvHariIni.setLayoutManager(mLayoutManager);
        rvHariIni.setHasFixedSize(true);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void fetchHariIni(final HariIniFragment hariIniFragment) {

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading data...");
        progressDialog.show();

        sessionManager = new SessionManager(getActivity().getApplicationContext());
        HashMap<String, String> user = sessionManager.getUserDetails();
        String token = user.get(SessionManager.TOKEN_KEY);
        String idDriver = user.get(SessionManager.ID_DRIVER);

        final HashMap<String, String> params = new HashMap<>();
        params.put("f", "getDetailJobOrderHariIni");
        params.put("id_driver", idDriver);
        Log.i("hariini", params.toString());

        RequestQueue queue = Volley.newRequestQueue(hariIniFragment.getActivity());
        StringRequest sr = new StringRequest(Request.Method.POST, "http://manajemenkendaraan.com/tms/webservice.asp",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Log.i("RESPONSEhariini", response);
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray job = obj.getJSONArray("job");
                            jobOrder2ModelArrayList = new ArrayList<>();
                            for (int i = 0; i < job.length(); i++) {
                                JSONObject hasil = job.getJSONObject(i);
                                Log.i("hahaharini", hasil.toString());
                                JobOrder2Model jobOrder2Model = new JobOrder2Model();
                                jobOrder2Model.setOrderNo(hasil.getString("order_id"));
                                jobOrder2Model.setTanggal(hasil.getString("job_blast"));
                                jobOrder2Model.setContainerNo(hasil.getString("container_no"));
                                jobOrder2Model.setContainerName(hasil.getString("container_name"));
                                jobOrder2Model.setComodity(hasil.getString("container_cargo_name"));
                                jobOrder2Model.setJobName(hasil.getString("job_pickup_name"));
                                jobOrder2Model.setOrigin(hasil.getString("job_pickup_address"));
                                jobOrder2Model.setDestination(hasil.getString("job_deliver_address"));
                                jobOrder2Model.setJobId(hasil.getInt("id"));
                                jobOrder2Model.setJobDeliverStatus(hasil.getInt("job_deliver_status"));
                                jobOrder2Model.setJobPickupLatitude(hasil.getString("job_pickup_latitude"));
                                jobOrder2Model.setJobPickupLongitude(hasil.getString("job_pickup_longitude"));
                                jobOrder2Model.setJobDeliverLatitude(hasil.getString("job_deliver_latitude"));
                                jobOrder2Model.setJobDeliverLongitude(hasil.getString("job_deliver_longitude"));
                                jobOrder2Model.setJobType(hasil.getInt("job_type"));
                                Log.i("onResponse:hariini", jobOrder2Model.getJobDeliverLatitude());
                                jobOrder2ModelArrayList.add(jobOrder2Model);
                                Log.i("arrayhariini", jobOrder2ModelArrayList.toString());

                                if (jobOrder2ModelArrayList.isEmpty()) {
                                    rvHariIni.setVisibility(View.GONE);
                                    clEmptyView.setVisibility(View.VISIBLE);
                                } else {
                                    rvHariIni.setVisibility(View.VISIBLE);
                                    clEmptyView.setVisibility(View.GONE);
                                }

                                mAdapter = new HariIniListAdapter(getActivity(), jobOrder2ModelArrayList);
                                rvHariIni.setAdapter(mAdapter);
                                mAdapter.notifyDataSetChanged();
                            }

                        } catch (Throwable t) {
                            Log.i("tms", "Could not parse malformed JSON: \"" + response + "\"");
                        }

//                        mAdapter.notifyDataSetChanged();
                    }

                },
                new Response.ErrorListener() {
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
