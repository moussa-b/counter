package com.mbo.counter.countergrouplist;

import android.support.annotation.NonNull;

import com.mbo.counter.data.model.CounterGroup;
import com.mbo.counter.data.source.CounterDataSource;
import com.mbo.counter.data.source.ormlite.OrmLiteDataSource;

import java.util.List;

public class CounterGroupListPresenter implements CounterGroupListContract.Presenter
{
    @NonNull
    private final CounterDataSource mCounterDataSource;

    @NonNull
    private final CounterGroupListContract.View mCounterGroupListView;

    public CounterGroupListPresenter(@NonNull OrmLiteDataSource counterDataSource, @NonNull CounterGroupListContract.View counterListView)
    {
        this.mCounterDataSource = counterDataSource;
        this.mCounterGroupListView = counterListView;
        mCounterGroupListView.setPresenter(this);
    }

    @Override
    public void start()
    {
        loadCounterGroups();
    }

    @Override
    public void loadCounterGroups()
    {
        mCounterDataSource.getCounterGroups(new CounterDataSource.LoadCounterGroupsCallback()
        {
            @Override
            public void onCounterGroupsLoaded(List<CounterGroup> counterGroups)
            {
                processCounterGroups(counterGroups);
            }

            @Override
            public void onDataNotAvailable()
            {

            }
        });
    }

    private void processCounterGroups(List<CounterGroup> counterGroups)
    {
        if (counterGroups.isEmpty())
        {
            // Show a message indicating there are no counter groups
            processEmptyCounters();
        }
        else
        {
            // Show the list of counter groups
            mCounterGroupListView.showCounterGroups(counterGroups);
        }
    }

    private void processEmptyCounters()
    {
        mCounterGroupListView.showNoCounterGroups();
    }
}
