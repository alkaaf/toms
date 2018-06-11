package com.spil.dev.tms.Activity.Manager;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

/**
 * Created by andresual on 2/21/2018.
 */

public class SessionKendaraan {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    private Context _context;
    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "kendaraanPref";
    private static final String IS_LOGIN = "isLoggedIn";
    public static final String ID_KENDARAAN = "idKendaraan";
    public static final String NOPOL_KENDARAAN = "nopolKendaraan";

    public SessionKendaraan(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }
    public void createLoginSession(String idKendaraan, String nopol) {
        editor.putString(ID_KENDARAAN, idKendaraan);
        editor.putString(NOPOL_KENDARAAN, nopol);
        editor.commit();
    }

    public HashMap<String, String> getKendaraanDetails() {
        HashMap<String, String> data = new HashMap<String, String>();
        data.put(ID_KENDARAAN, pref.getString(ID_KENDARAAN, null));
        data.put(NOPOL_KENDARAAN, pref.getString(NOPOL_KENDARAAN, null));
        boolean val = data.isEmpty();
        System.out.println("ada kendaraan ?" + val);
        return data;
    }

    public void clearKendaraan() {
        editor.clear();
        editor.commit();
    }
}