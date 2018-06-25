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

        void showCounters(List<Counter> counters);

        void showAddCounter();

        void showNoCounters();

        void showCounterEditUi(String counterId);
    }

    interface Presenter extends BasePresenter
    {
        void loadCounters();

        void openCounterDetails(@NonNull Counter clickedCounter);
    }
}
