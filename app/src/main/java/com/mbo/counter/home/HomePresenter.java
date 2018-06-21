package com.mbo.counter.home;

import android.support.annotation.NonNull;

import com.mbo.counter.data.model.Counter;

import java.util.ArrayList;
import java.util.List;

public class HomePresenter implements HomeContract.Presenter
{

    private final HomeContract.View mHomeView;

    public HomePresenter(@NonNull HomeContract.View mHomeView)
    {
        this.mHomeView = mHomeView;
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
        // TODO : Replace by real data source
        List<Counter> countersToShow = new ArrayList<>();
        countersToShow.add(new Counter("My first counter"));
        countersToShow.add(new Counter("My first counter"));
        countersToShow.add(new Counter("My first counter"));
        countersToShow.add(new Counter("My first counter"));
        countersToShow.add(new Counter("My first counter"));
        countersToShow.add(new Counter("My first counter"));
        countersToShow.add(new Counter("My first counter"));
        countersToShow.add(new Counter("My first counter"));
        countersToShow.add(new Counter("My first counter"));
        processCounters(countersToShow);
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
