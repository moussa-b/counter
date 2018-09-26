package com.mbo.counter.settings;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.google.gson.Gson;
import com.mbo.counter.BuildConfig;
import com.mbo.counter.R;
import com.mbo.counter.commons.FileUtils;
import com.mbo.counter.commons.Utils;
import com.mbo.counter.data.model.Counter;
import com.mbo.counter.data.model.Export;
import com.mbo.counter.data.model.Folder;
import com.mbo.counter.data.model.Statistics;
import com.mbo.counter.data.source.CounterDataSource;
import com.mbo.counter.data.source.ormlite.OrmLiteDataSource;
import com.mbo.counter.data.source.ormlite.OrmLiteHelper;

import java.io.File;
import java.util.List;

public class SettingsPresenter implements SettingsContract.Presenter
{
    @NonNull
    private final CounterDataSource mCounterDataSource;

    @NonNull
    private final SettingsContract.View mSettingsView;

    public SettingsPresenter(@NonNull OrmLiteDataSource counterDataSource, @NonNull SettingsContract.View settingsView)
    {
        this.mSettingsView = settingsView;
        this.mCounterDataSource = counterDataSource;
        mSettingsView.setPresenter(this);
    }

    @Override
    public void start()
    {

    }

    @Override
    public void exportData()
    {
        final Context context = ((PreferenceFragmentCompat) mSettingsView).getContext();
        if (context != null)
        {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED)
            {
                Utils.showWarningDialog(context, false, R.string.information, R.string.storage_permission_warning, null, null, R.drawable.ic_menu_info);
            }
            else if (FileUtils.isExternalStorageWritable())
            {
                final File exportFile = FileUtils.getPublicDownloadStorageFile("export_counter_" + Utils.getCurrentDateTimeForFileName() + ".json");

                final Export export = new Export();

                mCounterDataSource.getCountersForExport(new CounterDataSource.LoadCountersCallback()
                {
                    @Override
                    public void onCountersLoaded(List<Counter> counters)
                    {
                        export.setCounters(counters);

                        mCounterDataSource.getFoldersForExport(new CounterDataSource.LoadFoldersCallback()
                        {
                            @Override
                            public void onFoldersLoaded(List<Folder> folders)
                            {
                                export.setFolders(folders);

                                mCounterDataSource.getStatisticsForExport(new CounterDataSource.LoadStatisticsCallback()
                                {
                                    @Override
                                    public void onStatisticsLoaded(List<Statistics> statistics)
                                    {
                                        export.setStatistics(statistics);
                                        export.setDatabaseVersion(OrmLiteHelper.DATABASE_VERSION);
                                        export.setVersionCode(BuildConfig.VERSION_CODE);
                                        export.setVersionName(BuildConfig.VERSION_NAME);

                                        Gson gson = new Gson();
                                        String export_data_message = context.getResources().getString(R.string.export_data_error_message);
                                        if (FileUtils.writeToFile(gson.toJson(export), exportFile))
                                            export_data_message = String.format(context.getResources().getString(R.string.export_data_success_message), exportFile.getAbsolutePath());

                                        Utils.showWarningDialog(context, false, context.getResources().getString(R.string.information), export_data_message, null, null, R.drawable.ic_menu_info);
                                    }

                                    @Override
                                    public void onDataNotAvailable()
                                    {

                                    }
                                });
                            }

                            @Override
                            public void onDataNotAvailable()
                            {

                            }
                        });
                    }

                    @Override
                    public void onDataNotAvailable()
                    {

                    }
                });
            }
        }
    }
}
