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
        void setName(String name);

        void setCurrentCount(int currentCount);

        void setProgression(int count, int currentCount);
    }

    interface Presenter extends BasePresenter
    {
        int getCount();

        int incrementCounter();

        void saveCounter(Counter counter);

        void populateCounter();
    }
}
