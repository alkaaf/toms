package com.spil.dev.tms.Activity.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by andresual on 1/31/2018.
 */

public class TerimaModel implements Parcelable {

    String email;
    String idJob;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIdJob() {
        return idJob;
    }

    public void setIdJob(String idJob) {
        this.idJob = idJob;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.email);
        dest.writeString(this.idJob);
    }

    public TerimaModel() {
    }

    protected TerimaModel(Parcel in) {
        this.email = in.readString();
        this.idJob = in.readString();
    }

    public static final Parcelable.Creator<TerimaModel> CREATOR = new Parcelable.Creator<TerimaModel>() {
        @Override
        public TerimaModel createFromParcel(Parcel source) {
            return new TerimaModel(source);
        }

        @Override
        public TerimaModel[] newArray(int size) {
            return new TerimaModel[size];
        }
    };
}
