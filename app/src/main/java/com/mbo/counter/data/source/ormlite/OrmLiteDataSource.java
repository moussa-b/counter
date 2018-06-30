package com.mbo.counter.data.source.ormlite;

import android.support.annotation.NonNull;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.mbo.counter.data.model.Counter;
import com.mbo.counter.data.model.Statistics;
import com.mbo.counter.data.source.CounterDataSource;

import java.sql.SQLException;
import java.util.List;

public class OrmLiteDataSource implements CounterDataSource
{
    private static OrmLiteDataSource INSTANCE;

    private DaoCounter mDaoCounter;

    private DaoStatistics mDaoStatistics;

    // Prevent direct instantiation.
    private OrmLiteDataSource(@NonNull OrmLiteSqliteOpenHelper helper)
    {
        try
        {
            mDaoCounter = new DaoCounter(helper.getConnectionSource(), Counter.class);
            mDaoStatistics = new DaoStatistics(helper.getConnectionSource(), Statistics.class);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public static OrmLiteDataSource getInstance()
    {
        if (INSTANCE == null)
            throw new IllegalStateException("OrmLiteDataSource has not been initialized");
        else if (INSTANCE.mDaoCounter == null)
            throw new IllegalStateException("Error in CounterDao initialization");
        else
            return INSTANCE;
    }

    public static synchronized void initialize(@NonNull OrmLiteSqliteOpenHelper helper)
    {
        if (INSTANCE == null)
            INSTANCE = new OrmLiteDataSource(helper);
    }

    @Override
    public void addStatistics(@NonNull Statistics statistics)
    {
        try
        {
            mDaoStatistics.createOrUpdate(statistics);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void getStatistics(int counterId, @NonNull LoadStatisticsCallback callback)
    {
        try
        {
            final List<Statistics.Row> statistics = mDaoStatistics.getCounterStatisticsById(counterId);
            if (statistics != null)
            {
                callback.onStatisticsLoaded(statistics);
            }
            else
            {
                callback.onDataNotAvailable();
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void getCounters(@NonNull LoadCountersCallback callback)
    {
        try
        {
            final List<Counter> counters = mDaoCounter.queryForAll();
            if (counters != null)
            {
                callback.onCountersLoaded(counters);
            }
            else
            {
                callback.onDataNotAvailable();
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void getCounter(int counterId, @NonNull GetCounterCallback callback)
    {
        try
        {
            final Counter counter = mDaoCounter.queryForId((long) counterId);
            if (counter != null)
            {
                callback.onCounterLoaded(counter);
            }
            else
            {
                callback.onDataNotAvailable();
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void saveCounter(@NonNull Counter counter)
    {
        try
        {
            mDaoCounter.createOrUpdate(counter);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void refreshCounters()
    {

    }

    @Override
    public void deleteAllCounters()
    {
        try
        {
            mDaoCounter.deleteBuilder().delete();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteCounter(int counterId)
    {
        try
        {
            mDaoCounter.deleteById((long) counterId);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
}
