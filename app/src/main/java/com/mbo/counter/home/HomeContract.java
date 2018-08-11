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

        void contactUs();

        int getCheckedSegmentedButton();

        boolean isFabMenuOpen();

        void prepareViewByCheckedId(int id);

        void shareApplication();

        void showAboutUi();

        void showAddCounter();

        void showAddCounterGroup();

        void showEvaluateUi();

        void showFabMenu();

        void showSettingsUi();

        void toggleFabVisibility(boolean visible);
    }

    interface Presenter extends BasePresenter
    {
        void saveCounterGroup(CounterGroup counterGroup);
    }
}
