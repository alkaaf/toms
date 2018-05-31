package com.andresual.dev.tms.Activity.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andresual.dev.tms.Activity.Adapter.BerandaListAdapter;
import com.andresual.dev.tms.Activity.DashboardActivity;
import com.andresual.dev.tms.Activity.Manager.SessionKendaraan;
import com.andresual.dev.tms.Activity.Manager.SessionManager;
import com.andresual.dev.tms.Activity.Maps.LocationBroadcaster;
import com.andresual.dev.tms.Activity.Model.DashboardPointModel;
import com.andresual.dev.tms.Activity.Model.JobOrder2Model;
import com.andresual.dev.tms.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.android.volley.VolleyLog.TAG;

public class BerandaFragment extends Fragment {

    RelativeLayout clEmptyView;
    RecyclerView rvBeranda;
    SessionManager sessionManager;
    SessionKendaraan sessionKendaraan;
    BerandaListAdapter mAdapter;
    ArrayList<JobOrder2Model> jobOrder2ModelArrayList;
    ArrayList<DashboardPointModel> dashboardPointModelArrayList;
    String email;
    TextView displayDateTime;
    Calendar calendar;
    SimpleDateFormat simpledateformat;
    String date;
    String bulanIni, hariIni, selesai, point;
    TextView tvBulanIni, tvHariIni, tvSelesai, tvPoint;
    LocationManager manager;

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

    //GETTER SETTER/////////////////////////////////////////////////////////////////////////////////


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<JobOrder2Model> getJobOrder2ModelArrayList() {
        return jobOrder2ModelArrayList;
    }

    public void setJobOrder2ModelArrayList(ArrayList<JobOrder2Model> jobOrder2ModelArrayList) {
        this.jobOrder2ModelArrayList = jobOrder2ModelArrayList;
    }

    public ArrayList<DashboardPointModel> getDashboardPointModelArrayList() {
        return dashboardPointModelArrayList;
    }

    public void setDashboardPointModelArrayList(ArrayList<DashboardPointModel> dashboardPointModelArrayList) {
        this.dashboardPointModelArrayList = dashboardPointModelArrayList;
    }

    public static BerandaFragment newInstance() {
        BerandaFragment berandaFragment = new BerandaFragment();
        return berandaFragment;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_beranda, container, false);
        ((DashboardActivity) getActivity())
                .setActionBarTitle("Beranda");

        manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE );
        boolean statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if(!statusOfGPS)
        {
            buildAlertMessageNoGps();
            getActivity().stopService(new Intent(getActivity(), LocationBroadcaster.class));
        }

        fetchBerandaPoint(this);
        fetchBeranda(this);
        getActivity().startService(new Intent(getActivity(), LocationBroadcaster.class));

        //VIEW//
        tvBulanIni = view.findViewById(R.id.tv_bulan_ini);
        tvHariIni = view.findViewById(R.id.tv_hari_ini);
        tvSelesai = view.findViewById(R.id.tv_selesai);
        tvPoint = view.findViewById(R.id.tv_point);
        clEmptyView = view.findViewById(R.id.cl2);
        ////////

        displayDateTime = (TextView) view.findViewById(R.id.tv_tanggal);
        calendar = Calendar.getInstance(Locale.getDefault());
        simpledateformat = new SimpleDateFormat("dd/MM/yy hh:mm a", Locale.getDefault());
        date = simpledateformat.format(calendar.getTime());
        displayDateTime.setText(date);

        sessionKendaraan = new SessionKendaraan(getActivity().getApplicationContext());
        HashMap<String, String> data = sessionKendaraan.getKendaraanDetails();
        String idKendaraan = data.get(SessionKendaraan.ID_KENDARAAN);
        Log.i("idKendaraanberanda", idKendaraan, null);

        sessionManager = new SessionManager(getActivity().getApplicationContext());
        HashMap<String, String> user = sessionManager.getUserDetails();
        String token = user.get(SessionManager.TOKEN_KEY);
        String id = user.get(SessionManager.ID_DRIVER);
        email = user.get(SessionManager.EMAIL_DRIVER);

        Log.i("token", token, null);
        Log.i("idDriver", id, null);
        Log.i("email", email, null);

        setEmail(email);

        rvBeranda = view.findViewById(R.id.rv_job_order);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvBeranda.setLayoutManager(mLayoutManager);
        rvBeranda.setHasFixedSize(true);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchBerandaPoint(this);
        fetchBeranda(this);
    }

    public void fetchBeranda (final BerandaFragment berandaFragment) {

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading data...");
        progressDialog.show();

        sessionManager = new SessionManager(getActivity().getApplicationContext());
        HashMap<String, String> user = sessionManager.getUserDetails();
        String token = user.get(SessionManager.TOKEN_KEY);
        String idDriver = user.get(SessionManager.ID_DRIVER);
        email = user.get(SessionManager.EMAIL_DRIVER);

        setEmail(email);

        final HashMap<String, String> params = new HashMap<>();
        params.put("f", "getDetailJobOrderHariIni");
        params.put("id_driver", idDriver);
        Log.i("beranda", params.toString());

        RequestQueue queue = Volley.newRequestQueue(berandaFragment.getActivity());
        StringRequest sr = new StringRequest(Request.Method.POST, "http://manajemenkendaraan.com/tms/webservice.asp",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Log.i("RESPONSE", response);
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray job = obj.getJSONArray("job");
                            jobOrder2ModelArrayList = new ArrayList<>();
                            for (int i = 0; i < job.length(); i++) {
                                JSONObject hasil = job.getJSONObject(i);
                                Log.i("haha", hasil.toString());
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
                                Log.i("onResponse:", jobOrder2Model.getJobType().toString());
                                jobOrder2ModelArrayList.add(jobOrder2Model);
                                Log.i("array", jobOrder2ModelArrayList.toString());

                                if (jobOrder2ModelArrayList.isEmpty()) {
                                    rvBeranda.setVisibility(View.GONE);
                                    clEmptyView.setVisibility(View.VISIBLE);
                                }
                                else {
                                    rvBeranda.setVisibility(View.VISIBLE);
                                    clEmptyView.setVisibility(View.GONE);
                                }

                                mAdapter = new BerandaListAdapter(getActivity(), jobOrder2ModelArrayList);
                                rvBeranda.setAdapter(mAdapter);
                                mAdapter.notifyDataSetChanged();
                            }

                        } catch (Throwable t) {
                            Log.i("tms", "Could not parse malformed JSON: \"" + response + "\"");
                        }

//                        mAdapter.notifyDataSetChanged();
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

    public void fetchBerandaPoint (final BerandaFragment berandaFragment) {

        sessionManager = new SessionManager(getActivity().getApplicationContext());
        HashMap<String, String> user = sessionManager.getUserDetails();
        String token = user.get(SessionManager.TOKEN_KEY);
        String idDriver = user.get(SessionManager.ID_DRIVER);
        email = user.get(SessionManager.EMAIL_DRIVER);

        setEmail(email);

        final HashMap<String, String> params = new HashMap<>();
        params.put("f", "GetDashboardDriver");
        params.put("id_driver", idDriver);
        Log.i("berandapoint", params.toString());

        RequestQueue queue = Volley.newRequestQueue(berandaFragment.getActivity());
        StringRequest sr = new StringRequest(Request.Method.POST, "http://manajemenkendaraan.com/tms/webservice.asp",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("RESPONSE", response);
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONObject data = obj.getJSONObject("data");
                            dashboardPointModelArrayList = new ArrayList<>();
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
                                dashboardPointModelArrayList.add(dashboardPointModel);

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

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("This app needs GPS for sharing location. Please turn on the GPS")
                .setCancelable(false)
                .setTitle("Hi!")
                .setPositiveButton("Enable",
                        new DialogInterface.OnClickListener() {
                            public void onClick(
                                    @SuppressWarnings("unused") final DialogInterface dialog,
                                    @SuppressWarnings("unused") final int id) {
                                startActivity(new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                            }
                        })
                .setNegativeButton("Exit",
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