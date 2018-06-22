package com.mbo.counter.data.source;

import android.support.annotation.NonNull;

import com.mbo.counter.data.model.Counter;

import java.util.List;

public interface CounterDataSource
{
    void getCounters(@NonNull LoadCountersCallback callback);

    void getCounter(@NonNull String counterId, @NonNull GetCounterCallback callback);

    void saveCounter(@NonNull Counter counter);

    void refreshCounters();

    void deleteAllCounters();

    void deleteCounter(@NonNull String counterId);

    interface LoadCountersCallback
    {

        void onCountersLoaded(List<Counter> counters);

        void onDataNotAvailable();
    }

    interface GetCounterCallback
    {

        void onCounterLoaded(Counter counter);

        void onDataNotAvailable();
    }
}
