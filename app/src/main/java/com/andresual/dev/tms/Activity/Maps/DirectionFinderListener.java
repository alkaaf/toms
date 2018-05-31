package com.andresual.dev.tms.Activity.Maps;

import com.andresual.dev.tms.Activity.Model.RouteModel;

import java.util.List;

/**
 * Created by andresual on 2/23/2018.
 */

public interface DirectionFinderListener {
    void onDirectionFinderStart();
    void onDirectionFinderSuccess(List<RouteModel> route);
}
