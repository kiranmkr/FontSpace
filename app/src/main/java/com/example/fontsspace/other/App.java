package com.example.fontsspace.other;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LifecycleObserver;
import androidx.multidex.MultiDexApplication;

import com.example.fontsspace.billing.GBilling;


@SuppressLint("StaticFieldLeak")
public class App extends MultiDexApplication implements LifecycleObserver {

    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        Log.d("myApplication", "onCreate App");
        GBilling.initializeInAppClass(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

}
