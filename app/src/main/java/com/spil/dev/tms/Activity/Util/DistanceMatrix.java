package com.spil.dev.tms.Activity.Util;

import android.content.Context;
import android.location.Location;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class DistanceMatrix {
    //    ?origins=Vancouver+BC|Seattle&destinations=San+Francisco|Victoria+BC&key=YOUR_API_KEY
    //    mode=car&language=id-ID
    public static final String API = "https://maps.googleapis.com/maps/api/distancematrix/json?";
    public static final String KEY = "AIzaSyD5MFSKdKfj3UaL0ZSMSCzlkw5S-jmGZdM";

    public interface DistanceResult {
        public void onResult(String status, String distanceText, long distanceMeter, String durationText, long durationMin);
    }

    static Context context;

    public static void init(Context context) {
        DistanceMatrix.context = context;
    }

    public static void get(LatLng s, LatLng d, DistanceResult callBack) {
        get(s.latitude, s.longitude, d.latitude, d.longitude, callBack);

    }

    public static void get(Location sLoc, Location dLoc, DistanceResult callBack) {
        get(sLoc.getLatitude(), sLoc.getLongitude(), dLoc.getLatitude(), dLoc.getLongitude(), callBack);
    }

    public static void get(double sLat, double sLng, double dLat, double dLng, final DistanceResult callBack) {
        final StringHashMap req = new StringHashMap()
                .putMore("key", KEY)
                .putMore("origins", sLat + "," + sLng)
                .putMore("destinations", dLat + "," + dLng)
                .putMore("mode", "car")
                .putMore("language", "id-ID");
        String url = API + "key=" + KEY + "&" +
                "origins=" + sLat + "," + sLng + "&" +
                "destinations=" + dLat + "," + dLng + "&" +
                "mode=car&" +
                "language=id-ID";
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject o = new JSONObject(response);
                    JSONArray row = o.getJSONArray("rows");
                    JSONObject elements = row.getJSONObject(0).getJSONArray("elements").getJSONObject(0);
                    if (callBack != null) {
                        callBack.onResult(elements.getString("status"),
                                elements.getJSONObject("distance").getString("text"),
                                elements.getJSONObject("distance").getLong("value"),
                                elements.getJSONObject("duration").getString("text"),
                                elements.getJSONObject("distance").getLong("value")
                        );
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    if (callBack != null) callBack.onResult("ERROR", null, 0, null, 0);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (callBack != null) callBack.onResult("ERROR", null, 0, null, 0);
            }
        }));
    }
}
