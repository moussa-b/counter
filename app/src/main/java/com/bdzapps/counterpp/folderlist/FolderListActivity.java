package com.bdzapps.counterpp.folderlist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.bdzapps.counterpp.commons.Utils;
import com.bdzapps.counterpp.data.source.ormlite.OrmLiteDataSource;
import com.mbo.counter.R;

public class FolderListActivity extends AppCompatActivity
{
    private FolderListPresenter mFolderListPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.counter_list_activity);

        FolderListFragment folderListFragment = (FolderListFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (folderListFragment == null)
        {
            // Create the fragment
            folderListFragment = FolderListFragment.newInstance();

            Utils.addFragmentToActivity(getSupportFragmentManager(), folderListFragment, R.id.contentFrame);

            mFolderListPresenter = new FolderListPresenter(OrmLiteDataSource.getInstance(), folderListFragment);
        }
    }
}
