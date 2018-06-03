package com.andresual.dev.tms.Activity.Controller;

import android.app.Activity;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.andresual.dev.tms.Activity.Fragment.BerandaFragment;
import com.andresual.dev.tms.Activity.Manager.SessionKendaraan;
import com.andresual.dev.tms.Activity.Manager.SessionManager;
import com.andresual.dev.tms.Activity.MapsOrderActivity;
import com.andresual.dev.tms.Activity.MengantarActivity;
import com.andresual.dev.tms.Activity.MenurunkanActivity;
import com.andresual.dev.tms.Activity.Model.AlasanModel;
import com.andresual.dev.tms.Activity.Model.BerandaModel;
import com.andresual.dev.tms.Activity.Model.CityModel;
import com.andresual.dev.tms.Activity.Model.DriverModel;
import com.andresual.dev.tms.Activity.Model.JobOrder2Model;
import com.andresual.dev.tms.Activity.Model.JobOrderModel;
import com.andresual.dev.tms.Activity.Model.LocationModel;
import com.andresual.dev.tms.Activity.Model.TerimaModel;
import com.andresual.dev.tms.Activity.Model.TolakModel;
import com.andresual.dev.tms.Activity.OrderBaruActivity;
import com.andresual.dev.tms.Activity.RegisterActivity;
import com.andresual.dev.tms.Activity.SiapAntarActivity;
import com.andresual.dev.tms.Activity.TolakOrderActivity;
import com.andresual.dev.tms.Activity.Util.FcmMessagingService;
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
import java.util.Iterator;
import java.util.Map;

/**
 * Created by andresual on 1/30/2018.
 */

public class OperationalController {

    ProgressDialog progressDialog;

    private static OperationalController mInstance;

    public OperationalController() {

    }

    public static OperationalController getmInstance() {
        if (mInstance == null) {
            mInstance = new OperationalController();
        }
        return mInstance;
    }

    private Activity mContext;
    public OperationalController(Activity context) {
        this.mContext = context;
    }

    String jobId, emailpass;
    SessionManager sessionManager;
    SessionKendaraan sessionKendaraan;
    SharedPreferences sharedPreferences;
    TerimaModel terimaModel;
    TolakModel tolakModel;
    JobOrderModel jobOrderModel;
    DriverModel driverModel;
    JobOrder2Model jobOrder2Model;
    LocationModel locationActive;
    AlasanModel alasanActive;
    ArrayList<CityModel> cityModelArrayList = new ArrayList<>();
    ArrayList<BerandaModel> jobOrderModelArrayList = new ArrayList<>();
    ArrayList<JobOrder2Model> jobOrder2ModelArrayList= new ArrayList<>();
    BerandaFragment berandaFragment = new BerandaFragment();

    public AlasanModel getAlasanActive() {
        return alasanActive;
    }

    public void setAlasanActive(AlasanModel alasanActive) {
        this.alasanActive = alasanActive;
    }

    public LocationModel getLocationActive() {
        return locationActive;
    }

    public void setLocationActive(LocationModel locationActive) {
        this.locationActive = locationActive;
    }

    public JobOrder2Model getJobOrder2Model() {
        return jobOrder2Model;
    }

    public void setJobOrder2Model(JobOrder2Model jobOrder2Model) {
        this.jobOrder2Model = jobOrder2Model;
    }

    public ArrayList<JobOrder2Model> getJobOrder2ModelArrayList() {
        return jobOrder2ModelArrayList;
    }

    public void setJobOrder2ModelArrayList(ArrayList<JobOrder2Model> jobOrder2ModelArrayList) {
        this.jobOrder2ModelArrayList = jobOrder2ModelArrayList;
    }

    public TolakModel getTolakModel() {
        return tolakModel;
    }

    public void setTolakModel(TolakModel tolakModel) {
        this.tolakModel = tolakModel;
    }

    public JobOrderModel getJobOrderModel() {
        return jobOrderModel;
    }

    public void setJobOrderModel(JobOrderModel jobOrderModel) {
        this.jobOrderModel = jobOrderModel;
    }

    public ArrayList<BerandaModel> getJobOrderModelArrayList() {
        return jobOrderModelArrayList;
    }

    public void setJobOrderModelArrayList(ArrayList<BerandaModel> jobOrderModelArrayList) {
        this.jobOrderModelArrayList = jobOrderModelArrayList;
    }

    public ArrayList<CityModel> getCityModelArrayList() {
        return cityModelArrayList;
    }

    public void setCityModelArrayList(ArrayList<CityModel> cityModelArrayList) {
        this.cityModelArrayList = cityModelArrayList;
    }

    public TerimaModel getTerimaModel() {
        return terimaModel;
    }

    public void setTerimaModel(TerimaModel terimaModel) {
        this.terimaModel = terimaModel;
    }

    //ORDER BARU
    public void acceptJob(final OrderBaruActivity orderBaruActivity) {

        final Map<String, String> params = new HashMap<>();
        params.put("f", "acceptjob");
        params.put("email", AuthController.getmInstance().getDriverActive().getEmail());
        params.put("idjob", jobOrder2Model.getJobId().toString());
        Log.i("acceptjob", params.toString());

        RequestQueue queue = Volley.newRequestQueue(orderBaruActivity);
        StringRequest sr = new StringRequest(Request.Method.POST, "http://manajemenkendaraan.com/tms/webservice.asp",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("response", response);
                        try {
                            JSONObject obj = new JSONObject(response);
                            Log.i("status", obj.getString("status"));
                            Log.i("messages", obj.getString("message"));
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

    public void rejectJob(final TolakOrderActivity tolakOrderActivity) {

        final Map<String, String> params = new HashMap<>();
        params.put("f", "rejectjob");
        params.put("email", tolakModel.getEmail());
        params.put("idjob", tolakModel.getIdJob());
        params.put("rejectnote", tolakModel.getRejectNote());
        Log.i("rejectjob", params.toString());

        RequestQueue queue = Volley.newRequestQueue(tolakOrderActivity);
        StringRequest sr = new StringRequest(Request.Method.POST, "http://manajemenkendaraan.com/tms/webservice.asp",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("responsess", response);
                        try {
                            JSONObject obj = new JSONObject(response);
                            Log.i("status", obj.getString("status"));
                            Log.i("messages", obj.getString("message"));
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

    public void jemputJob(final MapsOrderActivity mapsOrderActivity) {
        final Map<String, String> params = new HashMap<>();
        params.put("f", "pickupjob");
        params.put("email", AuthController.getmInstance().getDriverActive().getEmail());
        params.put("idjob", jobOrder2Model.getJobId().toString());
        Log.i("rejectjob", params.toString());

        RequestQueue queue = Volley.newRequestQueue(mapsOrderActivity);
        StringRequest sr = new StringRequest(Request.Method.POST, "http://manajemenkendaraan.com/tms/webservice.asp",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("response", response);
                        try {
                            JSONObject obj = new JSONObject(response);
                            Log.i("status", obj.getString("status"));
                            Log.i("messages", obj.getString("message"));
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

    public void siapAntarJob(final SiapAntarActivity siapAntarActivity) {
        final Map<String, String> params = new HashMap<>();
        params.put("f", "readyjob");
        params.put("email", AuthController.getmInstance().getDriverActive().getEmail());
        params.put("idjob", jobOrder2Model.getJobId().toString());
        Log.i("rejectjob", params.toString());

        RequestQueue queue = Volley.newRequestQueue(siapAntarActivity);
        StringRequest sr = new StringRequest(Request.Method.POST, "http://manajemenkendaraan.com/tms/webservice.asp",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("response", response);
                        try {
                            JSONObject obj = new JSONObject(response);
                            Log.i("status", obj.getString("status"));
                            Log.i("messages", obj.getString("message"));
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

    public void mengantarJob(final MengantarActivity mengantarActivity) {
        final Map<String, String> params = new HashMap<>();
        params.put("f", "deliverjob");
        params.put("email", AuthController.getmInstance().getDriverActive().getEmail());
        params.put("idjob", jobOrder2Model.getJobId().toString());
        params.put("latitude", locationActive.getLatitude().toString());
        params.put("longitude", locationActive.getLongitude().toString());
        Log.i("mengantarjob", params.toString());

        RequestQueue queue = Volley.newRequestQueue(mengantarActivity);
        StringRequest sr = new StringRequest(Request.Method.POST, "http://manajemenkendaraan.com/tms/webservice.asp",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("response", response);
                        try {
                            JSONObject obj = new JSONObject(response);
                            Log.i("status", obj.getString("status"));
                            Log.i("messages", obj.getString("message"));
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

    public void menurunkanJob(final MenurunkanActivity menurunkanActivity) {
        final Map<String, String> params = new HashMap<>();
        params.put("f", "finishjob");
        params.put("email", AuthController.getmInstance().getDriverActive().getEmail());
        params.put("idjob", jobOrder2Model.getJobId().toString());
        params.put("latitude", locationActive.getLatitude().toString());
        params.put("longitude", locationActive.getLongitude().toString());
        Log.i("finishjob", params.toString());

        RequestQueue queue = Volley.newRequestQueue(menurunkanActivity);
        StringRequest sr = new StringRequest(Request.Method.POST, "http://manajemenkendaraan.com/tms/webservice.asp",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("response", response);
                        try {
                            JSONObject obj = new JSONObject(response);
                            Log.i("status", obj.getString("status"));
                            Log.i("messages", obj.getString("message"));
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

    public void fetchCity(final RegisterActivity registerActivity) {

//        final ProgressDialog progressDialog = new ProgressDialog(registerActivity);
//        progressDialog.setMessage("Loading data...");
//        progressDialog.show();

        final Map<String, String> params = new HashMap<>();
        params.put("f", "getcity");
        Log.i("city", params.toString());

        RequestQueue queue = Volley.newRequestQueue(registerActivity);
        StringRequest sr = new StringRequest(Request.Method.GET, "http://manajemenkendaraan.com/tms/webservice.asp?f=getcity",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("RESPONSE", response);
                        progressDialog.dismiss();
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray city = obj.getJSONArray("city");
                            cityModelArrayList = new ArrayList<>();
                            for (int i = 0; i < city.length(); i++) {
                                JSONObject hasil = city.getJSONObject(i);
                                CityModel cityModel = new CityModel();
                                cityModel.setId(hasil.getString("id"));
                                cityModel.setNamaKota(hasil.getString("nama"));
                                cityModelArrayList.add(cityModel);
                            }
                        } catch (Throwable t) {
                            Log.i("urbankabinet", "Could not parse malformed JSON: \"" + response + "\"");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String,String> getParams(){
                return params;
            }
        };
        queue.add(sr);
    }

    public void detailJob (OrderBaruActivity orderBaruActivity) {
        final Map<String, String> params = new HashMap<>();
        params.put("f", "getDetailJobOrder");
        params.put("id", "19");
        Log.i("job", params.toString());

        RequestQueue queue = Volley.newRequestQueue(orderBaruActivity);
        StringRequest sr = new StringRequest(Request.Method.POST, "http://manajemenkendaraan.com/tms/webservice.asp",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("RESPONSE", response);
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONObject hasil = obj.getJSONObject("job-19");
                            final String test = hasil.getString("order_id");
                            Log.i("test", test);
                            jobOrderModelArrayList = new ArrayList<>();
                            for (int i = 0; i < hasil.length(); i++) {
                                Log.i("hasil", hasil.toString());
                                BerandaModel jobOrderModel = new BerandaModel();
                                jobOrderModel.setMessage(hasil.getString("message"));
                                jobOrderModel.setStatus(hasil.getInt("status"));
                                jobOrderModel.setOrder_id(hasil.getInt("order_id"));
                                jobOrderModel.setFleet_driver_email(hasil.getString("fleet_driver_email"));
                                jobOrderModel.setMessage(hasil.getString("job_type"));
                                jobOrderModel.setJob_pickup_name(hasil.getString("job_pickup_name"));
                                jobOrderModel.setJob_description(hasil.getString("job_description"));
                                jobOrderModel.setCustomer_id(hasil.getString("customer_id"));
                                jobOrderModel.setConsignee_id(hasil.getString("consignee_id"));
                                jobOrderModel.setJob_created(hasil.getString("job_created"));
                                jobOrderModel.setJob_blast(hasil.getString("job_blast"));
                                jobOrderModel.setJob_pickup_name(hasil.getString("job_pickup_name"));
                                jobOrderModel.setJob_pickup_address_id(hasil.getString("job_pickup_address_id"));
                                jobOrderModel.setJob_pickup_address(hasil.getString("job_pickup_address"));
                                jobOrderModel.setJob_pickup_latitude(hasil.getString("job_pickup_latitude"));
                                jobOrderModel.setJob_pickup_longitude(hasil.getString("job_pickup_longitude"));
                                jobOrderModel.setJob_pickup_datetime(hasil.getString("job_pickup_datetime"));
                                jobOrderModel.setJob_pickup_status(hasil.getString("job_pickup_status"));
                                jobOrderModel.setJob_pickup_reject_note(hasil.getString("job_pickup_reject_note"));
                                jobOrderModel.setJob_accept_time(hasil.getString("job_accept_time"));
                                jobOrderModel.setJob_deliver_address_id(hasil.getString("job_deliver_address_id"));
                                jobOrderModel.setJob_deliver_address(hasil.getString("job_deliver_address"));
                                jobOrderModel.setJob_deliver_latitude(hasil.getString("job_deliver_latitude"));
                                jobOrderModel.setJob_deliver_longitude(hasil.getString("job_deliver_longitude"));
                                jobOrderModel.setJob_deliver_starttime(hasil.getString("job_deliver_starttime"));
                                jobOrderModel.setJob_deliver_finishtime(hasil.getString("job_deliver_finishtime"));
                                jobOrderModel.setJob_deliver_status(hasil.getString("job_deliver_status"));
                                jobOrderModel.setJob_deliver_cancelnote(hasil.getString("job_deliver_cancelnote"));
                                jobOrderModel.setFleet_id(hasil.getInt("fleet_id"));
                                jobOrderModel.setFleet_nopol(hasil.getString("fleet_nopol"));
                                jobOrderModel.setFleet_driver_id(hasil.getInt("fleet_driver_id"));
                                jobOrderModel.setFleet_driver_name(hasil.getString("fleet_driver_name"));
                                jobOrderModel.setFleet_driver_score(hasil.getInt("fleet_driver_score"));
                                jobOrderModel.setFleet_ownership(hasil.getString("fleet_ownership"));
                                jobOrderModel.setTime_zone(hasil.getString("time_zone"));
                                jobOrderModel.setPortorderid(hasil.getInt("portorderid"));
                                jobOrderModel.setContainer_no(hasil.getString("container_no"));
                                jobOrderModel.setContainer_sizeID(hasil.getInt("container_sizeID"));
                                jobOrderModel.setContainer_name(hasil.getString("container_name"));
                                jobOrderModel.setContainer_isfull(hasil.getString("container_isfull"));
                                jobOrderModel.setContainer_ownership(hasil.getString("container_ownership"));
                                jobOrderModel.setContainer_cargo_id(hasil.getInt("container_cargo_id"));
                                jobOrderModel.setContainer_cargo_name(hasil.getString("container_cargo_name"));
                                jobOrderModel.setId(hasil.getInt("id"));

                                jobOrderModelArrayList.add(jobOrderModel);
                                Log.i("asem", jobOrderModelArrayList.toString());
                                Integer idJob = jobOrderModel.getId();
                                Log.i("idjobb", idJob.toString());

                            }
                        } catch (Throwable t) {
                            Log.i("tms", "Could not parse malformed JSON: \"" + response + "\"");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String,String> getParams(){
                return params;
            }
        };
        queue.add(sr);
    }

    public void fetchBeranda (final BerandaFragment berandaFragment) {

//        sessionManager = new SessionManager(getApplicationContext());
//        HashMap<String, String> user = sessionManager.getUserDetails();
//        String token = user.get(SessionManager.TOKEN_KEY);
//        String id = user.get(SessionManager.ID_DRIVER);

        final HashMap<String, String> params = new HashMap<>();
        params.put("f", "getDetailJobOrder2");
        params.put("id_driver", "2");
        Log.i("beranda", params.toString());

        RequestQueue queue = Volley.newRequestQueue(berandaFragment.getActivity());
        StringRequest sr = new StringRequest(Request.Method.POST, "http://manajemenkendaraan.com/tms/webservice.asp",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
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
                                Log.i("onResponse:", jobOrder2Model.getJobId().toString());
                                jobOrder2ModelArrayList.add(jobOrder2Model);
                                Log.i("array", jobOrder2ModelArrayList.toString());
                                jobId = hasil.getString("id");
                            }
//                            jobId = jobOrder2Model.getJobId().toString();
//                            Log.i("jobid", jobId);

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
