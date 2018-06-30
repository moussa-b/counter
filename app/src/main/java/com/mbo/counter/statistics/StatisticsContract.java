package com.mbo.counter.statistics;

import com.mbo.counter.BasePresenter;
import com.mbo.counter.BaseView;
import com.mbo.counter.data.model.Statistics;

import java.util.List;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface StatisticsContract
{

    interface View extends BaseView<Presenter>
    {
        void showStatistics(List<Statistics.Row> statistics);

        void showNoStatistics();
    }

    interface Presenter extends BasePresenter
    {
        void loadStatistics();
    }
}
