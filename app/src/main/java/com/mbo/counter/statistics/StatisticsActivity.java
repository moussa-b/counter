package com.mbo.counter.statistics;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.mbo.commons.utils.Utils;
import com.mbo.counter.R;
import com.mbo.counter.data.source.ormlite.OrmLiteDataSource;

public class StatisticsActivity extends AppCompatActivity
{
    private ActionBar mActionBar;

    private StatisticsPresenter mStatisticsPresenter;

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
            mActionBar.setTitle("Statistics");
        }

        StatisticsFragment statisticsFragment = (StatisticsFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (statisticsFragment == null)
        {
            // Create the fragment
            statisticsFragment = StatisticsFragment.newInstance();

            int counterId = 0;
            if (getIntent().hasExtra(StatisticsFragment.ARGUMENT_STATISTICS_COUNTER_ID))
                counterId = getIntent().getIntExtra(StatisticsFragment.ARGUMENT_STATISTICS_COUNTER_ID, 0);

            Utils.addFragmentToActivity(getSupportFragmentManager(), statisticsFragment, R.id.contentFrame);

            mStatisticsPresenter = new StatisticsPresenter(counterId, OrmLiteDataSource.getInstance(), statisticsFragment);
        }
    }

    @Override
    public boolean onSupportNavigateUp()
    {
        onBackPressed();
        return true;
    }
}
