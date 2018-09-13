package com.mbo.counter.folderstatistics;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mbo.counter.R;

public class FolderStatisticsFragment extends Fragment implements FolderStatisticsContract.View
{
    public static final String ARGUMENT_STATISTICS_FOLDER_ID = "STATISTICS_FOLDER_ID";

    public FolderStatisticsFragment()
    {
    }

    public static FolderStatisticsFragment newInstance()
    {
        return new FolderStatisticsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.folder_statistics_fragment, container, false);
        return root;
    }

    @Override
    public void setPresenter(FolderStatisticsContract.Presenter presenter)
    {

    }
}
