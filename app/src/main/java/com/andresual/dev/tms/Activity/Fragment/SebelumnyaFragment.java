package com.andresual.dev.tms.Activity.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andresual.dev.tms.Activity.Adapter.HariIniListAdapter;
import com.andresual.dev.tms.Activity.Adapter.SebelumnyaListAdapter;
import com.andresual.dev.tms.Activity.Manager.SessionManager;
import com.andresual.dev.tms.Activity.Model.DriverModel;
import com.andresual.dev.tms.Activity.Model.JobOrder2Model;
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
import java.util.Map;

public class SebelumnyaFragment extends Fragment {

    RecyclerView rvSebelumnya;
    SebelumnyaListAdapter mAdapter;
    RelativeLayout clEmptyView;
    SwipeRefreshLayout swipe;
    ArrayList<JobOrder2Model> jobList = new ArrayList<>();

    Pref pref;
    DriverModel driverModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_sebelumnya, container, false);
        pref = new Pref(getContext());
        driverModel = pref.getDriverModel();


        clEmptyView = view.findViewById(R.id.cl2);
        rvSebelumnya = view.findViewById(R.id.rv1);
        swipe = view.findViewById(R.id.swipe);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mAdapter = new SebelumnyaListAdapter(getContext(), jobList);
        mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                clEmptyView.setVisibility(mAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
            }
        });

        rvSebelumnya.setAdapter(mAdapter);

        rvSebelumnya.setLayoutManager(mLayoutManager);
        rvSebelumnya.setHasFixedSize(true);

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetch();
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        fetch();
    }

    public void fetch() {
        swipe.setRefreshing(true);
        new Netter(getContext()).webService(Request.Method.POST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                swipe.setRefreshing(false);
                try {
                    JSONObject obj = new JSONObject(response);
                    ArrayList<JobOrder2Model> temp = new Gson().fromJson(obj.getString("job"), new TypeToken<ArrayList<JobOrder2Model>>() {
                    }.getType());
                    jobList.clear();
                    jobList.addAll(temp);
                    mAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, Netter.getDefaultErrorListener(getContext(), new Runnable() {
            @Override
            public void run() {
                swipe.setRefreshing(false);
            }
        }), Netter.Webservice.GETJOBPAST, new StringHashMap().putMore("id_driver", driverModel.getIdDriver()));

    }
}
