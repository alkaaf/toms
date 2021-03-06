package com.spil.dev.tms.Activity.Maps;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.spil.dev.tms.Activity.ActivitySplash;
import com.spil.dev.tms.Activity.MainActivity;
import com.spil.dev.tms.Activity.Model.DriverModel;
import com.spil.dev.tms.Activity.Model.KendaraanModel;
import com.spil.dev.tms.Activity.Util.Haversine;
import com.spil.dev.tms.Activity.Util.Netter;
import com.spil.dev.tms.Activity.Util.Pref;
import com.spil.dev.tms.Activity.Util.StringHashMap;
import com.spil.dev.tms.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by andresual on 2/24/2018.
 */

public class LocationBroadcaster extends Service {

    private static final String TAG = "BROADCASTGPS";
    private LocationManager locationManager = null;
    private static final int LOCATION_INTERVAL = 0;
    private static final float LOCATION_DISTANCE = 0;

    private static final double SEND_MIN_DISTANCE = 30;
    private static final double SEND_MIN_MILLIS = 30000;

    public static final String LOCATION_DATA = "location_Data";
    public static final String LOCATION_BROADCAST_ACTION = "location.broadcast.action";
    private static Location location;
    public static final int SERVICE_ID = 13;

    public static Location getLocation() {
        return location;
    }

    Location lastKnown;
    long lastUpdate;

    public void updateDriverLocation() {
        Pref pref = new Pref(this);
        DriverModel driverModel = pref.getDriverModel();
        KendaraanModel kendaraanModel = pref.getKendaraan();
        if (driverModel != null) {
            if (lastKnown != null && lastUpdate != 0) {
                // check the distance
                final double distance = Haversine.calculate(lastKnown, location);
                long delta = System.currentTimeMillis() - lastUpdate;
                Log.i("MIN_DISTANCE", "distance " + distance);
                if ((distance >= SEND_MIN_DISTANCE || delta >= SEND_MIN_MILLIS) && driverModel != null) {
//                    if (location.getProvider().equalsIgnoreCase("gps")) {
                    new Netter(this).webService(Request.Method.POST, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject obj = new JSONObject(response);
                                        if (obj.getInt("status") == 200) {
                                            lastKnown = location;
                                            lastUpdate = System.currentTimeMillis();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    Log.e("UPDATE_LOC", response + " with distance " + distance);
                                }
                            }, Netter.getSilentErrorListener(this, null), Netter.Webservice.UPDATELOKASIDRIVER,
                            new StringHashMap().putMore("idkendaraan", kendaraanModel.getIdKendaraan())
                                    .putMore("email", driverModel.getEmail())
                                    .putMore("lat", Double.toString(getLocation().getLatitude()))
                                    .putMore("lng", Double.toString(getLocation().getLongitude()))
                                    .putMore("jaraktempuh", distance)
                                    .putMore("provider", getLocation().getProvider())
                    );
                }
            } else {
                lastKnown = location;
                lastUpdate = System.currentTimeMillis();
            }
        }
    }

    private class LocationListener implements android.location.LocationListener {

        Location mLastLocation;

        public LocationListener(String provider) {
            Log.i(TAG, "LocationListener " + provider);
            mLastLocation = new Location(provider);
        }

        @Override
        public void onLocationChanged(Location location) {
            Log.i(TAG, "onLocationChanged: " + location);
            Intent intent = new Intent(LOCATION_BROADCAST_ACTION);
            intent.putExtra(LOCATION_DATA, location);
            sendBroadcast(intent);
            mLastLocation.set(location);
            LocationBroadcaster.location = location;
            updateDriverLocation();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.i(TAG, "onStatusChanged: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.i(TAG, "onProviderEnabled: " + provider);
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.i(TAG, "onProviderDisabled: " + provider);
        }
    }

    LocationListener[] mLocationListener = new LocationListener[]{
            new LocationListener(locationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressLint("MissingPermission")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);
        initializeLocationManager();
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE, mLocationListener[0]);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE, mLocationListener[1]);
        startForeground(256, getForegroundNotif());
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        Log.i(TAG, "onCreate");

    }


    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy");
        super.onDestroy();
        if (locationManager != null) {
            for (int i = 0; i < mLocationListener.length; i++) {
                try {
                    locationManager.removeUpdates(mLocationListener[i]);
                } catch (Exception ex) {
                    Log.i(TAG, "fail to remove location listners, ignore", ex);
                }
            }
        }
    }

    private void initializeLocationManager() {
        Log.i(TAG, "initializeLocationManager");
        if (locationManager == null) {
            locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }

    public Notification getForegroundNotif() {
        Intent notificationIntent = new Intent(this, ActivitySplash.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);
        return new NotificationCompat.Builder(this,"chanel")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("TomS Driver")
                .setContentText("TomS Driver sedang berjalan")
                .setAutoCancel(false)
                .setContentIntent(pendingIntent)
                .build();
    }
}
