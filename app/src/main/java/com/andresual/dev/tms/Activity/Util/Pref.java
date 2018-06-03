package com.andresual.dev.tms.Activity.Util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.andresual.dev.tms.Activity.Model.DriverModel;
import com.andresual.dev.tms.Activity.Model.KendaraanModel;
import com.andresual.dev.tms.App;
import com.google.gson.Gson;

public class Pref {
    SharedPreferences sp;
    SharedPreferences.Editor edit;
    static final String SP_NAME = "tms.default.sp";

    static final String MODEL_DRIVER = "model.driver";
    static final String MODEL_KENDARAAN = "model.kendaraan";

    public Pref(Context context) {
        sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        edit = sp.edit();
    }

    public SharedPreferences getSp() {
        return sp;
    }

    public static SharedPreferences instance() {
        return new Pref(App.getContext()).getSp();
    }

    public void putModelDriver(DriverModel driverModel) {
        edit.putString(MODEL_DRIVER, new Gson().toJson(driverModel));
        edit.commit();
    }

    public DriverModel getDriverModel() {
        return new Gson().fromJson(getSp().getString(MODEL_DRIVER, null), DriverModel.class);
    }

    public void putKendaraan(KendaraanModel kendaraan) {
        edit.putString(MODEL_KENDARAAN, new Gson().toJson(kendaraan));
        edit.commit();
    }

    public KendaraanModel getKendaraan() {
        return new Gson().fromJson(getSp().getString(MODEL_KENDARAAN, null), KendaraanModel.class);
    }

    public boolean checkDriver() {
        return !TextUtils.isEmpty(sp.getString(MODEL_DRIVER, null));
    }

    public boolean checkKendaraan() {
        return !TextUtils.isEmpty(sp.getString(MODEL_KENDARAAN, null));
    }

    public void clearDriver(){
        edit.remove(MODEL_DRIVER).commit();
    }
    public void clearKendaraan(){
        edit.remove(MODEL_KENDARAAN).commit();
    }
}
