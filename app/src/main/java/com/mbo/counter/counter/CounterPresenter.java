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
    public int getTotal()
    {
        return mCounter.getTotal();
    }

    @Override
    public int incrementCounter()
    {
        if (mCounter.getCount() < mCounter.getTotal())
            mCounter.setCount(mCounter.getCount() + 1);
        else
            mCounter.setCount(0);

        saveCounter(mCounter);

        return mCounter.getCount();
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
                mCounterView.setTotal(counter.getTotal());
                mCounterView.setProgression(counter.getTotal(), counter.getCount());
                mCounterView.setCount(counter.getCount());
            }

            @Override
            public void onDataNotAvailable()
            {
            }
        });
    }
}
