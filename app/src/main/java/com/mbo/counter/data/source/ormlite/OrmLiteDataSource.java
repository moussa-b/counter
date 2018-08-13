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

import static com.mbo.counter.data.model.StatisticsType.DECREMENT;
import static com.mbo.counter.data.model.StatisticsType.INCREMENT;
import static com.mbo.counter.data.model.StatisticsType.RESET;

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
    public int decrementCounter(int counterId)
    {
        try
        {
            final Counter counter = mDaoCounter.queryForId((long) counterId);
            if (counter != null)
            {
                Date today = new Date();
                counter.setLastModificationDate(today);

                if (counter.getLimit() == 0 || (counter.getCount() - counter.getStep()) >= 0)
                    counter.setCount(counter.getCount() - counter.getStep());
                else
                    counter.setCount(counter.getLimit() + counter.getCount() - counter.getStep());

                mDaoCounter.update(counter);
                mDaoStatistics.create(new Statistics(today, counter.getId(), 0, DECREMENT));
                return counter.getCount();
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return 0;
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
            CounterGroup counterGroup = mDaoCounterGroup.queryForId((long) counterGroupId);
            if (counterGroup != null && counterGroup.getCounters() != null && counterGroup.getCounters().size() > 0)
            {
                counterGroup.getCounters().clear();
            }
            mDaoCounterGroup.deleteById((long) counterGroupId);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteCountersInGroup(int counterGroupId)
    {
        try
        {
            CounterGroup counterGroup = mDaoCounterGroup.queryForId((long) counterGroupId);
            if (counterGroup != null && counterGroup.getCounters() != null && counterGroup.getCounters().size() > 0)
            {
                counterGroup.getCounters().clear();
            }
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
    public void getCounters(@NonNull LoadCountersCallback callback)
    {
        try
        {
            final List<Counter> counters = mDaoCounter.queryBuilder()
                    .orderBy("order", true)
                    .where().ne("id", 1).query();
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
    public void getCounterGroup(int counterGroupId, @NonNull GetCounterGroupCallback callback)
    {
        try
        {
            final CounterGroup counterGroup = mDaoCounterGroup.queryForId((long) counterGroupId);
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
    public void getCounterGroups(@NonNull LoadCounterGroupsCallback callback)
    {
        try
        {
            final List<CounterGroup> counters = mDaoCounterGroup.queryBuilder()
                    .orderBy("order", true)
                    .where().ne("id", 1).query();
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
    public int incrementCounter(int counterId)
    {
        try
        {
            final Counter counter = mDaoCounter.queryForId((long) counterId);
            if (counter != null)
            {
                Date today = new Date();
                counter.setLastModificationDate(today);

                if (counter.getLimit() == 0 || (counter.getCount() + counter.getStep()) <= counter.getLimit())
                    counter.setCount(counter.getCount() + counter.getStep());
                else
                    counter.setCount((counter.getCount() + counter.getStep()) - counter.getLimit());

                mDaoCounter.update(counter);
                mDaoStatistics.create(new Statistics(today, counter.getId(), 0, INCREMENT));
                return counter.getCount();
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public void saveCounter(@NonNull Counter counter)
    {
        try
        {
            if (counter.getCreationDate() == null)
                counter.setCreationDate(new Date());

            counter.setLastModificationDate(new Date());

            if (counter.getId() == 0)
            {
                mDaoCounter.create(counter);
                counter.setOrder(10000 * counter.getId());
                counter.setOrderInGroup(10000 * counter.getId());
                mDaoCounter.update(counter);
            }
            else
                mDaoCounter.update(counter);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void resetCounter(int counterId)
    {
        try
        {
            final Counter counter = mDaoCounter.queryForId((long) counterId);
            if (counter != null)
            {
                Date today = new Date();
                counter.setLastModificationDate(today);
                counter.setCount(0);
                mDaoCounter.update(counter);
                mDaoStatistics.create(new Statistics(today, counter.getId(), 0, RESET));
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void resetCountersInGroup(int counterGroupId)
    {
        try
        {
            final CounterGroup counterGroup = mDaoCounterGroup.queryForId((long) counterGroupId);
            if (counterGroup != null && counterGroup.getCounters() != null && counterGroup.getCounters().size() > 0)
            {
                for (Counter counter : counterGroup.getCounters())
                {
                    Date today = new Date();
                    counter.setCount(0);
                    counter.setLastModificationDate(today);
                    mDaoCounter.update(counter);
                    mDaoStatistics.create(new Statistics(today, counter.getId(), 0, RESET));
                }
            }
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

            if (counterGroup.getId() == 0)
            {
                mDaoCounterGroup.create(counterGroup);
                counterGroup.setOrder(10000 * counterGroup.getId());
                mDaoCounterGroup.update(counterGroup);
            }
            else
                mDaoCounterGroup.update(counterGroup);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
}
