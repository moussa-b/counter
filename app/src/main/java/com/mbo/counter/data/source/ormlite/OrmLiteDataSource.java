package com.mbo.counter.data.source.ormlite;

import android.support.annotation.NonNull;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.mbo.counter.data.model.Counter;
import com.mbo.counter.data.model.CounterGroup;
import com.mbo.counter.data.model.Statistics;
import com.mbo.counter.data.source.CounterDataSource;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class OrmLiteDataSource implements CounterDataSource
{
    private static OrmLiteDataSource INSTANCE;

    private DaoCounter mDaoCounter;

    private DaoStatistics mDaoStatistics;

    private DaoCounterGroup mDaoCounterGroup;

    // Prevent direct instantiation.
    private OrmLiteDataSource(@NonNull OrmLiteSqliteOpenHelper helper)
    {
        try
        {
            mDaoCounter = new DaoCounter(helper.getConnectionSource(), Counter.class);
            mDaoStatistics = new DaoStatistics(helper.getConnectionSource(), Statistics.class);
            mDaoCounterGroup = new DaoCounterGroup(helper.getConnectionSource(), CounterGroup.class);
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
    public void duplicateCounter(int counterId)
    {
        try
        {
            Counter counter = mDaoCounter.queryForId((long) counterId);
            if (counter != null)
            {
                counter.setId(0);
                counter.setCreationDate(new Date());
                counter.setLastModificationDate(new Date());
                mDaoCounter.create(counter);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void duplicateCounterGroup(int counterGroupId)
    {
        try
        {
            CounterGroup counterGroup = mDaoCounterGroup.queryForId((long) counterGroupId);
            if (counterGroup != null)
            {
                counterGroup.setId(0);
                counterGroup.setCreationDate(new Date());
                counterGroup.setLastModificationDate(new Date());
                mDaoCounterGroup.create(counterGroup);
            }
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
            final List<Counter> counters = mDaoCounter.queryBuilder().where().ne("id", 1).query();
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
    public void getCounterGroups(@NonNull LoadCounterGroupsCallback callback)
    {
        try
        {
            final List<CounterGroup> counters = mDaoCounterGroup.queryBuilder().where().ne("id", 1).query();
            if (counters != null)
            {
                callback.onCounterGroupsLoaded(counters);
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
    public void getCounterGroup(int counterId, @NonNull GetCounterGroupCallback callback)
    {
        try
        {
            final CounterGroup counterGroup = mDaoCounterGroup.queryForId((long) counterId);
            if (counterGroup != null)
            {
                callback.onCounterGroupLoaded(counterGroup);
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
            if (counter.getCreationDate() == null)
                counter.setCreationDate(new Date());

            counter.setLastModificationDate(new Date());

            mDaoCounter.createOrUpdate(counter);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void saveCounterGroup(@NonNull CounterGroup counterGroup)
    {
        try
        {
            if (counterGroup.getCreationDate() == null)
                counterGroup.setCreationDate(new Date());

            counterGroup.setLastModificationDate(new Date());

            mDaoCounterGroup.createOrUpdate(counterGroup);
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
            mDaoCounter.deleteBuilder().where().ne("id", 1);
            mDaoCounter.deleteBuilder().delete();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteAllCounterGroups()
    {
        try
        {
            mDaoCounterGroup.deleteBuilder().where().ne("id", 1);
            mDaoCounterGroup.deleteBuilder().delete();
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

    @Override
    public void deleteCounterGroup(int counterGroupId)
    {
        try
        {
            mDaoCounterGroup.deleteById((long) counterGroupId);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
}
