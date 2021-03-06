package com.bdzapps.counterpp.counter;

import android.support.annotation.NonNull;

import com.bdzapps.counterpp.data.model.Counter;
import com.bdzapps.counterpp.data.source.CounterDataSource;

public class CounterPresenter implements CounterContract.Presenter
{
    @NonNull
    private final CounterDataSource mCounterDataSource;

    @NonNull
    private final CounterContract.View mCounterView;

    private int mCounterId;

    private Counter mCounter;

    public CounterPresenter(int counterId, @NonNull CounterDataSource counterDataSource,
                            @NonNull CounterContract.View counterView)
    {
        this.mCounterId = counterId;
        this.mCounterDataSource = counterDataSource;
        this.mCounterView = counterView;

        mCounterView.setPresenter(this);
    }

    @Override
    public void start()
    {
        populateCounter();
    }

    @Override
    public int getCounterId()
    {
        return mCounterId;
    }

    @Override
    public int getLimit()
    {
        return mCounter.getLimit();
    }

    @Override
    public int decrementCounter()
    {
        return mCounterDataSource.decrementCounter(mCounter.getId());
    }

    @Override
    public int incrementCounter()
    {
        return mCounterDataSource.incrementCounter(mCounter.getId());
    }

    @Override
    public void resetCounter()
    {
        mCounterDataSource.resetCounter(mCounter.getId());
    }

    @Override
    public void populateCounter()
    {
        mCounterDataSource.getCounter(mCounterId, new CounterDataSource.GetCounterCallback()
        {
            @Override
            public void onCounterLoaded(Counter counter)
            {
                mCounter = counter;
                mCounterView.setName(counter.getName());
                mCounterView.setProgressBarDrawable(counter.getLimit());
                mCounterView.setLimit(counter.getLimit());
                mCounterView.setProgression(counter.getLimit(), counter.getCount());
                mCounterView.setCount(counter.getCount());
                mCounterView.setColor(counter.getColor());
                mCounterView.setMenu();
            }

            @Override
            public void onDataNotAvailable()
            {
            }
        });
    }
}
