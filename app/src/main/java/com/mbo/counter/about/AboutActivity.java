package com.mbo.counter.about;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.mbo.commons.utils.Utils;
import com.mbo.counter.R;

public class AboutActivity extends AppCompatActivity
{

    private ActionBar mActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.counter_activity);

        // Set up the toolbar
        mActionBar = getSupportActionBar();
        if (mActionBar != null)
        {
            mActionBar.setDisplayHomeAsUpEnabled(true);
            mActionBar.setDisplayShowHomeEnabled(true);
            mActionBar.setTitle(getString(R.string.about) + " " + getString(R.string.app_name));
        }

        AboutFragment aboutFragment = (AboutFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (aboutFragment == null)
        {
            // Create the fragment
            aboutFragment = AboutFragment.newInstance();
            Utils.addFragmentToActivity(getSupportFragmentManager(), aboutFragment, R.id.contentFrame);
        }
    }

    @Override
    public boolean onSupportNavigateUp()
    {
        onBackPressed();
        return true;
    }
}
