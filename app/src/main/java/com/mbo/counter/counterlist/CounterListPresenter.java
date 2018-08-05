package com.mbo.counter.counterlist;

import android.support.annotation.NonNull;

import com.mbo.counter.data.model.Counter;
import com.mbo.counter.data.model.Statistics;
import com.mbo.counter.data.model.StatisticsType;
import com.mbo.counter.data.source.CounterDataSource;
import com.mbo.counter.data.source.ormlite.OrmLiteDataSource;

import java.util.Date;
import java.util.List;

public class CounterListPresenter implements CounterListContract.Presenter
{
    @NonNull
    private final CounterDataSource mCounterDataSource;

    @NonNull
    private final CounterListContract.View mCounterListView;

    public CounterListPresenter(@NonNull OrmLiteDataSource counterDataSource, @NonNull CounterListContract.View counterListView)
    {
        this.mCounterDataSource = counterDataSource;
        this.mCounterListView = counterListView;
        mCounterListView.setPresenter(this);
    }

    @Override
    public void start()
    {
        loadCounters();
    }

    @Override
    public void decrementCounter(final int index, final int counterId)
    {
        mCounterDataSource.getCounter(counterId, new CounterDataSource.GetCounterCallback()
        {
            @Override
            public void onCounterLoaded(Counter counter)
            {
                if ((counter.getCount() - counter.getStep()) >= 0)
                    counter.setCount(counter.getCount() - counter.getStep());
                else
                    counter.setCount(counter.getLimit() + counter.getCount() - counter.getStep());

                mCounterDataSource.saveCounter(counter);
                mCounterDataSource.addStatistics(new Statistics(new Date(), counterId, -counter.getStep(), StatisticsType.DECREMENT));
                mCounterListView.setCount(index, counter.getCount());
                int progression = (int) (100 * (float) counter.getCount() / ((float) counter.getLimit()));
                mCounterListView.setProgression(index, progression);

            }

            @Override
            public void onDataNotAvailable()
            {

            }
        });
    }

    @Override
    public void deleteCounter(int counterId)
    {
        mCounterDataSource.deleteCounter(counterId);
    }

    @Override
    public void incrementCounter(final int index, final int counterId)
    {
        mCounterDataSource.getCounter(counterId, new CounterDataSource.GetCounterCallback()
        {
            @Override
            public void onCounterLoaded(Counter counter)
            {
                if ((counter.getCount() + counter.getStep()) <= counter.getLimit())
                    counter.setCount(counter.getCount() + counter.getStep());
                else
                    counter.setCount((counter.getCount() + counter.getStep()) - counter.getLimit());

                mCounterDataSource.saveCounter(counter);
                mCounterDataSource.addStatistics(new Statistics(new Date(), counterId, counter.getStep(), StatisticsType.INCREMENT));
                mCounterListView.setCount(index, counter.getCount());
                int progression = (int) (100 * (float) counter.getCount() / ((float) counter.getLimit()));
                mCounterListView.setProgression(index, progression);
            }

            @Override
            public void onDataNotAvailable()
            {

            }
        });
    }

    @Override
    public void loadCounters()
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
    }

    @Override
    public void openCounter(@NonNull Counter clickedCounter)
    {
        mCounterListView.showCounterUi(clickedCounter.getId());
    }

    @Override
    public void resetCounter(final int index, final int counterId)
    {
        mCounterDataSource.getCounter(counterId, new CounterDataSource.GetCounterCallback()
        {
            @Override
            public void onCounterLoaded(Counter counter)
            {
                counter.setCount(0);
                mCounterDataSource.saveCounter(counter);
                mCounterDataSource.addStatistics(new Statistics(new Date(), counterId, 0, StatisticsType.RESET));
                mCounterListView.setCount(index, 0);
            }

            @Override
            public void onDataNotAvailable()
            {

            }
        });
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
}
