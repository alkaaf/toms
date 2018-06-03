package com.andresual.dev.tms.Activity.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andresual.dev.tms.Activity.Adapter.BerandaListAdapter;
import com.andresual.dev.tms.Activity.DashboardActivity;
import com.andresual.dev.tms.Activity.Maps.LocationBroadcaster;
import com.andresual.dev.tms.Activity.Model.DashboardModel;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class BerandaFragment extends Fragment {

    RelativeLayout clEmptyView;
    RecyclerView rvBeranda;
    TextView displayDateTime;
    TextView tvBulanIni, tvHariIni, tvSelesai, tvProses;
    SwipeRefreshLayout swipe;

    BerandaListAdapter mAdapter;
    ArrayList<JobOrder2Model> jobList = new ArrayList<>();
    DashboardModel dashboardModel;
    LocationManager manager;
    Pref pref;
    StringHashMap map = new StringHashMap();
    DriverModel driver;

    public BerandaFragment() {
        // Required empty public constructor
    }

    private static BerandaFragment mInstance;

    public static BerandaFragment getmInstance() {
        if (mInstance == null) {
            mInstance = new BerandaFragment();
        }
        return mInstance;
    }

    public static BerandaFragment newInstance() {
        BerandaFragment berandaFragment = new BerandaFragment();
        return berandaFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_beranda, container, false);
        ((DashboardActivity) getActivity())
                .setActionBarTitle("Beranda");

        pref = new Pref(getContext());
        driver = pref.getDriverModel();
        map.putMore("id_driver", driver.getIdDriver());
        manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        boolean statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!statusOfGPS) {
            buildAlertMessageNoGps();
            getActivity().stopService(new Intent(getActivity(), LocationBroadcaster.class));
        }

        getActivity().startService(new Intent(getActivity(), LocationBroadcaster.class));

        //VIEW//
        tvBulanIni = view.findViewById(R.id.tv_bulan_ini);
        tvHariIni = view.findViewById(R.id.tv_hari_ini);
        tvSelesai = view.findViewById(R.id.tv_selesai);
        tvProses = view.findViewById(R.id.tv_proses);
        clEmptyView = view.findViewById(R.id.cl2);
        swipe = view.findViewById(R.id.swipe);
        displayDateTime = view.findViewById(R.id.tv_tanggal);
        ////////

        displayDateTime.setText(new SimpleDateFormat("dd/MM/yy hh:mm a", Locale.getDefault()).format(new Date()));

        // list config
        mAdapter = new BerandaListAdapter(getContext(), jobList);
        rvBeranda = view.findViewById(R.id.rv_job_order);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvBeranda.setLayoutManager(mLayoutManager);
        rvBeranda.setHasFixedSize(true);
        rvBeranda.setAdapter(mAdapter);

        mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                clEmptyView.setVisibility(mAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
            }
        });
        mAdapter.notifyDataSetChanged();
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchListJob();
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchListJob();
    }

    public void fetchListJob() {
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
                    fetchDashboard();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, Netter.getDefaultErrorListener(getContext(), new Runnable() {
            @Override
            public void run() {
                swipe.setRefreshing(false);
            }
        }), Netter.Webservice.GETJOBHARIINI, map);
    }

    public void fetchDashboard() {
        new Netter(getContext()).webService(Request.Method.POST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    DashboardModel dashboardModel = new Gson().fromJson(obj.getString("data"), DashboardModel.class);
                    tvBulanIni.setText(String.format("%02d", dashboardModel.bulan_ini));
                    tvHariIni.setText(String.format("%02d", dashboardModel.hari_ini));
                    tvProses.setText(String.format("%02d", dashboardModel.proses));
                    tvSelesai.setText(String.format("%02d", dashboardModel.selesai));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, Netter.getDefaultErrorListener(getContext(), null), Netter.Webservice.GETDASHBOARDDRIVER, map);
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Aplikasi ini memerlukan GPS aktif untuk dapat digunakan.")
                .setCancelable(false)
                .setTitle("Aktifkan GPS")
                .setPositiveButton("Aktifkan",
                        new DialogInterface.OnClickListener() {
                            public void onClick(
                                    @SuppressWarnings("unused") final DialogInterface dialog,
                                    @SuppressWarnings("unused") final int id) {
                                startActivity(new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                            }
                        })
                .setNegativeButton("Keluar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog,
                                                @SuppressWarnings("unused") final int id) {
                                System.exit(0);
                            }
                        });
        final AlertDialog alert = builder.create();
        alert.show();
    }

}