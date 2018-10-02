package com.bdzapps.counterpp;

import android.app.Application;

import com.bdzapps.counterpp.data.source.ormlite.OrmLiteDataSource;
import com.bdzapps.counterpp.data.source.ormlite.OrmLiteHelper;

public class CounterApplication extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();
        OrmLiteDataSource.initialize(new OrmLiteHelper(getApplicationContext()));
    }
}
