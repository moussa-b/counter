package com.mbo.counter.counter;

import android.support.annotation.NonNull;

import com.mbo.counter.data.model.Counter;
import com.mbo.counter.data.model.Statistics;
import com.mbo.counter.data.model.StatisticsType;
import com.mbo.counter.data.source.CounterDataSource;

import java.util.Date;

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
    public void addDecrementStatistics()
    {
        mCounterDataSource.addStatistics(new Statistics(new Date(), mCounterId, -mCounter.getStep(), StatisticsType.DECREMENT));
    }

    @Override
    public void addIncrementStatistics()
    {
        mCounterDataSource.addStatistics(new Statistics(new Date(), mCounterId, mCounter.getStep(), StatisticsType.INCREMENT));
    }

    @Override
    public void addResetStatistics()
    {
        mCounterDataSource.addStatistics(new Statistics(new Date(), mCounterId, 0, StatisticsType.RESET));
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
        if ((mCounter.getCount() - mCounter.getStep()) >= 0)
            mCounter.setCount(mCounter.getCount() - mCounter.getStep());
        else
            mCounter.setCount(mCounter.getLimit() + mCounter.getCount() - mCounter.getStep());

        saveCounter(mCounter);
        addDecrementStatistics();

        return mCounter.getCount();
    }

    @Override
    public int incrementCounter()
    {
        if ((mCounter.getCount() + mCounter.getStep()) <= mCounter.getLimit())
            mCounter.setCount(mCounter.getCount() + mCounter.getStep());
        else
            mCounter.setCount((mCounter.getCount() + mCounter.getStep()) - mCounter.getLimit());

        saveCounter(mCounter);
        addIncrementStatistics();

        return mCounter.getCount();
    }

    @Override
    public void resetCounter()
    {
        mCounter.setCount(0);
        saveCounter(mCounter);
        addResetStatistics();
    }

    @Override
    public void saveCounter(Counter counter)
    {
        mCounterDataSource.saveCounter(counter);
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
                mCounterView.setLimit(counter.getLimit());
                mCounterView.setProgression(counter.getLimit(), counter.getCount());
                mCounterView.setCount(counter.getCount());
            }

            @Override
            public void onDataNotAvailable()
            {
            }
        });
    }
}
