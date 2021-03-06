package com.spil.dev.tms.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.github.javiersantos.appupdater.AppUpdater;
import com.github.javiersantos.appupdater.AppUpdaterUtils;
import com.github.javiersantos.appupdater.enums.AppUpdaterError;
import com.github.javiersantos.appupdater.enums.UpdateFrom;
import com.github.javiersantos.appupdater.objects.Update;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.spil.dev.tms.Activity.Fragment.AkunFragment;
import com.spil.dev.tms.Activity.Fragment.BerandaFragment;
import com.spil.dev.tms.Activity.Fragment.RejectFragment;
import com.spil.dev.tms.Activity.Fragment.RingkasanFragment;
import com.spil.dev.tms.Activity.Fragment.RiwayatFragment;
import com.spil.dev.tms.Activity.Manager.SessionKendaraan;
import com.spil.dev.tms.Activity.Manager.SessionManager;
import com.spil.dev.tms.Activity.Maps.LocationBroadcaster;
import com.spil.dev.tms.Activity.Model.DriverModel;
import com.spil.dev.tms.Activity.Model.KendaraanModel;
import com.spil.dev.tms.Activity.Model.PassingLocationModel;
import com.spil.dev.tms.Activity.Model.SimpleJob;
import com.spil.dev.tms.Activity.ProsesActivity.ActivityProses1And2;
import com.spil.dev.tms.Activity.ProsesActivity.ActivityProses3;
import com.spil.dev.tms.Activity.ProsesActivity.ActivityProsesFrom4To7;
import com.spil.dev.tms.Activity.ProsesActivity.ActivityProsesMoreThan8;
import com.spil.dev.tms.Activity.Util.DF;
import com.spil.dev.tms.Activity.Util.FcmMessagingService;
import com.spil.dev.tms.Activity.Util.Netter;
import com.spil.dev.tms.Activity.Util.Pref;
import com.spil.dev.tms.Activity.Util.StringHashMap;
import com.spil.dev.tms.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static android.support.v4.content.PermissionChecker.PERMISSION_GRANTED;

public class DashboardActivity extends BaseActivity /*implements*/ {

    private static final int LOCATION_REQUEST_CODE = 101;
    LocationManager manager;

    DriverModel driverModel;
    KendaraanModel kendaraanModel;
    Pref pref;
    IntentFilter filter;
    Context mContext;
    Fragment selectedFragment;
    GoogleApiClient mGoogleApiClient;
    public static final int REQ_LOCATION_HIGH = 1000;
    public static final long FORCE_CHANGE_PASS_THRESHOLD = 1000L * 60L * 60L * 24L * 30L * 3L;
//    public static final long FORCE_CHANGE_PASS_THRESHOLD = 5000;

    BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(LocationBroadcaster.LOCATION_BROADCAST_ACTION)) {
                Location loc = intent.getParcelableExtra(LocationBroadcaster.LOCATION_DATA);
            }
        }
    };

    public static void locationChecker(GoogleApiClient mGoogleApiClient, final Activity activity) {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        // requests here.

                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(
                                    activity, REQ_LOCATION_HIGH);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE: {
                        Toast.makeText(activity, "Unavail", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    // Location settings are not satisfied. However, we have no way to fix the
                    // settings so we won't show the dialog.
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_LOCATION_HIGH && resultCode == 0) {
            Toast.makeText(mContext, "Harap aktifkan GPS dengan mode \"High Accuracy\" untuk melanjutkan penggunaan aplikasi", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        mContext = this;
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .enableAutoManage(this, 34992, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                    }
                })
                .addApi(LocationServices.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(@Nullable Bundle bundle) {

                    }

                    @Override
                    public void onConnectionSuspended(int i) {

                    }
                })
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                    }
                })
                .build();

        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        pref = new Pref(this);
        driverModel = pref.getDriverModel();
        kendaraanModel = pref.getKendaraan();
        filter = new IntentFilter(LocationBroadcaster.LOCATION_BROADCAST_ACTION);

        if (getSupportActionBar() != null && driverModel != null && kendaraanModel != null) {
            getSupportActionBar().setSubtitle(driverModel.getUsername() + " | " + kendaraanModel.getIdNopol());
            getSupportActionBar().setElevation(0);
        }

        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        locationChecker(mGoogleApiClient, this);
        if (!statusOfGPS) {
//            processStopLocation();

        }

        Intent intent = new Intent(this, LocationBroadcaster.class);
        startService(intent);


        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectedFragment = null;
                switch (item.getItemId()) {
                    case R.id.navigation_beranda:
                        selectedFragment = new BerandaFragment();
                        break;

                    case R.id.navigation_riwayat:
                        selectedFragment = RiwayatFragment.newInstance();
                        break;

                    case R.id.navigation_ringkasan:
                        selectedFragment = RingkasanFragment.newInstance();
                        break;

                    case R.id.navigation_reject:
                        selectedFragment = RejectFragment.newInstance();
                        break;

                    case R.id.navigation_akun:
                        selectedFragment = AkunFragment.newInstance();
                        break;
                }
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, selectedFragment);
                transaction.commit();
                return true;
            }
        });

        //tampilkan fragment pertama secara manual
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, new BerandaFragment());
        transaction.commit();

        if (isMockSettingsON(getApplicationContext())) {
            Toast.makeText(this, "Mock location anda sedang aktif", Toast.LENGTH_LONG).show();
        }

        // Handle notification
        final String id = getIntent().getStringExtra(FcmMessagingService.INTENT_ID_DATA);
        String type = getIntent().getStringExtra(FcmMessagingService.NOTIF_TYPE);
        if (id != null && type.equals("blast")) {
            if (selectedFragment != null && selectedFragment instanceof BerandaFragment) {
                ((BerandaFragment) selectedFragment).fetchDashboard();
//                ((BerandaFragment) selectedFragment).fetchListJob();
            }
            final ProgressDialog pdNotif = new ProgressDialog(this);
            pdNotif.setMessage("Memproses notifikasi");
            new Netter(this).webService(Request.Method.POST, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    JSONObject obj = null;
                    pdNotif.dismiss();
                    try {
                        obj = new JSONObject(response);
                        SimpleJob simpleJob = new Gson().fromJson(obj.getString("job-" + id), SimpleJob.class);

                        if (simpleJob != null) {
                            if (simpleJob.getFleetDriverId() == Integer.parseInt(driverModel.getIdDriver()) && simpleJob.getFleetNopol().equals(kendaraanModel.getIdNopol())) {

                                if (simpleJob.getJobType() <= 2) {
                                    ActivityProses1And2.startProses(mContext, simpleJob);
                                } else if (simpleJob.getJobType() <= 3) {
                                    ActivityProses3.startProses(mContext, simpleJob);
                                } else if (simpleJob.getJobType() <= 7) {
                                    ActivityProsesFrom4To7.startProses(mContext, simpleJob);
                                } else {
                                    ActivityProsesMoreThan8.startProses(mContext, simpleJob);
                                }
                            } else {
                                new AlertDialog.Builder(DashboardActivity.this).setMessage("Job telah kadaluarsa").show();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, Netter.getDefaultErrorListener(this, new Runnable() {
                @Override
                public void run() {
                    pdNotif.dismiss();
                }
            }), Netter.Webservice.DETAILPICKUP, new StringHashMap().putMore("id", id));
        }
        checkObsoletePassword();
    }


    @Override
    public void onStart() {
        super.onStart();
        isMockSettingsON(DashboardActivity.this);
        areThereMockPermissionApps(DashboardActivity.this);
        registerReceiver(br, filter);
    }

    private void checkObsoletePassword() {
        final ProgressDialog pdPass = new ProgressDialog(mContext);
        pdPass.setMessage("Mengubah password");

        if (!TextUtils.isEmpty(driverModel.getLastChangePassword())) {
            long lastChange = DF.parse(driverModel.getLastChangePassword()).getTime();
            Log.i("DATE_NOW", "" + System.currentTimeMillis());
            Log.i("DATE_LAST", "" + (lastChange));
            Log.i("DATE_Should_EXP", "" + (lastChange + FORCE_CHANGE_PASS_THRESHOLD));

            if (lastChange + FORCE_CHANGE_PASS_THRESHOLD <= System.currentTimeMillis()) {
                View vPass = LayoutInflater.from(this).inflate(R.layout.alert_change_password, null, false);
                final EditText iOldPass = vPass.findViewById(R.id.iOldPass);
                final EditText iNewPass = vPass.findViewById(R.id.iNewPass);
                final EditText iNewPass2 = vPass.findViewById(R.id.iNewPass2);
                final AlertDialog alertDialog = new AlertDialog.Builder(this)
                        .setTitle("Ubah password")
                        .setView(vPass)
                        .setPositiveButton("Simpan", null)
                        .setNegativeButton("Batal", null).create();
                alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // checking
                                if (iOldPass.getText().toString().isEmpty() || iNewPass.getText().toString().isEmpty() || iNewPass2.getText().toString().isEmpty()) {
                                    toast("Harap isi seluruh form");
                                    return;
                                }
                                if (!iOldPass.getText().toString().equals(driverModel.getPassword())) {
                                    toast("Kata sandi lama tidak sesuai");
                                    return;
                                }
                                if (!iNewPass.getText().toString().equals(iNewPass2.getText().toString())) {
                                    toast("Kata sandi baru tidak sama, cek kembali");
                                    return;
                                }

                                pdPass.show();
                                new Netter(mContext).webService(Request.Method.POST, new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                pdPass.dismiss();
                                                try {
                                                    JSONObject obj = new JSONObject(response);
                                                    toast(obj.getString("message"));
                                                    if (obj.getInt("status") == 200) {
                                                        driverModel.setPassword(iNewPass.getText().toString());
                                                        driverModel.setLastChangePassword(DF.format(DF.ASP_FORMAT, System.currentTimeMillis()));
                                                        pref.putModelDriver(driverModel);
                                                        alertDialog.dismiss();
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }, Netter.getDefaultErrorListener(mContext, new Runnable() {
                                            @Override
                                            public void run() {

                                            }
                                        }), Netter.Webservice.UPDATEPASSWORD, new StringHashMap()
                                                .putMore("newpassword", iNewPass.getText().toString())
                                                .putMore("oldpassword", iOldPass.getText().toString())
                                                .putMore("email", driverModel.getEmail())
                                );

                            }
                        });
                    }
                });
                alertDialog.show();
            } else {
                driverModel.setLastChangePassword(DF.format(DF.ASP_FORMAT, System.currentTimeMillis()));
                pref.putModelDriver(driverModel);
            }
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        unregisterReceiver(br);
//        googleApiClient.disconnect();
    }

    Boolean twice = false;

    @Override
    public void onBackPressed() { //bisa dipake terus
        if (twice) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }

        twice = true;

        Toast.makeText(DashboardActivity.this, "Press back again to exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                twice = false;
            }
        }, 3000);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PERMISSION_GRANTED) {
                } else
                    Toast.makeText(this, "Locatiion Permission Denied", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public static boolean isMockSettingsON(Context context) {

        return !Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ALLOW_MOCK_LOCATION).equals("0");
    }

    public static boolean areThereMockPermissionApps(Context context) {
        int count = 0;

        PackageManager pm = context.getPackageManager();
        List<ApplicationInfo> packages =
                pm.getInstalledApplications(PackageManager.GET_META_DATA);

        for (ApplicationInfo applicationInfo : packages) {
            try {
                PackageInfo packageInfo = pm.getPackageInfo(applicationInfo.packageName,
                        PackageManager.GET_PERMISSIONS);

                String[] requestedPermissions = packageInfo.requestedPermissions;

                if (requestedPermissions != null) {
                    for (int i = 0; i < requestedPermissions.length; i++) {
                        if (requestedPermissions[i]
                                .equals("android.permission.ACCESS_MOCK_LOCATION")
                                && !applicationInfo.packageName.equals(context.getPackageName())) {
                            count++;
                        }
                    }
                }
            } catch (PackageManager.NameNotFoundException e) {
                Log.e("Got exception ", e.getMessage());
            }
        }

        if (count > 0)
            return true;
        return false;
    }


    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("This app needs GPS for sharing location. Please turn on the GPS")
                .setCancelable(false)
                .setTitle("Hi!")
                .setPositiveButton("Enable",
                        new DialogInterface.OnClickListener() {
                            public void onClick(
                                    @SuppressWarnings("unused") final DialogInterface dialog,
                                    @SuppressWarnings("unused") final int id) {
                                startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog,
                                                @SuppressWarnings("unused") final int id) {
                            }
                        });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

}
