package com.mbo.counter.counterdetail;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.mbo.counter.R;
import com.mbo.counter.utils.Utils;

public class CounterDetailActivity extends AppCompatActivity
{
    public static final String EXTRA_COUNTER_ID = "EXTRA_COUNTER_ID";
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
            mActionBar.setTitle("Counter detail");
        }

        CounterDetailFragment counterDetailFragment = (CounterDetailFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (counterDetailFragment == null)
        {
            // Create the fragment
            counterDetailFragment = CounterDetailFragment.newInstance();
            Utils.addFragmentToActivity(getSupportFragmentManager(), counterDetailFragment, R.id.contentFrame);
        }
    }

    @Override
    public boolean onSupportNavigateUp()
    {
        onBackPressed();
        return true;
    }
}
