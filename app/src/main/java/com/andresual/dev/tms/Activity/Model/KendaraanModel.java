package com.andresual.dev.tms.Activity.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by andresual on 2/20/2018.
 */

public class KendaraanModel implements Parcelable {

    private String idDriver;
    @SerializedName("idkendaraan")
    private String idKendaraan;
    @SerializedName("nopol")
    private String idNopol;

    public String getIdDriver() {
        return idDriver;
    }

    public void setIdDriver(String idDriver) {
        this.idDriver = idDriver;
    }

    public String getIdKendaraan() {
        return idKendaraan;
    }

    public void setIdKendaraan(String idKendaraan) {
        this.idKendaraan = idKendaraan;
    }

    public String getIdNopol() {
        return idNopol;
    }

    public void setIdNopol(String idNopol) {
        this.idNopol = idNopol;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.idDriver);
        dest.writeString(this.idKendaraan);
        dest.writeString(this.idNopol);
    }

    public KendaraanModel() {
    }

    protected KendaraanModel(Parcel in) {
        this.idDriver = in.readString();
        this.idKendaraan = in.readString();
        this.idNopol = in.readString();
    }

    public static final Parcelable.Creator<KendaraanModel> CREATOR = new Parcelable.Creator<KendaraanModel>() {
        @Override
        public KendaraanModel createFromParcel(Parcel source) {
            return new KendaraanModel(source);
        }

        @Override
        public KendaraanModel[] newArray(int size) {
            return new KendaraanModel[size];
        }
    };
}
