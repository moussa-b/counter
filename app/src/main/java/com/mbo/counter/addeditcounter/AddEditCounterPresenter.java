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
        loadCounterGroups();
        populateCounter();
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
        if (mCounterId != 0)
        {
            mCounterDataSource.getCounter(mCounterId, new CounterDataSource.GetCounterCallback()
            {
                @Override
                public void onCounterLoaded(Counter counter)
                {
                    mCounter = counter;
                    mAddCounterView.setName(mCounter.getName());
                    mAddCounterView.setColor(mCounter.getColor());
                    mAddCounterView.setGroup(mCounter.getCounterGroup());
                    mAddCounterView.setLimit(mCounter.getLimit());
                    mAddCounterView.setStep(mCounter.getStep());
                    mAddCounterView.setNote(mCounter.getNote());
                }

                @Override
                public void onDataNotAvailable()
                {
                }
            });
        }
        else
        {
            mCounter = new Counter();
            mAddCounterView.setStep(mCounter.getStep());
        }
    }
}
