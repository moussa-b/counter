package com.bdzapps.counterpp;

import android.app.Application;

import com.bdzapps.counterpp.data.source.ormlite.OrmLiteDataSource;
import com.bdzapps.counterpp.data.source.ormlite.OrmLiteHelper;
import com.crashlytics.android.Crashlytics;
import com.mbo.counter.BuildConfig;

import io.fabric.sdk.android.Fabric;

public class CounterApplication extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();
        if (!BuildConfig.DEBUG)
            Fabric.with(this, new Crashlytics());
        OrmLiteDataSource.initialize(new OrmLiteHelper(getApplicationContext()));
    }
}
