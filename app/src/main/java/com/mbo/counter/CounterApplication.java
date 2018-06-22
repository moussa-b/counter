package com.mbo.counter;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class CounterApplication extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration realmConfig = new RealmConfiguration.Builder()
                .name("counter.realm")
                .schemaVersion(0)
                .build();
        Realm.setDefaultConfiguration(realmConfig);
    }
}
