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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.spil.dev.tms.R;

import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivitySplash extends BaseActivity {
    @BindView(R.id.tvUpdate)
    TextView tvUpdate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        ButterKnife.bind(this);
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
                                    .setNegativeButton("nanti", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            cont();
                                        }
                                    })
                                    .show();
                        } else {
                            tvUpdate.setText("Poof!");
                            cont();
                        }

                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    tvUpdate.setText("Error while checking update");
                    cont();
                }
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().hide();
    }

    public void cont(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
