package com.spil.dev.tms.Activity;

import android.Manifest;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.spil.dev.tms.Activity.Maps.LocationBroadcaster;
import com.spil.dev.tms.Activity.Model.UserData;
import com.spil.dev.tms.Activity.Util.Pref;
import com.spil.dev.tms.App;

import static com.spil.dev.tms.Activity.DashboardActivity.REQ_LOCATION_HIGH;

public class BaseActivity extends AppCompatActivity {
    private int PERM_REQ;
    IntentFilter filter;
    public static final String ACTION_GO_LOGOUT = "lougto kono";
    public static final String ACTION_GO_LOGOUT2 = "lougto kono cuk";
    public static final String STTLOGIN = "userdata";

    public void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public void longToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ACTION_GO_LOGOUT)) {
                UserData d = intent.getParcelableExtra(STTLOGIN);
                if (d != null && !d.sttlogin) {
                    NotificationManager nm = ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE));
                    if (nm != null)
                        nm.cancelAll();
                    logout();
                }
            } else if (intent.getAction().equals(ACTION_GO_LOGOUT2)) {
                logout();
            } else if(intent.getAction().equals(Intent.ACTION_TIME_TICK)){
                App.blocker(BaseActivity.this);
            }
        }
    };

    public void logout() {
        Pref pref = new Pref(this);
        pref.clearKendaraan();
        pref.clearDriver();
        finish();
        startActivity(new Intent(this, ActivitySplash.class));
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(br, filter);
        App.blocker(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(br);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_LOCATION_HIGH && resultCode == 0) {
            Toast.makeText(this, "Harap aktifkan GPS dengan mode \"High Accuracy\" untuk melanjutkan penggunaan aplikasi", Toast.LENGTH_SHORT).show();
            finish();
        }
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                ) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERM_REQ);
        }
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        filter = new IntentFilter(ACTION_GO_LOGOUT);
        filter.addAction(Intent.ACTION_TIME_TICK);
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(!statusOfGPS){
            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Harap perbolehkan seluruh permisi untuk menggunakan aplikasi", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public void uiRunner(Runnable runnable) {
        if (!isFinishing() && runnable != null) {
            runOnUiThread(runnable);
        }
    }

}