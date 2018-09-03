package com.mbo.counter.counterstatistics;

import com.mbo.counter.BasePresenter;
import com.mbo.counter.BaseView;
import com.mbo.counter.data.model.Statistics;

import java.util.List;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface CounterStatisticsContract
{
    interface View extends BaseView<Presenter>
    {
        void showStatistics(List<Statistics> statistics);

        void showNoStatistics();
    }

    interface Presenter extends BasePresenter
    {
        void loadStatistics();
    }
}
