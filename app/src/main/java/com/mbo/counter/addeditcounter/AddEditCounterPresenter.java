package com.mbo.counter.addeditcounter;

import android.support.annotation.NonNull;

import com.mbo.counter.data.model.Counter;
import com.mbo.counter.data.model.CounterGroup;
import com.mbo.counter.data.source.CounterDataSource;

import java.util.List;

public class AddEditCounterPresenter implements AddEditCounterContract.Presenter
{
    @NonNull
    private final CounterDataSource mCounterDataSource;

    @NonNull
    private final AddEditCounterContract.View mAddCounterView;

    private int mCounterId;

    private Counter mCounter;

    public AddEditCounterPresenter(int counterId, @NonNull CounterDataSource counterDataSource,
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
        else
            mCounter = new Counter();

        loadCounterGroups();
    }

    @Override
    public Counter getCounter()
    {
        return mCounter;
    }

    @Override
    public void loadCounterGroups()
    {
        mCounterDataSource.getCounterGroups(new CounterDataSource.LoadCounterGroupsCallback()
        {
            @Override
            public void onCounterGroupsLoaded(List<CounterGroup> counterGroups)
            {
                mAddCounterView.processCounterGroups(counterGroups);
            }

            @Override
            public void onDataNotAvailable()
            {

            }
        });
    }

    @Override
    public void saveCounter()
    {
        mCounterDataSource.saveCounter(mCounter);
        mAddCounterView.showCountersList();
    }

    @Override
    public void saveCounterGroup(CounterGroup counterGroup)
    {
        mCounterDataSource.saveCounterGroup(counterGroup);
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
                mAddCounterView.setLimit(counter.getLimit());
                mAddCounterView.setNote(counter.getNote());
                mCounter = counter;
            }

            @Override
            public void onDataNotAvailable()
            {
            }
        });
    }
}
