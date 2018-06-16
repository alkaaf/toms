package com.spil.dev.tms.Activity.Fragment;

import android.animation.TimeInterpolator;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

import com.daasuu.cat.CountAnimationTextView;
import com.spil.dev.tms.Activity.DashboardActivity;
import com.spil.dev.tms.Activity.Model.DashboardModel;
import com.spil.dev.tms.Activity.Model.DriverModel;
import com.spil.dev.tms.Activity.Util.Netter;
import com.spil.dev.tms.Activity.Util.Pref;
import com.spil.dev.tms.Activity.Util.StringHashMap;
import com.spil.dev.tms.R;
import com.android.volley.Request;
import com.android.volley.Response;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class RingkasanFragment extends Fragment {

    TextView displayDateTime;
    CountAnimationTextView tvBulanIni, tvHariIni, tvSelesai, tvProses;
    Pref pref;
    DriverModel driverModel;

    public RingkasanFragment() {
        // Required empty public constructor
    }

    public static RingkasanFragment newInstance() {
        RingkasanFragment ringkasanFragment = new RingkasanFragment();
        return ringkasanFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_ringkasan, container, false);
        ((DashboardActivity) getActivity())
                .setActionBarTitle("Ringkasan");

        pref = new Pref(getContext());
        driverModel = pref.getDriverModel();
        //VIEW DATE//
        displayDateTime = (TextView) view.findViewById(R.id.tv_tanggal);
        displayDateTime.setText(new SimpleDateFormat("dd/MM/yy hh:mm a", Locale.getDefault()).format(new Date()));
        /////////////

        //VIEW DASHBOARD POINT//
        tvBulanIni = view.findViewById(R.id.tv_bulan_ini);
        tvHariIni = view.findViewById(R.id.tv_hari_ini);
        tvSelesai = view.findViewById(R.id.tv_selesai);
        tvProses = view.findViewById(R.id.tv_proses);
        ////////////////////////

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchDashboard();
    }

    public void fetchDashboard() {
        new Netter(getContext()).webService(Request.Method.POST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    DashboardModel dashboardModel = new Gson().fromJson(obj.getString("data"), DashboardModel.class);
//                    tvBulanIni.setText(String.format("%02d", dashboardModel.bulan_ini));
//                    tvHariIni.setText(String.format("%02d", dashboardModel.hari_ini));
//                    tvProses.setText(String.format("%02d", dashboardModel.proses));
//                    tvSelesai.setText(String.format("%02d", dashboardModel.selesai));
                    tvBulanIni.setInterpolator(new AccelerateDecelerateInterpolator())
                            .countAnimation(0, dashboardModel.bulan_ini);
                    tvHariIni.setInterpolator(new AccelerateDecelerateInterpolator())
                            .countAnimation(0, dashboardModel.hari_ini);
                    tvProses.setInterpolator(new AccelerateDecelerateInterpolator())
                            .countAnimation(0, dashboardModel.proses);
                    tvSelesai.setInterpolator(new AccelerateDecelerateInterpolator())
                            .countAnimation(0, dashboardModel.selesai);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, Netter.getDefaultErrorListener(getContext(), null), Netter.Webservice.GETDASHBOARDDRIVER, new StringHashMap().putMore("id_driver", driverModel.getIdDriver()));
    }
}
