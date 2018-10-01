package com.mbo.counter.counter;

import com.mbo.counter.BasePresenter;
import com.mbo.counter.BaseView;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface CounterContract
{

    interface View extends BaseView<Presenter>
    {
        void decrementCounter();

        void incrementCounter();

        void rotateProgressBar(int angle);

        void setColor(String color);

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
        int getCounterId();

        int getLimit();

        int decrementCounter();

        int incrementCounter();

        void resetCounter();

        void populateCounter();
    }
}
