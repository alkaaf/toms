package com.andresual.dev.tms.Activity.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.andresual.dev.tms.Activity.Adapter.HariIniListAdapter;
import com.andresual.dev.tms.Activity.Model.DriverModel;
import com.andresual.dev.tms.Activity.Model.JobOrder2Model;
import com.andresual.dev.tms.Activity.Util.Netter;
import com.andresual.dev.tms.Activity.Util.Pref;
import com.andresual.dev.tms.Activity.Util.StringHashMap;
import com.andresual.dev.tms.R;
import com.android.volley.Request;
import com.android.volley.Response;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HariIniFragment extends Fragment {

    RecyclerView rvHariIni;
    HariIniListAdapter mAdapter;
    RelativeLayout clEmptyView;
    SwipeRefreshLayout swipe;

    ArrayList<JobOrder2Model> jobList = new ArrayList<>();

    Pref pref;
    DriverModel driverModel;

    public HariIniFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_hari_ini, container, false);
        pref = new Pref(getContext());
        driverModel = pref.getDriverModel();
        clEmptyView = view.findViewById(R.id.cl2);
        rvHariIni = view.findViewById(R.id.rv1);
        swipe = view.findViewById(R.id.swipe);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        mAdapter = new HariIniListAdapter(getContext(), jobList);
        mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                clEmptyView.setVisibility(mAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
            }
        });

        rvHariIni.setAdapter(mAdapter);
        rvHariIni.setLayoutManager(mLayoutManager);
        rvHariIni.setHasFixedSize(true);

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
        }), Netter.Webservice.GETJOBHARIINI, new StringHashMap().putMore("id_driver", driverModel.getIdDriver()));
    }
}
