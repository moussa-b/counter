package com.mbo.counter.counterlist;

import android.support.annotation.NonNull;

import com.mbo.counter.BasePresenter;
import com.mbo.counter.BaseView;
import com.mbo.counter.data.model.Counter;

import java.util.List;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface CounterListContract
{
    interface View extends BaseView<Presenter>
    {
        void setCount(int index, int count);

        void setProgression(int index, int progression);

        void showAddCounter();

        void showCounterMenu(int counterId);

        void showCounters(List<Counter> counters);

        void showCounterUi(int counterId);

        void showEditCounterUi(int counterId);

        void showNoCounters();
    }

    interface Presenter extends BasePresenter
    {
        void decrementCounter(final int index, final int counterId);

        void deleteCounter(int counterId);

        void incrementCounter(final int index, final int counterId);

        void loadCounters();

        void openCounter(@NonNull Counter clickedCounter);

        void resetCounter(final int index, final int counterId);
    }
}
