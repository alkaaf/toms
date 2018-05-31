package com.andresual.dev.tms.Activity.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by andresual on 2/28/2018.
 */

public class DashboardPointModel implements Parcelable {

    @SerializedName("bulan_ini")
    private String bulanIni;
    @SerializedName("hari_ini")
    private String hariIni;
    @SerializedName("selesai")
    private String selesai;
    @SerializedName("poin")
    private String poin;

    public String getBulanIni() {
        return bulanIni;
    }

    public void setBulanIni(String bulanIni) {
        this.bulanIni = bulanIni;
    }

    public String getHariIni() {
        return hariIni;
    }

    public void setHariIni(String hariIni) {
        this.hariIni = hariIni;
    }

    public String getSelesai() {
        return selesai;
    }

    public void setSelesai(String selesai) {
        this.selesai = selesai;
    }

    public String getPoin() {
        return poin;
    }

    public void setPoin(String poin) {
        this.poin = poin;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.bulanIni);
        dest.writeString(this.hariIni);
        dest.writeString(this.selesai);
        dest.writeString(this.poin);
    }

    public DashboardPointModel() {
    }

    protected DashboardPointModel(Parcel in) {
        this.bulanIni = in.readString();
        this.hariIni = in.readString();
        this.selesai = in.readString();
        this.poin = in.readString();
    }

    public static final Parcelable.Creator<DashboardPointModel> CREATOR = new Parcelable.Creator<DashboardPointModel>() {
        @Override
        public DashboardPointModel createFromParcel(Parcel source) {
            return new DashboardPointModel(source);
        }

        @Override
        public DashboardPointModel[] newArray(int size) {
            return new DashboardPointModel[size];
        }
    };
}
