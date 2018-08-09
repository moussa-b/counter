package com.mbo.counter.counterlist;

import android.support.annotation.NonNull;

import com.mbo.counter.data.model.Counter;
import com.mbo.counter.data.source.CounterDataSource;
import com.mbo.counter.data.source.ormlite.OrmLiteDataSource;

import java.util.List;

public class CounterListPresenter implements CounterListContract.Presenter
{
    @NonNull
    private final CounterDataSource mCounterDataSource;

    @NonNull
    private final CounterListContract.View mCounterListView;

    public CounterListPresenter(@NonNull OrmLiteDataSource counterDataSource, @NonNull CounterListContract.View counterListView)
    {
        this.mCounterDataSource = counterDataSource;
        this.mCounterListView = counterListView;
        mCounterListView.setPresenter(this);
    }

    @Override
    public void start()
    {
        loadCounters();
    }

    @Override
    public void decrementCounter(final int position, final int counterId, int limit)
    {
        int count = mCounterDataSource.decrementCounter(counterId);
        mCounterListView.setCount(position, count);
        int progression = (int) (100 * (float) count / ((float) limit));
        mCounterListView.setProgression(position, progression);

    }

    @Override
    public void deleteCounter(int counterId)
    {
        mCounterDataSource.deleteCounter(counterId);
        loadCounters();
    }

    @Override
    public void duplicateCounter(int counterId)
    {
        mCounterDataSource.duplicateCounter(counterId);
        loadCounters();
    }

    @Override
    public void incrementCounter(final int position, final int counterId, int limit)
    {
        int count = mCounterDataSource.incrementCounter(counterId);
        mCounterListView.setCount(position, count);
        int progression = (int) (100 * (float) count / ((float) limit));
        mCounterListView.setProgression(position, progression);
    }

    @Override
    public void loadCounters()
    {
        mCounterDataSource.getCounters(new CounterDataSource.LoadCountersCallback()
        {
            @Override
            public void onCountersLoaded(List<Counter> counters)
            {
                processCounters(counters);
            }

            @Override
            public void onDataNotAvailable()
            {

            }
        });
    }

    @Override
    public void resetCounter(final int position, final int counterId)
    {
        mCounterDataSource.resetCounter(counterId);
        mCounterListView.setCount(position, 0);
        mCounterListView.setProgression(position, 0);
    }

    private void processCounters(List<Counter> counters)
    {
        if (counters.isEmpty())
        {
            // Show a message indicating there are no tasks for that filter type.
            processEmptyCounters();
        }
        else
        {
            // Show the list of tasks
            mCounterListView.showCounters(counters);
        }
    }

    private void processEmptyCounters()
    {
        mCounterListView.showNoCounters();
    }
}
