package com.andresual.dev.tms.Activity.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by andresual on 2/20/2018.
 */

public class KendaraanModel implements Parcelable {

    private String idDriver;
    @SerializedName("id_kendaraan")
    private String idKendaraan;
    @SerializedName("nopol")
    private String idNopol;
    @SerializedName("kode")
    String kode;
    @SerializedName("status_driver")
    String statusDriver;

    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }

    public String getStatusDriver() {
        return statusDriver;
    }

    public void setStatusDriver(String statusDriver) {
        this.statusDriver = statusDriver;
    }


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

    public KendaraanModel() {
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
        dest.writeString(this.kode);
        dest.writeString(this.statusDriver);
    }

    protected KendaraanModel(Parcel in) {
        this.idDriver = in.readString();
        this.idKendaraan = in.readString();
        this.idNopol = in.readString();
        this.kode = in.readString();
        this.statusDriver = in.readString();
    }

    public static final Creator<KendaraanModel> CREATOR = new Creator<KendaraanModel>() {
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
