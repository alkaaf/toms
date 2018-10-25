package com.spil.dev.tms.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.google.gson.Gson;
import com.spil.dev.tms.Activity.Model.DriverModel;
import com.spil.dev.tms.Activity.Model.UserData;
import com.spil.dev.tms.Activity.Util.Netter;
import com.spil.dev.tms.Activity.Util.Pref;
import com.spil.dev.tms.Activity.Util.StringHashMap;
import com.spil.dev.tms.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivitySplash extends BaseActivity {
    @BindView(R.id.tvUpdate)
    TextView tvUpdate;

    Pref pref;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        ButterKnife.bind(this);

        pref = new Pref(this);

        checkUpdate();
    }

    public void checkUpdate() {
        final String FC_UPDATE_NOTES = "update_notes";
        final String FC_UPDATE_CODE = "update_version_code";
        final String FC_UPDATE_NAME = "update_version_name";
        final FirebaseRemoteConfig fc = FirebaseRemoteConfig.getInstance();
        fc.setConfigSettings(new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(true)
                .build());
        Map<String, Object> def = new HashMap<>();
        def.put(FC_UPDATE_NOTES, "");
        def.put(FC_UPDATE_CODE, 0);
        def.put(FC_UPDATE_NAME, "0.0.0");
        fc.setDefaults(def);
        fc.fetch(5).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    fc.activateFetched();
                    String notes = fc.getString(FC_UPDATE_NOTES);
                    long code = fc.getLong(FC_UPDATE_CODE);
                    String name = fc.getString(FC_UPDATE_NAME);
                    try {
                        PackageInfo pi = getPackageManager().getPackageInfo(getPackageName(), 0);
                        if (code > pi.versionCode) {
                            tvUpdate.setText("New Update!");
                            new AlertDialog.Builder(ActivitySplash.this)
                                    .setCancelable(false)
                                    .setTitle("Update " + name)
                                    .setMessage(notes.replace("-", "\n-"))
                                    .setPositiveButton("update", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
                                            finish();
                                        }
                                    })
                                    .show();
                        } else {
                            tvUpdate.setText("Poof!");
                            sessionCheck();
                        }

                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    tvUpdate.setText("Error while checking update");
                    sessionCheck();
                }
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().hide();
    }

    public void cont() {

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void sessionCheck() {
        tvUpdate.setText("Checking session");
        DriverModel dm = pref.getDriverModel();
        // jika driver ada
        if (dm != null) {
            new Netter(this).webService(Request.Method.POST, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject ob = new JSONObject(response);
                        if (ob.getInt("status") == 200) {
                            UserData ud = new Gson().fromJson(ob.getString("data"), UserData.class);
                            if (ud.sttlogin) {
                                tvUpdate.setText("Session valid");
                                cont();
                            } else {
                                tvUpdate.setText("Please login to continue");
                                pref.clearDriver();
                                pref.clearKendaraan();
                                cont();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        cont();
                    }
                }
            }, Netter.getSilentErrorListener(this, new Runnable() {
                @Override
                public void run() {
                    cont();
                }
            }), Netter.Webservice.GETDATAUSER, new StringHashMap().putMore("email", pref.getDriverModel().getEmail()));
        } else {
            cont();
        }
    }
}
