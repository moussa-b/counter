package com.bdzapps.counterpp.folderstatistics;

import android.graphics.Color;
import android.support.annotation.NonNull;

import com.bdzapps.counterpp.data.model.Counter;
import com.bdzapps.counterpp.data.model.Folder;
import com.bdzapps.counterpp.data.source.CounterDataSource;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;

public class FolderStatisticsPresenter implements FolderStatisticsContract.Presenter
{
    @NonNull
    private final FolderStatisticsContract.View mStatisticsView;

    @NonNull
    private final CounterDataSource mCounterDataSource;

    private int mFolderId;

    public FolderStatisticsPresenter(int folderId, @NonNull CounterDataSource counterDataSource, @NonNull FolderStatisticsContract.View statisticsView)
    {
        this.mFolderId = folderId;
        this.mStatisticsView = statisticsView;
        this.mCounterDataSource = counterDataSource;
        mStatisticsView.setPresenter(this);
    }

    @Override
    public void start()
    {
        loadStatistics();
    }

    @Override
    public void loadStatistics()
    {
        mCounterDataSource.getFolder(mFolderId, new CounterDataSource.GetFolderCallback()
        {
            @Override
            public void onFolderLoaded(Folder folder)
            {
                processCounters(new ArrayList<>(folder.getCounters()));
            }

            @Override
            public void onDataNotAvailable()
            {
                processEmptyCounters();
            }
        });
    }

    private void processCounters(List<Counter> counters)
    {
        ArrayList<PieEntry> yvalues = new ArrayList<>();
        ArrayList<Integer> colors = new ArrayList<>();
        for (int i = 0; i < counters.size(); i++)
        {
            yvalues.add(new PieEntry(counters.get(i).getCount(), counters.get(i).getName(), i));
            colors.add(Color.parseColor(counters.get(i).getColor()));
        }

        mStatisticsView.showStatistics(new PieDataSet(yvalues, ""), counters, colors);
    }

    private void processEmptyCounters()
    {
        mStatisticsView.showNoStatistics();
    }

}
