package com.andresual.dev.tms.Activity.Manager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.andresual.dev.tms.Activity.DashboardActivity;
import com.andresual.dev.tms.Activity.MainActivity;

import java.util.HashMap;

/**
 * Created by andresual on 1/30/2018.
 */

public class SessionManager {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    private Context _context;
    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "userPref";
    private static final String IS_LOGIN = "isLoggedIn";
    public static final String TOKEN_KEY = "tokenKey";
    public static final String ID_DRIVER = "idDriver";
    public static final String EMAIL_DRIVER = "emailDriver";

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    //masukin token ke shared preferences. gak paham cek sign in activity
    public void createLoginSession(String token, String idDriver, String email) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(TOKEN_KEY, token);
        editor.putString(ID_DRIVER, idDriver);
        editor.putString(EMAIL_DRIVER, email);
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
        token.put(TOKEN_KEY, pref.getString(TOKEN_KEY, null));
        token.put(ID_DRIVER, pref.getString(ID_DRIVER, null));
        token.put(EMAIL_DRIVER, pref.getString(EMAIL_DRIVER, null));

        boolean val = token.isEmpty();
        System.out.println("hashmap kosong tora ? " + val);
        return token;
    }

    public void logoutUser() {
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
