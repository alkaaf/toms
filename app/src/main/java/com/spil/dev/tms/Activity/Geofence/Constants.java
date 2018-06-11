package com.spil.dev.tms.Activity.Geofence;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;

/**
 * Created by andresual on 3/5/2018.
 */

public class Constants {

    public static final String GEOFENCE_ID_PORT_1 = "PORT_1";
    public static final String GEOFENCE_ID_PORT_2 = "PORT_2";

    public static final float GEOFENCE_RADIUS_IN_METERS = 100;

    public static final HashMap<String, LatLng> AREA_PORT = new HashMap<String, LatLng>();

    static {
        AREA_PORT.put(GEOFENCE_ID_PORT_1, new LatLng(-7.3361185,112.7789856));
        AREA_PORT.put(GEOFENCE_ID_PORT_2, new LatLng(-7.3351981,112.7787898));
    }
}
