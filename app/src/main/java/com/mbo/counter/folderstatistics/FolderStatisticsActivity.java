package com.mbo.counter.folderstatistics;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.mbo.counter.R;
import com.mbo.counter.commons.Utils;
import com.mbo.counter.data.source.ormlite.OrmLiteDataSource;

public class FolderStatisticsActivity extends AppCompatActivity
{
    private ActionBar mActionBar;

    private FolderStatisticsPresenter mStatisticsPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.folder_statistics_activity);

        // Set up the toolbar
        mActionBar = getSupportActionBar();
        if (mActionBar != null)
        {
            mActionBar.setDisplayHomeAsUpEnabled(true);
            mActionBar.setDisplayShowHomeEnabled(true);
            mActionBar.setTitle(getResources().getString(R.string.statistics));
        }

        FolderStatisticsFragment folderStatisticsFragment = (FolderStatisticsFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (folderStatisticsFragment == null)
        {
            // Create the fragment
            folderStatisticsFragment = FolderStatisticsFragment.newInstance();

            int folderId = 0;
            if (getIntent().hasExtra(FolderStatisticsFragment.ARGUMENT_STATISTICS_FOLDER_ID))
                folderId = getIntent().getIntExtra(FolderStatisticsFragment.ARGUMENT_STATISTICS_FOLDER_ID, 0);

            Utils.addFragmentToActivity(getSupportFragmentManager(), folderStatisticsFragment, R.id.contentFrame);

            mStatisticsPresenter = new FolderStatisticsPresenter(folderId, OrmLiteDataSource.getInstance(), folderStatisticsFragment);
        }
    }

    @Override
    public boolean onSupportNavigateUp()
    {
        onBackPressed();
        return true;
    }
}