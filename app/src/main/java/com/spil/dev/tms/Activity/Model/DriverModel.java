package com.spil.dev.tms.Activity.Model;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;

import com.android.volley.toolbox.StringRequest;
import com.google.gson.annotations.SerializedName;

/**
 * Created by andresual on 1/30/2018.
 */

public class DriverModel {

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

}
