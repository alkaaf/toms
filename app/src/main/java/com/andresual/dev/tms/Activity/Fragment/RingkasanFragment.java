package com.andresual.dev.tms.Activity.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andresual.dev.tms.Activity.DashboardActivity;
import com.andresual.dev.tms.Activity.Manager.SessionKendaraan;
import com.andresual.dev.tms.Activity.Manager.SessionManager;
import com.andresual.dev.tms.Activity.Model.DashboardPointModel;
import com.andresual.dev.tms.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class RingkasanFragment extends Fragment {

    TextView displayDateTime;
    SessionManager sessionManager;
    Calendar calendar;
    SimpleDateFormat simpledateformat;
    String date;
    String bulanIni, hariIni, selesai, point;
    TextView tvBulanIni, tvHariIni, tvSelesai, tvPoint;

    public RingkasanFragment() {
        // Required empty public constructor
    }

    public static RingkasanFragment newInstance() {
        RingkasanFragment ringkasanFragment = new RingkasanFragment();
        return ringkasanFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_ringkasan, container, false);
        ((DashboardActivity) getActivity())
                .setActionBarTitle("Ringkasan");

        fetchBerandaPoint(this);

        //VIEW DATE//
        displayDateTime = (TextView) view.findViewById(R.id.tv_tanggal);
        calendar = Calendar.getInstance(Locale.getDefault());
        simpledateformat = new SimpleDateFormat("dd/MM/yy hh:mm a", Locale.getDefault());
        date = simpledateformat.format(calendar.getTime());
        displayDateTime.setText(date);
        /////////////

        //VIEW DASHBOARD POINT//
        tvBulanIni = view.findViewById(R.id.tv_bulan_ini);
        tvHariIni = view.findViewById(R.id.tv_hari_ini);
        tvSelesai = view.findViewById(R.id.tv_selesai);
        tvPoint = view.findViewById(R.id.tv_point);
        ////////////////////////

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

    public void fetchBerandaPoint (final RingkasanFragment ringkasanFragment) {

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading data...");
        progressDialog.show();

        sessionManager = new SessionManager(getActivity().getApplicationContext());
        HashMap<String, String> user = sessionManager.getUserDetails();
        String token = user.get(SessionManager.TOKEN_KEY);
        String idDriver = user.get(SessionManager.ID_DRIVER);

        final HashMap<String, String> params = new HashMap<>();
        params.put("f", "GetDashboardDriver");
        params.put("id_driver", idDriver);
        Log.i("berandapoint", params.toString());

        RequestQueue queue = Volley.newRequestQueue(ringkasanFragment.getActivity());
        StringRequest sr = new StringRequest(Request.Method.POST, "http://manajemenkendaraan.com/tms/webservice.asp",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Log.i("RESPONSE", response);
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONObject data = obj.getJSONObject("data");
//                            dashboardPointModelArrayList = new ArrayList<>();
                            for (int i = 0; i < data.length(); i++) {
                                Log.i("point", data.toString());
                                DashboardPointModel dashboardPointModel = new DashboardPointModel();
                                dashboardPointModel.setBulanIni(data.getString("bulan_ini"));
                                bulanIni = dashboardPointModel.getBulanIni();
                                dashboardPointModel.setHariIni(data.getString("hari_ini"));
                                hariIni = dashboardPointModel.getHariIni();
                                dashboardPointModel.setSelesai(data.getString("selesai"));
                                selesai = dashboardPointModel.getSelesai();
                                dashboardPointModel.setPoin(data.getString("poin"));
                                point = dashboardPointModel.getPoin();
                                Log.i("onResponsepoin:", dashboardPointModel.getHariIni());
//                                dashboardPointModelArrayList.add(dashboardPointModel);

                                tvBulanIni.setText("0" + dashboardPointModel.getBulanIni());
                                tvHariIni.setText("0" + dashboardPointModel.getHariIni());
                                tvSelesai.setText("0" +dashboardPointModel.getSelesai());
                                tvPoint.setText("0" + dashboardPointModel.getPoin());
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
            protected Map<String,String> getParams(){
                return params;
            }
        };
        queue.add(sr);
    }
}
