package com.mbo.counter.counterstatistics;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.mbo.counter.commons.Utils;
import com.mbo.counter.R;
import com.mbo.counter.data.source.ormlite.OrmLiteDataSource;

public class CounterStatisticsActivity extends AppCompatActivity
{
    private ActionBar mActionBar;

    private CounterStatisticsPresenter mStatisticsPresenter;

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

        CounterStatisticsFragment counterStatisticsFragment = (CounterStatisticsFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (counterStatisticsFragment == null)
        {
            // Create the fragment
            counterStatisticsFragment = CounterStatisticsFragment.newInstance();

            int counterId = 0;
            if (getIntent().hasExtra(CounterStatisticsFragment.ARGUMENT_STATISTICS_COUNTER_ID))
                counterId = getIntent().getIntExtra(CounterStatisticsFragment.ARGUMENT_STATISTICS_COUNTER_ID, 0);

            Utils.addFragmentToActivity(getSupportFragmentManager(), counterStatisticsFragment, R.id.contentFrame);

            mStatisticsPresenter = new CounterStatisticsPresenter(counterId, OrmLiteDataSource.getInstance(), counterStatisticsFragment);
        }
    }

    @Override
    public boolean onSupportNavigateUp()
    {
        onBackPressed();
        return true;
    }
}
