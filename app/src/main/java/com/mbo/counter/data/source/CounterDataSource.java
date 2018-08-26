package com.mbo.counter.data.source;

import android.support.annotation.NonNull;

import com.mbo.counter.data.model.Counter;
import com.mbo.counter.data.model.Folder;
import com.mbo.counter.data.model.Statistics;

import java.util.List;

public interface CounterDataSource
{
    void addStatistics(@NonNull Statistics statistics);

    int decrementCounter(int counterId);

    void deleteAllCounters();

    void deleteAllCounterGroups();

    void deleteCounter(int counterId);

    void deleteCounterGroup(int counterGroupId);

    void deleteCountersInGroup(int counterGroupId);

    void duplicateCounter(int counterId);

    void duplicateCounterGroup(int counterGroupId);

    void getStatistics(int counterId, @NonNull LoadStatisticsCallback callback);

    void getCounters(@NonNull LoadCountersCallback callback);

    void getCounter(int counterId, @NonNull GetCounterCallback callback);

    void getCounterGroups(@NonNull LoadCounterGroupsCallback callback);

    void getCounterGroup(int counterId, @NonNull GetCounterGroupCallback callback);

    int incrementCounter(int counterId);

    void saveCounter(@NonNull Counter counter);

    void saveCounterGroup(@NonNull Folder folder);

    void resetCounter(int counterId);

    void resetCountersInGroup(int counterGroupId);

    interface LoadCountersCallback
    {
        void onCountersLoaded(List<Counter> counters);

        void onDataNotAvailable();
    }

    interface LoadCounterGroupsCallback
    {
        void onCounterGroupsLoaded(List<Folder> folders);

        void onDataNotAvailable();
    }

    interface LoadStatisticsCallback
    {
        void onStatisticsLoaded(List<Statistics.Row> counters);

        void onDataNotAvailable();
    }

    interface GetCounterCallback
    {
        void onCounterLoaded(Counter counter);

        void onDataNotAvailable();
    }

    interface GetCounterGroupCallback
    {
        void onCounterGroupLoaded(Folder folder);

        void onDataNotAvailable();
    }
}
