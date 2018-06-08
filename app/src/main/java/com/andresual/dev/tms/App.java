package com.andresual.dev.tms;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.widget.Toast;

import com.andresual.dev.tms.Activity.Maps.LocationBroadcaster;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.decoder.SimpleProgressiveJpegConfig;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;

import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;
import org.acra.sender.HttpSender;

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
        public void onReceive(Context context, Intent intent) {
            Log.i("TIME_TICK", "Time is tick tock");
//            startService(new Intent(App.this, LocationBroadcaster.class));
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
//        IntentFilter filter = new IntentFilter(Intent.ACTION_TIME_TICK);
//        registerReceiver(receiver, filter);
    }


    public static Context getContext() {
        return context;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        ACRA.init(this);
    }
}
