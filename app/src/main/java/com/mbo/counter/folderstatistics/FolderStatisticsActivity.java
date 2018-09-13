package com.mbo.counter.folderstatistics;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.mbo.counter.R;
import com.mbo.counter.commons.Utils;

public class FolderStatisticsActivity extends AppCompatActivity
{
    private ActionBar mActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.folder_statistics_activity);

        // Set up the toolbar
        mActionBar = getSupportActionBar();
        if (mActionBar != null)
        {
            mActionBar.setDisplayHomeAsUpEnabled(true);
            mActionBar.setDisplayShowHomeEnabled(true);
            mActionBar.setTitle(getResources().getString(R.string.statistics));
        }

        FolderStatisticsFragment settingsFragment = (FolderStatisticsFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (settingsFragment == null)
        {
            // Create the fragment
            settingsFragment = FolderStatisticsFragment.newInstance();
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
