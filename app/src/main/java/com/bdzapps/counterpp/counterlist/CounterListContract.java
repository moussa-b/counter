package com.bdzapps.counterpp.counterlist;

import com.bdzapps.counterpp.BasePresenter;
import com.bdzapps.counterpp.BaseView;
import com.bdzapps.counterpp.data.model.Counter;
import com.bdzapps.counterpp.data.model.Statistics;

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

        void showLastStatistics(Counter counter, Statistics statistics);

        void showNoCounters();

        void showNoLastStatistics();
    }

    interface Presenter extends BasePresenter
    {
        void decrementCounter(final int position, final int counterId, int limit);

        void deleteCounter(int counterId);

        void duplicateCounter(int counterId);

        void incrementCounter(final int position, final int counterId, int limit);

        void loadCounters();

        void resetCounter(final int position, final int counterId, final int limit);

        void saveCounter(Counter counter);
    }
}
