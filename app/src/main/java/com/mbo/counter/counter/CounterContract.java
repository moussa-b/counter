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
        void setCount(int count);

        void setTotal(int total);

        void setName(String name);

        void setProgression(int total, int count);

        void showEditCounter();

        void showCounterStatistics();

        void showCountersList();
    }

    interface Presenter extends BasePresenter
    {
        int getCounterId();

        int getTotal();

        int incrementCounter();

        void saveCounter(Counter counter);

        void populateCounter();
    }
}
