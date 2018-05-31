package com.andresual.dev.tms.Activity.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by andresual on 2/13/2018.
 */

public class EstimationModel implements Parcelable {

    String origins;
    String destination;
    String token;

    public String getOrigins() {
        return origins;
    }

    public void setOrigins(String origins) {
        this.origins = origins;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.origins);
        dest.writeString(this.destination);
        dest.writeString(this.token);
    }

    public EstimationModel() {
    }

    protected EstimationModel(Parcel in) {
        this.origins = in.readString();
        this.destination = in.readString();
        this.token = in.readString();
    }

    public static final Parcelable.Creator<EstimationModel> CREATOR = new Parcelable.Creator<EstimationModel>() {
        @Override
        public EstimationModel createFromParcel(Parcel source) {
            return new EstimationModel(source);
        }

        @Override
        public EstimationModel[] newArray(int size) {
            return new EstimationModel[size];
        }
    };
}
