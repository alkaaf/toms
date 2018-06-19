package com.spil.dev.tms.Activity.Model;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;

import com.android.volley.toolbox.StringRequest;
import com.google.gson.annotations.SerializedName;

/**
 * Created by andresual on 1/30/2018.
 */

public class DriverModel implements Parcelable {

    @SerializedName("nama")
    String username;
    @SerializedName("password")
    String password;
    @SerializedName("token")
    String token;
    @SerializedName("id")
    Integer id;
    @SerializedName("email")
    String email;
    @SerializedName("alamat")
    String alamat;
    @SerializedName("kode")
    String kode;
    @SerializedName("telp")
    String telp;
    @SerializedName("expiredtoken")
    String expiredToken;
    @SerializedName("id_driver")
    String idDriver;
    @SerializedName("kota")
    String kota;
    @SerializedName("last_changepassword")
    String lastChangePassword;

    public String getLastChangePassword() {
        return lastChangePassword;
    }

    public void setLastChangePassword(String lastChangePassword) {
        this.lastChangePassword = lastChangePassword;
    }

    public String getKota() {
        return kota;
    }

    public void setKota(String kota) {
        this.kota = kota;
    }

    public String getIdDriver() {
        return idDriver;
    }

    public void setIdDriver(String idDriver) {
        this.idDriver = idDriver;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }

    public String getTelp() {
        return telp;
    }

    public void setTelp(String telp) {
        this.telp = telp;
    }

    public String getExpiredToken() {
        return expiredToken;
    }

    public void setExpiredToken(String expiredToken) {
        this.expiredToken = expiredToken;
    }

    public String getInitial() {
        return username.substring(0, 1);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.username);
        dest.writeString(this.password);
        dest.writeString(this.token);
        dest.writeValue(this.id);
        dest.writeString(this.email);
        dest.writeString(this.alamat);
        dest.writeString(this.kode);
        dest.writeString(this.telp);
        dest.writeString(this.expiredToken);
        dest.writeString(this.idDriver);
        dest.writeString(this.kota);
        dest.writeString(this.lastChangePassword);
    }

    public DriverModel() {
    }

    protected DriverModel(Parcel in) {
        this.username = in.readString();
        this.password = in.readString();
        this.token = in.readString();
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.email = in.readString();
        this.alamat = in.readString();
        this.kode = in.readString();
        this.telp = in.readString();
        this.expiredToken = in.readString();
        this.idDriver = in.readString();
        this.kota = in.readString();
        this.lastChangePassword = in.readString();
    }

    public static final Parcelable.Creator<DriverModel> CREATOR = new Parcelable.Creator<DriverModel>() {
        @Override
        public DriverModel createFromParcel(Parcel source) {
            return new DriverModel(source);
        }

        @Override
        public DriverModel[] newArray(int size) {
            return new DriverModel[size];
        }
    };
}
