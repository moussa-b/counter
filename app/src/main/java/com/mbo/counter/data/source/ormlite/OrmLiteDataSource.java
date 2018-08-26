package com.mbo.counter.data.source.ormlite;

import android.support.annotation.NonNull;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.mbo.counter.data.model.Counter;
import com.mbo.counter.data.model.Folder;
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

    private DaoFolder mDaoFolder;

    // Prevent direct instantiation.
    private OrmLiteDataSource(@NonNull OrmLiteSqliteOpenHelper helper)
    {
        try
        {
            mDaoCounter = new DaoCounter(helper.getConnectionSource(), Counter.class);
            mDaoStatistics = new DaoStatistics(helper.getConnectionSource(), Statistics.class);
            mDaoFolder = new DaoFolder(helper.getConnectionSource(), Folder.class);
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
    public void deleteAllFolders()
    {
        try
        {
            mDaoFolder.deleteBuilder().where().ne("id", 1);
            mDaoFolder.deleteBuilder().delete();
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
    public void deleteFolder(int folderId)
    {
        try
        {
            Folder folder = mDaoFolder.queryForId((long) folderId);
            if (folder != null && folder.getCounters() != null && folder.getCounters().size() > 0)
            {
                folder.getCounters().clear();
            }
            mDaoFolder.deleteById((long) folderId);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteCountersInGroup(int folderId)
    {
        try
        {
            Folder folder = mDaoFolder.queryForId((long) folderId);
            if (folder != null && folder.getCounters() != null && folder.getCounters().size() > 0)
            {
                folder.getCounters().clear();
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
    public void duplicateFolder(int folderId)
    {
        try
        {
            Folder folder = mDaoFolder.queryForId((long) folderId);
            if (folder != null)
            {
                folder.setId(0);
                folder.setCreationDate(new Date());
                folder.setLastModificationDate(new Date());
                mDaoFolder.create(folder);
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
    public void getFolderGroup(int folderId, @NonNull GetFolderCallback callback)
    {
        try
        {
            final Folder folder = mDaoFolder.queryForId((long) folderId);
            if (folder != null)
            {
                callback.onFolderLoaded(folder);
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
    public void getFolders(@NonNull LoadFoldersCallback callback)
    {
        try
        {
            final List<Folder> counters = mDaoFolder.queryBuilder()
                    .orderBy("order", true)
                    .where().ne("id", 1).query();
            if (counters != null)
            {
                callback.onFoldersLoaded(counters);
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
    public void resetCountersInGroup(int folderId)
    {
        try
        {
            final Folder folder = mDaoFolder.queryForId((long) folderId);
            if (folder != null && folder.getCounters() != null && folder.getCounters().size() > 0)
            {
                for (Counter counter : folder.getCounters())
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
    public void saveFolder(@NonNull Folder folder)
    {
        try
        {
            if (folder.getCreationDate() == null)
                folder.setCreationDate(new Date());

            folder.setLastModificationDate(new Date());

            if (folder.getId() == 0)
            {
                mDaoFolder.create(folder);
                folder.setOrder(10000 * folder.getId());
                mDaoFolder.update(folder);
            }
            else
                mDaoFolder.update(folder);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
}
