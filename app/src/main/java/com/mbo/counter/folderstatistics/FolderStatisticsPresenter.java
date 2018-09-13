package com.mbo.counter.folderstatistics;

import android.support.annotation.NonNull;

import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.mbo.counter.data.model.Counter;
import com.mbo.counter.data.model.Folder;
import com.mbo.counter.data.source.CounterDataSource;

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
        for (int i = 0; i < counters.size(); i++)
            yvalues.add(new PieEntry(counters.get(i).getCount(), i));

        mStatisticsView.showStatistics(new PieDataSet(yvalues, ""));
    }

    private void processEmptyCounters()
    {
        mStatisticsView.showNoStatistics();
    }

}
