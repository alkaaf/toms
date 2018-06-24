package com.spil.dev.tms.Activity.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.PolyUtil;

import java.util.List;

public class Fence implements Parcelable {
    public String type;
    public String path;

    public List<LatLng> getDecodedPath() {
        return PolyUtil.decode(path);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.type);
        dest.writeString(this.path);
    }

    public Fence() {
    }

    protected Fence(Parcel in) {
        this.type = in.readString();
        this.path = in.readString();
    }

    public static final Parcelable.Creator<Fence> CREATOR = new Parcelable.Creator<Fence>() {
        @Override
        public Fence createFromParcel(Parcel source) {
            return new Fence(source);
        }

        @Override
        public Fence[] newArray(int size) {
            return new Fence[size];
        }
    };
}
