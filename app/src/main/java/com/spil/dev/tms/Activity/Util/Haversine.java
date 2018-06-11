package com.spil.dev.tms.Activity.Util;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

public class Haversine {
    public static double calculate(LatLng src, LatLng dst) {
        return calculate(src.latitude, src.longitude, dst.latitude, dst.longitude);
    }

    public static double calculate(Location src, Location dst) {
        return calculate(src.getLatitude(), src.getLongitude(), dst.getLatitude(), dst.getLongitude());
    }

    public static double calculate(double slat, double slng, double dlat, double dlng) {
        float pk = (float) (180.f / Math.PI);

        double a1 = slat / pk;
        double a2 = slng / pk;
        double b1 = dlat / pk;
        double b2 = dlng / pk;

        double t1 = Math.cos(a1) * Math.cos(a2) * Math.cos(b1) * Math.cos(b2);
        double t2 = Math.cos(a1) * Math.sin(a2) * Math.cos(b1) * Math.sin(b2);
        double t3 = Math.sin(a1) * Math.sin(b1);
        double tt = Math.acos(t1 + t2 + t3);

        return 6366000 * tt;
    }
}
