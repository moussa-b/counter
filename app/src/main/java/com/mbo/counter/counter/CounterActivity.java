package com.mbo.counter.counter;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.Menu;

import com.mbo.counter.R;
import com.mbo.counter.commons.Utils;
import com.mbo.counter.data.source.ormlite.OrmLiteDataSource;

import static com.mbo.counter.counter.CounterFragment.ARGUMENT_COUNTER_ID;

public class CounterActivity extends AppCompatActivity
{
    private ActionBar mActionBar;

    private CounterPresenter mCounterPresenter;

    private CounterFragment mCounterView;

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

        mCounterView = (CounterFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (mCounterView == null)
        {
            // Create the fragment
            mCounterView = CounterFragment.newInstance();

            int counterId = 0;
            if (getIntent().hasExtra(ARGUMENT_COUNTER_ID))
                counterId = getIntent().getIntExtra(ARGUMENT_COUNTER_ID, 0);

            Utils.addFragmentToActivity(getSupportFragmentManager(), mCounterView, R.id.contentFrame);

            mCounterPresenter = new CounterPresenter(counterId, OrmLiteDataSource.getInstance(), mCounterView);
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isVolumeButtonEnabled = sharedPreferences.getBoolean(getString(R.string.key_count_volume_buttons), false);
        if (isVolumeButtonEnabled)
        {
            if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)
            {
                mCounterView.decrementCounter();
                return true;
            }
            else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP)
            {
                mCounterView.incrementCounter();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
