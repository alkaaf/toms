package com.andresual.dev.tms.Activity.Manager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.andresual.dev.tms.Activity.DashboardActivity;
import com.andresual.dev.tms.Activity.MainActivity;

import java.util.HashMap;

/**
 * Created by andresual on 2/28/2018.
 */

public class SessionDriverInfo {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    private Context _context;
    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "userPref";
    private static final String IS_LOGIN = "isLoggedIn";
    public static final String EMAIL_DRIVER = "emailDriver";
    public static final String ALAMAT_DRIVER = "alamatDriver";
    public static final String TELP_DRIVER = "telpDriver";
    public static final String KOTA_DRIVER = "kotaDriver";
    public static final String NAMA_DRIVER = "namaDriver";


    public SessionDriverInfo(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    //masukin token ke shared preferences. gak paham cek sign in activity
    public void createLoginSession(String nama, String email, String alamat, String telp, String kota) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(NAMA_DRIVER, nama);
        editor.putString(EMAIL_DRIVER, email);
        editor.putString(ALAMAT_DRIVER, alamat);
        editor.putString(TELP_DRIVER, telp);
        editor.putString(KOTA_DRIVER, kota);
        editor.commit();
    }

    public void checkLogin() {
        //cek login status
        if (this.isLoggedIn()) {
            //jika login status user true, direct ning kene
            Intent i = new Intent(_context, DashboardActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            //iki aku yora paham bro
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            _context.startActivity(i);
        }
    }

    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> token = new HashMap<String, String>();
        token.put(EMAIL_DRIVER, pref.getString(EMAIL_DRIVER, null));
        token.put(ALAMAT_DRIVER, pref.getString(ALAMAT_DRIVER, null));
        token.put(TELP_DRIVER, pref.getString(TELP_DRIVER, null));
        token.put(KOTA_DRIVER, pref.getString(KOTA_DRIVER, null));
        token.put(NAMA_DRIVER, pref.getString(NAMA_DRIVER, null));


        boolean val = token.isEmpty();
        System.out.println("hashmap kosong tora ? " + val);
        return token;
    }

    public void clearDriverInfo() {
        editor.clear();
        editor.commit();

        Intent i = new Intent(_context, MainActivity.class);
        // kok ada ini lagi
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // brosing
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // mulai
        _context.startActivity(i);
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }

}
