package com.mbo.counter.home;

import android.support.annotation.NonNull;

import com.mbo.counter.data.model.Counter;
import com.mbo.counter.data.model.CounterGroup;
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
    }

    @Override
    public void saveCounterGroup(CounterGroup counterGroup)
    {
        mCounterDataSource.saveCounterGroup(counterGroup);
    }
}
