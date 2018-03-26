package com.mbo.counter.counter;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.mbo.counter.R;
import com.mbo.counter.utils.Utils;

public class CounterActivity extends AppCompatActivity {

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
            mActionBar.setTitle("Counter");
        }

        CounterFragment counterFragment = (CounterFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (counterFragment == null) {
            // Create the fragment
            counterFragment = CounterFragment.newInstance();
            Utils.addFragmentToActivity(getSupportFragmentManager(), counterFragment, R.id.contentFrame);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
