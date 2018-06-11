package com.spil.dev.tms.Activity.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by andresual on 1/29/2018.
 */

public class CobaModel {

    String fleet_driver_email;
    String fleet_driver_id;

    public String getFleet_driver_email() {
        return fleet_driver_email;
    }

    public void setFleet_driver_email(String fleet_driver_email) {
        this.fleet_driver_email = fleet_driver_email;
    }

    public String getFleet_driver_id() {
        return fleet_driver_id;
    }

    public void setFleet_driver_id(String fleet_driver_id) {
        this.fleet_driver_id = fleet_driver_id;
    }
}
