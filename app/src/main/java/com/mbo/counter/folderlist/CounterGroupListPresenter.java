package com.mbo.counter.folderlist;

import android.support.annotation.NonNull;

import com.mbo.counter.data.model.Folder;
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
    public void decrementCounter(final int groupPosition, final int childPosition, final int counterId)
    {
        mCounterGroupListView.setCount(groupPosition, childPosition, mCounterDataSource.decrementCounter(counterId));
    }

    @Override
    public void deleteCounter(int counterId)
    {
        mCounterDataSource.deleteCounter(counterId);
        loadCounterGroups();
    }

    @Override
    public void deleteCounterGroup(int counterGroupId)
    {
        mCounterDataSource.deleteCounterGroup(counterGroupId);
        loadCounterGroups();
    }

    @Override
    public void deleteCountersInGroup(int counterGroupId)
    {
        mCounterDataSource.deleteCountersInGroup(counterGroupId);
        loadCounterGroups();
    }

    @Override
    public void duplicateCounter(int counterId)
    {
        mCounterDataSource.duplicateCounter(counterId);
        loadCounterGroups();
    }

    @Override
    public void duplicateCounterGroup(int counterGroupId)
    {
        mCounterDataSource.duplicateCounterGroup(counterGroupId);
        loadCounterGroups();
    }

    @Override
    public void incrementCounter(final int groupPosition, final int childPosition, final int counterId)
    {
        mCounterGroupListView.setCount(groupPosition, childPosition, mCounterDataSource.incrementCounter(counterId));
    }

    @Override
    public void loadCounterGroups()
    {
        mCounterDataSource.getCounterGroups(new CounterDataSource.LoadCounterGroupsCallback()
        {
            @Override
            public void onCounterGroupsLoaded(List<Folder> folders)
            {
                processCounterGroups(folders);
            }

            @Override
            public void onDataNotAvailable()
            {

            }
        });
    }

    @Override
    public void resetCounter(final int groupPosition, final int childPosition, final int counterId)
    {
        mCounterDataSource.resetCounter(counterId);
        mCounterGroupListView.setCount(groupPosition, childPosition, 0);
    }

    @Override
    public void resetCountersInGroup(int counterGroupId)
    {
        mCounterDataSource.resetCountersInGroup(counterGroupId);
        loadCounterGroups();
    }

    @Override
    public void saveCounterGroup(Folder folder)
    {
        mCounterDataSource.saveCounterGroup(folder);
    }

    private void processCounterGroups(List<Folder> folders)
    {
        if (folders.isEmpty())
        {
            // Show a message indicating there are no counter groups
            processEmptyCounters();
        }
        else
        {
            // Show the list of counter groups
            mCounterGroupListView.showCounterGroups(folders);
        }
    }

    private void processEmptyCounters()
    {
        mCounterGroupListView.showNoCounterGroups();
    }
}
