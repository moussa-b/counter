package com.bdzapps.counterpp.folderstatistics;

import com.bdzapps.counterpp.BasePresenter;
import com.bdzapps.counterpp.BaseView;
import com.bdzapps.counterpp.data.model.Counter;
import com.github.mikephil.charting.data.PieDataSet;

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
