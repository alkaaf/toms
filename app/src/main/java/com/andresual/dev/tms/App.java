package com.andresual.dev.tms;

import android.app.Application;
import android.content.Context;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.decoder.SimpleProgressiveJpegConfig;

import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;
import org.acra.sender.HttpSender;

@ReportsCrashes(/*mailTo = "alfa.alkaaf@gmail.com",*/
        httpMethod = HttpSender.Method.PUT,
        reportType = HttpSender.Type.JSON,
        formUri = "http://35.187.154.93:5984/acra-toms/_design/acra-storage/_update/report",
//        formUri = "http://35.187.154.93:5984/acra-smsjin/_design/acra-storage/_update/report",
        formUriBasicAuthLogin = "dalbo",
        formUriBasicAuthPassword = "dalbo",
        mode = ReportingInteractionMode.TOAST,
        resToastText = R.string.crash_dialog_title
        /*customReportContent = {ReportField.USER_COMMENT, ReportField.APP_VERSION_CODE, ReportField.APP_VERSION_NAME, ReportField.ANDROID_VERSION, ReportField.PHONE_MODEL, ReportField.CUSTOM_DATA, ReportField.STACK_TRACE},
        mode = ReportingInteractionMode.TOAST,
        resToastText = R.string.crash_dialog_title*/)
public class App extends Application {
    public static Context context;
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
