package com.bdzapps.counterpp.addeditcounter;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.view.WindowManager;

import com.bdzapps.counterpp.commons.Utils;
import com.bdzapps.counterpp.counterlist.CounterListFragment;
import com.bdzapps.counterpp.data.source.ormlite.OrmLiteDataSource;
import com.mbo.counter.R;

public class AddEditCounterActivity extends AppCompatActivity
{
    public static final int REQUEST_ADD_COUNTER = 100;

    private ActionBar mActionBar;

    private AddEditCounterPresenter mAddEditCounterPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_edit_counter_activity);

        // Set up the toolbar
        mActionBar = getSupportActionBar();
        if (mActionBar != null)
        {
            mActionBar.setDisplayHomeAsUpEnabled(true);
            mActionBar.setDisplayShowHomeEnabled(true);
            mActionBar.setTitle("Add / Edit counter");
        }

        AddEditCounterFragment addEditFragment = (AddEditCounterFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (addEditFragment == null)
        {
            // Create the fragment
            addEditFragment = AddEditCounterFragment.newInstance();

            int counterId = 0;
            int folderId = 0;
            if (getIntent().hasExtra(AddEditCounterFragment.ARGUMENT_EDIT_COUNTER_ID))
                counterId = getIntent().getIntExtra(AddEditCounterFragment.ARGUMENT_EDIT_COUNTER_ID, 0);
            if (getIntent().hasExtra(CounterListFragment.ARGUMENT_FOLDER_ID))
                folderId = getIntent().getIntExtra(CounterListFragment.ARGUMENT_FOLDER_ID, 0);

            Utils.addFragmentToActivity(getSupportFragmentManager(), addEditFragment, R.id.contentFrame);

            mAddEditCounterPresenter = new AddEditCounterPresenter(counterId, folderId, OrmLiteDataSource.getInstance(), addEditFragment);
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
}
