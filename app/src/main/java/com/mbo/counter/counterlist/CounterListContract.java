package com.mbo.counter.counterlist;

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
        void setCount(int position, int count);

        void setProgression(int position, int progression);

        void showAddCounter();

        void showCounters(List<Counter> counters);

        void showCounterUi(int counterId);

        void showCounterStatisticsUi(int counterId);

        void showEditCounterUi(int counterId);

        void showNoCounters();
    }

    interface Presenter extends BasePresenter
    {
        void decrementCounter(final int position, final int counterId, int limit);

        void deleteCounter(int counterId);

        void duplicateCounter(int counterId);

        void incrementCounter(final int position, final int counterId, int limit);

        void loadCounters();

        void resetCounter(final int position, final int counterId);

        void saveCounter(Counter counter);
    }
}
