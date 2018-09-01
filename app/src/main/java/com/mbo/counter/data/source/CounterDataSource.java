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

    void deleteAllFolders();

    void deleteCounter(int counterId);

    void deleteFolder(int folderId);

    void deleteCountersInGroup(int folderId);

    void duplicateCounter(int counterId);

    void duplicateFolder(int folderId);

    void getStatistics(int counterId, @NonNull LoadStatisticsCallback callback);

    void getCounters(@NonNull LoadCountersCallback callback);

    void getCounter(int counterId, @NonNull GetCounterCallback callback);

    void getFolders(@NonNull LoadFoldersCallback callback);

    void getFolder(int counterId, @NonNull GetFolderCallback callback);

    int incrementCounter(int counterId);

    void saveCounter(@NonNull Counter counter);

    void saveFolder(@NonNull Folder folder);

    void resetCounter(int counterId);

    void resetCountersInGroup(int folderId);

    interface LoadCountersCallback
    {
        void onCountersLoaded(List<Counter> counters);

        void onDataNotAvailable();
    }

    interface LoadFoldersCallback
    {
        void onFoldersLoaded(List<Folder> folders);

        void onDataNotAvailable();
    }

    interface LoadStatisticsCallback
    {
        void onStatisticsLoaded(List<Statistics> counters);

        void onDataNotAvailable();
    }

    interface GetCounterCallback
    {
        void onCounterLoaded(Counter counter);

        void onDataNotAvailable();
    }

    interface GetFolderCallback
    {
        void onFolderLoaded(Folder folder);

        void onDataNotAvailable();
    }
}
