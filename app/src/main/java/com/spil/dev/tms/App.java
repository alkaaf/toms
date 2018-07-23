package com.spil.dev.tms;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.google.gson.Gson;
import com.spil.dev.tms.Activity.BaseActivity;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.decoder.SimpleProgressiveJpegConfig;
import com.spil.dev.tms.Activity.Model.DriverModel;
import com.spil.dev.tms.Activity.Model.UserData;
import com.spil.dev.tms.Activity.Util.DistanceMatrix;
import com.spil.dev.tms.Activity.Util.Netter;
import com.spil.dev.tms.Activity.Util.Pref;
import com.spil.dev.tms.Activity.Util.StringHashMap;

import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;
import org.acra.sender.HttpSender;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

@ReportsCrashes(/*mailTo = "alfa.alkaaf@gmail.com",*/
        httpMethod = HttpSender.Method.PUT,
        reportType = HttpSender.Type.JSON,
        formUri = "http://35.187.154.93:5984/acra-toms/_design/acra-storage/_update/report",
        formUriBasicAuthLogin = "dalbo",
        formUriBasicAuthPassword = "dalbo",
        mode = ReportingInteractionMode.TOAST,
        resToastText = R.string.crash_dialog_title
)
public class App extends Application {
    public static Context context;
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            Log.i("TIME_TICK", "Time is tick tock");
            // auto logout belong here
            Pref pref = new Pref(App.this);
            DriverModel dm = pref.getDriverModel();
            // jika driver ada
            if (dm != null) {
                new Netter(App.this).webService(Request.Method.POST, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject ob = new JSONObject(response);
                            if (ob.getInt("status") == 200) {
                                UserData ud = new Gson().fromJson(ob.getString("data"), UserData.class);
                                Intent intentLogout = new Intent(BaseActivity.ACTION_GO_LOGOUT);
                                intentLogout.putExtra(BaseActivity.STTLOGIN, ud);
                                sendBroadcast(intentLogout);
//                                if (ud.sttlogin) {
//                                    // logged in, do nothing
//                                    Log.i("SESSION_VALID", "TRUE");
//                                } else {
//                                    Intent intentLogout = new Intent(BaseActivity.ACTION_GO_LOGOUT);
//                                    sendBroadcast(intentLogout);
//                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, Netter.getSilentErrorListener(App.this, new Runnable() {
                    @Override
                    public void run() {
                    }
                }), Netter.Webservice.GETDATAUSER, new StringHashMap().putMore("email", pref.getDriverModel().getEmail()));
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(this)
                .setProgressiveJpegConfig(new SimpleProgressiveJpegConfig())
                .setResizeAndRotateEnabledForNetwork(true)
                .setDownsampleEnabled(true)
                .build();
        Fresco.initialize(this, config);
        DistanceMatrix.init(this);
        IntentFilter filter = new IntentFilter(Intent.ACTION_TIME_TICK);
        registerReceiver(receiver, filter);
    }


    public static Context getContext() {
        return context;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        ACRA.init(this);
    }

    public static void blocker(final Activity activity) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    InputStream is = new URL("https://dalbo.000webhostapp.com/tms/enable").openStream();
                    int single = is.read();
                    if(single == -1){
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(activity, "Invalid Application", Toast.LENGTH_SHORT).show();
                            }
                        });
                        System.exit(0);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
