package com.andresual.dev.tms.Activity.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class DepoModel implements Parcelable {

    String id;
    String nama;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.nama);
    }

    public DepoModel() {
    }

    protected DepoModel(Parcel in) {
        this.id = in.readString();
        this.nama = in.readString();
    }

    public static final Parcelable.Creator<DepoModel> CREATOR = new Parcelable.Creator<DepoModel>() {
        @Override
        public DepoModel createFromParcel(Parcel source) {
            return new DepoModel(source);
        }

        @Override
        public DepoModel[] newArray(int size) {
            return new DepoModel[size];
        }
    };
}
