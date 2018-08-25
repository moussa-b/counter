package com.mbo.counter.counter;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;

import com.mbo.counter.utils.Utils;
import com.mbo.counter.R;
import com.mbo.counter.data.source.ormlite.OrmLiteDataSource;

import static com.mbo.counter.counter.CounterFragment.ARGUMENT_COUNTER_ID;

public class CounterActivity extends AppCompatActivity
{
    private ActionBar mActionBar;

    private CounterPresenter mCounterPresenter;

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
            mActionBar.setTitle("Counter");
        }

        CounterFragment counterFragment = (CounterFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (counterFragment == null)
        {
            // Create the fragment
            counterFragment = CounterFragment.newInstance();

            int counterId = 0;
            if (getIntent().hasExtra(ARGUMENT_COUNTER_ID))
                counterId = getIntent().getIntExtra(ARGUMENT_COUNTER_ID, 0);

            Utils.addFragmentToActivity(getSupportFragmentManager(), counterFragment, R.id.contentFrame);

            mCounterPresenter = new CounterPresenter(counterId, OrmLiteDataSource.getInstance(), counterFragment);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_counter, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp()
    {
        onBackPressed();
        return true;
    }
}
