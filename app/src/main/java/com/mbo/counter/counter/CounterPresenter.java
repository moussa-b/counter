package com.mbo.counter.counter;

import android.support.annotation.NonNull;

import com.mbo.counter.data.model.Counter;
import com.mbo.counter.data.source.CounterDataSource;

public class CounterPresenter implements CounterContract.Presenter
{
    @NonNull
    private final CounterDataSource mCounterDataSource;

    @NonNull
    private final CounterContract.View mCounterView;

    private long mCounterId;

    private Counter mCounter;

    public CounterPresenter(long counterId, @NonNull CounterDataSource counterDataSource,
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
    public int getCount()
    {
        return mCounter.getCount();
    }

    @Override
    public int incrementCounter()
    {
        if (mCounter.getCurrentCount() < mCounter.getCount())
            mCounter.setCurrentCount(mCounter.getCurrentCount() + 1);
        else
            mCounter.setCurrentCount(0);

        saveCounter(mCounter);

        return mCounter.getCurrentCount();
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
                mCounterView.setCurrentCount(counter.getCurrentCount());
                mCounterView.setProgression(counter.getCount(), counter.getCurrentCount());
            }

            @Override
            public void onDataNotAvailable()
            {
            }
        });
    }
}
