package com.mbo.counter.settings;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.mbo.counter.R;
import com.mbo.counter.commons.Utils;
import com.mbo.counter.data.model.Counter;
import com.mbo.counter.data.model.Export;
import com.mbo.counter.data.model.Folder;
import com.mbo.counter.data.model.Statistics;
import com.mbo.counter.data.source.CounterDataSource;

import java.lang.ref.WeakReference;
import java.util.List;

public class AsyncTaskImportDataFromJson extends AsyncTask<Export, Void, Boolean>
{
    @NonNull
    private final CounterDataSource mCounterDataSource;
    private ProgressDialog mProgressDialog;
    @NonNull
    private WeakReference<Context> context;

    public AsyncTaskImportDataFromJson(@NonNull Context context, @NonNull CounterDataSource counterDataSource)
    {
        this.mCounterDataSource = counterDataSource;
        this.context = new WeakReference<>(context);
    }

    @Override
    protected void onPreExecute()
    {
        mProgressDialog = Utils.createProgressDialog(context.get(), R.string.import_data_dialog_message);
        mProgressDialog.show();
    }

    @Override
    protected Boolean doInBackground(Export... exports)
    {
        final boolean[] isSuccessImport = {false}; // Hack because isSuccessImport is accessed inside inner class

        Export export = exports[0];
        final List<Counter> counters = export.getCounters();
        final List<Folder> folders = export.getFolders();
        final List<Statistics> statistics = export.getStatistics();

        mCounterDataSource.resetAllData(new CounterDataSource.ResetAllDataCallback()
        {
            @Override
            public void onSuccess()
            {
                if (counters != null && counters.size() != 0)
                    mCounterDataSource.saveCounters(counters);

                if (folders != null && folders.size() != 0)
                    mCounterDataSource.saveFolders(folders);

                if (statistics != null && statistics.size() != 0)
                    mCounterDataSource.saveStatistics(statistics);

                mProgressDialog.dismiss();
                isSuccessImport[0] = true;
            }

            @Override
            public void onError(String message)
            {
                mProgressDialog.dismiss();
                isSuccessImport[0] = false;
            }
        });

        return isSuccessImport[0];
    }

    @Override
    protected void onPostExecute(Boolean isSuccessImport)
    {
        super.onPostExecute(isSuccessImport);
        int stringId = isSuccessImport ? R.string.import_data_success_message : R.string.import_data_failure_message;
        Toast.makeText(context.get(), stringId, Toast.LENGTH_LONG).show();
    }
}
