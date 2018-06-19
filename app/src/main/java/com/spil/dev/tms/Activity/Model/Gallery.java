package com.spil.dev.tms.Activity.Model;

public class Gallery {
    public String url;
    public double lat;
    public double lng;
    public String addr;
    public long timestamp;

    @Override
    public String toString() {
        return url;
    }
}
