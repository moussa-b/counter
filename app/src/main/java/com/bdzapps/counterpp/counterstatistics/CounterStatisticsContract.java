package com.bdzapps.counterpp.counterstatistics;

import com.bdzapps.counterpp.BasePresenter;
import com.bdzapps.counterpp.BaseView;
import com.github.mikephil.charting.data.BarData;

import java.util.List;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface CounterStatisticsContract
{
    interface View extends BaseView<Presenter>
    {
        String getStringById(int id);

        void showStatistics(BarData barData, List<Long> timeStampGroups, List<CounterStatisticsAdapter.Row> statisticsRows);

        void showNoStatistics();
    }

    interface Presenter extends BasePresenter
    {
        void loadStatistics();

        void updateStatistics(long period);
    }
}
