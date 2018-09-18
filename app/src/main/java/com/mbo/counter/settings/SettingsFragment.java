package com.mbo.counter.settings;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.mbo.counter.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class SettingsFragment extends PreferenceFragmentCompat implements SettingsContract.View
{

    public SettingsFragment()
    {
    }

    public static SettingsFragment newInstance()
    {
        return new SettingsFragment();
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey)
    {
        addPreferencesFromResource(R.xml.pref_main);
    }

    @Override
    public void setPresenter(SettingsContract.Presenter presenter)
    {

    }

}
