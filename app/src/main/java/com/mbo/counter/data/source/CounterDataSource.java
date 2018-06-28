package com.mbo.counter.data.source;

import android.support.annotation.NonNull;

import com.mbo.counter.data.model.Counter;
import com.mbo.counter.data.model.Statistics;

import java.util.List;

public interface CounterDataSource
{
    void addStatistics(@NonNull Statistics statistics);

    void getCounters(@NonNull LoadCountersCallback callback);

    void getCounter(int counterId, @NonNull GetCounterCallback callback);

    void saveCounter(@NonNull Counter counter);

    void refreshCounters();

    void deleteAllCounters();

    void deleteCounter(int counterId);

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
