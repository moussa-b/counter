package com.mbo.counter.home;

import android.support.annotation.NonNull;

import com.mbo.counter.BasePresenter;
import com.mbo.counter.BaseView;
import com.mbo.counter.data.model.Counter;

import java.util.List;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface HomeContract
{

    interface View extends BaseView<Presenter>
    {

        void showFabMenu();

        void closeFabMenu();

        void showCounters(List<Counter> counters);

        void showAddCounter();

        void showAddCounterGroup();

        void showNoCounters();

        void showCounterUi(int counterId);

        void editCounters(boolean activeEdition);

        boolean isFabMenuOpen();
    }

    interface Presenter extends BasePresenter
    {
        void deleteCounter(int counterId);

        void loadCounters();

        void openCounter(@NonNull Counter clickedCounter);
    }
}
