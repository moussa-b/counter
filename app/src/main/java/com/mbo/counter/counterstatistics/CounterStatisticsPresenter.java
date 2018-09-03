package com.mbo.counter.counterstatistics;

import android.support.annotation.NonNull;

import com.mbo.counter.data.model.Statistics;
import com.mbo.counter.data.source.CounterDataSource;

import java.util.List;

public class CounterStatisticsPresenter implements CounterStatisticsContract.Presenter
{
    @NonNull
    private final CounterStatisticsContract.View mStatisticsView;

    @NonNull
    private final CounterDataSource mCounterDataSource;

    private int mCounterId;

    public CounterStatisticsPresenter(int counterId, @NonNull CounterDataSource counterDataSource, @NonNull CounterStatisticsContract.View statisticsView)
    {
        this.mCounterId = counterId;
        this.mStatisticsView = statisticsView;
        this.mCounterDataSource = counterDataSource;
        mStatisticsView.setPresenter(this);
    }


    @Override
    public void start()
    {
        loadStatistics();
    }

    @Override
    public void loadStatistics()
    {
        mCounterDataSource.getStatistics(mCounterId, new CounterDataSource.LoadStatisticsCallback()
        {
            @Override
            public void onStatisticsLoaded(List<Statistics> statistics)
            {
                processStatistics(statistics);
            }

            @Override
            public void onDataNotAvailable()
            {
            }
        });
    }

    private void processStatistics(List<Statistics> statistics)
    {
        if (statistics.isEmpty())
        {
            // Show a message indicating there are no tasks for that filter type.
            processEmptyStatistics();
        }
        else
        {
            // Show the list of tasks
            mStatisticsView.showStatistics(statistics);
        }
    }

    private void processEmptyStatistics()
    {
        mStatisticsView.showNoStatistics();
    }
}
