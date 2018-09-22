package com.mbo.counter.folderstatistics;

import com.github.mikephil.charting.data.PieDataSet;
import com.mbo.counter.BasePresenter;
import com.mbo.counter.BaseView;
import com.mbo.counter.data.model.Counter;

import java.util.List;

public interface FolderStatisticsContract
{
    interface View extends BaseView<Presenter>
    {
        void showNoStatistics();

        void showStatistics(PieDataSet dataSet, List<Counter> counters, List<Integer> colors);
    }

    interface Presenter extends BasePresenter
    {
        void loadStatistics();
    }
}
