package com.mbo.counter.addeditcounter;

import com.mbo.counter.BasePresenter;
import com.mbo.counter.BaseView;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface AddEditCounterContract
{

    interface View extends BaseView<Presenter>
    {
        void setCount(int count);

        void setColor(String color);

        void setDirection(String direction);

        void setNote(String note);

        void setName(String name);

        void showCountersList();
    }

    interface Presenter extends BasePresenter
    {
        void saveCounter(String name, int limit, String note, String direction, String color);

        void populateCounter();
    }
}
