package com.bdzapps.counterpp.counter;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.WindowManager;

import com.bdzapps.counterpp.commons.Utils;
import com.bdzapps.counterpp.data.source.ormlite.OrmLiteDataSource;
import com.mbo.counter.R;

import static com.bdzapps.counterpp.counter.CounterFragment.ARGUMENT_COUNTER_ID;

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
            mActionBar.setTitle(R.string.counter);
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

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        final boolean isScreenAlwaysOnEnabled = sharedPreferences.getBoolean(getString(R.string.key_screen_always_on), false);
        if (isScreenAlwaysOnEnabled)
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
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
