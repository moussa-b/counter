package com.mbo.counter.countersettings;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.mbo.counter.utils.Utils;
import com.mbo.counter.R;

public class CounterSettingsActivity extends AppCompatActivity
{

    private ActionBar mActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.counter_settings_activity);

        // Set up the toolbar
        mActionBar = getSupportActionBar();
        if (mActionBar != null)
        {
            mActionBar.setDisplayHomeAsUpEnabled(true);
            mActionBar.setDisplayShowHomeEnabled(true);
            mActionBar.setTitle("Settings");
        }

        CounterSettingsFragment settingsFragment = (CounterSettingsFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (settingsFragment == null)
        {
            // Create the fragment
            settingsFragment = CounterSettingsFragment.newInstance();
            Utils.addFragmentToActivity(getSupportFragmentManager(), settingsFragment, R.id.contentFrame);
        }
    }

    @Override
    public boolean onSupportNavigateUp()
    {
        onBackPressed();
        return true;
    }
}
