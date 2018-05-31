package com.andresual.dev.tms.Activity.Model;

import com.andresual.dev.tms.Activity.Maps.Distance;
import com.google.android.gms.maps.model.LatLng;

import java.time.Duration;
import java.util.List;

/**
 * Created by andresual on 2/23/2018.
 */

public class RouteModel {

    public Distance distance;
    public com.andresual.dev.tms.Activity.Maps.Duration duration;
    public String endAddress;
    public LatLng endLocation;
    public String startAddress;
    public LatLng startLocation;

    public List<LatLng> points;

}
