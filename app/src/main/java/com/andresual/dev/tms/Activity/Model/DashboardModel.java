package com.andresual.dev.tms.Activity.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class DashboardModel implements Parcelable {
    public String id_driver;
    public int hari_ini;
    public int bulan_ini;
    public int proses;
    public int selesai;


    public DashboardModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id_driver);
        dest.writeInt(this.hari_ini);
        dest.writeInt(this.bulan_ini);
        dest.writeInt(this.proses);
        dest.writeInt(this.selesai);
    }

    protected DashboardModel(Parcel in) {
        this.id_driver = in.readString();
        this.hari_ini = in.readInt();
        this.bulan_ini = in.readInt();
        this.proses = in.readInt();
        this.selesai = in.readInt();
    }

    public static final Creator<DashboardModel> CREATOR = new Creator<DashboardModel>() {
        @Override
        public DashboardModel createFromParcel(Parcel source) {
            return new DashboardModel(source);
        }

        @Override
        public DashboardModel[] newArray(int size) {
            return new DashboardModel[size];
        }
    };
}
