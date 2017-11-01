package com.writm.writm;

import android.app.Application;

import Utils.TypefaceUtil;

/**
 * Created by shash on 8/23/2017.
 */

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "josephregular.ttf"); // font from assets: "assets/fonts/Roboto-Regular.ttf
    }
}
