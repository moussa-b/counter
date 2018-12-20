package com.bdzapps.counterpp.data.source.ormlite;

import android.support.annotation.NonNull;

import com.bdzapps.counterpp.data.model.Counter;
import com.bdzapps.counterpp.data.model.Folder;
import com.bdzapps.counterpp.data.model.Statistics;
import com.bdzapps.counterpp.data.model.StatisticsType;
import com.bdzapps.counterpp.data.source.CounterDataSource;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.table.TableUtils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

import static com.bdzapps.counterpp.data.model.StatisticsType.DECREMENT;
import static com.bdzapps.counterpp.data.model.StatisticsType.INCREMENT;
import static com.bdzapps.counterpp.data.model.StatisticsType.RESET;

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
                counter.setLastModificationTimeStamp(today.getTime());

                if (counter.getLimit() == 0 || (counter.getCount() - counter.getStep()) >= 0)
                    counter.setCount(counter.getCount() - counter.getStep());
                else
                    counter.setCount(counter.getLimit() + counter.getCount() - counter.getStep());

                mDaoCounter.update(counter);
                mDaoStatistics.create(new Statistics(today.getTime(), counter.getId(), 0, DECREMENT));
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
                counter.setCount(0);
                counter.setCreationTimeStamp(new Date().getTime());
                counter.setLastModificationTimeStamp(new Date().getTime());
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
                Date today = new Date();
                folder.setId(0);
                folder.setCreationTimeStamp(today.getTime());
                folder.setLastModificationTimeStamp(today.getTime());
                mDaoFolder.create(folder);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void generateRandomStatistics(int counterId, StatisticsType statisticsType, int nbDays, int nbStatistics)
    {
        for (int i = 0; i < nbStatistics; i++) {
            int nbDay = (int)(Math.random() * nbDays);
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date()); // Today
            cal.add(Calendar.DATE, -nbDay);
            try
            {
                mDaoStatistics.create(new Statistics(cal.getTimeInMillis(), counterId, 0, statisticsType));
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void getLastStatistics(@NonNull LoadStatisticCallback callback)
    {
        try
        {
            final List<Statistics> statistics = mDaoStatistics
                    .queryBuilder()
                    .orderBy("dateTimeStamp", false)
                    .limit(1L)
                    .query();

            if (statistics != null)
            {
                callback.onStatisticLoaded(statistics.get(0));
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
    public void getLastStatisticsInFolder(int folderId, @NonNull LoadStatisticCallback callback)
    {
        try
        {
            List<Statistics> statistics = new ArrayList<>();
            GenericRawResults<String[]> rawResults =
                    mDaoCounter.queryRaw(
                            "select c.id as counterId, s.dateTimeStamp, s.id, s.type, s.value from counter as c" +
                                    " inner join statistics as s" +
                                    " on s.counterId = c.id" +
                                    " where folder_id = " + folderId +
                                    " order by s.dateTimeStamp desc " +
                                    "limit 1");

            for (String[] resultArray : rawResults)
            {
                statistics.add(new Statistics(resultArray[0], resultArray[1], resultArray[2], resultArray[3], resultArray[4]));
            }
            rawResults.close();

            if (statistics.size() > 0)
            {
                callback.onStatisticLoaded(statistics.get(0));
            }
            else
            {
                callback.onDataNotAvailable();
            }
        }
        catch (SQLException | IOException e)
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
    public void getCountersForExport(@NonNull LoadCountersCallback callback)
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
    public void getFolder(int folderId, @NonNull GetFolderCallback callback)
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
    public void getFoldersForExport(@NonNull LoadFoldersCallback callback)
    {
        try
        {
            final List<Folder> counters = mDaoFolder.queryForAll();
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
            final List<Statistics> statistics = mDaoStatistics
                    .queryBuilder().where()
                    .eq("counterId", counterId).query();
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
    public void getStatisticsForExport(@NonNull LoadStatisticsCallback callback)
    {
        try
        {
            final List<Statistics> statistics = mDaoStatistics.queryForAll();
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
    public void getStatisticsInInterval(int counterId, long startTimeStamp, long endTimeStamp, @NonNull LoadStatisticsCallback callback)
    {
        try
        {
            final List<Statistics> statistics = mDaoStatistics
                    .queryBuilder().where()
                    .eq("counterId", counterId)
                    .and()
                    .ge("dateTimeStamp", startTimeStamp)
                    .and()
                    .lt("dateTimeStamp", endTimeStamp)
                    .query();

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
                counter.setLastModificationTimeStamp(today.getTime());

                if (counter.getLimit() == 0 || (counter.getCount() + counter.getStep()) <= counter.getLimit())
                    counter.setCount(counter.getCount() + counter.getStep());
                else
                    counter.setCount((counter.getCount() + counter.getStep()) - counter.getLimit());

                mDaoCounter.update(counter);
                mDaoStatistics.create(new Statistics(today.getTime(), counter.getId(), 0, INCREMENT));
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
            Date today = new Date();
            if (counter.getCreationTimeStamp() == 0)
                counter.setCreationTimeStamp(today.getTime());

            counter.setLastModificationTimeStamp(today.getTime());

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
    public void saveCounters(@NonNull final List<Counter> counters)
    {
        try
        {
            mDaoCounter.callBatchTasks(new Callable<Object>()
            {
                @Override
                public Object call() throws Exception
                {
                    for (Counter counter : counters)
                        mDaoCounter.createOrUpdate(counter);
                    return null;
                }
            });
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
                counter.setLastModificationTimeStamp(today.getTime());
                counter.setCount(0);
                mDaoCounter.update(counter);
                mDaoStatistics.create(new Statistics(today.getTime(), counter.getId(), 0, RESET));
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void resetCountersInFolder(int folderId)
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
                    counter.setLastModificationTimeStamp(today.getTime());
                    mDaoCounter.update(counter);
                    mDaoStatistics.create(new Statistics(today.getTime(), counter.getId(), 0, RESET));
                }
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void resetAllData(@NonNull ResetAllDataCallback resetAllDataCallback)
    {
        try
        {
            TableUtils.dropTable(mDaoCounter, true);
            TableUtils.dropTable(mDaoFolder, true);
            TableUtils.dropTable(mDaoStatistics, true);

            TableUtils.createTable(mDaoCounter);
            TableUtils.createTable(mDaoFolder);
            TableUtils.createTable(mDaoStatistics);

            Folder defaultFolder = new Folder("");
            Counter defaultCounter = new Counter("");
            mDaoFolder.create(defaultFolder);
            defaultCounter.setFolder(defaultFolder);
            mDaoCounter.create(defaultCounter);

            resetAllDataCallback.onSuccess();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            resetAllDataCallback.onError(e.getLocalizedMessage());
        }
    }

    @Override
    public void saveFolder(@NonNull Folder folder)
    {
        try
        {
            Date today = new Date();
            if (folder.getCreationTimeStamp() == 0)
                folder.setCreationTimeStamp(today.getTime());

            folder.setLastModificationTimeStamp(today.getTime());

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

    @Override
    public void saveFolders(@NonNull final List<Folder> folders)
    {
        try
        {
            mDaoFolder.callBatchTasks(new Callable<Object>()
            {
                @Override
                public Object call() throws Exception
                {
                    for (Folder folder : folders)
                        mDaoFolder.createOrUpdate(folder);
                    return null;
                }
            });
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void saveStatistics(@NonNull final List<Statistics> statistics)
    {
        try
        {
            mDaoStatistics.callBatchTasks(new Callable<Object>()
            {
                @Override
                public Object call() throws Exception
                {
                    for (Statistics stat : statistics)
                        mDaoStatistics.createOrUpdate(stat);
                    return null;
                }
            });
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
}
