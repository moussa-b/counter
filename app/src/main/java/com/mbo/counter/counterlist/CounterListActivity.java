package com.mbo.counter.counterlist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mbo.counter.commons.Utils;
import com.mbo.counter.R;
import com.mbo.counter.data.source.ormlite.OrmLiteDataSource;

public class CounterListActivity extends AppCompatActivity
{
    private CounterListPresenter mCounterListPresenter;

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

            Utils.addFragmentToActivity(getSupportFragmentManager(), counterListFragment, R.id.contentFrame);

            mCounterListPresenter = new CounterListPresenter(OrmLiteDataSource.getInstance(), counterListFragment);
        }
    }
}
