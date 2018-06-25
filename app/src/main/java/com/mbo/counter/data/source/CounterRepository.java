package com.mbo.counter.data.source;

import android.support.annotation.NonNull;

import com.mbo.counter.data.model.Counter;

import java.util.UUID;

import io.realm.Realm;

public class CounterRepository implements CounterDataSource
{
    private static CounterRepository INSTANCE = null;

    private Realm mRealm;

    // Prevent direct instantiation.
    private CounterRepository()
    {
        mRealm = Realm.getDefaultInstance();
    }

    public static CounterRepository getInstance()
    {
        if (INSTANCE == null)
            INSTANCE = new CounterRepository();

        return INSTANCE;
    }

    @Override
    public void getCounters(@NonNull LoadCountersCallback callback)
    {
        callback.onCountersLoaded(mRealm.where(Counter.class).findAll());
    }

    @Override
    public void getCounter(@NonNull String counterId, @NonNull GetCounterCallback callback)
    {
        callback.onCounterLoaded(mRealm.where(Counter.class).equalTo("id", counterId).findFirst());
    }

    @Override
    public void saveCounter(@NonNull final Counter counter)
    {
        mRealm.executeTransaction(new Realm.Transaction()
        {
            @Override
            public void execute(Realm realm)
            {
                if (counter.getId() == null)
                    counter.setId(UUID.randomUUID().toString());
                realm.copyToRealmOrUpdate(counter);
            }
        });
    }

    @Override
    public void refreshCounters()
    {

    }

    @Override
    public void deleteAllCounters()
    {

    }

    @Override
    public void deleteCounter(@NonNull final String counterId)
    {
        mRealm.executeTransaction(new Realm.Transaction()
        {
            @Override
            public void execute(Realm realm)
            {
                mRealm.where(Counter.class).equalTo("id", counterId)
                        .findFirst()
                        .deleteFromRealm();
            }
        });
    }
}
