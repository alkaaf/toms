package com.spil.dev.tms.Activity.Util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.location.Location;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class QuickLocation {
    @SuppressLint("MissingPermission")
    public static void get(Activity activity, OnSuccessListener<Location> onSuccessListener){
        LocationServices.getFusedLocationProviderClient(activity).getLastLocation().addOnSuccessListener(onSuccessListener);
    }
}
