package com.bdzapps.counterpp.settings;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.widget.Toast;

import com.bdzapps.counterpp.commons.CallBack;
import com.bdzapps.counterpp.commons.FileUtils;
import com.bdzapps.counterpp.commons.PropertiesReader;
import com.bdzapps.counterpp.commons.Utils;
import com.bdzapps.counterpp.data.model.Counter;
import com.bdzapps.counterpp.data.model.Export;
import com.bdzapps.counterpp.data.model.Folder;
import com.bdzapps.counterpp.data.model.Statistics;
import com.bdzapps.counterpp.data.source.CounterDataSource;
import com.bdzapps.counterpp.data.source.ormlite.OrmLiteDataSource;
import com.bdzapps.counterpp.data.source.ormlite.OrmLiteHelper;
import com.google.gson.Gson;
import com.mbo.counter.BuildConfig;
import com.mbo.counter.R;

import java.io.File;
import java.util.List;
import java.util.Properties;

public class SettingsPresenter implements SettingsContract.Presenter
{
    @NonNull
    private final CounterDataSource mCounterDataSource;
    @NonNull
    private final SettingsContract.View mSettingsView;
    private Properties properties;

    public SettingsPresenter(@NonNull OrmLiteDataSource counterDataSource, @NonNull SettingsContract.View settingsView)
    {
        this.mSettingsView = settingsView;
        this.mCounterDataSource = counterDataSource;
        mSettingsView.setPresenter(this);
    }

    @Override
    public void start()
    {
        Context context = ((PreferenceFragmentCompat) mSettingsView).getContext();
        if (context != null)
        {
            PropertiesReader propertiesReader = new PropertiesReader(context);
            properties = propertiesReader.getProperties("configuration.properties");
        }
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

    @Override
    public String getProperty(String property)
    {
        return (properties != null) ? properties.getProperty(property) : null;
    }

    @Override
    public void importData(Uri importDataUri)
    {
        Context context = ((PreferenceFragmentCompat) mSettingsView).getContext();
        if (context != null)
        {
            String jsonData = FileUtils.readTextFromUri(context, importDataUri);
            try
            {
                Export export = new Gson().fromJson(jsonData, Export.class);
                AsyncTaskImportDataFromJson asyncTaskThumbnailCreator = new AsyncTaskImportDataFromJson(context, mCounterDataSource);
                asyncTaskThumbnailCreator.execute(export);
            }
            catch (Exception exception)
            {
                Toast.makeText(context, R.string.import_data_failure_message, Toast.LENGTH_LONG).show();
                exception.printStackTrace();
            }
        }
    }

    @Override
    public void resetData()
    {
        final Context context = ((PreferenceFragmentCompat) mSettingsView).getContext();
        if (context != null)
        {
            Utils.showWarningDialog(context, true, R.string.warning, R.string.reset_data_warning, new CallBack()
            {
                @Override
                public void execute(Object data)
                {
                    mCounterDataSource.resetAllData(new CounterDataSource.ResetAllDataCallback()
                    {
                        @Override
                        public void onSuccess()
                        {
                            Utils.showWarningDialog(context, false, R.string.information, R.string.reset_all_data_success_message, null, null, R.drawable.ic_menu_info);
                        }

                        @Override
                        public void onError(String message)
                        {
                            Utils.showWarningDialog(context, false, context.getResources().getString(R.string.error), message, null, null, R.drawable.ic_menu_info);
                        }
                    });
                }
            }, null, R.drawable.ic_menu_info);
        }
    }
}
