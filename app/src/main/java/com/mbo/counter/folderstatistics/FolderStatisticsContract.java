package com.mbo.counter.folderstatistics;

import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.mbo.counter.BasePresenter;
import com.mbo.counter.BaseView;

public interface FolderStatisticsContract
{
    interface View extends BaseView<Presenter>
    {
        void showNoStatistics();

        void showStatistics(PieDataSet dataSet);
    }

    interface Presenter extends BasePresenter
    {
        void loadStatistics();
    }
}
