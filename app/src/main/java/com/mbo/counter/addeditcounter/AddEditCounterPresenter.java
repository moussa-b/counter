package com.mbo.counter.addeditcounter;

import android.support.annotation.NonNull;

import com.mbo.counter.data.model.Counter;
import com.mbo.counter.data.source.CounterDataSource;

public class AddEditCounterPresenter implements AddEditCounterContract.Presenter
{
    @NonNull
    private final CounterDataSource mCounterDataSource;

    @NonNull
    private final AddEditCounterContract.View mAddCounterView;

    private long mCounterId;

    public AddEditCounterPresenter(long counterId, @NonNull CounterDataSource counterDataSource,
                                   @NonNull AddEditCounterContract.View addCounterView)
    {
        this.mCounterId = counterId;
        this.mCounterDataSource = counterDataSource;
        this.mAddCounterView = addCounterView;

        mAddCounterView.setPresenter(this);
    }

    @Override
    public void start()
    {
        if (mCounterId != 0)
            populateCounter();
    }

    @Override
    public void saveCounter(final String name, int count, String note, String direction, String color)
    {
        mCounterDataSource.saveCounter(new Counter(mCounterId, name, count, note, direction, color));
        mAddCounterView.showCountersList();
    }

    @Override
    public void populateCounter()
    {
        mCounterDataSource.getCounter(mCounterId, new CounterDataSource.GetCounterCallback()
        {
            @Override
            public void onCounterLoaded(Counter counter)
            {
                mAddCounterView.setName(counter.getName());
                mAddCounterView.setCount(counter.getCount());
                mAddCounterView.setNote(counter.getNote());
            }

            @Override
            public void onDataNotAvailable()
            {
            }
        });
    }
}
