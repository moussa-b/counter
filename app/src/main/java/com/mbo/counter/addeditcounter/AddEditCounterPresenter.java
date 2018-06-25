package com.mbo.counter.addeditcounter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.mbo.counter.data.model.Counter;
import com.mbo.counter.data.source.CounterDataSource;

import io.realm.Realm;

public class AddEditCounterPresenter implements AddEditCounterContract.Presenter
{
    @NonNull
    private final CounterDataSource mCounterRepository;

    @NonNull
    private final AddEditCounterContract.View mAddCounterView;

    @Nullable
    private String mCounterId;

    private Counter mCounter;

    public AddEditCounterPresenter(@Nullable String counterId, @NonNull CounterDataSource counterRepository,
                                   @NonNull AddEditCounterContract.View addCounterView)
    {
        this.mCounterId = counterId;
        this.mCounterRepository = counterRepository;
        this.mAddCounterView = addCounterView;

        mAddCounterView.setPresenter(this);
    }

    @Override
    public void start()
    {
        if (mCounterId != null)
            populateCounter();
        else
            mCounter = new Counter();
    }

    @Override
    public void saveCounter(final String name, int count, String direction, String note, String color)
    {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction()
        {
            @Override
            public void execute(Realm realm)
            {
                mCounter.setName(name);
            }
        });

        realm.close();
        realm = null;
    }

    @Override
    public void populateCounter()
    {
        mCounterRepository.getCounter(mCounterId, new CounterDataSource.GetCounterCallback()
        {
            @Override
            public void onCounterLoaded(Counter counter)
            {
                mCounter = counter;
                mAddCounterView.setName(counter.getName());
                mAddCounterView.setCount(counter.getCount());
                mAddCounterView.setNote(counter.getNote());
            }

            @Override
            public void onDataNotAvailable()
            {
                mCounter = new Counter();
            }
        });
    }
}
