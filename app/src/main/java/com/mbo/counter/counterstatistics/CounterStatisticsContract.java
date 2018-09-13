package com.mbo.counter.counterstatistics;

import com.github.mikephil.charting.data.BarData;
import com.mbo.counter.BasePresenter;
import com.mbo.counter.BaseView;

import java.util.List;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface CounterStatisticsContract
{
    interface View extends BaseView<Presenter>
    {
        String getStringById(int id);

        void showStatistics(BarData barData, List<Long> timeStampGroups, List<StatisticsAdapter.Row> statisticsRows);

        void showNoStatistics();
    }

    interface Presenter extends BasePresenter
    {
        void loadStatistics();
    }
}
