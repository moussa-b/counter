package com.mbo.counter;

import android.app.Application;

import com.mbo.counter.data.source.ormlite.OrmLiteDataSource;
import com.mbo.counter.data.source.ormlite.OrmLiteHelper;

public class CounterApplication extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();
        OrmLiteDataSource.initialize(new OrmLiteHelper(getApplicationContext()));
    }
}
