package com.mbo.counter.home;

import com.mbo.counter.BasePresenter;
import com.mbo.counter.BaseView;
import com.mbo.counter.data.model.CounterGroup;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface HomeContract
{
    interface View extends BaseView<Presenter>
    {
        void closeFabMenu();

        int getCheckedSegmentedButton();

        boolean isFabMenuOpen();

        void prepareViewByCheckedId(int id);

        void showAddCounter();

        void showAddCounterGroup();

        void showFabMenu();

        void toggleFabVisibility(boolean visible);
    }

    interface Presenter extends BasePresenter
    {
        void saveCounterGroup(CounterGroup counterGroup);
    }
}
