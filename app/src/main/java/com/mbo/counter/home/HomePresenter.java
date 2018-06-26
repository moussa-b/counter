package com.mbo.counter.home;

import android.support.annotation.NonNull;

import com.mbo.counter.data.model.Counter;
import com.mbo.counter.data.source.CounterDataSource;

import java.util.List;

public class HomePresenter implements HomeContract.Presenter
{
    @NonNull
    private final HomeContract.View mHomeView;

    @NonNull
    private final CounterDataSource mCounterDataSource;

    public HomePresenter(@NonNull CounterDataSource counterDataSource, @NonNull HomeContract.View homeView)
    {
        this.mCounterDataSource = counterDataSource;
        this.mHomeView = homeView;
        mHomeView.setPresenter(this);
    }

    @Override
    public void start()
    {
        loadCounters();
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
    public void openCounterDetails(@NonNull Counter clickedCounter)
    {
        mHomeView.showCounterEditUi(clickedCounter.getId());
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
            mHomeView.showCounters(counters);
        }
    }

    private void processEmptyCounters()
    {
        mHomeView.showNoCounters();
    }
}
