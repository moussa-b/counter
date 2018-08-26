package com.mbo.counter.settings;

import android.support.annotation.NonNull;

public class SettingsPresenter implements SettingsContract.Presenter
{
    @NonNull
    private final SettingsContract.View mSettingsView;

    public SettingsPresenter(@NonNull SettingsContract.View settingsView)
    {
        this.mSettingsView = settingsView;
    }

    @Override
    public void start()
    {

    }
}
