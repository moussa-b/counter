package com.mbo.counter.counterlist;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.view.WindowManager;

import com.mbo.counter.R;
import com.mbo.counter.commons.Utils;
import com.mbo.counter.data.source.ormlite.OrmLiteDataSource;

public class CounterListActivity extends AppCompatActivity
{
    private CounterListPresenter mCounterListPresenter;
    private ActionBar mActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.counter_list_activity);

        CounterListFragment counterListFragment = (CounterListFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (counterListFragment == null)
        {
            // Create the fragment
            counterListFragment = CounterListFragment.newInstance();

            int folderId = 0;
            if (getIntent().hasExtra(CounterListFragment.ARGUMENT_FOLDER_ID))
                folderId = getIntent().getIntExtra(CounterListFragment.ARGUMENT_FOLDER_ID, 0);

            if (folderId != 0 && getIntent().hasExtra(CounterListFragment.ARGUMENT_FOLDER_NAME))
            {
                String folderName = getIntent().getStringExtra(CounterListFragment.ARGUMENT_FOLDER_NAME);
                if (folderName == null)
                    folderName = getString(R.string.counter_list);

                // Set up the toolbar
                mActionBar = getSupportActionBar();
                if (mActionBar != null)
                {
                    mActionBar.setDisplayHomeAsUpEnabled(true);
                    mActionBar.setDisplayShowHomeEnabled(true);
                    mActionBar.setTitle(folderName);
                }
            }

            Utils.addFragmentToActivity(getSupportFragmentManager(), counterListFragment, R.id.contentFrame);

            mCounterListPresenter = new CounterListPresenter(OrmLiteDataSource.getInstance(), counterListFragment, folderId);

            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            final boolean isScreenAlwaysOnEnabled = sharedPreferences.getBoolean(getString(R.string.key_screen_always_on), false);
            if (isScreenAlwaysOnEnabled)
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }

    @Override
    public boolean onSupportNavigateUp()
    {
        onBackPressed();
        finish();
        return true;
    }
}
