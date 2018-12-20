package com.bdzapps.counterpp.data.source;

import android.support.annotation.NonNull;

import com.bdzapps.counterpp.data.model.Counter;
import com.bdzapps.counterpp.data.model.Folder;
import com.bdzapps.counterpp.data.model.Statistics;
import com.bdzapps.counterpp.data.model.StatisticsType;

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

    void generateRandomStatistics(int counterId, StatisticsType statisticsType, int nbDays, int nbStatistics);

    void getLastStatistics(@NonNull LoadStatisticCallback callback);

    void getLastStatisticsInFolder(int folderId, @NonNull LoadStatisticCallback callback);

    void getStatistics(int counterId, @NonNull LoadStatisticsCallback callback);

    void getStatisticsForExport(@NonNull LoadStatisticsCallback callback);

    void getStatisticsInInterval(int counterId, long startTimeStamp, long endTimeStamp, @NonNull LoadStatisticsCallback callback);

    void getCounters(@NonNull LoadCountersCallback callback);

    void getCountersForExport(@NonNull LoadCountersCallback callback);

    void getCounter(int counterId, @NonNull GetCounterCallback callback);

    void getFolders(@NonNull LoadFoldersCallback callback);

    void getFoldersForExport(@NonNull LoadFoldersCallback callback);

    void getFolder(int folderId, @NonNull GetFolderCallback callback);

    int incrementCounter(int counterId);

    void saveCounter(@NonNull Counter counter);

    void saveCounters(@NonNull List<Counter> counters);

    void saveFolder(@NonNull Folder folder);

    void saveFolders(@NonNull List<Folder> folders);

    void saveStatistics(@NonNull List<Statistics> statistics);

    void resetCounter(int counterId);

    void resetCountersInFolder(int folderId);

    void resetAllData(@NonNull ResetAllDataCallback resetAllDataCallback);

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
        void onStatisticsLoaded(List<Statistics> statistics);

        void onDataNotAvailable();
    }

    interface LoadStatisticCallback
    {
        void onStatisticLoaded(Statistics statistics);

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

    interface ResetAllDataCallback
    {
        void onSuccess();

        void onError(String message);
    }
}
