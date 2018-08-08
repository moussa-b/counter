package com.mbo.counter.addeditcounter;

import com.mbo.counter.BasePresenter;
import com.mbo.counter.BaseView;
import com.mbo.counter.data.model.CounterGroup;

import java.util.List;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface AddEditCounterContract
{
    interface View extends BaseView<Presenter>
    {
        void processCounterGroups(List<CounterGroup> counterGroups);

        void setCount(int count);

        void setColor(String color);

        void setDirection(String direction);

        void setNote(String note);

        void setName(String name);

        void showCountersList();
    }

    interface Presenter extends BasePresenter
    {
        void loadCounterGroups();

        void saveCounter();

        void saveCounterGroup(CounterGroup counterGroup);

        void populateCounter();
    }
}
