package com.app.sportcity.applications;

import android.app.Application;
import android.support.multidex.MultiDexApplication;

import com.app.sportcity.R;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by bugatti on 09/11/16.
 */

public class MyApplication extends MultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/nsr.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }
}
