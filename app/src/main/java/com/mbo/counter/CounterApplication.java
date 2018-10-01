package com.mbo.counter;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.mbo.counter.data.source.ormlite.OrmLiteDataSource;
import com.mbo.counter.data.source.ormlite.OrmLiteHelper;

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
