package com.mbo.counter.countergrouplist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mbo.commons.utils.Utils;
import com.mbo.counter.R;
import com.mbo.counter.data.source.ormlite.OrmLiteDataSource;

public class CounterGroupListActivity extends AppCompatActivity
{
    private CounterGroupListPresenter mCounterGroupListPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.counter_list_activity);

        CounterGroupListFragment counterGroupListFragment = (CounterGroupListFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (counterGroupListFragment == null)
        {
            // Create the fragment
            counterGroupListFragment = CounterGroupListFragment.newInstance();

            Utils.addFragmentToActivity(getSupportFragmentManager(), counterGroupListFragment, R.id.contentFrame);

            mCounterGroupListPresenter = new CounterGroupListPresenter(OrmLiteDataSource.getInstance(), counterGroupListFragment);
        }
    }
}
