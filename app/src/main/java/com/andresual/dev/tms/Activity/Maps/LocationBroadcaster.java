package com.andresual.dev.tms.Activity.Maps;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.andresual.dev.tms.Activity.ActivitySplash;
import com.andresual.dev.tms.Activity.MainActivity;
import com.andresual.dev.tms.Activity.Model.DriverModel;
import com.andresual.dev.tms.Activity.Model.KendaraanModel;
import com.andresual.dev.tms.Activity.Util.Netter;
import com.andresual.dev.tms.Activity.Util.Pref;
import com.andresual.dev.tms.Activity.Util.StringHashMap;
import com.andresual.dev.tms.R;
import com.android.volley.Request;
import com.android.volley.Response;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by andresual on 2/24/2018.
 */

public class LocationBroadcaster extends Service {

    private static final String TAG = "BROADCASTGPS";
    private LocationManager locationManager = null;
    private static final int LOCATION_INTERVAL = 0;
    private static final float LOCATION_DISTANCE = 0;
    public static final String LOCATION_DATA = "location_Data";
    public static final String LOCATION_BROADCAST_ACTION = "location.broadcast.action";
    private static Location location;
    public static final int SERVICE_ID = 13;
    public static Location getLocation() {
        return location;
    }
    public void updateDriverLocation() {
        Pref pref = new Pref(this);
        DriverModel driverModel = pref.getDriverModel();
        KendaraanModel kendaraanModel = pref.getKendaraan();
        if (driverModel != null) {
           /* new Netter(this).webService(Request.Method.POST, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("UPDATE_LOC", response);
                        }
                    }, Netter.getDefaultErrorListener(this, null), Netter.Webservice.UPDATELOKASIDRIVER,
                    new StringHashMap().putMore("idkendaraan", kendaraanModel.getIdKendaraan())
                            .putMore("email", driverModel.getEmail())
                            .putMore("lat", Double.toString(getLocation().getLatitude()))
                            .putMore("lng", Double.toString(getLocation().getLongitude()))
            );*/
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
            Log.i(TAG, "onLocationChanged: broadcasting");
            Intent intent = new Intent(LOCATION_BROADCAST_ACTION);
            intent.putExtra(LOCATION_DATA, location);
            sendBroadcast(intent);
            mLastLocation.set(location);
            LocationBroadcaster.location = location;
            startFg();
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

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);
        initializeLocationManager();
        try {
            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListener[1]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "network provider does not exist, " + ex.getMessage());
        }
        try {
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListener[0]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
        }

        startFg();
        Intent intentBc = new Intent(LOCATION_BROADCAST_ACTION);
        intentBc.putExtra(LOCATION_DATA, location);
        sendBroadcast(intent);
        return START_STICKY;
    }

    @Override
    public void onCreate() {

        Log.i(TAG, "onCreate");




    }

    private void startFg(){
        Intent intent = new Intent(this, ActivitySplash.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,123,intent,PendingIntent.FLAG_IMMUTABLE,null);
        NotificationManager nm = ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE));
        Notification ntf = new Notification.Builder(this)
                .setContentTitle("ToMS")
                .setContentText("Last location update " + new SimpleDateFormat("HH:mm:ss").format(new Date()))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(SERVICE_ID, ntf);
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
}
