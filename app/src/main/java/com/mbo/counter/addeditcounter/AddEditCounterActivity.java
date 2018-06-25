package com.mbo.counter.addeditcounter;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.mbo.counter.R;
import com.mbo.counter.data.source.CounterRepository;
import com.mbo.counter.utils.Utils;

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

            String counterId = null;

            if (getIntent().hasExtra(AddEditCounterFragment.ARGUMENT_EDIT_COUNTER_ID))
            {
                counterId = getIntent().getStringExtra(AddEditCounterFragment.ARGUMENT_EDIT_COUNTER_ID);
            }

            Utils.addFragmentToActivity(getSupportFragmentManager(), addEditFragment, R.id.contentFrame);

            mAddEditCounterPresenter = new AddEditCounterPresenter(counterId, CounterRepository.getInstance(), addEditFragment);
        }
    }

    @Override
    public boolean onSupportNavigateUp()
    {
        onBackPressed();
        return true;
    }
}
