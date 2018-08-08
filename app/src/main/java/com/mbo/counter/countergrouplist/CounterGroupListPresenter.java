package com.mbo.counter.countergrouplist;

import android.support.annotation.NonNull;

import com.mbo.counter.data.model.Counter;
import com.mbo.counter.data.model.CounterGroup;
import com.mbo.counter.data.model.Statistics;
import com.mbo.counter.data.model.StatisticsType;
import com.mbo.counter.data.source.CounterDataSource;
import com.mbo.counter.data.source.ormlite.OrmLiteDataSource;

import java.util.Date;
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
        mCounterDataSource.getCounter(counterId, new CounterDataSource.GetCounterCallback()
        {
            @Override
            public void onCounterLoaded(Counter counter)
            {
                if ((counter.getCount() - counter.getStep()) >= 0)
                    counter.setCount(counter.getCount() - counter.getStep());
                else
                    counter.setCount(counter.getLimit() + counter.getCount() - counter.getStep());

                mCounterDataSource.saveCounter(counter);
                mCounterDataSource.addStatistics(new Statistics(new Date(), counterId, -counter.getStep(), StatisticsType.DECREMENT));
                mCounterGroupListView.setCount(groupPosition, childPosition, counter.getCount());

            }

            @Override
            public void onDataNotAvailable()
            {

            }
        });
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
        mCounterDataSource.getCounter(counterId, new CounterDataSource.GetCounterCallback()
        {
            @Override
            public void onCounterLoaded(Counter counter)
            {
                if ((counter.getCount() + counter.getStep()) <= counter.getLimit())
                    counter.setCount(counter.getCount() + counter.getStep());
                else
                    counter.setCount((counter.getCount() + counter.getStep()) - counter.getLimit());

                mCounterDataSource.saveCounter(counter);
                mCounterDataSource.addStatistics(new Statistics(new Date(), counterId, counter.getStep(), StatisticsType.INCREMENT));
                mCounterGroupListView.setCount(groupPosition, childPosition, counter.getCount());
            }

            @Override
            public void onDataNotAvailable()
            {

            }
        });
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

    @Override
    public void resetCounter(final int groupPosition, final int childPosition, final int counterId)
    {
        mCounterDataSource.getCounter(counterId, new CounterDataSource.GetCounterCallback()
        {
            @Override
            public void onCounterLoaded(Counter counter)
            {
                counter.setCount(0);
                mCounterDataSource.saveCounter(counter);
                mCounterDataSource.addStatistics(new Statistics(new Date(), counterId, 0, StatisticsType.RESET));
                mCounterGroupListView.setCount(groupPosition, childPosition, 0);
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
