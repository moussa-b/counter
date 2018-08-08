package com.mbo.counter.counter;

import com.mbo.counter.BasePresenter;
import com.mbo.counter.BaseView;
import com.mbo.counter.data.model.Counter;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface CounterContract
{

    interface View extends BaseView<Presenter>
    {
        void rotateProgressBar(int angle);

        void setCount(int count);

        void setLimit(int limit);

        void setName(String name);

        void setProgression(int limit, int count);

        void showEditCounter();

        void showCounterStatistics();

        void showCountersList();
    }

    interface Presenter extends BasePresenter
    {
        void addDecrementStatistics();

        void addIncrementStatistics();

        void addResetStatistics();

        int getCounterId();

        int getLimit();

        int decrementCounter();

        int incrementCounter();

        void resetCounter();

        void saveCounter(Counter counter);

        void populateCounter();
    }
}
