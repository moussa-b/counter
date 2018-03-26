package com.mbo.counter.statistics;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.mbo.counter.R;
import com.mbo.counter.utils.Utils;

public class StatisticsActivity extends AppCompatActivity {

    private ActionBar mActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.counter_activity);

        // Set up the toolbar
        mActionBar = getSupportActionBar();
        if (mActionBar != null) {
            mActionBar.setDisplayHomeAsUpEnabled(true);
            mActionBar.setDisplayShowHomeEnabled(true);
            mActionBar.setTitle("Statistics");
        }

        StatisticsFragment statisticsFragment = (StatisticsFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (statisticsFragment == null) {
            // Create the fragment
            statisticsFragment = StatisticsFragment.newInstance();
            Utils.addFragmentToActivity(getSupportFragmentManager(), statisticsFragment, R.id.contentFrame);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
