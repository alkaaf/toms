package com.spil.dev.tms.Activity.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class UserData implements Parcelable {
    public int id;
    public int id_driver;
    public String nama;
    public String email;
    public String expiredtoken;
    public String last_changepassword;
    public boolean sttlogin;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.id_driver);
        dest.writeString(this.nama);
        dest.writeString(this.email);
        dest.writeString(this.expiredtoken);
        dest.writeString(this.last_changepassword);
        dest.writeByte(this.sttlogin ? (byte) 1 : (byte) 0);
    }

    public UserData() {
    }

    protected UserData(Parcel in) {
        this.id = in.readInt();
        this.id_driver = in.readInt();
        this.nama = in.readString();
        this.email = in.readString();
        this.expiredtoken = in.readString();
        this.last_changepassword = in.readString();
        this.sttlogin = in.readByte() != 0;
    }

    public static final Parcelable.Creator<UserData> CREATOR = new Parcelable.Creator<UserData>() {
        @Override
        public UserData createFromParcel(Parcel source) {
            return new UserData(source);
        }

        @Override
        public UserData[] newArray(int size) {
            return new UserData[size];
        }
    };
}
