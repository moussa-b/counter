package com.bdzapps.counterpp.counterlist;

import android.support.annotation.NonNull;

import com.bdzapps.counterpp.data.model.Counter;
import com.bdzapps.counterpp.data.model.Folder;
import com.bdzapps.counterpp.data.model.Statistics;
import com.bdzapps.counterpp.data.source.CounterDataSource;
import com.bdzapps.counterpp.data.source.ormlite.OrmLiteDataSource;

import java.util.ArrayList;
import java.util.List;

public class CounterListPresenter implements CounterListContract.Presenter
{
    @NonNull
    private final CounterDataSource mCounterDataSource;

    @NonNull
    private final CounterListContract.View mCounterListView;

    private final int mFolderId;

    public CounterListPresenter(@NonNull OrmLiteDataSource counterDataSource, @NonNull CounterListContract.View counterListView, int folderId)
    {
        this.mCounterDataSource = counterDataSource;
        this.mCounterListView = counterListView;
        this.mFolderId = folderId;
        mCounterListView.setPresenter(this);
    }

    public CounterListPresenter(@NonNull OrmLiteDataSource counterDataSource, @NonNull CounterListContract.View counterListView)
    {
        this(counterDataSource, counterListView, 0);
    }

    @Override
    public void start()
    {
        loadCounters();
    }

    @Override
    public void decrementCounter(final int position, final int counterId, int limit)
    {
        int count = mCounterDataSource.decrementCounter(counterId);
        mCounterListView.setCount(position, count);
        if (limit != 0)
        {
            int progression = (int) (100 * (float) count / ((float) limit));
            mCounterListView.setProgression(position, progression);
        }
    }

    @Override
    public void deleteCounter(int counterId)
    {
        mCounterDataSource.deleteCounter(counterId);
        loadCounters();
    }

    @Override
    public void duplicateCounter(int counterId)
    {
        mCounterDataSource.duplicateCounter(counterId);
        loadCounters();
    }

    @Override
    public void incrementCounter(final int position, final int counterId, int limit)
    {
        int count = mCounterDataSource.incrementCounter(counterId);
        mCounterListView.setCount(position, count);
        if (limit != 0)
        {
            int progression = (int) (100 * (float) count / ((float) limit));
            mCounterListView.setProgression(position, progression);
        }
    }

    @Override
    public void loadCounters()
    {
        if (mFolderId == 0)
        {
            mCounterDataSource.getCounters(new CounterDataSource.LoadCountersCallback()
            {
                @Override
                public void onCountersLoaded(List<Counter> counters)
                {
                    processCounters(counters);
                }

                @Override
                public void onDataNotAvailable()
                {

                }
            });

            mCounterDataSource.getLastStatistics(new CounterDataSource.LoadStatisticCallback()
            {
                @Override
                public void onStatisticLoaded(Statistics statistics)
                {
                    processLastStatistics(statistics);
                }

                @Override
                public void onDataNotAvailable()
                {
                    processEmptyLastStatistics();
                }
            });
        }
        else
        {
            mCounterDataSource.getFolder(mFolderId, new CounterDataSource.GetFolderCallback()
            {
                @Override
                public void onFolderLoaded(Folder folder)
                {
                    processCounters(new ArrayList<>(folder.getCounters()));
                }

                @Override
                public void onDataNotAvailable()
                {

                }
            });

            mCounterDataSource.getLastStatisticsInFolder(mFolderId,
                    new CounterDataSource.LoadStatisticCallback()
                    {
                        @Override
                        public void onStatisticLoaded(Statistics statistics)
                        {
                            processLastStatistics(statistics);
                        }

                        @Override
                        public void onDataNotAvailable()
                        {
                            processEmptyLastStatistics();
                        }
                    }
            );
        }

    }

    @Override
    public void resetCounter(final int position, final int counterId, final int limit)
    {
        mCounterDataSource.resetCounter(counterId);
        mCounterListView.setCount(position, 0);
        if (limit != 0)
            mCounterListView.setProgression(position, 0);
    }

    @Override
    public void saveCounter(Counter counter)
    {
        mCounterDataSource.saveCounter(counter);
    }

    private void processCounters(List<Counter> counters)
    {
        if (counters.isEmpty())
        {
            // Show a message indicating there are no tasks for that filter type.
            processEmptyCounters();
        }
        else
        {
            // Show the list of tasks
            mCounterListView.showCounters(counters);
        }
    }

    private void processEmptyCounters()
    {
        mCounterListView.showNoCounters();
    }

    private void processLastStatistics(final Statistics statistics)
    {
        if (statistics != null)
        {
            mCounterDataSource.getCounter(statistics.getCounterId(), new CounterDataSource.GetCounterCallback()
            {
                @Override
                public void onCounterLoaded(Counter counter)
                {
                    mCounterListView.showLastStatistics(counter, statistics);
                }

                @Override
                public void onDataNotAvailable()
                {

                }
            });
        }
        else
        {
            processEmptyLastStatistics();
        }

    }

    private void processEmptyLastStatistics()
    {
        mCounterListView.showNoLastStatistics();
    }
}
