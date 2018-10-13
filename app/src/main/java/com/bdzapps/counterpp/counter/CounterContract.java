package com.bdzapps.counterpp.counter;

import com.bdzapps.counterpp.BasePresenter;
import com.bdzapps.counterpp.BaseView;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface CounterContract
{

    interface View extends BaseView<Presenter>
    {
        void decrementCounter();

        void incrementCounter();

        void resetCounter();

        void rotateProgressBar(int angle);

        void setColor(String color);

        void setCount(int count);

        void setLimit(int limit);

        void setMenu();

        void setName(String name);

        void setProgressBarDrawable(int limit);

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
