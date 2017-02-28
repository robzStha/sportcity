package com.app.sportcity.applications;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.multidex.MultiDexApplication;

import com.app.sportcity.R;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by bugatti on 09/11/16.
 */

public class MyApplication extends MultiDexApplication {

    private static MyApplication instance;
    @Override
    public void onCreate() {
        super.onCreate();
        instance= this;

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/nsr.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }

    public static MyApplication getInstance(){
        return instance;
    }

    public static boolean hasNetwork ()
    {
        return instance.checkIfHasNetwork();
    }

    public boolean checkIfHasNetwork()
    {
        ConnectivityManager cm = (ConnectivityManager) getSystemService( Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

}
