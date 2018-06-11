package com.spil.dev.tms.Font;

import android.app.Application;

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        TypeFaceUtil.overrideFont(getApplicationContext(), "ROBOTO", "fonts/roboto_slab_bold.ttf"); // font from assets: "assets/fonts/Roboto-Regular.ttf
    }
}
