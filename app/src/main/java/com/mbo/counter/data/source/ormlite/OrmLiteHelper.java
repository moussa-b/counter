package com.mbo.counter.data.source.ormlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.mbo.counter.data.model.Counter;
import com.mbo.counter.data.model.Folder;
import com.mbo.counter.data.model.Statistics;
import com.mbo.counter.data.source.CounterDataSource;

import java.sql.SQLException;

public class OrmLiteHelper extends OrmLiteSqliteOpenHelper
{
    private static final String DATABASE_NAME = "counter.db";
    private static final int DATABASE_VERSION = 1;

    public OrmLiteHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource)
    {
        try
        {
            TableUtils.createTable(connectionSource, Counter.class);
            TableUtils.createTable(connectionSource, Folder.class);
            TableUtils.createTable(connectionSource, Statistics.class);

            CounterDataSource dataSource = OrmLiteDataSource.getInstance();
            Folder defaultFolder = new Folder("");
            Counter defaultCounter = new Counter("");
            dataSource.saveCounterGroup(defaultFolder);
            defaultCounter.setFolder(defaultFolder);
            dataSource.saveCounter(defaultCounter);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion)
    {

    }
}
