package com.andresual.dev.tms.Activity.Controller;

import android.content.Context;
import android.util.Log;

import com.andresual.dev.tms.Activity.MapsOrderActivity;
import com.andresual.dev.tms.Activity.MengantarActivity;
import com.andresual.dev.tms.Activity.MenurunkanActivity;
import com.andresual.dev.tms.Activity.Model.LocationModel;
import com.andresual.dev.tms.Activity.Notification.JemputNotificationActivity;
import com.andresual.dev.tms.Activity.Notification.MengantarNotificationActivity;
import com.andresual.dev.tms.Activity.Notification.MenurunkanNotificationActivity;
import com.andresual.dev.tms.Activity.Notification.SiapAntarNotificationActivity;
import com.andresual.dev.tms.Activity.OrderBaruActivity;
import com.andresual.dev.tms.Activity.OrderBaruNotificationActivity;
import com.andresual.dev.tms.Activity.SiapAntarActivity;
import com.andresual.dev.tms.Activity.Util.FcmMessagingService;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by andresual on 2/22/2018.
 */

public class NotificationController {

    private static NotificationController mInstance;
    public static NotificationController getmInstance() {
        if (mInstance == null) {
            mInstance = new NotificationController();
        }
        return mInstance;
    }


    String id;
    LocationModel locationActive;

    public LocationModel getLocationActive() {
        return locationActive;
    }

    public void setLocationActive(LocationModel locationActive) {
        this.locationActive = locationActive;
    }

    public void acceptJob(final OrderBaruNotificationActivity orderBaruNotificationActivity) {


        final Map<String, String> params = new HashMap<>();
        params.put("f", "acceptjob");
        params.put("email", AuthController.getmInstance().getDriverActive().getEmail());
        params.put("idjob", id);
        Log.i("acceptjob", params.toString());

        RequestQueue queue = Volley.newRequestQueue(orderBaruNotificationActivity);
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

    public void jemputJob(final JemputNotificationActivity jemputNotificationActivity) {


        final Map<String, String> params = new HashMap<>();
        params.put("f", "pickupjob");
        params.put("email", AuthController.getmInstance().getDriverActive().getEmail());
        params.put("idjob", id);
        Log.i("rejectjob", params.toString());

        RequestQueue queue = Volley.newRequestQueue(jemputNotificationActivity);
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

    public void siapAntarJob(final SiapAntarNotificationActivity siapAntarNotificationActivity) {


        final Map<String, String> params = new HashMap<>();
        params.put("f", "readyjob");
        params.put("email", AuthController.getmInstance().getDriverActive().getEmail());
        params.put("idjob", id);
        Log.i("rejectjob", params.toString());

        RequestQueue queue = Volley.newRequestQueue(siapAntarNotificationActivity);
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

    public void mengantarJob(final MengantarNotificationActivity mengantarNotificationActivity) {


        final Map<String, String> params = new HashMap<>();
        params.put("f", "deliverjob");
        params.put("email", AuthController.getmInstance().getDriverActive().getEmail());
        params.put("idjob", id);
        params.put("latitude", locationActive.getLatitude().toString());
        params.put("longitude", locationActive.getLongitude().toString());
        Log.i("mengantarjob", params.toString());

        RequestQueue queue = Volley.newRequestQueue(mengantarNotificationActivity);
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

    public void menurunkanJob(final MenurunkanNotificationActivity menurunkanNotificationActivity) {


        final Map<String, String> params = new HashMap<>();
        params.put("f", "finishjob");
        params.put("email", AuthController.getmInstance().getDriverActive().getEmail());
        params.put("idjob", id);
        params.put("latitude", locationActive.getLatitude().toString());
        params.put("longitude", locationActive.getLongitude().toString());
        Log.i("finishjob", params.toString());

        RequestQueue queue = Volley.newRequestQueue(menurunkanNotificationActivity);
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

}
